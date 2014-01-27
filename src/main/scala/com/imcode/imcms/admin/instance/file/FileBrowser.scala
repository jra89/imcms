package com.imcode
package imcms
package admin.instance.file

import com.vaadin.ui.themes.Reindeer
import scala.collection.JavaConverters._
import com.vaadin.ui._
import scala.collection.mutable.{Map => MMap}
import com.vaadin.data.util.FilesystemContainer
import java.io.{FilenameFilter, File}
import java.util.concurrent.atomic.AtomicReference
import com.imcode.util.event.Publisher
import imcode.server.Imcms
import com.imcode.imcms.vaadin.component._
import com.imcode.imcms.vaadin.event._
import com.imcode.imcms.vaadin.data._
import com.vaadin.server.Resource
import com.vaadin.event.ItemClickEvent


/** Hierarchical filesystem (non-hidden dirs) container with a single root. */
class LocationTreeContainer(root: File) extends FilesystemContainer(root) {

  import java.util.Collections._

  setFilter(new FilenameFilter {
    def accept(file: File, name: String) = new File(file, name) |> { fsNode =>
      fsNode.isDirectory && !fsNode.isHidden
    }
  })

  override def rootItemIds() = root |> singleton[File] |> unmodifiableCollection[File]

  override def addRoot(root: File) = sys.error("Operation is not allowed.")
}


/** Predefined dir items filters. */
object LocationItemsFilter {

  import scala.util.matching.Regex

  /** Creates a compund filter from a sequence of filters. */
  def apply(filter: File => Boolean, filters: (File => Boolean)*) = (file: File) => filter +: filters forall { _ apply file }

  val notHidden = apply(!_.isHidden)

  //val fileOnly = apply(notHidden, _.isFile)

  def nameRE(re: Regex)(fsNode: File) = re.unapplySeq(fsNode.getName).isDefined

  def fileWithExt(ext: String, exts: String*) = nameRE("""(?i).*\.(%s)""".format((ext +: exts).mkString("|")).r)_

  val imageFile = fileWithExt("png", "gif", "jpg", "jpeg")

  val templateFile = fileWithExt("jsp", "jspx", "html")
}


/**
 * File browser location (bookmark) conf.
 */
case class LocationConf(dir: File, relativePath: String = "", itemsFilter: File => Boolean = LocationItemsFilter.notHidden, recursive: Boolean = true)


case class LocationSelection(dir: File, items: Seq[File]) {
  def firstItem = items.headOption
  def hasItems = items.nonEmpty
  def hasSingleItem = items.size == 1
}


object ImcmsFileBrowser {

  def addLocation(captionResourceId: String, conf: LocationConf, image: Option[Resource])(browser: FileBrowser) =
    browser |>> { _ => browser.addLocation(captionResourceId.i, conf, image) }

  val addHomeLocation = addLocation("file_browser.location.home", LocationConf(Imcms.getPath), Theme.Icon.Browser.TabHome32.asOption)_

  val addImagesLocation =
    addLocation("file_browser.location.images", LocationConf(Imcms.getPath, "images"), Theme.Icon.Browser.TabImages32.asOption)_

  val addTemplatesLocation =
    addLocation("file_browser.location.templates", LocationConf(Imcms.getPath, "WEB-INF/templates/text"), Theme.Icon.Browser.TabTemplates32.asOption)_

  val addLogsLocation =
    addLocation("file_browser.location.logs", LocationConf(Imcms.getPath, "WEB-INF/logs"), Theme.Icon.Browser.TabLogs32.asOption)_

  val addConfLocation =
    addLocation("file_browser.location.conf", LocationConf(Imcms.getPath, "WEB-INF/conf"), Theme.Icon.Browser.TabConf32.asOption)_

  val addAllLocations =
    Function.chain(Seq(addHomeLocation, addImagesLocation, addTemplatesLocation, addLogsLocation, addConfLocation))
}



/**
 * A file browser can have any number of locations (bookmarks).
 * A location is uniquely identified by its root dir.
 */
