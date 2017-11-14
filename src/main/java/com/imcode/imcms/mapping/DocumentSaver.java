package com.imcode.imcms.mapping;

import com.imcode.imcms.api.DocumentLanguage;
import com.imcode.imcms.api.DocumentVersion;
import com.imcode.imcms.domain.service.core.VersionService;
import com.imcode.imcms.mapping.container.*;
import com.imcode.imcms.mapping.jpa.doc.*;
import com.imcode.imcms.mapping.jpa.doc.Meta.DocumentType;
import com.imcode.imcms.persistence.entity.CommonContent;
import com.imcode.imcms.persistence.entity.Language;
import com.imcode.imcms.persistence.entity.Version;
import com.imcode.imcms.persistence.repository.CommonContentRepository;
import com.imcode.imcms.persistence.repository.LanguageRepository;
import imcode.server.document.DocumentDomainObject;
import imcode.server.document.DocumentPermissionSetTypeDomainObject;
import imcode.server.document.RoleIdToDocumentPermissionSetTypeMappings;
import imcode.server.document.textdocument.NoPermissionToAddDocumentToMenuException;
import imcode.server.document.textdocument.TextDocumentDomainObject;
import imcode.server.document.textdocument.TextDomainObject;
import imcode.server.user.RoleId;
import imcode.server.user.UserDomainObject;
import imcode.util.Utility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used internally by DocumentMapper.
 */
@Service
public class DocumentSaver {

    private DocumentMapper documentMapper;
    private final DocRepository docRepository;
    private final VersionRepository versionRepository;
    private final VersionService versionService;
    private final LanguageRepository languageRepository;
    private final CommonContentRepository commonContentRepository;
    private final MetaRepository metaRepository;
    private final TextDocumentContentSaver textDocumentContentSaver;
    private final DocumentContentMapper documentContentMapper;
    private final DocumentVersionMapper versionMapper;
    private final PropertyRepository propertyRepository;
    private final DocumentCreatingVisitor documentCreatingVisitor;
    private final DocumentSavingVisitor documentSavingVisitor;
    private final DocumentPermissionSetMapper documentPermissionSetMapper = new DocumentPermissionSetMapper();

    @Inject
    public DocumentSaver(DocRepository docRepository, VersionRepository versionRepository,
                         VersionService versionService, LanguageRepository languageRepository,
                         CommonContentRepository commonContentRepository, MetaRepository metaRepository,
                         TextDocumentContentSaver textDocumentContentSaver, DocumentContentMapper documentContentMapper,
                         DocumentVersionMapper versionMapper, PropertyRepository propertyRepository,
                         DocumentCreatingVisitor documentCreatingVisitor,
                         DocumentSavingVisitor documentSavingVisitor) {

        this.docRepository = docRepository;
        this.versionRepository = versionRepository;
        this.versionService = versionService;
        this.languageRepository = languageRepository;
        this.commonContentRepository = commonContentRepository;
        this.metaRepository = metaRepository;
        this.textDocumentContentSaver = textDocumentContentSaver;
        this.documentContentMapper = documentContentMapper;
        this.versionMapper = versionMapper;
        this.propertyRepository = propertyRepository;
        this.documentCreatingVisitor = documentCreatingVisitor;
        this.documentSavingVisitor = documentSavingVisitor;
    }

    /**
     * Updates doc's last modified date time if it was not set explicitly.
     */
    public void updateModifiedDtIfNotSetExplicitly(DocumentDomainObject doc) {
        Date explicitlyModifiedDatetime = Utility.truncateDateToMinutePrecision(doc.getActualModifiedDatetime());
        Date modifiedDatetime = Utility.truncateDateToMinutePrecision(doc.getModifiedDatetime());
        boolean modifiedDatetimeUnchanged = explicitlyModifiedDatetime.equals(modifiedDatetime);

        if (modifiedDatetimeUnchanged) {
            doc.setModifiedDatetime(new Date());
        }
    }

