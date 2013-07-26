package com.imcode
package imcms
package admin.doc.content.filedoc

import com.imcode._
import com.imcode.imcms._
import com.imcode.imcms.admin.instance.file.{FileProperties, UploadedFile, FileUploaderDialog}

import com.vaadin.ui._
import com.vaadin.ui.Table.ColumnGenerator
import imcode.server.document.FileDocumentDomainObject
import imcode.server.document.FileDocumentDomainObject.FileDocumentFile
import imcode.util.io.FileInputStreamSource
import scala.collection.breakOut
import scala.collection.JavaConverters._
import scala.collection.immutable.ListMap
import scala.collection.mutable.{Map => MMap}
import com.imcode.imcms.vaadin.ui._
import com.imcode.imcms.vaadin.data._
import com.imcode.imcms.vaadin.server._
import com.imcode.imcms.vaadin.ui.dialog._
import com.imcode.imcms.admin.doc.content.DocContentEditor
import com.vaadin.server.Page


/**
 * File document is a `container` which may contain one or more related or unrelated files.
 * If there is more than one file then one of them must be set as default.
 * Default file content is returned when an user clicks on a doc link in a web browser.
 *
 * @param doc used as a read only value object to initialize editor.
 */
class FileDocContentEditor(doc: FileDocumentDomainObject) extends DocContentEditor with ImcmsServicesSupport {
  type Data = FileDocumentDomainObject

  private type MimeType = String
  private type DisplayName = String

  private case class Values(fdfs: Map[FileId, FileDocumentFile], defaultFdfId: Option[FileId])
  private var values = Values(
    doc.getFiles.asScala.map { case (id, fdf) => id -> fdf.clone } (breakOut),
    doc.getDefaultFileId.asOption
  )

  private lazy val mimeTypes: ListMap[MimeType, DisplayName] =
    imcmsServices.getDocumentMapper
      .getAllMimeTypesWithDescriptions(UI.getCurrent.imcmsUser)
      .map { case Array(mimeType, displayName) => mimeType -> displayName } (breakOut)

  private def findFDFByName(name: String, ignoreCase: Boolean = true): Option[FileDocumentFile] = {
    def namePredicate(fileName: String) = if (ignoreCase) name.equalsIgnoreCase(fileName) else name.equals(fileName)

    values.fdfs.collectFirst {
      case (_, fdf) if namePredicate(fdf.getFilename) => fdf
    }
  }

