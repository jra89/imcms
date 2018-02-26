package com.imcode.imcms.domain.service.api;

import com.imcode.imcms.domain.dto.DocumentDTO;
import com.imcode.imcms.domain.factory.DocumentDtoFactory;
import com.imcode.imcms.domain.service.*;
import com.imcode.imcms.model.CommonContent;
import com.imcode.imcms.persistence.entity.Meta;
import com.imcode.imcms.persistence.entity.Version;
import com.imcode.imcms.persistence.repository.MetaRepository;
import com.imcode.imcms.util.Value;
import com.imcode.imcms.util.function.TernaryFunction;
import imcode.server.document.index.DocumentIndex;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Service for work with common document entities.
 * Every specified document type has it's own corresponding service.
 */
@Service
class DefaultDocumentService implements DocumentService<DocumentDTO> {

    private final MetaRepository metaRepository;
    private final TernaryFunction<Meta, Version, List<CommonContent>, DocumentDTO> documentMapping;
    private final CommonContentService commonContentService;
    private final VersionService versionService;
    private final TextService textService;
    private final ImageService imageService;
    private final LoopService loopService;
    private final DocumentIndex documentIndex;
    private final DocumentDtoFactory documentDtoFactory;
    private final List<VersionedContentService> versionedContentServices;
    private final Function<DocumentDTO, Meta> documentSaver;

    private DeleterByDocumentId[] docContentServices = {};

    DefaultDocumentService(MetaRepository metaRepository,
                           TernaryFunction<Meta, Version, List<CommonContent>, DocumentDTO> metaToDocumentDTO,
                           Function<DocumentDTO, Meta> documentDtoToMeta,
                           CommonContentService commonContentService,
                           VersionService versionService,
                           TextService textService,
                           ImageService imageService,
                           LoopService loopService,
                           DocumentIndex documentIndex,
                           DocumentDtoFactory documentDtoFactory,
                           List<VersionedContentService> versionedContentServices) {

        this.metaRepository = metaRepository;
        this.documentMapping = metaToDocumentDTO;
        this.commonContentService = commonContentService;
        this.versionService = versionService;
        this.textService = textService;
        this.imageService = imageService;
        this.loopService = loopService;
        this.documentIndex = documentIndex;
        this.documentDtoFactory = documentDtoFactory;
        this.versionedContentServices = versionedContentServices;
        this.documentSaver = ((Function<Meta, Meta>) metaRepository::save).compose(documentDtoToMeta);
    }

    @PostConstruct
    private void init() {
        docContentServices = new DeleterByDocumentId[]{
                textService,
                imageService,
                loopService,
                commonContentService,
                versionService
        };
    }

    @Override
    public DocumentDTO createFromParent(Integer parentDocId) { // todo: use copying to create new doc based on parent
        return documentDtoFactory.createEmpty();
    }

    @Override
    public DocumentDTO get(int docId) {
        final Version latestVersion = versionService.getLatestVersion(docId);
        final List<CommonContent> commonContents = commonContentService.getOrCreateCommonContents(
                docId, latestVersion.getNo()
        );
        return documentMapping.apply(metaRepository.findOne(docId), latestVersion, commonContents);
    }

    @Override
    @Transactional
    public DocumentDTO save(DocumentDTO saveMe) {
        final boolean isNew = (saveMe.getId() == null);
        final Integer docId = documentSaver.apply(saveMe).getId();

        if (isNew) {
            saveMe.setId(docId);
            versionService.create(docId);
            saveMe.getCommonContents().forEach(commonContentDTO -> commonContentDTO.setDocId(docId));
        }

        commonContentService.save(docId, saveMe.getCommonContents());
        documentIndex.indexDocument(docId);

        return saveMe;
    }

    @Override
    public boolean publishDocument(int docId, int userId) {
        if (!versionService.hasNewerVersion(docId)) {
            return false;
        }

        final Version workingVersion = versionService.getDocumentWorkingVersion(docId);
        final Version newVersion = versionService.create(docId, userId);

        versionedContentServices.forEach(vcs -> vcs.createVersionedContent(workingVersion, newVersion));

        return true;
    }

