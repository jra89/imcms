package com.imcode
package imcms.admin.system.file

import scala.collection.JavaConversions._
import com.vaadin.ui._
import imcode.server.{Imcms}
import com.imcode.imcms.vaadin.{ContainerProperty => CP, _}
import imcode.server.document.{CategoryDomainObject}
import java.io.File
import com.vaadin.ui.Window.Notification

class FileManager(app: ImcmsApplication) {
  val browser = letret(new FileBrowser(isMultiSelect = true)) { browser =>
    browser.addPlace("Home", Place(Imcms.getPath))
    browser.addPlace("Templates", Place(new File(Imcms.getPath, "WEB-INF/templates/text")))
    browser.addPlace("Images", Place(new File(Imcms.getPath, "images")))
    browser.addPlace("Conf", Place(new File(Imcms.getPath, "WEB-INF/conf")))
    browser.addPlace("Logs", Place(new File(Imcms.getPath, "WEB-INF/logs")))
  }

  val ui = letret(new FileManagerUI(browser.ui)) { ui =>
    ui.miDelete setCommand block {
      let(browser.dirContentSelection.items) {
        case fsNodes if fsNodes.nonEmpty =>
          app.initAndShow(new ConfirmationDialog("Delete selected items")) { dlg =>
            dlg setOkHandler {
              fsNodes foreach (_.delete)
              browser.reloadDirContent()
            }
          }

        case _ =>
      }

      browser.dirContentSelection.items foreach { fsNode =>

      }
    }

    ui.miCopy setCommand block {
      browser.dirContentSelection.items foreach { file =>
      }
    }

    ui.miMove setCommand block {
      browser.dirContentSelection.items foreach { file =>
      }
    }

    ui.miView setCommand block {
      browser.dirContentSelection.first foreach { file =>
      }
    }

    ui.miEdit setCommand block {
      browser.dirContentSelection.first foreach { file =>
      }
    }

    ui.miUpload setCommand block {
    }

    ui.miDownload setCommand block {
      browser.dirContentSelection.first foreach { file =>
      }
    }

    ui.miReload setCommand block {
      //reload tree content?
      browser.reloadDirContent
    }
  }
}


class FileManagerUI(browserUI: FileBrowserUI) extends VerticalLayout with Spacing with FullSize {
  val mb = new MenuBar
  val miReload = mb.addItem("Reload")
  val miView = mb.addItem("View", null)
  val miEdit = mb.addItem("Edit", null)
  val miCopy = mb.addItem("Copy", null)
  val miMove = mb.addItem("Move", null)
  val miDelete = mb.addItem("Delete", null)
  val miDownload = mb.addItem("Download", null)
  val miUpload = mb.addItem("Uploa, null)d", null)

  addComponents(this, mb, browserUI)
  setExpandRatio(browserUI, 1.0f)
}


//      lytButtons.btnReload addListener block { fileBrowser.reload()}
//      lytButtons.btnCopy addListener block {
//        initAndShow(new OkCancelDialog("Copy to - choose destination directory")
//            with CustomSizeDialog with BottomMarginDialog, resizable = true) { w =>
//          let(w.mainContent = new FileBrowser) { b =>
//            b setSplitPosition 30
//            b addDirectoryTree("Home", Imcms.getPath)
//            b addDirectoryTree("Templates", new File(Imcms.getPath, "WEB-INF/templates/text"))
//            b addDirectoryTree("Images", new File(Imcms.getPath, "images"))
//            b addDirectoryTree("Conf", new File(Imcms.getPath, "WEB-INF/conf"))
//            b addDirectoryTree("Logs", new File(Imcms.getPath, "WEB-INF/logs"))
//          }
//
//          w setWidth "600px"
//          w setHeight "400px"
//        }
//      }