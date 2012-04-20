package com.imcode
package imcms.dao

import scala.collection.JavaConversions._
import com.imcode.imcms.api.I18nLanguage
import com.imcode.imcms.api.TextHistory
import imcode.server.document.textdocument.TextDomainObject

import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = Array(classOf[Throwable]))
class TextDao extends HibernateSupport {

  /** Inserts or updates text. */
  //@Transactional
  def saveText(text: TextDomainObject) = hibernate.saveOrUpdate(text)


  //@Transactional
  def getTextById(id: JLong) = hibernate.get[TextDomainObject](id)


  //@Transactional
  def deleteTexts(docId: JInteger, docVersionNo: JInteger, language: I18nLanguage): Int =
    deleteTexts(docId, docVersionNo, language.getId)


  //@Transactional
  def deleteTexts(docId: JInteger, docVersionNo: JInteger, languageId: JInteger) = hibernate.bulkUpdateByNamedQueryAndNamedParams(
    "Text.deleteTexts",
    "docId" -> docId, "docVersionNo" -> docVersionNo, "languageId" -> languageId
  )


  //@Transactional
  def saveTextHistory(textHistory: TextHistory) = hibernate.save(textHistory)


  /**
   * @param docId
   * @param docVersionNo
   *
   * @return all texts in a doc.
   */
  //@Transactional
  def getTexts(docId: JInteger, docVersionNo: JInteger) = hibernate.listByNamedQueryAndNamedParams[TextDomainObject](
    "Text.getByDocIdAndDocVersionNo", "docId" -> docId, "docVersionNo" -> docVersionNo
  )

  /**
   * Returns text fields for the same doc, version and language.
   */
  //@Transactional
  def getTexts(docId: JInteger, docVersionNo: JInteger, languageId: JInteger): JList[TextDomainObject] =
    hibernate.listByNamedQueryAndNamedParams[TextDomainObject](
      "Text.getByDocIdAndDocVersionNoAndLanguageId",
      "docId" -> docId, "docVersionNo" -> docVersionNo, "languageId" -> languageId
    )

  /**
   * Returns text fields for the same doc, version and language.
   */
  //@Transactional
  def getTexts(docId: JInteger, docVersionNo: JInteger, language: I18nLanguage): JList[TextDomainObject] =
    getTexts(docId, docVersionNo, language.getId)
}