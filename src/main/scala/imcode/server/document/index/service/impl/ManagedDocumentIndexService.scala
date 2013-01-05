package imcode.server.document.index.service.impl

import _root_.com.imcode._
import _root_.imcode.server.user.UserDomainObject
import _root_.imcode.server.document.DocumentDomainObject
import _root_.imcode.server.document.index.service._
import org.apache.solr.client.solrj.{SolrQuery, SolrServer}
import org.apache.solr.client.solrj.response.QueryResponse
import java.lang.{InterruptedException, Thread}
import java.util.concurrent.atomic.{AtomicReference, AtomicBoolean}
import java.util.concurrent._

/**
 * Implements all DocumentIndexService functionality.
 * Ensures that update and rebuild never run concurrently.
 *
 * Indexing (ops) errors are wrapped and published asynchronously as ManagedSolrDocumentIndexService.IndexError events.
 * On index write failure (either update or rebuild) the service stops processing index write requests.
 *
 * The business-logic, (like rebuild scheduling or index recovery) should be implemented on higher levels.
 */
class ManagedSolrDocumentIndexService(
    solrServerReader: SolrServer,
    solrServerWriter: SolrServer,
    serviceOps: DocumentIndexServiceOps,
    serviceErrorHandler: ManagedSolrDocumentIndexService.ServiceError => Unit) extends DocumentIndexService {

  private val lock = new AnyRef
  private val shutdownRef = new AtomicBoolean(false)
  private val indexRebuildThreadRef = new AtomicReference[Thread]
  private val indexUpdateThreadRef = new AtomicReference[Thread]
  private val indexUpdateRequests = new LinkedBlockingQueue[IndexUpdateRequest](1000)
  private val indexWriteErrorRef = new AtomicReference[ManagedSolrDocumentIndexService.IndexWriteError]
  private val indexRebuildTaskRef = new AtomicReference[IndexRebuildTask]


  /**
   * Creates and starts new index-rebuild-thread if there is no already running one.
   * Any exception or an interruption terminates index-rebuild-thread.
   *
   * An existing index-update-thread is stopped before rebuilding happens and a new index-update-thread is started
   * immediately after running index-rebuild-thread is terminated without errors.
   */
  override def requestIndexRebuild(): Option[IndexRebuildTask] = lock.synchronized {
    logger.info("attempting to start new document-index-rebuild thread.")

    (shutdownRef.get, indexWriteErrorRef.get, indexRebuildThreadRef.get, indexRebuildTask()) match {
      case (shutdown@true, _, _, currentIndexRebuildTask) =>
        logger.info("new document-index-rebuild thread can not be started - service is shut down.")
        currentIndexRebuildTask

      case (_, indexWriteError, _, currentIndexRebuildTask) if indexWriteError != null =>
        logger.info(s"new document-index-rebuild thread can not be started - previous index write attempt has failed with error [$indexWriteError].")
        currentIndexRebuildTask

      case (_, _, indexRebuildThread, currentIndexRebuildTask) if Threads.notTerminated(indexRebuildThread) =>
        logger.info(s"new document-index-rebuild thread can not be started - document-index-rebuild thread [$indexRebuildThread] is allready running.")
        currentIndexRebuildTask

      case _ =>
        new IndexRebuildTask {
          val progressRef = new AtomicReference[IndexRebuildProgress]
          val futureTask = new FutureTask[Unit](Threads.mkCallable {
            serviceOps.rebuildIndex(solrServerWriter) { progress =>
              progressRef.set(progress)
            }
          })

          def progress(): Option[IndexRebuildProgress] = Option(progressRef.get)

          def future(): Future[_] = futureTask
        } |>> indexRebuildTaskRef.set |>> { indexRebuildTaskImpl =>
          new Thread { indexRebuildThread =>
            override def run() {
              try {
                interruptIndexUpdateThreadAndAwaitTermination()
                indexUpdateRequests.clear()
                indexRebuildTaskImpl.futureTask.run()
                if (!indexRebuildTaskImpl.futureTask.isCancelled) {
                  try {
                    indexRebuildTaskImpl.futureTask.get
                  } catch {
                    case e: ExecutionException => throw e.getCause
                    case e => throw e
                  }
                }

                Threads.spawnDaemon {
                  indexRebuildThread.join()
                  startNewIndexUpdateThread()
                }
              } catch {
                case _: InterruptedException =>
                  logger.trace(s"document-index-rebuild thread [$indexRebuildThread] was interrupted")
                  Threads.spawnDaemon {
                    indexRebuildThread.join()
                    startNewIndexUpdateThread()
                  }

                case e =>
                  val writeError = ManagedSolrDocumentIndexService.IndexRebuildError(ManagedSolrDocumentIndexService.this, e)
                  logger.error(s"Error in document-index-rebuild thread [$indexRebuildThread].", e)
                  indexWriteErrorRef.set(writeError)
                  Threads.spawnDaemon {
                    serviceErrorHandler(writeError)
                  }
              } finally {
                logger.info(s"document-index-rebuild thread [$indexRebuildThread] is about to terminate.")
              }
            }
          } |>> indexRebuildThreadRef.set |>> { indexRebuildThread =>
            indexRebuildThread.setName(s"document-index-rebuild-${indexRebuildThread.getId}")
            indexRebuildThread.start()
            logger.info(s"new document-index-rebuild thread [$indexRebuildThread] has been started")
          }
        } |> opt
    }
  }


  /**
   * Creates and starts new index-update-thread if there is no already running index-update or index-rebuild thread.
   * Any exception or an interruption terminates index-update-thread.
   *
   * As the final action, index-update-thread submits start of a new index-update-thread .
   */
  private def startNewIndexUpdateThread(): Unit = lock.synchronized {
    logger.info("attempting to start new document-index-update thread.")

    (shutdownRef.get, indexWriteErrorRef.get, indexRebuildThreadRef.get, indexUpdateThreadRef.get) match {
      case (shutdown@true, _, _, _) =>
        logger.info("new document-index-update thread can not be started - service is shut down.")

      case (_, indexWriteError, _, _) if indexWriteError != null =>
        logger.info(s"new document-index-update thread can not be started - previous index write attempt has failed [$indexWriteError].")

      case (_, _, indexRebuildThread, _) if Threads.notTerminated(indexRebuildThread) =>
        logger.info(s"new document-index-update thread can not be started while document-index-rebuild thread [$indexRebuildThread] is running.")

      case (_, _, _, indexUpdateThread) if Threads.notTerminated(indexUpdateThread) =>
        logger.info(s"new document-index-update thread can not be started - document-index-update thread [$indexUpdateThread] is allready running.")

      case _ =>
        new Thread { indexUpdateThread =>
          override def run() {
            try {
              while (true) {
                indexUpdateRequests.take() match {
                  case AddDocToIndex(docId) => serviceOps.addDocsToIndex(solrServerWriter, docId)
                  case DeleteDocFromIndex(docId) => serviceOps.deleteDocsFromIndex(solrServerWriter, docId)
                }
              }
            } catch {
              case e: InterruptedException =>
                logger.trace(s"document-index-update thread [$indexUpdateThread] was interrupted")

              case e =>
                val writeError = ManagedSolrDocumentIndexService.IndexUpdateError(ManagedSolrDocumentIndexService.this, e)
                logger.error(s"error in document-index-update thread [$indexUpdateThread].", e)
                indexWriteErrorRef.set(writeError)
                Threads.spawnDaemon {
                  serviceErrorHandler(writeError)
                }
            } finally {
              logger.info(s"document-index-update thread [$indexUpdateThread] is about to terminate.")
            }
          }
        } |>> indexUpdateThreadRef.set |>> { indexUpdateThread =>
          indexUpdateThread.setName(s"document-index-update-${indexUpdateThread.getId}")
          indexUpdateThread.start()
          logger.info(s"new document-index-update thread [$indexUpdateThread] has been started")
        }
    }
  }


  private def interruptIndexUpdateThreadAndAwaitTermination() {
    Threads.interruptAndAwaitTermination(indexUpdateThreadRef.get)
  }


  private def interruptIndexRebuildThreadAndAwaitTermination() {
    Threads.interruptAndAwaitTermination(indexRebuildThreadRef.get)
  }


  def requestIndexUpdate(request: IndexUpdateRequest) {
    Threads.spawnDaemon {
      shutdownRef.get match {
        case true =>
          logger.error(s"Can't submit index update request [$request], server is shut down.")

        case _ =>
          // todo: publish query is full???
          if (indexUpdateRequests.offer(request)) startNewIndexUpdateThread()
          else logger.error(s"Can't submit index update request [$request], requests query is full.")
      }
    }
  }


  override def query(solrQuery: SolrQuery): QueryResponse = {
    serviceOps.query(solrServerReader, solrQuery)
  }


  override def search(solrQuery: SolrQuery, searchingUser: UserDomainObject): Iterator[DocumentDomainObject] = {
    try {
      serviceOps.search(solrServerReader, solrQuery, searchingUser)
    } catch {
      case e: Throwable =>
        logger.error(s"Search error. solrParams: $solrQuery, searchingUser: $searchingUser", e)
        Threads.spawnDaemon {
          serviceErrorHandler(ManagedSolrDocumentIndexService.IndexSearchError(ManagedSolrDocumentIndexService.this, e))
        }
        Iterator.empty
    }
  }


  override def shutdown(): Unit = lock.synchronized {
    if (shutdownRef.compareAndSet(false, true)) {
      logger.info("Attempting to shut down the service.")
      try {
        interruptIndexUpdateThreadAndAwaitTermination()
        interruptIndexRebuildThreadAndAwaitTermination()

        solrServerReader.shutdown()
        solrServerWriter.shutdown()
        logger.info("Service has been shut down.")
      } catch {
        case e: Throwable =>
          logger.error("An error occured while shutting down the service.", e)
          throw e
      }
    }
  }

  override def indexRebuildTask(): Option[IndexRebuildTask] = Option(indexRebuildTaskRef.get)
}