    /**
     * Saves edited text-document text and non-saved enclosing content loop if any.
     * If text is enclosed into unsaved content loop then the loop must also exist in document.
     *
     * @throws IllegalStateException if a text refers non-existing content loop.
     */
    @Transactional
    public void saveText(TextDocTextContainer container, UserDomainObject user)
            throws NoPermissionInternalException, DocumentSaveException {
        textDocumentContentSaver.saveText(container, user);
        docRepository.touch(container.getDocVersionRef(), user);
    }

    @Transactional
    public void saveTexts(TextDocTextsContainer container, UserDomainObject user)
            throws NoPermissionInternalException, DocumentSaveException {
        textDocumentContentSaver.saveTexts(container, user);
        docRepository.touch(container.getVersionRef(), user);
    }

    @Transactional
    public void saveImages(TextDocImagesContainer container, UserDomainObject user)
            throws NoPermissionInternalException, DocumentSaveException {
        textDocumentContentSaver.saveImages(container);
        docRepository.touch(container.getVersionRef(), user);
    }

    @Transactional
    public void saveMenu(TextDocMenuContainer container, UserDomainObject user)
            throws NoPermissionInternalException, DocumentSaveException {
        textDocumentContentSaver.saveMenu(container);
        docRepository.touch(container.getVersionRef(), user);
    }

    @Transactional
    public void saveImage(TextDocImageContainer container, UserDomainObject user)
            throws NoPermissionInternalException, DocumentSaveException {
        textDocumentContentSaver.saveImage(container);
        docRepository.touch(container.getDocVersionRef(), user);
    }

    @Transactional
    public void changeDocumentDefaultVersion(int docId, int newDefaultVersionNo, UserDomainObject publisher) {
        Version currentDefaultVersion = versionRepository.findDefault(docId);

        if (currentDefaultVersion.getNo() != newDefaultVersionNo) {
            versionRepository.updateDefaultNo(docId, newDefaultVersionNo, publisher.getId());

            docRepository.touch(VersionRef.of(docId, newDefaultVersionNo), publisher);
        }
    }

    public DocumentVersion makeDocumentVersion(List<DocumentDomainObject> docs, UserDomainObject user) {

        DocumentDomainObject firstDoc = docs.get(0);
        DocumentMeta meta = firstDoc.getMeta().clone();
        DocumentVersion nextVersion = versionMapper.create(meta.getId(), user.getId());

        saveContent(user, docs, meta, nextVersion.getNo(), firstDoc);

        return nextVersion;
    }

    @Transactional
    public void updateDocument(DocumentDomainObject doc,
                               Map<DocumentLanguage, DocumentCommonContent> commonContents,
                               DocumentDomainObject oldDoc,
                               UserDomainObject user)
            throws NoPermissionToAddDocumentToMenuException, DocumentSaveException {

        checkDocumentForSave(doc);

        Meta jpaMeta = toJpaObject(doc.getMeta());

        if (user.canEditPermissionsFor(oldDoc)) {
            newUpdateDocumentRolePermissions(jpaMeta, doc, user, oldDoc);
            documentPermissionSetMapper.saveRestrictedDocumentPermissionSets(jpaMeta, doc, user, oldDoc);
        }

        metaRepository.saveAndFlush(jpaMeta);

        commonContents.forEach((language, dcc) -> {
            CommonContent ormDcc = commonContentRepository.findByDocIdAndVersionNoAndLanguageCode(
                    doc.getId(), doc.getVersionNo(), language.getCode());
            if (ormDcc == null) {
                ormDcc = new CommonContent();
            }

            ormDcc.setHeadline(dcc.getHeadline());
            ormDcc.setMenuImageURL(dcc.getMenuImageURL());
            ormDcc.setMenuText(dcc.getMenuText());
            ormDcc.setEnabled(dcc.getEnabled());
            ormDcc.setVersionNo(doc.getVersionNo());

            if (ormDcc.getId() == null) {
                Language ormLanguage = languageRepository.findByCode(language.getCode());

                ormDcc.setDocId(doc.getId());
                ormDcc.setLanguage(ormLanguage);
                commonContentRepository.save(ormDcc);
            }
        });

        doc.accept(documentSavingVisitor);
        updateModifiedDtIfNotSetExplicitly(doc);
        docRepository.touch(doc.getVersionRef(), user, doc.getModifiedDatetime());
    }