  val ui = new FileDocContentEditorUI with OnceOnlyAttachAction |>> { ui =>
    ui.tblFiles.addGeneratedColumn("Type", new ColumnGenerator {
      def generateCell(source: Table, itemId: AnyRef, columnId: AnyRef): String = {
        val mimeType = values.fdfs(itemId.asInstanceOf[FileId]).getMimeType
        mimeTypes.get(mimeType) match {
          case Some(displayName) => displayName
          case _ => mimeType
        }
      }
    })

    ui.tblFiles.addGeneratedColumn("Size", new ColumnGenerator {
      def generateCell(source: Table, itemId: AnyRef, columnId: AnyRef): String =
        values.fdfs(itemId.asInstanceOf[FileId]).getInputStreamSource.getSize |> FileProperties.sizeAsString
    })

    ui.tblFiles.addGeneratedColumn("Default", new ColumnGenerator {
      def generateCell(source: Table, itemId: AnyRef, columnId: AnyRef) = new CheckBox |>> { chk =>
        for (id <- values.defaultFdfId if id == itemId.asInstanceOf[FileId]) {
          chk.check()
        }

        chk.setReadOnly(true)
      }
    })

    ui.tblFiles.setColumnAlignment("Default", Table.ALIGN_CENTER)

    ui.miUpload.setCommandHandler {
      new FileUploaderDialog("Add file") |>> { dlg =>
        dlg.setOkButtonHandler {
          for (UploadedFile(_, mimeType, file) <- dlg.uploader.uploadedFile) {
            val saveAsName = dlg.uploader.saveAsName

            findFDFByName(saveAsName) |> {
              case _ if saveAsName.isEmpty => Left("Name is required")

              case Some(fdf) if !dlg.uploader.mayOverwrite => Left("File with such name allready exists")

              // todo: ??? return new instance ???
              case Some(fdf) => Right(fdf)

              case None =>
                val id = (
                    for ((AnyInt(id), _) <- values.fdfs) yield id
                  ) |> { ids =>
                    if (ids.isEmpty) 1 else ids.max + 1
                  } |> {
                    _.toString
                  }

                new FileDocumentFile |>> { _.setId(id) } |> Right.apply
            } |> {
              case Left(errMsg: String) =>
                dlg.uploader.ui.txtSaveAsName.setComponentError(errMsg)
                Page.getCurrent.showErrorNotification(errMsg)

              case Right(fdf: FileDocumentFile) =>
                fdf.setFilename(saveAsName)
                fdf.setMimeType(mimeType)
                fdf.setInputStreamSource(new FileInputStreamSource(file))

                values = Values(values.fdfs.updated(fdf.getId, fdf), values.defaultFdfId.orElse(Some(fdf.getId)))

                sync()
                dlg.close()
            }
          }
        }
      } |> UI.getCurrent.addWindow
    } // ui.miUpload.setCommandHandler

    ui.miEditProperties.setCommandHandler {
      ui.tblFiles.selection match {
        case Nil =>
          Page.getCurrent.showWarningNotification("Please select a file")

        case Seq(_, _, _*) =>
          Page.getCurrent.showWarningNotification("Can't edit multiple files", "Please select a single file")

        case Seq(fileId) =>
          new OkCancelDialog("Edit file properties") |>> { dlg =>
            val fdf = values.fdfs(fileId)
            val editorUI = new FileDocFilePropertiesEditorUI |>> { eui =>
              eui.txtId.value = fdf.getId
              eui.txtName.value = fdf.getFilename

              for ((mimeType, displayName) <- mimeTypes) {
                eui.cbType.addItem(mimeType, s"$displayName ($mimeType)")
              }

              val mimeType = fdf.getMimeType

              if (mimeTypes.get(mimeType).isEmpty) {
                eui.cbType.addItem(mimeType, mimeType)
              }

              eui.cbType.value = mimeType
            }

            dlg.mainUI = editorUI
            dlg.setOkButtonHandler {
              val errors = MMap.empty[AbstractComponent, ErrorMsg]

              editorUI.txtId.trimOpt match {
                case None =>
                  errors += editorUI.txtId -> "id is required"

                case Some(newId) if newId != fdf.getId =>
                  for (fdf2 <- values.fdfs.get(newId)) {
                    errors += editorUI.txtId -> "id is allready assigned to other file in this document"
                  }

                case _ =>
              }

              editorUI.txtName.trimOpt match {
                case None =>
                  errors += editorUI.txtName -> "name is required"

                case Some(newName) if !newName.equalsIgnoreCase(fdf.getFilename) =>
                  for (fdf2 <- findFDFByName(newName)) {
                    errors += editorUI.txtName -> "name is allready assigned to other file in this document"
                  }

                case _ =>
              }

              editorUI.txtId.setComponentError(null)
              editorUI.txtName.setComponentError(null)

              if (errors.nonEmpty) {
                for ((component, errMsg) <- errors) {
                  component.setComponentError(errMsg)
                }
              } else {
                // if id has changed, update doc filemap
                val newId = editorUI.txtId.trim
                val fdfs = values.fdfs - fileId + (newId -> fdf)
                val defaultFdfId = values.defaultFdfId.map {
                  case id if (fileId == id) && (fileId != newId) => newId
                  case id => id
                }

                fdf.setId(newId)
                fdf.setFilename(editorUI.txtName.trim)
                fdf.setMimeType(editorUI.cbType.value)

                values = Values(fdfs, defaultFdfId)

                sync()
                dlg.close()
              }
            }
          } |> UI.getCurrent.addWindow
      }
    }

    ui.miDelete.setCommandHandler {
      ui.tblFiles.selection match {
        case Nil =>
          Page.getCurrent.showWarningNotification("Please select file(s)")

        case fileIds =>
          val fdfs = values.fdfs filterKeys fileIds.toSet.andThen(!_)
          val ids = fdfs.keySet
          val defaultFdfId = ids.find(_ == values.defaultFdfId.get).orElse(ids.headOption)

          values = Values(fdfs, defaultFdfId)
          sync()
          Page.getCurrent.showInfoNotification("Selected file(s) have been deleted")
      }
    }


    ui.miMarkAsDefault.setCommandHandler {
      ui.tblFiles.selection match {
        case Nil =>
          Page.getCurrent.showWarningNotification("Please select a file")

        case Seq(_, _, _*) =>
          Page.getCurrent.showWarningNotification("Please select a single file")

        case Seq(fileId) =>
          values = values.copy(defaultFdfId = Some(fileId))
          sync()
          Page.getCurrent.showInfoNotification("File has been marked as default")
      }
    }

    ui.attachAction = Some(_ => resetValues())
  } // ui

  def collectValues() = {
    if (values.fdfs.isEmpty) {
      Left(Seq("Document must contain at least one file."))
    } else {
      Right(doc.clone.asInstanceOf[FileDocumentDomainObject] |>> { clone =>
        for ((fileId, _) <- clone.getFiles.asScala) {
          clone.removeFile(fileId)
        }

        for ((fileId, fdf) <- values.fdfs) {
          clone.addFile(fileId, fdf)
        }

        clone.setDefaultFileId(values.defaultFdfId.get)
      })
    }
  } // data


  def resetValues() {
    values = Values(
      doc.getFiles.asScala.map { case (id, fdf) => id -> fdf.clone } (breakOut),
      doc.getDefaultFileId.asOption
    )

    sync()
  }


  private def sync() {
    ui.tblFiles.removeAllItems()

    for ((fileId, fdf) <- values.fdfs) {
      ui.tblFiles.addItem(Array[AnyRef](fileId, fdf.getFilename), fileId)
    }
  }
}