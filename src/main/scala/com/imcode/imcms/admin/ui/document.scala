package com.imcode.imcms.admin.ui

import com.imcode.imcms.servlet.superadmin.vaadin.template._
import java.lang.{Class => JClass, Boolean => JBoolean, Integer => JInteger}
import scala.collection.JavaConversions._
import com.imcode._
import com.vaadin.event.ItemClickEvent
import com.vaadin.ui._
import com.vaadin.data.Property
import com.vaadin.data.Property._
import com.imcode.imcms.dao.{MetaDao, SystemDao, LanguageDao, IPAccessDao}
import imcms.api._
import imcms.mapping.CategoryMapper
import imcms.servlet.superadmin.AdminSearchTerms
import com.imcode.imcms.api.Document.PublicationStatus
import imcms.servlet.superadmin.vaadin.ChatTopic.Message
import imcms.servlet.superadmin.vaadin.permissions.{UserUI, UsersView}
import imcode.util.Utility
import imcode.server.user._
import imcode.server.{SystemData, Imcms}
import java.util.{Date, Collection => JCollection}
import com.vaadin.ui.Layout.MarginInfo
import com.imcode.imcms.servlet.superadmin.vaadin.ui._
import com.imcode.imcms.servlet.superadmin.vaadin.ui.AbstractFieldWrapper._
import java.util.concurrent.atomic.AtomicReference
import scala.actors.Actor._
import scala.actors._
import imcode.server.document.textdocument.TextDocumentDomainObject
import java.io.{ByteArrayInputStream, OutputStream, FileOutputStream, File}
import com.vaadin.terminal.{ThemeResource, UserError}
import scala.collection.mutable.{Map => MMap}
import imcode.server.document._

class MetaModel(val meta: Meta,
                val defaultLanguage: I18nLanguage,
                val languages: MMap[I18nLanguage, Boolean],
                val labels: Map[I18nLanguage, I18nMeta],
                val versionInfo: Option[DocumentVersionInfo] = Option.empty) {

  val isNew = versionInfo.isEmpty
}


class FlowUI(page: Component, pages: Component*) extends VerticalLayout {
  addComponent(page)



//  def firstPage: Component
//  def lastPage = firstPage

//  def setOkButton(b: Button): Unit
//  def setCancelButton(b: Button): Unit
//  def setNextButton(b: Button): Unit
//  def setPrevButton(b: Button): Unit
}


class DocFlowFactory(app: VaadinApplication) {

  def editDocFlow: FlowUI = error("Not implemented")

  def newDocFlow(docType: DocumentTypeDomainObject, parentDoc: DocumentDomainObject): FlowUI = {
    val doc = Imcms.getServices.getDocumentMapper.createDocumentOfTypeFromParent(docType.getId, parentDoc, Imcms.getServices.getImcmsAuthenticatorAndUserAndRoleMapper.getUser(UserDomainObject.DEFAULT_USER_ID))
    val defaultLanguage = Imcms.getI18nSupport.getDefaultLanguage
    val availableLanguages = Imcms.getI18nSupport.getLanguages
    val languages = availableLanguages.zip(Stream.continually(false)).toMap.updated(defaultLanguage, true)
    val labels = availableLanguages map { language =>
      let(new I18nMeta) { labels =>
        labels.setHeadline("")
        labels.setMenuText("")
        labels.setMenuImageURL("")
        labels.setLanguage(language)

        language -> labels
      }
    } toMap

    val metaModel = new MetaModel(
      doc.getMeta,
      Imcms.getI18nSupport.getDefaultLanguage,
      MMap(languages.toSeq : _*),
      labels
    )

    docType match {
      case DocumentTypeDomainObject.TEXT =>
        val metaMVC = new MetaMVC(app, metaModel)

        new FlowUI(metaMVC.view)
        // setOkButton, setFlowButtons....

      case otherType => error("Not implemented. doc type: " + otherType)
    }
  }
}


class MetaMVC(val app: VaadinApplication, val metaModel: MetaModel) {
  
  val view = createView 
  
//  def addLanguage(la: LanguagesArea) = {}
//  def setActiveLanguages(la: LanguagesArea, languages: Seq[I18nLanguage]) = {}

