package com.imcode.imcms.domain.service.core;

import com.imcode.imcms.domain.dto.UberDocumentDTO;
import com.imcode.imcms.domain.service.DocumentService;
import com.imcode.imcms.model.Document;
import org.apache.solr.common.SolrInputDocument;

/**
 * Wrapping around concrete document service.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 10.01.18.
 */
public class WrappingDocumentService<T extends Document> implements DocumentService<UberDocumentDTO> {

    private final DocumentService<T> typedDocumentService;

    public WrappingDocumentService(DocumentService<T> typedDocumentService) {
        this.typedDocumentService = typedDocumentService;
    }

    @Override
    public UberDocumentDTO createFromParent(Integer parentDocId) {
        return UberDocumentDTO.of(typedDocumentService.createFromParent(parentDocId));
    }

    @Override
    public UberDocumentDTO get(int docId) {
        return UberDocumentDTO.of(typedDocumentService.get(docId));
    }

    @Override
    public UberDocumentDTO save(UberDocumentDTO saveMe) {
        return UberDocumentDTO.of(typedDocumentService.save(saveMe.toTypedDocument()));
    }

    @Override
    public boolean publishDocument(int docId, int userId) {
        return typedDocumentService.publishDocument(docId, userId);
    }

    @Override
    public SolrInputDocument index(int docId) {
        return typedDocumentService.index(docId);
    }

    @Override
    public UberDocumentDTO copy(int docId) {
        return UberDocumentDTO.of(typedDocumentService.copy(docId));
    }

    @Override
    public void deleteByDocId(Integer docIdToDelete) {
        typedDocumentService.deleteByDocId(docIdToDelete);
    }
}
