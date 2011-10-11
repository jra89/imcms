package com.imcode
package imcms
package admin.system.file

import scala.collection.JavaConversions._
import com.vaadin.ui._
import com.imcode.imcms.vaadin._
import com.imcode.util.event.Publisher
import java.io._
import org.apache.commons.io.FileUtils


case class UploadedFile(filename: String, mimeType: String, file: File)


sealed trait UploadStatus
case object UploadReseted extends UploadStatus
case class UploadStarted(event: Upload.StartedEvent) extends UploadStatus
case class UploadProgressUpdated(readBytes: Long, contentLength: Long) extends UploadStatus
case class UploadSucceeded(event: Upload.SucceededEvent, uploadedFile: UploadedFile) extends UploadStatus
case class UploadFailed(event: Upload.FailedEvent) extends UploadStatus


class FileUploaderDialog(caption: String = "") extends OkCancelDialog(caption) {
  val uploader = new FileUploader

  mainUI = uploader.ui

  uploader.listen { btnOk setEnabled _.isInstanceOf[UploadSucceeded] }

  wrapCancelHandler {
    if (uploader.ui.upload.isUploading) {
      uploader.ui.upload.interruptUpload
    }
  }
}


/**
 * Uploads a file chosen by a user into system-dependent default temporary-file directory.
 */
// opts?:
// allows edit original filename
// allows overwrite existing file with the same filename
// upload receiver

class FileUploader extends Publisher[UploadStatus] {
  private val uploadedFileOptRef = Atoms.OptRef[UploadedFile]

  /** Creates file save as name from original filename. */
  var fileNameToSaveAsName = identity[String]_

  val ui = letret(new FileUploaderUI) { ui =>
    // File based receiver
    val receiver = new Upload.Receiver {
      val file = letret(File.createTempFile("imcms_upload", null)) {
        _.deleteOnExit()
      }

      def receiveUpload(filename: String, mimeType: String) = new FileOutputStream(file)
    }

    ui.upload.setReceiver(receiver)
    ui.upload.addListener(new Upload.StartedListener {
      def uploadStarted(ev: Upload.StartedEvent) = {
        reset()
        ui.txtSaveAsName.setEnabled(true)
        ui.txtSaveAsName.value = fileNameToSaveAsName(ev.getFilename)
        ui.txtSaveAsName.setEnabled(false)
        ui.pgiBytesReceived.setEnabled(true)
        notifyListeners(UploadStarted(ev))
      }
    })
    ui.upload.addListener(new Upload.ProgressListener {
      def updateProgress(readBytes: Long, contentLength: Long) {
        ui.pgiBytesReceived.setValue(Float.box(readBytes.toFloat / contentLength))
        notifyListeners(UploadProgressUpdated(readBytes, contentLength))
      }
    })
    ui.upload.addListener(new Upload.FailedListener {
      def uploadFailed(ev: Upload.FailedEvent) {
        ui.txtSaveAsName.setEnabled(true)
        ui.txtSaveAsName.value = ""
        ui.txtSaveAsName.setEnabled(false)
        ui.pgiBytesReceived.setEnabled(false)
        FileUtils.deleteQuietly(receiver.file)
        ui.getApplication.showWarningNotification("file.upload.interrupted.warn.msg".i)
        notifyListeners(UploadFailed(ev))
      }
    })
    ui.upload.addListener(new Upload.SucceededListener {
      def uploadSucceeded(ev: Upload.SucceededEvent) {
        ui.txtSaveAsName.setEnabled(true)
        ui.chkOverwrite.setEnabled(true)
        ui.pgiBytesReceived.setValue(1f)

        let(UploadedFile(ev.getFilename, ev.getMIMEType, receiver.file)) { uploadedFile =>
          uploadedFileOptRef.set(Some(uploadedFile))
          notifyListeners(UploadSucceeded(ev, uploadedFile))
        }
      }
    })
  }

  //todo: delete uploaded file???
  def reset() {
    uploadedFileOptRef.set(None)
    ui.chkOverwrite.setEnabled(true)
    ui.chkOverwrite.value = false
    ui.chkOverwrite.setEnabled(false)
    ui.txtSaveAsName.setEnabled(true)
    ui.txtSaveAsName.value = ""
    ui.txtSaveAsName.setEnabled(false)
    ui.pgiBytesReceived.setEnabled(true)
    ui.pgiBytesReceived.setValue(0f)
    ui.pgiBytesReceived.setPollingInterval(500)
    ui.pgiBytesReceived.setEnabled(false)
    notifyListeners(UploadReseted)
  }

  def uploadedFile = uploadedFileOptRef.get

  def saveAsName = ui.txtSaveAsName.value

  def isOverwrite = ui.chkOverwrite.booleanValue

  // init
  reset()
}


class FileUploaderUI extends FormLayout with UndefinedSize {
  val upload = new Upload("file.upload.dlg.frm.fld.select".i, null) with Immediate
  val txtSaveAsName = new TextField("file.upload.dlg.frm.fld.save_as".i)
  val pgiBytesReceived = new ProgressIndicator; pgiBytesReceived.setCaption("file.upload.dlg.frm.fld.progress".i)
  val chkOverwrite = new CheckBox("file.upload.dlg.frm.fld.overwrite".i)

  upload.setButtonCaption("...")
  addComponents(this, upload, pgiBytesReceived, txtSaveAsName, chkOverwrite)
}