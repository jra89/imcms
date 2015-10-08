package com.imcode.imcms.mapping.jpa.doc.content.textdoc;

import com.imcode.imcms.mapping.jpa.doc.Language;
import com.imcode.imcms.mapping.jpa.doc.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.Set;

@Repository
public interface TextHistoryRepository extends JpaRepository<TextHistory, Integer> {
    @Query("SELECT th FROM TextHistory th WHERE th.no = ?2 AND th.documentId = ?1")
    public Set<TextHistory> findAllByDocumentAndTextNo(int docId, int textId);

    public Set<TextHistory> findAllByVersionAndLanguageAndNo(Version version, Language language, int no);
}