    @Transactional
    public int saveNewDocsWithCommonMetaAndVersion(List<DocumentDomainObject> docs, UserDomainObject user)
            throws NoPermissionToAddDocumentToMenuException, DocumentSaveException {

        DocumentDomainObject firstDoc = docs.get(0);
        checkDocumentForSave(firstDoc);

        DocumentMeta meta = firstDoc.getMeta().clone();
        documentMapper.setCreatedAndModifiedDatetimes(meta, new Date());

        meta.setId(null);
        Meta jpaMeta = toJpaObject(meta);
        newUpdateDocumentRolePermissions(jpaMeta, firstDoc, user, null);

        // Update permissions
        documentPermissionSetMapper.saveRestrictedDocumentPermissionSets(jpaMeta, firstDoc, user, null);
        int newDocId = metaRepository.saveAndFlush(jpaMeta).getId();
        meta.setId(newDocId);

        docRepository.insertPropertyIfNotExists(
                newDocId,
                DocumentDomainObject.DOCUMENT_PROPERTIES__IMCMS_DOCUMENT_ALIAS,
                Integer.toString(newDocId)
        );
        int versionNo = versionService.create(newDocId, user.getId()).getNo();

        saveContent(user, docs, meta, versionNo, firstDoc);

        return newDocId;
    }

    private void saveContent(UserDomainObject user, List<DocumentDomainObject> docs, DocumentMeta meta, int no,
                             DocumentDomainObject firstDoc) {
        for (DocumentDomainObject doc : docs) {
            doc.setMeta(meta);
            doc.setVersionNo(no);

            documentContentMapper.saveCommonContent(doc);
        }

        // Currently only text docs contain non-common i18n content
        if (!(firstDoc instanceof TextDocumentDomainObject)) {
            firstDoc.accept(documentCreatingVisitor);
        } else {
            textDocumentContentSaver.createCommonContent((TextDocumentDomainObject) firstDoc);

            for (DocumentDomainObject doc : docs) {
                textDocumentContentSaver.createI18nContent((TextDocumentDomainObject) doc, user);
            }
        }
    }

    /**
     * Please note that custom (limited) permissions might be changed on save:
     * -If saving user is a super-admin or have full perms on a doc, then all custom perms settings are merely inherited
     * -Otherwise custom (lim1 and lim2) perms are replaced with permissions set for new document
     * <p/>
     * If user is a super-admin or has full permissions on a new document then
     */
    @Transactional
    public <T extends DocumentDomainObject> int saveNewDocument(T doc,
                                                                Map<DocumentLanguage, DocumentCommonContent> dccMap,
                                                                EnumSet<DocumentMapper.SaveOpts> saveOpts,
                                                                UserDomainObject user)
            throws NoPermissionToAddDocumentToMenuException, DocumentSaveException {

        checkDocumentForSave(doc);

        DocumentMeta metaDO = doc.getMeta().clone();
        documentMapper.setCreatedAndModifiedDatetimes(metaDO, new Date());
        metaDO.setId(null);
        metaDO.setDefaultVersionNo(DocumentVersion.WORKING_VERSION_NO);
        metaDO.setDocumentType(doc.getDocumentTypeId());

        if (!user.isSuperAdminOrHasFullPermissionOn(doc)) {
            metaDO.getPermissionSets().setRestricted1(metaDO.getPermissionSetsForNewDocument().getRestricted1());
            metaDO.getPermissionSets().setRestricted2(metaDO.getPermissionSetsForNewDocument().getRestricted2());
        }

        Meta jpaMeta = toJpaObject(metaDO);
        newUpdateDocumentRolePermissions(jpaMeta, doc, user, null);
        documentPermissionSetMapper.saveRestrictedDocumentPermissionSets(jpaMeta, doc, user, null);
        int newDocId = metaRepository.saveAndFlush(jpaMeta).getId();

        dccMap.forEach((language, dcc) -> {
            CommonContent jpaDcc = new CommonContent();
            Language jpaLanguage = languageRepository.findByCode(language.getCode());

            jpaDcc.setDocId(newDocId);
            jpaDcc.setHeadline(dcc.getHeadline());
            jpaDcc.setMenuImageURL(dcc.getMenuImageURL());
            jpaDcc.setMenuText(dcc.getMenuText());
            jpaDcc.setLanguage(jpaLanguage);
            jpaDcc.setEnabled(dcc.getEnabled());
            jpaDcc.setVersionNo(DocumentVersion.WORKING_VERSION_NO);

            commonContentRepository.save(jpaDcc);
        });

        docRepository.insertPropertyIfNotExists(
                newDocId,
                DocumentDomainObject.DOCUMENT_PROPERTIES__IMCMS_DOCUMENT_ALIAS,
                String.valueOf(newDocId)
        );

        Version version = versionService.create(newDocId, user.getId());
        doc.setVersionNo(version.getNo());
        doc.setId(newDocId);

        doc.accept(documentCreatingVisitor);

        if (doc instanceof TextDocumentDomainObject
                && saveOpts.contains(DocumentMapper.SaveOpts.CopyDocCommonContentIntoTextFields))
        {
            Map<DocumentLanguage, TextDomainObject> texts1 = new HashMap<>();
            Map<DocumentLanguage, TextDomainObject> texts2 = new HashMap<>();

            dccMap.forEach((language, dcc) -> {
                texts1.put(language, new TextDomainObject(dcc.getHeadline()));
                texts2.put(language, new TextDomainObject(dcc.getMenuText()));
            });

            textDocumentContentSaver.saveTexts(
                    TextDocTextsContainer.of(VersionRef.of(version.getDocId(), version.getNo()), 1, texts1),
                    user
            );

            textDocumentContentSaver.saveTexts(
                    TextDocTextsContainer.of(VersionRef.of(version.getDocId(), version.getNo()), 2, texts2),
                    user
            );
        }

        return newDocId;
    }

