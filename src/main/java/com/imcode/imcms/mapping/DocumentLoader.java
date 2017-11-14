package com.imcode.imcms.mapping;

import com.imcode.imcms.api.Document;
import com.imcode.imcms.api.DocumentLanguage;
import com.imcode.imcms.domain.dto.PermissionDTO;
import com.imcode.imcms.mapping.jpa.doc.PropertyRepository;
import com.imcode.imcms.persistence.entity.Meta;
import com.imcode.imcms.persistence.entity.Meta.Permission;
import com.imcode.imcms.persistence.repository.MetaRepository;
import imcode.server.document.DocumentDomainObject;
import imcode.server.document.RoleIdToDocumentPermissionSetTypeMappings;
import imcode.server.user.RoleId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loads documents from the database.
 */
@Component
public class DocumentLoader {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private DocumentLanguageMapper languageMapper;
    @Autowired
    private DocumentContentMapper contentMapper;
    @Autowired
    private DocumentContentInitializingVisitor documentContentInitializingVisitor;

    /**
     * Loads document's meta.
     *
     * @param docId document id.
     * @return loaded meta of null if meta with given id does not exists.
     */
    public DocumentMeta loadMeta(int docId) {
        return toDomainObject(metaRepository.findOne(docId));
    }

    /**
     * Loads and initializes document's content.
     */
    public <T extends DocumentDomainObject> T loadAndInitContent(T document) {
        DocumentCommonContent dcc = contentMapper.getCommonContent(document.getRef());

        document.setCommonContent(dcc != null
                ? dcc
                : DocumentCommonContent.builder().headline("").menuImageURL("").menuText("").build()
        );
        document.accept(documentContentInitializingVisitor);

        return document;
    }

    private Document.PublicationStatus publicationStatusFromInt(int publicationStatusInt) {
        Document.PublicationStatus publicationStatus = Document.PublicationStatus.NEW;
        if (Document.PublicationStatus.APPROVED.asInt() == publicationStatusInt) {
            publicationStatus = Document.PublicationStatus.APPROVED;
        } else if (Document.PublicationStatus.DISAPPROVED.asInt() == publicationStatusInt) {
            publicationStatus = Document.PublicationStatus.DISAPPROVED;
        }
        return publicationStatus;
    }

    // Moved from  DocumentInitializer.initDocuments
    private void initRoleIdToPermissionSetIdMap(DocumentMeta metaDO, Meta jpaMeta) {
        RoleIdToDocumentPermissionSetTypeMappings rolePermissionMappings =
                new RoleIdToDocumentPermissionSetTypeMappings();

        for (Map.Entry<Integer, Permission> roleIdToPermissionSetId : jpaMeta.getRoleIdToPermissionSetIdMap().entrySet()) {
            rolePermissionMappings.setPermissionSetTypeForRole(
                    new RoleId(roleIdToPermissionSetId.getKey()),
                    PermissionDTO.fromPermission(roleIdToPermissionSetId.getValue()));
        }

        metaDO.setRoleIdToDocumentPermissionSetTypeMappings(rolePermissionMappings);
    }

    private DocumentMeta toDomainObject(Meta meta) {
        if (meta == null) return null;

        DocumentMeta metaDO = new DocumentMeta();

        metaDO.setArchivedDatetime(meta.getArchivedDatetime());
        metaDO.setArchiverId(meta.getArchiverId());
        metaDO.setCategoryIds(meta.getCategoryIds());
        metaDO.setCreatedDatetime(meta.getCreatedDatetime());
        metaDO.setCreatorId(meta.getCreatorId());
        metaDO.setDefaultVersionNo(meta.getDefaultVersionNo());
        metaDO.setDisabledLanguageShowMode(DocumentMeta.DisabledLanguageShowMode.valueOf(meta.getDisabledLanguageShowMode().name()));
        metaDO.setDocumentType(meta.getDocumentType().ordinal());

        Set<DocumentLanguage> apiLanguages = meta.getEnabledLanguages().stream()
                .map(languageMapper::toApiObject)
                .collect(Collectors.toSet());

        metaDO.setEnabledLanguages(apiLanguages);
        metaDO.setId(meta.getId());
        metaDO.setKeywords(meta.getKeywords());
        metaDO.setLinkableByOtherUsers(meta.getLinkableByOtherUsers());
        metaDO.setLinkedForUnauthorizedUsers(meta.getLinkedForUnauthorizedUsers());
        metaDO.setModifiedDatetime(meta.getModifiedDatetime());
        metaDO.setActualModifiedDatetime(meta.getModifiedDatetime());
        metaDO.setProperties(meta.getProperties());
        metaDO.setPublicationEndDatetime(meta.getPublicationEndDatetime());
        metaDO.setDepublisherId(meta.getDepublisherId());
        metaDO.setPublicationStartDatetime(meta.getPublicationStartDatetime());
        metaDO.setPublicationStatus(publicationStatusFromInt(meta.getPublicationStatus().ordinal()));
        metaDO.setPublisherId(meta.getPublisherId());
        metaDO.setRestrictedOneMorePrivilegedThanRestrictedTwo(meta.getRestrictedOneMorePrivilegedThanRestrictedTwo());
        metaDO.setSearchDisabled(meta.isSearchDisabled());
        metaDO.setTarget(meta.getTarget());

        initRoleIdToPermissionSetIdMap(metaDO, meta);

        return metaDO;
    }

    PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }

}