class FileBrowser(val isSelectable: Boolean = true, val isMultiSelect: Boolean = false)
extends Publisher[Option[LocationSelection]] {

  type Location = (LocationTree, LocationItems)
  type LocationRoot = File
  type Tab = Component // location tree ui

  private val locationRootToConf = MMap.empty[LocationRoot, LocationConf]
  private val tabsToLocations = MMap.empty[Tab, Location]

  /** Current (visible) location. */
  private val locationRef = new AtomicReference(Option.empty[Location])

  /** Selection in a current location */
  private val selectionRef = new AtomicReference(Option.empty[LocationSelection])

  val view = new FileBrowserView |>> { w =>
    w.accLocationTrees.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener {
      def selectedTabChange(e: TabSheet.SelectedTabChangeEvent) {
        val locationOpt = tabsToLocations.get(e.getTabSheet.getSelectedTab)
        locationRef.set(locationOpt)

        for ((locationTree, locationItems) <- locationOpt) {
          w.spLocation.setSecondComponent(locationItems.view)
          updateSelection(locationTree, locationItems)
        }
      }
    })
  }

  def addLocation(caption: String, conf: LocationConf, icon: Option[Resource] = None) {
    val locationRoot = new File(conf.dir, conf.relativePath)
    val locationTree = new LocationTree(locationRoot)
    val locationItems = new LocationItems(conf.itemsFilter, isSelectable, isMultiSelect)

    locationTree.view.addValueChangeHandler { _ =>
      locationTree.view.firstSelected.asOption match {
        case Some(dir) =>
          locationItems.reload(dir)
          Some(LocationSelection(dir, Nil)) |> { selection =>
            selectionRef.set(selection)
            notifyListeners(selection)
          }

        case _ =>
          locationItems.view.removeAllItems()

          None |> { selection =>
            selectionRef.set(selection)
            notifyListeners(selection)
          }
      }
    }

    locationItems.view.addValueChangeHandler { _ => updateSelection(locationTree, locationItems) }

    locationItems.view.addItemClickHandler { event: ItemClickEvent =>
      if (event.isDoubleClick) {
        event.getItemId match {
          case item: File if item.isDirectory => locationTree.selection = item
          case _ =>
        }
      }
    }

    locationTree.reload()

    locationRootToConf(locationRoot) = conf
    tabsToLocations(locationTree.view) = (locationTree, locationItems)
    view.accLocationTrees.addTab(locationTree.view, caption, icon.orNull)
  }

  private def updateSelection(locationTree: LocationTree, locationItems: LocationItems) {
    locationTree.view.firstSelected.asOption match {
      case Some(dir) =>
        Some(LocationSelection(dir, locationItems.selection)) |> { selection =>
          selectionRef.set(selection)
          notifyListeners(selection)
        }

      case _ =>
        None |> { selection =>
          selectionRef.set(selection)
          notifyListeners(selection)
        }
    }
  }

  override def notifyListeners() = notifyListeners(selection)

  /** Returns selection in a current location. */
  def selection = selectionRef.get

  /** Returns current (visible) location */
  def location = locationRef.get

  def locations: Map[File, Location] =
    tabsToLocations.values.map {
      case loc @ (locationTree, _) => locationTree.root.getCanonicalFile -> loc
    }.toMap

  /** Returns location by its root. */
  def location(root: File): Option[Location] = locations.get(root.getCanonicalFile)

  /** Reloads current location. */
  def reloadLocation(preserveTreeSelection: Boolean = true) =
    for ((locationTree, _) <- location; dir = locationTree.view.firstSelected) {
      locationTree.reload()
      if (preserveTreeSelection && dir.isDirectory) locationTree.selection = dir
    }

  /** Reloads current location's items. */
  def reloadLocationItems() =
    for ((locationTree, locationItems) <- location; dir = locationTree.view.firstSelected)
      locationItems.reload(dir)


  /**
   * Changes current selection.
   * Also changes current location if its root is other than provided locationRoot.
   */
  def select(locationRoot: File, dir: File, items: Seq[File] = Nil): Unit =
    select(locationRoot, new LocationSelection(dir, items))

  /**
   * Changes current selection.
   * Also changes current location if its root is other than provided locationRoot.
   */
  def select(locationRoot: File, locationSelection: LocationSelection) =
    tabsToLocations.find {
      case (_, (locationTree, _)) => locationTree.root.getCanonicalFile == locationRoot.getCanonicalFile
    } foreach {
      case (tab, (locationTree, locationItems)) =>
        view.accLocationTrees.setSelectedTab(tab)
        locationTree.selection = locationSelection.dir
        if (isSelectable) locationItems.view.selection = locationSelection.items
    }

  // primary constructor
  listen { e =>
    view.lblSelectionPath.value = {
      val pathOpt =
        for {
          LocationSelection(dir, items) <- e
          (locationTree, _) <- location
          conf <- locationRootToConf.get(locationTree.root)
          confParent = conf.dir.getParentFile.asOption.map(_.getCanonicalFile).orNull
          dirs = Iterator.iterate(dir)(_.getParentFile).takeWhile(_.getCanonicalFile != confParent).toList.reverse
          dirPath = dirs.map(_.getName).mkString("", "/", "/")
        } yield {
          dirPath + (items match {
            case Nil => ""
            case Seq(item) => item.getName
            case _ => "..."
          })
        }

      pathOpt getOrElse ""
    }
  }

  notifyListeners()
}


