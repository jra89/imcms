package com.imcode
package imcms.admin.doc.template
package group

import com.imcode.imcms.vaadin.Current
import scala.util.control.{Exception => Ex}
import scala.collection.JavaConverters._
import com.vaadin.ui._
import imcode.server.{Imcms}
import imcode.server.document.{TemplateGroupDomainObject}
import imcms.security.{PermissionDenied, PermissionGranted}
import com.imcode.imcms.vaadin.ui._
import com.imcode.imcms.vaadin.ui.dialog._
import com.imcode.imcms.vaadin.data._
import com.imcode.imcms.vaadin.event._
import com.imcode.imcms.vaadin.server._
import com.vaadin.server.Page

//todo: form check
//todo: duplicate save check!
//todo: internal error check
class TemplateGroupManager(app: UI) {
  private val templateMapper = Imcms.getServices.getTemplateMapper

  val ui = new TemplateGroupManagerUI |>> { ui =>
    ui.rc.btnReload.addClickHandler { _ => reload() }
    ui.tblGroups.addValueChangeHandler { _ => handleSelection() }

    ui.miNew.setCommandHandler { _ => editAndSave(new TemplateGroupDomainObject(0, null)) }
    ui.miEdit.setCommandHandler { _ =>
      whenSelected(ui.tblGroups) { id =>
        templateMapper.getTemplateGroupById(id.intValue) match {
          case null => reload()
          case vo => editAndSave(vo)
        }
      }
    }
    ui.miDelete.setCommandHandler { _ =>
      whenSelected(ui.tblGroups) { id =>
        new ConfirmationDialog("Delete selected template group?") |>> { dlg =>
          dlg.setOkButtonHandler {
            app.privileged(permission) {
              Ex.allCatch.either(templateMapper deleteTemplateGroup id.intValue) match {
                case Right(_) =>
                  Current.page.showInfoNotification("Template group has been deleted")
                case Left(ex) =>
                  Current.page.showErrorNotification("Internal error")
                  throw ex
              }

              reload()
            }
          }
        } |> Current.ui.addWindow
      }
    }
  } // ui

  reload()
  // END OF PRIMARY CONSTRUCTOR

  def canManage = Current.ui.imcmsUser.isSuperAdmin
  def permission = if (canManage) PermissionGranted else PermissionDenied("No permissions to manage template groups")

  /** Edit in a modal dialog. */
  private def editAndSave(vo: TemplateGroupDomainObject) {
    val id = vo.getId
    val isNew = id == 0
    val dialogTitle = if (isNew) "Create new template group" else "Edit template group"

    new OkCancelDialog(dialogTitle) |>> { dlg =>
      dlg.mainUI = new TemplateGroupEditorUI |>> { c =>
        c.txtId.value = if (isNew) "" else id.toString
        c.txtName.value = vo.getName.trimToEmpty
        templateMapper.getTemplatesInGroup(vo).asScala.foreach(template => c.twsTemplates.addChosenItem(template.getName))
        templateMapper.getTemplatesNotInGroup(vo).asScala.foreach(template => c.twsTemplates.addAvailableItem(template.getName))

        dlg.setOkButtonHandler {
          app.privileged(permission) {
            val voc = if (isNew) {
              templateMapper.createTemplateGroup(c.txtName.value)
              templateMapper.getTemplateGroupByName(c.txtName.value)
            } else vo.clone() |>> { voc =>
              templateMapper.renameTemplateGroup(voc, c.txtName.value)
            }

            templateMapper.getTemplatesInGroup(voc).asScala.foreach { template =>
              templateMapper.removeTemplateFromGroup(template, voc)
            }

            for {
              name <- c.twsTemplates.chosenItemIds
              template <- templateMapper.getTemplateByName(name).asOption
            } templateMapper.addTemplateToGroup(template, voc)

            reload()
          }
        }
      }
    } |> Current.ui.addWindow
  }

  def reload() {
    ui.tblGroups.removeAllItems
    for {
      vo <- templateMapper.getAllTemplateGroups
      id = vo.getId : JInteger
    } ui.tblGroups.addItem(Array[AnyRef](id, vo.getName, templateMapper.getTemplatesInGroup(vo).size : JInteger), id)

    canManage |> { value =>
      ui.tblGroups.setSelectable(value)
      Seq[{def setEnabled(e: Boolean)}](ui.miNew, ui.miEdit, ui.miDelete).foreach(_.setEnabled(value))   //ui.mb,
    }

    handleSelection()
  }

  private def handleSelection() {
    (canManage && ui.tblGroups.isSelected) |> { enabled =>
      Seq(ui.miEdit, ui.miDelete).foreach(_.setEnabled(enabled))
    }
  }
}


class TemplateGroupManagerUI extends VerticalLayout with Spacing with UndefinedSize {
  import Theme.Icon._

  val mb = new MenuBar
  val miNew = mb.addItem("Add new", New16)
  val miEdit = mb.addItem("Edit", Edit16)
  val miDelete = mb.addItem("Delete", Delete16)
  val miHelp = mb.addItem("Help", Help16)
  val tblGroups = new Table with SingleSelect[TemplateGroupId] with Selectable with Immediate
  val rc = new ReloadableContentUI(tblGroups)

  addContainerProperties(tblGroups,
    PropertyDescriptor[JInteger]("Id"),
    PropertyDescriptor[String]("Name"),
    PropertyDescriptor[JInteger]("Templates count"))

  this.addComponents(mb, rc)
}

class TemplateGroupEditorUI extends FormLayout with UndefinedSize {
  val txtId = new TextField("Id") with Disabled
  val txtName = new TextField("Name") with Required
  val twsTemplates = new TwinSelect[String]("Templates")

  this.addComponents(txtId, txtName, twsTemplates)
}