    /**
     * Various non security checks.
     */
    private void checkDocumentForSave(DocumentDomainObject document)
            throws NoPermissionInternalException, DocumentSaveException {
        documentMapper.getCategoryMapper().checkMaxDocumentCategoriesOfType(document);
        checkIfAliasAlreadyExist(document);
    }

    /**
     * Update meta roles to permissions set mapping.
     * Modified copy of legacy updateDocumentRolePermissions method.
     * NB! Compared to legacy this method does not update database.
     *
     * @param document    document being saved
     * @param user        an authorized user
     * @param oldDocument original doc when updating or null when inserting (a new doc)
     */
    private void newUpdateDocumentRolePermissions(Meta jpaMeta, DocumentDomainObject document, UserDomainObject user,
                                                  DocumentDomainObject oldDocument) {

        // Original (old) and modified or new document permission set type mapping.
        RoleIdToDocumentPermissionSetTypeMappings mappings = new RoleIdToDocumentPermissionSetTypeMappings();

        // Copy original document' roles to mapping with NONE(4) permissions-set assigned
        if (null != oldDocument) {
            RoleIdToDocumentPermissionSetTypeMappings.Mapping[] oldDocumentMappings = oldDocument
                    .getRoleIdsMappedToDocumentPermissionSetTypes()
                    .getMappings();

            for (RoleIdToDocumentPermissionSetTypeMappings.Mapping mapping : oldDocumentMappings) {
                mappings.setPermissionSetTypeForRole(mapping.getRoleId(), DocumentPermissionSetTypeDomainObject.NONE);
            }
        }

        // Copy modified or new document' roles to mapping
        RoleIdToDocumentPermissionSetTypeMappings.Mapping[] documentMappings = document
                .getRoleIdsMappedToDocumentPermissionSetTypes()
                .getMappings();

        for (RoleIdToDocumentPermissionSetTypeMappings.Mapping mapping : documentMappings) {
            mappings.setPermissionSetTypeForRole(mapping.getRoleId(), mapping.getDocumentPermissionSetType());
        }

        RoleIdToDocumentPermissionSetTypeMappings.Mapping[] mappingsArray = mappings.getMappings();
        Map<Integer, Meta.Permission> roleIdToPermissionSetIdMap = jpaMeta.getRoleIdToPermissionSetIdMap();

        for (RoleIdToDocumentPermissionSetTypeMappings.Mapping mapping : mappingsArray) {
            RoleId roleId = mapping.getRoleId();
            DocumentPermissionSetTypeDomainObject documentPermissionSetType = mapping.getDocumentPermissionSetType();

            final boolean canSetDocumentPermissionSetTypeForRoleIdOnDocument = user
                    .canSetDocumentPermissionSetTypeForRoleIdOnDocument(documentPermissionSetType, roleId, oldDocument);

            if (null == oldDocument || canSetDocumentPermissionSetTypeForRoleIdOnDocument) {

                // According to schema design NONE value can not be save into the DB table
                if (documentPermissionSetType.equals(DocumentPermissionSetTypeDomainObject.NONE)) {
                    roleIdToPermissionSetIdMap.remove(roleId.intValue());
                } else {
                    roleIdToPermissionSetIdMap.put(roleId.intValue(), documentPermissionSetType.getPermission());
                }
            }
        }
    }

