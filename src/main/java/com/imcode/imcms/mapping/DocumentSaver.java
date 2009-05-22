package com.imcode.imcms.mapping;

import imcode.server.Imcms;
import imcode.server.document.DocumentDomainObject;
import imcode.server.document.DocumentPermissionSetTypeDomainObject;
import imcode.server.document.RoleIdToDocumentPermissionSetTypeMappings;
import imcode.server.document.textdocument.NoPermissionToAddDocumentToMenuException;
import imcode.server.document.textdocument.TextDocumentDomainObject;
import imcode.server.user.RoleId;
import imcode.server.user.UserDomainObject;
import imcode.util.Utility;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.imcode.imcms.api.DocumentProperty;
import com.imcode.imcms.api.DocumentVersion;
import com.imcode.imcms.api.Meta;
import com.imcode.imcms.dao.MetaDao;

/**
 * This class is instantiated using spring framework.
 * 
 * This class acts as DocumentMapper's helper and its API must not be used directly.  
 */
public class DocumentSaver {

    private DocumentMapper documentMapper;
    
    private MetaDao metaDao;
    
    /**
     * --experimental--
     * 
     * Existing API saves whole document even if only a fragment of it 
     * (say text field or image) was modified.
     * 
     * TODO: Create concrete calls: 
     *   saveText
     *   saveImages
     *   saveMenus,
     *   etc 
     */
    @Transactional     
    public void saveDocumentFragment(DocumentDomainObject document, UserDomainObject user, HibernateCallback hibernateCallback) throws NoPermissionInternalException, DocumentSaveException {
    	checkDocumentForSave(document);
    	
    	try {
    		HibernateTemplate template = (HibernateTemplate)Imcms.getServices().getSpringBean("hibernateTemplate");
    		
    		template.execute(hibernateCallback); 

            Date lastModifiedDatetime = Utility.truncateDateToMinutePrecision(document.getActualModifiedDatetime());
            Date modifiedDatetime = Utility.truncateDateToMinutePrecision(document.getModifiedDatetime());
            boolean modifiedDatetimeUnchanged = lastModifiedDatetime.equals(modifiedDatetime);
            
            if (modifiedDatetimeUnchanged) {            	
            	modifiedDatetime = documentMapper.getClock().getCurrentDate();
            }
            
            // TODO: Fix - does not work!!
            // Bulk update is used for speed purposes. 
            // Actually exactly one document's meta is updated.
    		//template.bulkUpdate("update Meta m set m.modifiedDatetime = ? where m.id = ?", 
    		//	new Object[] {modifiedDatetime, document.getMeta().getId()});
	    } finally {
	        documentMapper.invalidateDocument(document);
	    }    	
    }

    /**
     * Published working version of a document.
     */
    // TODO: Should throw NoPermissionToEditDocumentException ?
    @Transactional    
    public void publishWorkingDocument(DocumentDomainObject document, UserDomainObject user) 
    throws DocumentSaveException {
    	try {
    		metaDao.publishWorkingDocument(document.getMeta().getId());
    	} catch (RuntimeException e) {
    		throw new DocumentSaveException(e);
    	} finally {
    		documentMapper.invalidateDocument(document);
    	}
    }
    
    
    /**
     * Updates published or working document.
     */
    @Transactional
    public void updateDocument(DocumentDomainObject document, DocumentDomainObject oldDocument,
                      final UserDomainObject user) throws NoPermissionInternalException, DocumentSaveException {
        checkDocumentForSave(document);

        //document.loadAllLazilyLoaded();
        
        try {
            Date lastModifiedDatetime = Utility.truncateDateToMinutePrecision(document.getActualModifiedDatetime());
            Date modifiedDatetime = Utility.truncateDateToMinutePrecision(document.getModifiedDatetime());
            boolean modifiedDatetimeUnchanged = lastModifiedDatetime.equals(modifiedDatetime);
            if (modifiedDatetimeUnchanged) {
                document.setModifiedDatetime(documentMapper.getClock().getCurrentDate());
            }

            if (user.canEditPermissionsFor(oldDocument)) {
                newUpdateDocumentRolePermissions(document, user, oldDocument);
                documentMapper.getDocumentPermissionSetMapper().saveRestrictedDocumentPermissionSets(document, user, oldDocument);
            }
            
            DocumentSavingVisitor savingVisitor = new DocumentSavingVisitor(oldDocument, documentMapper.getImcmsServices(), user);
            
            saveMeta(document);
                        
            document.accept(savingVisitor);
        } finally {
            documentMapper.invalidateDocument(document);
        }
    }
    
