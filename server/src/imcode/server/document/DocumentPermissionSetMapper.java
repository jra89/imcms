package imcode.server.document;

import imcode.server.IMCConstants;
import imcode.server.IMCServiceInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DocumentPermissionSetMapper {

    /**
     * Stored procedure names used in this class
     */
    private static final String SPROC_GET_TEMPLATE_GROUPS_WITH_PERMISSIONS = "GetTemplateGroupsWithPermissions";
    private static final String SPROC_GET_TEMPLATE_GROUPS_WITH_NEW_PERMISSIONS = "GetTemplateGroupsWithNewPermissions";
    private static final String SPROC_SET_DOC_PERMISSION_SET = "SetDocPermissionSet";
    private static final String SPROC_SET_NEW_DOC_PERMISSION_SET = "SetNewDocPermissionSet";

    private static class PermissionPair {

        int bit;
        boolean hasPermission;

        public PermissionPair( int permissionBit, boolean hasPermission ) {
            this.hasPermission = hasPermission;
            this.bit = permissionBit;
        }
    }

    private int[] sqlGetDocTypesWithPermissions( int metaId,
                                                 DocumentPermissionSetDomainObject documentPermissionSet ) {
        String table = "doc_permission_sets_ex";
        if ( documentPermissionSet.isForNewDocuments() ) {
            table = "new_" + table;
        }
        String sqlStr = "SELECT permission_data FROM " + table
                        + " WHERE meta_id = ? AND set_id = ? AND permission_id = "
                        + IMCConstants.PERM_CREATE_DOCUMENT;
        String[] documentTypeIdStrings = service.sqlQuery( sqlStr, new String[]{
            "" + metaId, "" + documentPermissionSet.getPermissionType()
        } );
        int[] documentTypeIds = new int[documentTypeIdStrings.length];
        for ( int i = 0; i < documentTypeIdStrings.length; i++ ) {
            documentTypeIds[i] = Integer.parseInt( documentTypeIdStrings[i] );
        }
        return documentTypeIds;
    }

    private TemplateGroupDomainObject[] sqlGetTemplateGroupsWithPermissions( int metaId,
                                                                             DocumentPermissionSetDomainObject documentPermissionSet ) {
        String[] params = new String[]{
            String.valueOf( metaId ), String.valueOf( documentPermissionSet.getPermissionType() )
        };
        String sproc = documentPermissionSet.isForNewDocuments()
                       ? SPROC_GET_TEMPLATE_GROUPS_WITH_NEW_PERMISSIONS : SPROC_GET_TEMPLATE_GROUPS_WITH_PERMISSIONS;
        String[][] sprocResult = service.sqlProcedureMulti( sproc, params );
        List templateGroups = new ArrayList();
        for ( int i = 0; i < sprocResult.length; i++ ) {
            int groupId = Integer.parseInt( sprocResult[i][0] );
            String groupName = sprocResult[i][1];
            boolean hasPermission = -1 != Integer.parseInt( sprocResult[i][2] );
            if ( hasPermission ) {
                TemplateGroupDomainObject templateGroup = new TemplateGroupDomainObject( groupId, groupName );
                templateGroups.add( templateGroup );
            }
        }
        return (TemplateGroupDomainObject[])templateGroups.toArray( new TemplateGroupDomainObject[templateGroups.size()] );
    }

    private IMCServiceInterface service;

    public DocumentPermissionSetMapper( IMCServiceInterface service ) {
        this.service = service;
    }

    public DocumentPermissionSetDomainObject createFullPermissionSet() {
        DocumentPermissionSetDomainObject result;
        result = new DocumentPermissionSetDomainObject( IMCConstants.DOC_PERM_SET_FULL );
        result.setEditDocumentInformation( true );
        result.setEditPermissions( true );
        return result;
    }

    public DocumentPermissionSetDomainObject createRestrictedPermissionSet( DocumentDomainObject document,
                                                                            int permissionType,
                                                                            boolean forNewDocuments ) {
        DocumentPermissionSetDomainObject documentPermissionSet = null;
        if ( document instanceof TextDocumentDomainObject ) {
            documentPermissionSet = new TextDocumentPermissionSetDomainObject( permissionType );
        } else {
            documentPermissionSet = new DocumentPermissionSetDomainObject( permissionType );
        }
        documentPermissionSet.setForNewDocuments( forNewDocuments );
        setDocumentPermissionSetBitsFromDb( document, documentPermissionSet );

        return documentPermissionSet;
    }

    private void setDocumentPermissionSetBitsFromDb( DocumentDomainObject document,
                                                     DocumentPermissionSetDomainObject documentPermissionSet ) {
        String table = "doc_permission_sets";
        if ( documentPermissionSet.isForNewDocuments() ) {
            table = "new_" + table;
        }
        String sqlStr = "SELECT permission_id FROM " + table + " WHERE meta_id = ? AND set_id = ?";
        String permissionBitsString = service.sqlQueryStr( sqlStr, new String[]{
            String.valueOf( document.getId() ), String.valueOf( documentPermissionSet.getPermissionType() )
        } );
        int permissionBits = 0;
        if ( null != permissionBitsString ) {
            permissionBits = Integer.parseInt( permissionBitsString );
        }
        documentPermissionSet.setFromBits( document, this, permissionBits );
    }

    public DocumentPermissionSetDomainObject createReadPermissionSet() {
        DocumentPermissionSetDomainObject result;
        result = new DocumentPermissionSetDomainObject( IMCConstants.DOC_PERM_SET_READ );
        return result;
    }

    public DocumentPermissionSetDomainObject getPermissionSetRestrictedOne( DocumentDomainObject document ) {
        return createRestrictedPermissionSet( document, IMCConstants.DOC_PERM_SET_RESTRICTED_1, false );
    }

    public DocumentPermissionSetDomainObject getPermissionSetRestrictedTwo( DocumentDomainObject document ) {
        return createRestrictedPermissionSet( document, IMCConstants.DOC_PERM_SET_RESTRICTED_2, false );
    }

    public DocumentPermissionSetDomainObject getPermissionSetRestrictedOneForNewDocuments(
            DocumentDomainObject document ) {
        return createRestrictedPermissionSet( document, IMCConstants.DOC_PERM_SET_RESTRICTED_1, true );
    }

    public DocumentPermissionSetDomainObject getPermissionSetRestrictedTwoForNewDocuments(
            DocumentDomainObject document ) {
        return createRestrictedPermissionSet( document, IMCConstants.DOC_PERM_SET_RESTRICTED_2, true );
    }

    public void saveRestrictedDocumentPermissionSets( DocumentDomainObject document ) {
        saveRestrictedDocumentPermissionSet( document, document.getPermissionSetForRestrictedOne() );
        saveRestrictedDocumentPermissionSet( document, document.getPermissionSetForRestrictedTwo() );
        saveRestrictedDocumentPermissionSet( document, document.getPermissionSetForRestrictedOneForNewDocuments() );
        saveRestrictedDocumentPermissionSet( document, document.getPermissionSetForRestrictedTwoForNewDocuments() );
    }

    private void saveRestrictedDocumentPermissionSet( DocumentDomainObject document,
                                                      DocumentPermissionSetDomainObject documentPermissionSet ) {

        List permissionPairs = new ArrayList( Arrays.asList( new PermissionPair[]{
            new PermissionPair( EDIT_DOCINFO_PERMISSION_ID, documentPermissionSet.getEditDocumentInformation() ),
            new PermissionPair( EDIT_PERMISSIONS_PERMISSION_ID, documentPermissionSet.getEditPermissions() ),
            new PermissionPair( EDIT_DOCUMENT_PERMISSION_ID, documentPermissionSet.getEdit() )
        } ) );

        if ( documentPermissionSet instanceof TextDocumentPermissionSetDomainObject ) {
            TextDocumentPermissionSetDomainObject textDocumentPermissionSet = (TextDocumentPermissionSetDomainObject)documentPermissionSet;
            permissionPairs.add( new PermissionPair( EDIT_TEXT_DOCUMENT_IMAGES_PERMISSION_ID, textDocumentPermissionSet.getEditImages() ) );
            permissionPairs.add( new PermissionPair( EDIT_TEXT_DOCUMENT_MENUS_PERMISSION_ID, textDocumentPermissionSet.getEditMenus() ) );
            permissionPairs.add( new PermissionPair( EDIT_TEXT_DOCUMENT_INCLUDES_PERMISSION_ID, textDocumentPermissionSet.getEditIncludes() ) );

        }

        int permissionBits = 0;
        for ( Iterator iterator = permissionPairs.iterator(); iterator.hasNext(); ) {
            PermissionPair permissionPair = (PermissionPair)iterator.next();
            if ( permissionPair.hasPermission ) {
                permissionBits |= permissionPair.bit;
            }
        }

        String sproc = documentPermissionSet.isForNewDocuments()
                       ? SPROC_SET_NEW_DOC_PERMISSION_SET : SPROC_SET_DOC_PERMISSION_SET;
        service.sqlUpdateProcedure( sproc, new String[]{
            "" + document.getId(), "" + documentPermissionSet.getPermissionType(), "" + permissionBits
        } );


        sqlSaveAllowedDocumentTypes( documentPermissionSet, document );

        if ( documentPermissionSet instanceof TextDocumentPermissionSetDomainObject ) {
            TextDocumentPermissionSetDomainObject textDocumentPermissionSet = (TextDocumentPermissionSetDomainObject)documentPermissionSet;
            sqlSaveAllowedTemplateGroups( document, textDocumentPermissionSet );
        }
    }

    private void sqlSaveAllowedTemplateGroups( DocumentDomainObject document,
                                               TextDocumentPermissionSetDomainObject textDocumentPermissionSet ) {
        String table = getExtendedPermissionsTable( textDocumentPermissionSet );
        String sqlDeleteAllowedTemplateGroupIds = "DELETE FROM " + table
                                                  + " WHERE meta_id = ? AND set_id = ? AND permission_id = "
                                                  + EDIT_TEXT_DOCUMENT_TEMPLATE_PERMISSION_ID;
        service.sqlUpdateQuery( sqlDeleteAllowedTemplateGroupIds, new String[]{
            "" + document.getId(), "" + textDocumentPermissionSet.getPermissionType()
        } );
        TemplateGroupDomainObject[] allowedTemplateGroups = textDocumentPermissionSet.getAllowedTemplateGroups();
        if (null == allowedTemplateGroups) {
            return ;
        }
        String sqlInsertAllowedTemplateGroupId = "INSERT INTO " + table + " VALUES(?,?,"
                                                 + EDIT_TEXT_DOCUMENT_TEMPLATE_PERMISSION_ID
                                                 + ",?)";
        for ( int i = 0; i < allowedTemplateGroups.length; i++ ) {
            TemplateGroupDomainObject allowedTemplateGroup = allowedTemplateGroups[i];
            service.sqlUpdateQuery( sqlInsertAllowedTemplateGroupId, new String[]{
                "" + document.getId(), "" + textDocumentPermissionSet.getPermissionType(), ""
                                                                                           + allowedTemplateGroup.getId()
            } );
        }
    }

    private void sqlSaveAllowedDocumentTypes( DocumentPermissionSetDomainObject documentPermissionSet,
                                              DocumentDomainObject document ) {
        String table = getExtendedPermissionsTable( documentPermissionSet );
        String sqlDeleteAllowedDocumentTypeIds = "DELETE FROM " + table
                                                 + " WHERE meta_id = ? AND set_id = ? AND permission_id = "
                                                 + IMCConstants.PERM_CREATE_DOCUMENT;
        service.sqlUpdateQuery( sqlDeleteAllowedDocumentTypeIds, new String[]{
            "" + document.getId(), "" + documentPermissionSet.getPermissionType()
        } );
        int[] allowedDocumentTypeIds = documentPermissionSet.getAllowedDocumentTypeIds();
        if (null == allowedDocumentTypeIds) {
            return ;
        }
        String sqlInsertCreatableDocumentTypeId = "INSERT INTO " + table + " VALUES(?,?,"
                                                  + IMCConstants.PERM_CREATE_DOCUMENT
                                                  + ",?)";
        for ( int i = 0; i < allowedDocumentTypeIds.length; i++ ) {
            int creatableDocumentTypeId = allowedDocumentTypeIds[i];
            service.sqlUpdateQuery( sqlInsertCreatableDocumentTypeId, new String[]{
                "" + document.getId(), "" + documentPermissionSet.getPermissionType(), "" + creatableDocumentTypeId
            } );
        }
    }

    private String getExtendedPermissionsTable( DocumentPermissionSetDomainObject documentPermissionSet ) {
        String table = "doc_permission_sets_ex";
        if ( documentPermissionSet.isForNewDocuments() ) {
            table = "new_" + table;
        }
        return table;
    }

    private final static int EDIT_DOCINFO_PERMISSION_ID = IMCConstants.PERM_EDIT_DOCINFO;
    private final static int EDIT_PERMISSIONS_PERMISSION_ID = IMCConstants.PERM_EDIT_PERMISSIONS;
    private final static int EDIT_DOCUMENT_PERMISSION_ID = IMCConstants.PERM_EDIT_DOCUMENT;

    private final static int EDIT_TEXT_DOCUMENT_IMAGES_PERMISSION_ID = IMCConstants.PERM_EDIT_TEXT_DOCUMENT_IMAGES;
    private final static int EDIT_TEXT_DOCUMENT_MENUS_PERMISSION_ID = IMCConstants.PERM_EDIT_TEXT_DOCUMENT_MENUS;
    private final static int EDIT_TEXT_DOCUMENT_TEMPLATE_PERMISSION_ID = IMCConstants.PERM_EDIT_TEXT_DOCUMENT_TEMPLATE;
    private final static int EDIT_TEXT_DOCUMENT_INCLUDES_PERMISSION_ID = IMCConstants.PERM_EDIT_TEXT_DOCUMENT_INCLUDES;

    void setTextDocumentPermissionSetFromBits( DocumentDomainObject document,
                                               TextDocumentPermissionSetDomainObject textDocumentPermissionSet,
                                               int permissionBits ) {
        setDocumentPermissionSetFromBits( document, textDocumentPermissionSet, permissionBits );
        textDocumentPermissionSet.setEditImages( 0 != ( permissionBits & EDIT_TEXT_DOCUMENT_IMAGES_PERMISSION_ID ) );
        textDocumentPermissionSet.setEditMenus( 0 != ( permissionBits & EDIT_TEXT_DOCUMENT_MENUS_PERMISSION_ID ) );
        textDocumentPermissionSet.setEditIncludes( 0 != ( permissionBits & EDIT_TEXT_DOCUMENT_INCLUDES_PERMISSION_ID ) );
        textDocumentPermissionSet.setEditTemplates( 0
                                                    != ( permissionBits & EDIT_TEXT_DOCUMENT_TEMPLATE_PERMISSION_ID ) );

        if ( 0 != ( permissionBits & EDIT_TEXT_DOCUMENT_TEMPLATE_PERMISSION_ID ) ) {
            TemplateGroupDomainObject[] allowedTemplateGroups = sqlGetTemplateGroupsWithPermissions( document.getId(), textDocumentPermissionSet );
            textDocumentPermissionSet.setAllowedTemplateGroups( allowedTemplateGroups );
        }
    }

    void setDocumentPermissionSetFromBits( DocumentDomainObject document,
                                           DocumentPermissionSetDomainObject documentPermissionSet, int permissionBits ) {
        documentPermissionSet.setEditDocumentInformation( 0 != ( permissionBits & EDIT_DOCINFO_PERMISSION_ID ) );
        documentPermissionSet.setEditPermissions( 0 != ( permissionBits & EDIT_PERMISSIONS_PERMISSION_ID ) );
        documentPermissionSet.setEdit( 0 != ( permissionBits & EDIT_DOCUMENT_PERMISSION_ID ) );

        if ( 0 != ( permissionBits & IMCConstants.PERM_CREATE_DOCUMENT ) ) {
            int[] documentTypeIds = sqlGetDocTypesWithPermissions( document.getId(), documentPermissionSet );
            documentPermissionSet.setAllowedDocumentTypeIds( documentTypeIds );
        }
    }

}