    private void checkIfAliasAlreadyExist(DocumentDomainObject document) throws AliasAlreadyExistsInternalException {
        String alias = document.getAlias();

        if (alias != null) {
            Integer documentId = propertyRepository.findDocIdByAlias(alias);
            if (documentId != null && !documentId.equals(document.getId())) {
                throw new AliasAlreadyExistsInternalException(
                        String.format("Alias %s is already in use by document %d.", alias, documentId));
            }
        }
    }

    // todo: check permission
    private Meta toJpaObject(DocumentMeta metaDO) {
        Meta meta = new Meta();

        meta.setArchivedDatetime(metaDO.getArchivedDatetime());
        meta.setArchiverId(metaDO.getArchiverId());
        meta.setCategoryIds(metaDO.getCategoryIds());
        meta.setCreatedDatetime(metaDO.getCreatedDatetime());
        meta.setCreatorId(metaDO.getCreatorId());
        meta.setDefaultVersionNo(metaDO.getDefaultVersionNo());
        meta.setDisabledLanguageShowMode(Meta.DisabledLanguageShowMode.valueOf(
                metaDO.getDisabledLanguageShowMode().name()
        ));
        meta.setDocumentType(DocumentType.values()[metaDO.getDocumentType()]);

        Set<Language> enabledLanguages = metaDO.getEnabledLanguages()
                .stream()
                .map(l -> languageRepository.findByCode(l.getCode()))
                .collect(Collectors.toSet());

        meta.setEnabledLanguages(enabledLanguages);
        meta.setId(metaDO.getId());
        meta.setKeywords(metaDO.getKeywords());
        meta.setLinkableByOtherUsers(metaDO.getLinkableByOtherUsers());
        meta.setLinkedForUnauthorizedUsers(metaDO.getLinkedForUnauthorizedUsers());
        meta.setModifiedDatetime(metaDO.getModifiedDatetime());
        meta.setProperties(metaDO.getProperties());
        meta.setPublicationEndDatetime(metaDO.getPublicationEndDatetime());
        meta.setDepublisherId(metaDO.getDepublisherId());
        meta.setPublicationStartDatetime(metaDO.getPublicationStartDatetime());
        meta.setPublicationStatus(metaDO.getPublicationStatus().asEnum());
        meta.setPublisherId(metaDO.getPublisherId());
        meta.setRestrictedOneMorePrivilegedThanRestrictedTwo(
                metaDO.getRestrictedOneMorePrivilegedThanRestrictedTwo()
        );
        meta.setRoleIdToPermissionSetIdMap(
                Stream.of(metaDO.getRoleIdToDocumentPermissionSetTypeMappings().getMappings())
                        .collect(
                                Collectors.toMap(
                                        it -> it.getRoleId().getRoleId(),
                                        it -> it.getDocumentPermissionSetType().getPermission()
                                )
                        )
        );
        meta.setSearchDisabled(metaDO.getSearchDisabled());
        meta.setTarget(metaDO.getTarget());

        return meta;
    }

    public DocumentMapper getDocumentMapper() {
        return documentMapper;
    }

    public void setDocumentMapper(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    public DocRepository getDocRepository() {
        return docRepository;
    }

    public VersionRepository getVersionRepository() {
        return versionRepository;
    }

}