  def createView = letret(new MetaLyt) { v =>
    for {
      (language, enabled) <- metaModel.languages
      labels = metaModel.labels(language)
    } {     
      val lytLabels = letret(new LabelsLyt) { l =>        
        l.txtTitle setValue labels.getHeadline
        l.txtMenuText  setValue labels.getMenuText
      }

      let(v.lytI18n.tsLabels.addTab(lytLabels)) { tab =>
        if (Imcms.getI18nSupport.isDefault(language)) {
          tab.setCaption(language.getName + " (default)")  
        } else {
          tab.setCaption(language.getName)
          tab.setEnabled(enabled)
        }
      }
    }

    v.lytI18n.btnSettings addListener unit {
      app.initAndShow(new OkCancelDialog("Settings")) { w =>
        val content = new I18nSettingsDialogContent

        for ((language, enabled) <- metaModel.languages) {
          val chkLanguage = new CheckBox(language.getName) {
            setValue(enabled)
            setEnabled(!Imcms.getI18nSupport.isDefault(language))
            // add listner - disable tab
          }
          content.lytLanguages.addComponent(chkLanguage)
        }

        w.setMainContent(content)
      }
    }

    v.lytSearch.lytKeywords.btnEdit addListener unit {
      app.initAndShow(new OkCancelDialog("Keywords")) { w =>
        val content = new KeywordsDialogContent(List("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Fi", "Lambda"))
        
        w setMainContent content
        w addOkButtonClickListener unit {
          content.txtKeyword setValue content.lstKeywords.asList[String].mkString(", ") 
        }
      }
    }

    v.lytCategories.btnEdit addListener unit {
      app.initAndShow(new OkCancelDialog("Categories")) { w =>
        val mainContent = new CategoriesDialogContent
        
        let(w.setMainContent(mainContent)) { c =>
          c.setHeight("250px")
        }
      }
    }

    v.lytPublication.btnChoosePublisher addListener unit {
      app.initAndShow(new OkCancelDialog("Publisher")) { w =>
        w.setMainContent(new UserUI)
      }      
    }
  }
}


class I18nSettingsDialogContent extends FormLayout {
  val ogDisabledShowMode = new OptionGroup(
    "When disabled",
    List("Show in default language", "Show 'Not found' page")
  )

  val lytLanguages = new VerticalLayout with UndefinedSize {
    setCaption("Enabled languages")
  }

  addComponents(this, lytLanguages, ogDisabledShowMode)
}


class LabelsLyt extends FormLayout with NoSpacing with UndefinedSize {
  val txtTitle = new TextField("Title")
  val txtMenuText = new TextField("Menu text")
  val embLinkImage = new TextField("Link image")

  addComponents(this, txtTitle, txtMenuText, embLinkImage)
}


class PublicationLyt extends GridLayout(2, 4) with Spacing {
  val lblPublisher = new Label("Publisher") with UndefinedSize
  val lblPublisherName = new Label("No publisher selected") with UndefinedSize
  val btnChoosePublisher = new Button("...") with LinkStyle

  val lytPublisher = new HorizontalLayout with Spacing {
    addComponents(this, lblPublisherName, btnChoosePublisher)    
  }

  val lblStatus = new Label("Status") with UndefinedSize
  val sltStatus = new Select with NoNullSelection {
    addItem("Approved")
    addItem("Disapproved")
    select("Disapproved")
  }

  val lblVersion = new Label("Version") with UndefinedSize
  val sltVersion = new Select with NoNullSelection {
    addItem("Working")
    select("Working")
  }

  val calStart = new DateField { setValue(new Date) }
  val calEnd = new DateField
  val chkStart = new CheckBox("Start date") with Disabled { setValue(true) } // decoration, always disabled
  val chkEnd = new CheckBox("End date") with Immediate {
    setValue(false)
  }
  
  val frmSchedule = new Form with UndefinedSize {
    setCaption("Schedule")
    let(new GridLayout(2, 2) with Spacing) { lyt =>
      addComponents(lyt, chkStart, calStart, chkEnd, calEnd)
      setLayout(lyt)
    }
  }

  addComponents(this, lblStatus, sltStatus, lblVersion, sltVersion, lblPublisher, lytPublisher)
  addComponent(frmSchedule, 0, 3, 1, 3)
}


class KeywordsDialogContent(keywords: Seq[String] = Nil) extends GridLayout(3,2) with Spacing {

  type ItemIds = JCollection[String]

  val lstKeywords = new ListSelect with MultiSelect with Immediate with NullSelection {
    setRows(10)
    setColumns(10)
  }

  val btnAdd = new Button("+")
  val btnRemove = new Button("-")
  val txtKeyword = new TextField {
    setInputPrompt("New keyword")
  }