    /**
     * Creates working document version from existing document.
     * 
     * Actually only texts and images are copied into new document.
     * 
     * @param document an instance of {@link TextDocumentDomainObject}
     */
    @Transactional
    public void createWorkingDocumentFromExisting(DocumentDomainObject document, UserDomainObject user) throws NoPermissionInternalException, DocumentSaveException {
        //checkDocumentForSave(document);
        //document.loadAllLazilyLoaded();
    	
        //TODO: refactor - very ugly
    	// save document id
    	Meta meta = document.getMeta();
    	Integer documentId = meta.getId();
    		
    	//TODO: refactor - very ugly
    	// clone document, reset its dependencies meta id and assign its documentId again  
    	document = document.clone();
    	document.setAlias(null);
    	document.setDependenciesMetaIdToNull();
    	document.setId(documentId);

    	/*
        //try {
            Date lastModifiedDatetime = Utility.truncateDateToMinutePrecision(document.getActualModifiedDatetime());
            Date modifiedDatetime = Utility.truncateDateToMinutePrecision(document.getModifiedDatetime());
            boolean modifiedDatetimeUnchanged = lastModifiedDatetime.equals(modifiedDatetime);
            if (modifiedDatetimeUnchanged) {
                document.setModifiedDatetime(documentMapper.getClock().getCurrentDate());
            }
            
            saveMeta(documentId, document);
            
            document.accept(new DocumentCreatingVisitor(documentMapper.getImcmsServices(), user));
        //} finally {
        //    documentMapper.invalidateDocument(document);
        //}
        */  
            
        DocumentCreatingVisitor visitor = new DocumentCreatingVisitor(documentMapper.getImcmsServices(), user);
        TextDocumentDomainObject textDocument = (TextDocumentDomainObject)document;
        
        DocumentVersion documentVersion = metaDao.createWorkingVersion(documentId, user.getId());
        textDocument.getMeta().setVersion(documentVersion);
        
        visitor.updateTextDocumentTexts(textDocument, null, user);
        visitor.updateTextDocumentImages(textDocument, null, user);
    }
    


    @Transactional
    public void saveNewDocument(UserDomainObject user,
                         DocumentDomainObject document, boolean copying) throws NoPermissionToAddDocumentToMenuException, DocumentSaveException {
        checkDocumentForSave(document);

        //document.loadAllLazilyLoaded();                
        
        documentMapper.setCreatedAndModifiedDatetimes(document, new Date());

        boolean inheritRestrictedPermissions = !user.isSuperAdminOrHasFullPermissionOn(document) && !copying;
        if (inheritRestrictedPermissions) {
            document.getPermissionSets().setRestricted1(document.getPermissionSetsForNewDocuments().getRestricted1());
            document.getPermissionSets().setRestricted2(document.getPermissionSetsForNewDocuments().getRestricted2());
        }
        
        newUpdateDocumentRolePermissions(document, user, null);

        // Update permissions
        documentMapper.getDocumentPermissionSetMapper().saveRestrictedDocumentPermissionSets(document, user, null);
        
        document.setDependenciesMetaIdToNull();         
        Meta meta = saveMeta(document);
        
        DocumentVersion version = metaDao.createWorkingVersion(meta.getId(), user.getId());
        document.getMeta().setVersion(version);        
                
        document.accept(new DocumentCreatingVisitor(documentMapper.getImcmsServices(), user));
    	
        documentMapper.invalidateDocument(document);
    }
    
    
    /**
     * Temporary method
     * Copies data from attributes to meta and stores meta.
     * 
     * @return saved document meta.
     */
    private Meta saveMeta(DocumentDomainObject document) {
    	Meta meta = document.getMeta();
    	
    	meta.setPublicationStatusInt(document.getPublicationStatus().asInt());
    	
    	if (meta.getId() == null) {
        	meta.setDocumentType(document.getDocumentTypeId());
        	meta.setActivate(1);
    	} 
    	
    	//for update
        //private static final int META_HEADLINE_MAX_LENGTH = 255;
        //private static final int META_TEXT_MAX_LENGTH = 1000;
        //String headlineThatFitsInDB = headline.substring(0, Math.min(headline.length(), META_HEADLINE_MAX_LENGTH - 1));
        //String textThatFitsInDB = text.substring(0, Math.min(text.length(), META_TEXT_MAX_LENGTH - 1));
    	
    	// Converted from legacy queries:
    	// Should be handled separately from meta???
    	//meta.getRoleIdToPermissionSetIdMap();
    	//meta.getDocPermisionSetEx().clear();
    	//meta.getDocPermisionSetExForNew().clear();    	
    	//meta.getPermissionSetBitsMap().clear();
    	//meta.getPermissionSetBitsForNewMap().clear();    	    	
    	
    	// WHAT TO DO WITH THIS on copy save and on base save?    	
    	//meta.setSectionIds(document.getSectionIds());
    	//meta.setCategoryIds(document.getCategoryIds());
    	//meta.setProperties(document.getProperties());
    	
    	metaDao.saveMeta(meta);
    	
    	return meta;
    }
    

