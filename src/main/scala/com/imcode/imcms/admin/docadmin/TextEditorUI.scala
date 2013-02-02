package com.imcode
package imcms
package admin.docadmin

import com.vaadin.ui._
import com.imcode.imcms.vaadin.ui._


class TextEditorUI extends VerticalLayout with Spacing with FullSize {
  val mb = new MenuBar with FullWidth
  val miFormat = mb.addItem("Format")
  val miFormatHtml = miFormat.addItem("HTML") |>> { _.setCheckable(true) }
  val miFormatPlain = miFormat.addItem("Plain text")|>> { _.setCheckable(true) }
  val miHistory = mb.addItem("History")
  val miHelp = mb.addItem("Help")
  val tsTexts = new TabSheet with FullSize
  val lblStatus = new Label

  this.addComponents(mb, tsTexts, lblStatus)
  setExpandRatio(tsTexts, 1f)
}