class FileBrowserView extends VerticalLayout with Spacing with FullSize {
  val spLocation = new HorizontalSplitPanel with FullSize |>> { _.addStyleName(Reindeer.SPLITPANEL_SMALL) }
  val accLocationTrees = new Accordion with FullSize
  val lblSelectionPath = new Label

  spLocation.setFirstComponent(accLocationTrees)
  spLocation.setSplitPosition(15)

  addComponents(spLocation, lblSelectionPath)
  setExpandRatio(spLocation, 1.0f)
}


/** Select item can be a dir or a file. */
trait FSItemIcon extends AbstractSelect {
  override def getItemIcon(itemId: AnyRef) =
    if (itemId.asInstanceOf[File].isDirectory) Theme.Icon.Folder16 else Theme.Icon.File16
}


class LocationTree(val root: File) {
  val view = new Tree with SingleSelect[File] with Immediate with NoNullSelection with FSItemIcon

  def reload() {
    view.setContainerDataSource(new LocationTreeContainer(root.getCanonicalFile))
    view.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM)
    selection = root
  }

  def selection_=(dir: File) = dir.getCanonicalFile |> { dir =>
    view.select(dir)
    view.expandItem(if (dir == root) dir else dir.getParentFile)
  }

  def selection = view.firstSelected
}


class LocationItems(filter: File => Boolean, selectable: Boolean, multiSelect: Boolean) {

  val view = new Table with SelectWithTypedItemId[File] with FSItemIcon with Immediate with FullSize |>> { w =>
    w.setSelectable(selectable)
    w.setMultiSelect(multiSelect)

    addContainerProperties(w,
      PropertyDescriptor[String]("file_browser.items.col.name".i),
      PropertyDescriptor[String]("file_browser.items.col.modified".i),
      PropertyDescriptor[String]("file_browser.items.col.size".i),
      PropertyDescriptor[String]("file_browser.items.col.kind".i)
    )

    import Table._
    w.setColumnAlignments(Align.LEFT, Align.RIGHT, Align.RIGHT, Align.RIGHT)
    w.setRowHeaderMode(RowHeaderMode.ICON_ONLY)
    w.setColumnExpandRatio("file_browser.items.col.name", 1f)
    w.setStyleName(Reindeer.TABLE_BORDERLESS)
  }

  /** Populates table with dir items. */
  def reload(dir: File) {
    val base = 1024
    val baseFn = java.lang.Math.pow(1024, _: Double).toInt
    val (dirs, files) = dir.listFiles.partition(_.isDirectory)
    def lastModified(file: File) = "file_browser.items.col.modified.fmt".f(file.lastModified.asInstanceOf[AnyRef])

    view.removeAllItems()

    dirs.sortWith((d1, d2) => d1.getName.compareToIgnoreCase(d2.getName) < 0).foreach { dir =>
      view.addItem(Array[AnyRef](dir.getName, lastModified(dir), "--", "file_browser.items.col.kind.dir".i), dir)
    }

    for (file <- files.sortWith((f1, f2) => f1.getName.compareToIgnoreCase(f2.getName) < 0) if filter(file)) {
      view.addItem(
        Array[AnyRef](
          file.getName, lastModified(file), FileProperties.sizeAsString(file), "file_browser.items.col.kind.file".i
        ),
        file)
    }
  }

  def selection = view.selection
}


object FileProperties {

  private def powOf1024(pow: Int) = java.lang.Math.pow(1024, pow).toLong
  private val bytesInKb = powOf1024(1)
  private val bytesInMb = powOf1024(2)
  private val bytesInGb = powOf1024(3)

  def sizeAsString(file: File): String = sizeAsString(file.length)

  def sizeAsString(bytesInFile: Long): String = {
    val (size, units) = bytesInFile match {
      case bytesCount if bytesCount < bytesInKb => (0, "KB")
      case bytesCount if bytesCount < bytesInMb => (bytesCount / bytesInKb, "KB")
      case bytesCount if bytesCount < bytesInGb => (bytesCount / bytesInMb, "MB")
      case bytesCount => (bytesCount / bytesInGb, "GB")
    }

    s"$size $units"
  }
}