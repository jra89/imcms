package imcode.server.document.index

import com.imcode._
import com.imcode.Log4jLoggerSupport
import imcode.server.{ImcmsServices}
import imcode.server.document.index.solr._

object DocumentIndexServiceFactory extends Log4jLoggerSupport {

  def createIndexService(services: ImcmsServices): SolrDocumentIndexService = services.getConfig |> { config =>
    (Option(config.getSolrUrl), Option(config.getSolrHome)) |> {
      case (Some(solrUrl), _) => new RemoteSolrDocumentIndexService(solrUrl)

      case (_, Some(solrHome)) => new EmbeddedSolrDocumentIndexService(solrHome)

      case _ =>
        val errMsg = "Configuration error. Neither Config.solrUrl nor Config.solrHome is set."
        logger.fatal(errMsg)
        throw new IllegalArgumentException(errMsg)
    } |>> { service =>
      service.ops = new SolrDocumentIndexServiceOps(
        services.getDocumentMapper,
        new DocumentIndexer(
          services.getDocumentMapper,
          services.getCategoryMapper,
          new DocumentContentIndexer
        )
      )
    }
  }
}