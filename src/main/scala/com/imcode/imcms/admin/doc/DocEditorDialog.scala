package com.imcode
package imcms
package admin.doc

import _root_.imcode.server.document.DocumentDomainObject
import com.imcode.imcms.vaadin.ui._
import com.imcode.imcms.vaadin.ui.dialog.{BottomContentMarginDialog, CustomSizeDialog, OkCancelDialog}


class DocEditorDialog(caption: String, doc: DocumentDomainObject) extends OkCancelDialog(caption) with CustomSizeDialog with BottomContentMarginDialog {

  val docEditor = new DocEditor(doc)

  mainUI = docEditor.ui
  btnOk.setCaption("btn_save".i)

  this.setSize(500, 600)
}