  addComponent(txtKeyword, 0, 0)
  addComponent(btnAdd, 1, 0)
  addComponent(btnRemove, 2, 0)
  addComponent(lstKeywords, 0, 1, 2, 1)

  btnAdd addListener unit {
    txtKeyword.stringValue.trim.toLowerCase match {
      case value if value.length > 0 && lstKeywords.getItem(value) == null =>
        setKeywords(value :: lstKeywords.getItemIds.asInstanceOf[ItemIds].toList)
      case _ =>
    }

    txtKeyword setValue ""
  }

  btnRemove addListener unit {
    whenSelected[ItemIds](lstKeywords) { _ foreach (lstKeywords removeItem _) }
  }

  lstKeywords addListener unit {
    lstKeywords.getValue.asInstanceOf[ItemIds].toList match {
      case List(value) => txtKeyword setValue value
      case List(_, _, _*) => txtKeyword setValue ""
      case _ =>
    }
  }

  setKeywords(keywords)
  
  def setKeywords(keywords: Seq[String]) {
    lstKeywords.removeAllItems
    keywords.map(_.toLowerCase).sorted.foreach { lstKeywords addItem _ }
  }
}


class CategoriesDialogContent extends Panel {
  setStyleName(Panel.STYLE_LIGHT)

  val lytContent = new FormLayout

  setContent(lytContent)

  for {
    categoryType <- Imcms.getServices.getCategoryMapper.getAllCategoryTypes
    categories = Imcms.getServices.getCategoryMapper.getAllCategoriesOfType(categoryType)
    if categories.nonEmpty
  } {
    val sltCategory =
      if (categoryType.isSingleSelect) {
        letret(new Select) { slt =>
          slt.setNullSelectionAllowed(false)
          slt.setMultiSelect(false)

          categories foreach { c =>
            slt.addItem(c)
            slt.setItemCaption(c, c.getName)
          }
        }
      } else {
        letret(new TwinSelect[CategoryDomainObject]) { tws =>
          categories foreach { c =>
            tws.addAvailableItem(c, c.getName)
          }
        }
      }

    sltCategory.setCaption(categoryType.getName)

    lytContent.addComponent(sltCategory)
  }
}


class MetaLyt extends FormLayout with Margin {

  val lytIdentity = new HorizontalLayout with Spacing {
    val txtId = new TextField("Document Id") with Disabled
    val txtName = new TextField("Name")
    val txtAlias = new TextField("Alias")

    setCaption("Identity")    
    addComponents(this, txtId, txtName, txtAlias)
  }
  
  val lytI18n = new VerticalLayout with UndefinedSize {
    val tsLabels = new TabSheet with FullWidth
    val btnSettings = new Button("Configure...") with LinkStyle
    val chkCopyLabelsTextToPage = new CheckBox("Copy link heading & subheading to text 1 & text 2 in page")
                                                                                              
    setCaption("Appearence")
    addComponents(this, tsLabels, btnSettings, chkCopyLabelsTextToPage)
  }
  
  val lytLink = new VerticalLayout with Spacing {
    val chkOpenInNewWindow = new CheckBox("Open in new window")
    val chkShowToUnauthorizedUser = new CheckBox("Show to unauthorized user")

    setCaption("Link/menu item")
    addComponents(this, chkOpenInNewWindow, chkShowToUnauthorizedUser)
  }

  val lytSearch = new VerticalLayout with Spacing {
    val chkExclude = new CheckBox("Exclude this page from internal search")
    val lytKeywords = new HorizontalLayout with Spacing {
      val lblKeywords = new Label("Keywords")
      val txtKeywords = new TextField with Disabled { setColumns(30) }
      val btnEdit = new Button("Edit...") with LinkStyle

      addComponents(this, lblKeywords, txtKeywords, btnEdit)
    }

    setCaption("Search")
    addComponents(this, lytKeywords, chkExclude)
  }

  val lytCategories = new HorizontalLayout with Spacing {
    val lblCategories = new Label("Categories")
    val txtCategories = new TextField with Disabled { setColumns(30) }
    val btnEdit = new Button("Edit...") with LinkStyle
    
    addComponents(this, lblCategories, txtCategories, btnEdit)
  }

  val lytPublication = new PublicationLyt { setCaption("Publication") }

  forlet(lytIdentity, lytI18n, lytLink, lytSearch, lytCategories, lytPublication) { c =>
    c.setMargin(true)
    addComponent(c)
  }
}