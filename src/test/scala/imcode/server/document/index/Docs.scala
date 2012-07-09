package imcode.server.document.index

import com.imcode.{when => _, _}
import scala.collection.JavaConverters._
import imcode.server.document.textdocument.TextDocumentDomainObject
import imcode.server.user.RoleId
import com.imcode.imcms.api.I18nLanguage
import org.scalatest.mock.MockitoSugar._
import com.imcode.imcms.mapping.{CategoryMapper, DocumentMapper}
import scala.collection.mutable.{Map => MMap}
import imcode.server.document.{DocumentDomainObject, CategoryDomainObject, DocumentPermissionSetTypeDomainObject}
import org.mockito.Matchers._
import org.mockito.Mockito.{mock => _, _}
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock
import com.imcode.imcms.mapping.DocumentMapper.TextDocumentMenuIndexPair
import imcode.server.document.index.solr.{DocumentContentIndexer, DocumentIndexer}
import imcode.server.ImcmsServices
import com.imcode.imcms.test._
import com.imcode.imcms.test.fixtures.{DocFX, LanguageFX}



class DocIndexingMocksSetup {

  case class ParentDoc(docId: Int, menuNo: Int)

  private val documentMapperMock = mock[DocumentMapper]
  private val categoryMapperMock = mock[CategoryMapper]
  private val servicesMock = mock[ImcmsServices]

  private val docs = MMap.empty[Int, MMap[I18nLanguage, DocumentDomainObject]].withDefaultValue(MMap.empty.withDefaultValue(null))
  private val categories = MMap.empty[Int, CategoryDomainObject]
  private val parentDocs = MMap.empty[Int, Seq[ParentDoc]].withDefaultValue(Seq.empty)

  when(documentMapperMock.getImcmsServices).thenReturn(servicesMock)

  when(servicesMock.getI18nSupport).thenReturn(LanguageFX.mkI18nSupport())

  when(categoryMapperMock.getCategories(anyCollectionOf(classOf[JInteger]))).thenAnswer {
    invocation: InvocationOnMock =>
      val availableCategories = for {
        categoryId <- invocation.getArguments()(0).asInstanceOf[JCollection[JInteger]].asScala
        category <- categories.get(categoryId)
      } yield category

      availableCategories.toSet.asJava
  }

  when(documentMapperMock.getDefaultDocument(anyInt, any[I18nLanguage])).thenAnswer {
    invocation: InvocationOnMock => invocation.getArguments match {
      case Array(id: JInteger, language: I18nLanguage) => docs(id)(language)
    }
  }

  when(documentMapperMock.getAllDocumentIds).thenAnswer {
    _: InvocationOnMock => docs.keys.map(Int.box).toList.asJava
  }

  when(documentMapperMock.getDocumentMenuPairsContainingDocument(any[DocumentDomainObject])).thenAnswer {
    invocation: InvocationOnMock => invocation.getArguments match {
      case Array(doc: DocumentDomainObject) =>
        parentDocs(doc.getId).toArray.map {
          case ParentDoc(docId, menuNo) => new TextDocumentMenuIndexPair(new TextDocumentDomainObject(docId), menuNo)
        }
    }
  }


  val docIndexer = new DocumentIndexer |>> { di =>
    di.documentMapper = documentMapperMock
    di.categoryMapper = categoryMapperMock
    di.contentIndexer = new DocumentContentIndexer
  }

  // DocumentIndexer uses category id, name and type id, name as string index fields
  def addCategories(categories: CategoryDomainObject*) = this |>> { _ =>
    for (category <- categories) this.categories(category.getId) = category
  }

  // DocumentIndexer uses parent doc id and menu id as index fields
  def addParentDocumentsFor(docId: Int, pds: ParentDoc*) = this |>> { _ =>
    parentDocs(docId) = pds
  }

  def addDocument(doc: DocumentDomainObject) = this |>> { _ =>
    docs.get(doc.getId) |> {
      case None => MMap.empty[I18nLanguage, DocumentDomainObject].withDefaultValue(null) |>> { langToDoc =>
        docs(doc.getId) = langToDoc
      }

      case Some(langToDoc) => langToDoc
    } |> { langToDoc =>
      langToDoc.put(doc.getLanguage, doc)
    }
  }

  def addDocuments(docs: Seq[DocumentDomainObject]) = this |>> { _ =>
    docs foreach addDocument
  }

  // getDocumentMenuPairsContainingDocument
}