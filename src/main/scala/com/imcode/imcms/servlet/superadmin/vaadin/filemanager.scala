package com.imcode.imcms.servlet.superadmin.vaadin.filemanager

import java.lang.{Class => JClass, Boolean => JBoolean, Integer => JInteger, Long => JLong}
import scala.collection.JavaConversions._
import com.imcode._
import com.vaadin.event.ItemClickEvent
import com.vaadin.terminal.gwt.server.WebApplicationContext
import com.vaadin.ui._
import com.vaadin.data.Property
import com.vaadin.data.Property._
import com.imcode.imcms.dao.{MetaDao, SystemDao, LanguageDao, IPAccessDao}
import imcms.api.{CategoryType, SystemProperty, IPAccess, Document}
import imcms.mapping.CategoryMapper
import imcms.servlet.superadmin.AdminSearchTerms
import com.imcode.imcms.api.Document.PublicationStatus
import com.vaadin.terminal.UserError
import imcode.util.Utility
import imcode.server.user._
import imcode.server.{SystemData, Imcms}
import java.util.{Date, Collection => JCollection}
import com.vaadin.ui.Layout.MarginInfo
import java.io.{OutputStream, FileOutputStream, File}
import com.imcode.imcms.servlet.superadmin.vaadin.ui._
import com.imcode.imcms.servlet.superadmin.vaadin.ui.UI._
import com.imcode.imcms.servlet.superadmin.vaadin.ui.AbstractFieldWrapper._
import java.util.concurrent.atomic.AtomicReference
import imcode.server.document.{CategoryDomainObject, CategoryTypeDomainObject, DocumentDomainObject}
import scala.actors.Actor._
import scala.actors._
import imcode.server.document.textdocument.TextDocumentDomainObject

class DirectoryTree(val root: File) extends Tree {
  def reload() {
    require(root.isDirectory,
      "Tree root [%s] does not exists or not a directory." format root.getAbsoluteFile)

    getItemIds foreach (collapseItem(_)) // workaround; without collapsing root remains expanded
    removeAllItems()
    addDir(root)
    expandItem(root)
  }

  def addDir(dir: File, parent: Option[File] = None) {
    addItem(dir)
    setItemCaption(dir, "/"+dir.getName)
    setChildrenAllowed(dir, dir.listFiles.filter(_.isDirectory).length > 0)

    parent match {
      case Some(parentDir) => setParent(dir, parentDir)
      case _ =>
    }
  }

  addListener(new Tree.ExpandListener {
    def nodeExpand(e: Tree#ExpandEvent) = e.getItemId match {
      case dir: File => dir.listFiles filter (_.isDirectory) foreach (addDir(_, Some(dir)))
    }
  })
  
  setImmediate(true)
  reload()
}


class DirectoryContentTable extends Table {
  def reload(dir: Option[File]) {
    removeAllItems
    dir match {
      case Some(dir) => dir.listFiles filter (_.isFile) foreach { file =>
        addItem(Array(file.getName, new Date(file.lastModified), Long box file.length, "-"), file)
      }

      case _ =>
    }
  }

  addContainerProperty("Name", classOf[String], null)
  addContainerProperty("Date modified", classOf[Date], null)
  addContainerProperty("Size", classOf[JLong], null)
  addContainerProperty("Kind", classOf[String], null)

  setImmediate(true)
  setSizeFull  
}


class FileBrowser extends SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL) {
  val tblDirContent = new DirectoryContentTable
  val accDirTrees = new Accordion {
    addListener(new TabSheet.SelectedTabChangeListener {
      def selectedTabChange(e: TabSheet#SelectedTabChangeEvent) {
        getSelectedTab match {
          case dirTree: DirectoryTree => dirTree.getValue match {
            case dir: File => tblDirContent reload Some(dir)
            case _ => dirTree select dirTree.getItemIds.head
          }
          
          case _ => tblDirContent reload None
        }
      }
    })

    setSizeFull
  }

  val dirTreeValueChangeListener = new ValueChangeListener {
    def valueChange(e: ValueChangeEvent) = e.getProperty.getValue match {
        case dir: File => tblDirContent reload Some(dir)
        case _ => tblDirContent reload None
    }
  }

  def addDirectoryTree(root: File, caption: String, icon: com.vaadin.terminal.Resource=null) =
    letret(new DirectoryTree(root)) { dirTree =>
      dirTree addListener dirTreeValueChangeListener
      accDirTrees addTab (dirTree, caption, icon)
    }

  def reload() {
    accDirTrees.getComponentIterator foreach {
      case dirTree: DirectoryTree => dirTree.reload
      case _ =>
    }

    accDirTrees.getComponentIterator.toStream.headOption match {
      case Some(c) => accDirTrees setSelectedTab c
      case _ =>
    }
  }

  setFirstComponent(accDirTrees)
  setSecondComponent(tblDirContent)

  setSplitPosition(15)
  setSizeFull
}


//class ImagePreview extends 