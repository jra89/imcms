package com.imcode
package imcms
package admin.document

import com.imcode.imcms.vaadin._

import scala.collection.JavaConversions._
import vaadin.{ImcmsApplication, FullSize}
import imcode.server.Imcms
import dao.MetaDao
import imcode.server.document.DocumentDomainObject
import api.Document
import com.vaadin.event.Action
import com.vaadin.ui._


object Actions {
  val View = new Action("doc.tbl.action.view".i)
  val AddToSelection = new Action("doc.tbl.action.add_to_selection".i)
  val Edit = new Action("doc.tbl.action.edit".i)
  val Exclude = new Action("doc.tbl.action.exclude".i)
  val Delete = new Action("doc.tbl.action.delete".i)
}

class DocManager(app: ImcmsApplication) {
  val docSelection = new DocSelection(app)
  val docView = new DocFilteredView

  val docSelectionDlg = letret(new OKDialog("Selected documents") with CustomSizeDialog) { dlg =>
    dlg.mainUI = docSelection.ui
    dlg.setSize(500, 500)
  }

  val ui = letret(new DocManagerUI(docView.ui)) { ui =>
    ui.miViewSelection.setCommandHandler {
      app.show(docSelectionDlg, modal = false, resizable = true)
    }

    docView.docTableUI.addActionHandler(new Action.Handler {
      import Actions._

      def getActions(target: AnyRef, sender: AnyRef) = Array(AddToSelection, Exclude, View, Edit, Delete)

      def handleAction(action: Action, sender: AnyRef, target: AnyRef) =
        action match {
          case AddToSelection => docSelection.ui.tblDocs.addItem(target)
          case Exclude => sender.asInstanceOf[Table].removeItem(target)
          case _ =>
        }
    })
  }

  reload()

  // temp
  def reload() {
    val docMapper = Imcms.getServices.getDocumentMapper

    docMapper.getAllDocumentIds.foreach(docView.docTableUI.addItem(_))

//    for (id <- docMapper.getAllDocumentIds; doc <- ?(docMapper.getDefaultDocument(id))) {
//      val meta = doc.getMeta
//      val alias = ?(meta.getProperties.get(DocumentDomainObject.DOCUMENT_PROPERTIES__IMCMS_DOCUMENT_ALIAS)) getOrElse ""
//
//      val status = meta.getPublicationStatus match {
//        case Document.PublicationStatus.NEW => "New"
//        case Document.PublicationStatus.APPROVED => "Approved"
//        case Document.PublicationStatus.DISAPPROVED => "Disapproved"
//      }
//
//      ui.tblDocs.addItem(Array[AnyRef](id, Int box doc.getDocumentType.getId, status, alias), doc)
//    }
  }
}

class DocManagerUI(docViewUI: Component) extends VerticalLayout with Spacing with FullSize {
  val mb = new MenuBar
  val miDoc = mb.addItem("Document")
  val miDocNew = miDoc.addItem("New")
  val miDocEdit = miDoc.addItem("Edit")
  val miDocDelete = miDoc.addItem("Delete")
  val miSearch = mb.addItem("Search")
  val miView = mb.addItem("View")
  val miViewSelection = miView.addItem("Selection")

  addComponents(this, mb, docViewUI)
}