package com.imcode.imcms.persistence.repository;

import com.imcode.imcms.persistence.entity.LanguageJPA;
import com.imcode.imcms.persistence.entity.LoopEntryRefJPA;
import com.imcode.imcms.persistence.entity.TextJPA;
import com.imcode.imcms.persistence.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TextRepository extends JpaRepository<TextJPA, Integer>, VersionedContentRepository<TextJPA> {

    @Query("SELECT t FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2 AND t.loopEntryRef IS NULL")
    List<TextJPA> findByVersionAndLanguageWhereLoopEntryRefIsNull(Version version, LanguageJPA language);

    @Query("SELECT t FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2")
    Set<TextJPA> findByVersionAndLanguage(Version version, LanguageJPA language);

    @Query("SELECT t FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2 AND t.loopEntryRef IS NOT NULL")
    List<TextJPA> findByVersionAndLanguageWhereLoopEntryRefIsNotNull(Version version, LanguageJPA language);

    @Query("SELECT t FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2 AND t.index = ?3 AND t.loopEntryRef IS NULL")
    TextJPA findByVersionAndLanguageAndIndexWhereLoopEntryRefIsNull(Version version, LanguageJPA language, int no);

    @Query("SELECT t FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2 AND t.index = ?3 AND t.loopEntryRef = ?4")
    TextJPA findByVersionAndLanguageAndIndexAndLoopEntryRef(Version version, LanguageJPA language, int index, LoopEntryRefJPA loopEntryRef);

    @Query("SELECT t.id FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2 AND t.index = ?3 AND t.loopEntryRef IS NULL")
    Integer findIdByVersionAndLanguageAndIndexWhereLoopEntryRefIsNull(Version version, LanguageJPA language, int no);

    @Query("SELECT t.id FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2 AND t.index = ?3 AND t.loopEntryRef = ?4")
    Integer findIdByVersionAndLanguageAndIndexAndLoopEntryRef(Version version, LanguageJPA language, int no, LoopEntryRefJPA loopEntryRef);

    @Override
    @Query("SELECT t FROM TextJPA t WHERE t.version = ?1")
    List<TextJPA> findByVersion(Version version);

    @Query("SELECT t FROM TextJPA t WHERE t.index = ?1 AND t.text = ?2")
    List<TextJPA> findByIndexAndText(Integer index, String text);

    @Modifying
    @Query("DELETE FROM TextJPA t WHERE t.version = ?1 AND t.language = ?2")
    int deleteByVersionAndLanguage(Version version, LanguageJPA language);

    @Modifying
    @Query(value = "DELETE FROM imcms_text_doc_texts WHERE doc_id = ?1", nativeQuery = true)
    void deleteByDocId(Integer docId);
}