    @Override
    public SolrInputDocument index(int docId) {

        final DocumentDTO doc = get(docId);

        SolrInputDocument indexDoc = new SolrInputDocument();

        BiConsumer<String, Object> addFieldIfNotNull = (name, value) -> {
            if (value != null) indexDoc.addField(name, value);
        };

        indexDoc.addField(DocumentIndex.FIELD__ID, docId);
        indexDoc.addField(DocumentIndex.FIELD__TIMESTAMP, new Date());
        indexDoc.addField(DocumentIndex.FIELD__META_ID, docId);
        indexDoc.addField(DocumentIndex.FIELD__VERSION_NO, doc.getCurrentVersion().getId());
        indexDoc.addField(DocumentIndex.FIELD__SEARCH_ENABLED, !doc.isSearchDisabled());

        for (CommonContent commonContent : doc.getCommonContents()) {
            String headline = commonContent.getHeadline();
            String menuText = commonContent.getMenuText();

            final String langCode = commonContent.getLanguage().getCode();
            indexDoc.addField(DocumentIndex.FIELD__LANGUAGE_CODE, langCode);
            indexDoc.addField(DocumentIndex.FIELD__META_HEADLINE + "_" + langCode, headline);
            indexDoc.addField(DocumentIndex.FIELD__META_HEADLINE_KEYWORD + "_" + langCode, headline);
            indexDoc.addField(DocumentIndex.FIELD__META_TEXT + "_" + langCode, menuText);
        }

        indexDoc.addField(DocumentIndex.FIELD__DOC_TYPE_ID, doc.getType().ordinal());
        indexDoc.addField(DocumentIndex.FIELD__CREATOR_ID, doc.getCreated().getId());

        addFieldIfNotNull.accept(DocumentIndex.FIELD__PUBLISHER_ID, doc.getPublished().getId());

        addFieldIfNotNull.accept(DocumentIndex.FIELD__CREATED_DATETIME, doc.getCreated().getFormattedDate());
        addFieldIfNotNull.accept(DocumentIndex.FIELD__MODIFIED_DATETIME, doc.getModified().getFormattedDate());
        addFieldIfNotNull.accept(DocumentIndex.FIELD__ACTIVATED_DATETIME, doc.getPublished().getFormattedDate());
        addFieldIfNotNull.accept(DocumentIndex.FIELD__PUBLICATION_START_DATETIME,
                doc.getPublished().getFormattedDate());

        addFieldIfNotNull.accept(DocumentIndex.FIELD__PUBLICATION_END_DATETIME,
                doc.getPublicationEnd().getFormattedDate());

        addFieldIfNotNull.accept(DocumentIndex.FIELD__ARCHIVED_DATETIME, doc.getArchived().getFormattedDate());

        indexDoc.addField(DocumentIndex.FIELD__STATUS, doc.getPublicationStatus().ordinal());

        doc.getCategories().forEach(category -> {
            indexDoc.addField(DocumentIndex.FIELD__CATEGORY, category.getName());
            indexDoc.addField(DocumentIndex.FIELD__CATEGORY_ID, category.getId());

            Value.with(category.getType(), categoryType -> {
                indexDoc.addField(DocumentIndex.FIELD__CATEGORY_TYPE, categoryType.getName());
                indexDoc.addField(DocumentIndex.FIELD__CATEGORY_TYPE_ID, categoryType.getId());

            });
        });

        doc.getKeywords().
                forEach(documentKeyword -> indexDoc.addField(DocumentIndex.FIELD__KEYWORD, documentKeyword));

        addFieldIfNotNull.accept(DocumentIndex.FIELD__ALIAS, doc.getAlias());

        doc.getProperties()
                .forEach((key, value) -> indexDoc.addField(DocumentIndex.FIELD__PROPERTY_PREFIX + key, value));

        for (Integer roleId : doc.getRoleIdToPermission().keySet()) {
            indexDoc.addField(DocumentIndex.FIELD__ROLE_ID, roleId);
        }

        return indexDoc;
    }

    @Override
    @Transactional
    public DocumentDTO copy(int docId) {
        final DocumentDTO documentDTO = get(docId);

        documentDTO.getCommonContents()
                .forEach(commonContentDTO ->
                        commonContentDTO.setHeadline("(Copy/Kopia) " + commonContentDTO.getHeadline()));

        final DocumentDTO clonedDocumentDTO = documentDTO.clone();

        return save(clonedDocumentDTO);
    }

    @Override
    @Transactional
    public void deleteByDocId(Integer docIdToDelete) {
        deleteDocumentContent(docIdToDelete);
        metaRepository.delete(docIdToDelete);
        documentIndex.removeDocument(docIdToDelete);
    }

    @Transactional
    protected void deleteDocumentContent(Integer docIdToDelete) {
        for (DeleterByDocumentId docContentService : docContentServices) {
            docContentService.deleteByDocId(docIdToDelete);
        }
    }

}