object ManagedSolrDocumentIndexService {
  sealed abstract class ServiceError {
    val service: DocumentIndexService
    val error: Throwable
  }

  abstract class IndexWriteError extends ServiceError

  case class IndexUpdateError(service: DocumentIndexService, error: Throwable) extends IndexWriteError
  case class IndexRebuildError(service: DocumentIndexService, error: Throwable) extends IndexWriteError
  case class IndexSearchError(service: DocumentIndexService, error: Throwable) extends ServiceError
}


object Threads {

  def mkThread(runBody: => Unit): Thread =
    new Thread {
      override def run() {
        runBody
      }
    }

  def mkRunnable(runBody: => Unit): Runnable =
    new Runnable {
      def run() { runBody }
    }

  def mkCallable[A](callBody: => A): Callable[A]  =
    new Callable[A] {
      def call(): A = callBody
    }

  def spawn(runBody: => Unit): Thread = mkThread(runBody) |>> { t => t.start() }
  def spawnDaemon(runBody: => Unit): Thread = mkThread(runBody) |>> { t => t.setDaemon(true); t.start() }

  def terminated(thread: Thread): Boolean = thread == null || thread.getState == Thread.State.TERMINATED
  def notTerminated(thread: Thread): Boolean = !terminated(thread)

  def interruptAndAwaitTermination(thread: Thread) {
    if (thread != null) {
      thread.interrupt()
      thread.join()
    }
  }
}