    /**
     * Various non security checks. 
     * 
     * @param document
     * @throws NoPermissionInternalException
     * @throws DocumentSaveException
     */
    private void checkDocumentForSave(DocumentDomainObject document) throws NoPermissionInternalException, DocumentSaveException {

        documentMapper.getCategoryMapper().checkMaxDocumentCategoriesOfType(document);
        checkIfAliasAlreadyExist(document);

    }
    
    
    /**
     * Update meta roles to permissions set mapping.
     * Modified copy of legacy updateDocumentRolePermissions method.  
     * NB! Compared to legacy this method does not update database.
     */
    void newUpdateDocumentRolePermissions(DocumentDomainObject document, UserDomainObject user,
            DocumentDomainObject oldDocument) {

    	// Original (old) and modified or new document permission set type mapping.
		RoleIdToDocumentPermissionSetTypeMappings mappings = new RoleIdToDocumentPermissionSetTypeMappings();
		
		// Copy original document' roles to mapping with NONE(4) permissions-set assigned
		if (null != oldDocument) {
			RoleIdToDocumentPermissionSetTypeMappings.Mapping[] oldDocumentMappings = oldDocument.getRoleIdsMappedToDocumentPermissionSetTypes().getMappings();
			for ( int i = 0; i < oldDocumentMappings.length; i++ ) {
				RoleIdToDocumentPermissionSetTypeMappings.Mapping mapping = oldDocumentMappings[i];
				mappings.setPermissionSetTypeForRole(mapping.getRoleId(), DocumentPermissionSetTypeDomainObject.NONE);
			}
		}
		
		// Copy modified or new document' roles to mapping
		RoleIdToDocumentPermissionSetTypeMappings.Mapping[] documentMappings = document.getRoleIdsMappedToDocumentPermissionSetTypes().getMappings() ;
		for ( int i = 0; i < documentMappings.length; i++ ) {
			RoleIdToDocumentPermissionSetTypeMappings.Mapping mapping = documentMappings[i];
			mappings.setPermissionSetTypeForRole(mapping.getRoleId(), mapping.getDocumentPermissionSetType());
		}
		
		RoleIdToDocumentPermissionSetTypeMappings.Mapping[] mappingsArray = mappings.getMappings();
		Map<Integer, Integer> roleIdToPermissionSetIdMap = document.getMeta().getRoleIdToPermissionSetIdMap();
		
		for ( int i = 0; i < mappingsArray.length; i++ ) {
			RoleIdToDocumentPermissionSetTypeMappings.Mapping mapping = mappingsArray[i];
			RoleId roleId = mapping.getRoleId();
			DocumentPermissionSetTypeDomainObject documentPermissionSetType = mapping.getDocumentPermissionSetType();
			
			if (null == oldDocument
					|| user.canSetDocumentPermissionSetTypeForRoleIdOnDocument(documentPermissionSetType, roleId, oldDocument)) {
				
				// According to schema design NONE value can not be save into table 
				if (documentPermissionSetType.equals(DocumentPermissionSetTypeDomainObject.NONE)) {
					roleIdToPermissionSetIdMap.remove(roleId.intValue());
				} else {
					roleIdToPermissionSetIdMap.put(roleId.intValue(), documentPermissionSetType.getId());
				}
			}
		}
	}

    public void checkIfAliasAlreadyExist(DocumentDomainObject document) throws AliasAlreadyExistsInternalException {
    	String alias = document.getAlias();
    	
    	if (alias != null) {
    		DocumentProperty property = metaDao.getAliasProperty(alias);
    		if (property != null) {
    			Integer documentId = document.getId();
    			
    			if (!property.getDocumentId().equals(documentId)) {
                    throw new AliasAlreadyExistsInternalException(
                    		String.format("Alias %s is allready given to document %d.", alias, documentId));    				
    			}			
    		}
    	}
    }

	public DocumentMapper getDocumentMapper() {
		return documentMapper;
	}

	public void setDocumentMapper(DocumentMapper documentMapper) {
		this.documentMapper = documentMapper;
	}

	public MetaDao getMetaDao() {
		return metaDao;
	}

	public void setMetaDao(MetaDao metaDao) {
		this.metaDao = metaDao;
	}
}