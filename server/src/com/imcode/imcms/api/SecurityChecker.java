package com.imcode.imcms.api;

import imcode.server.document.DocumentDomainObject;
import imcode.server.document.DocumentMapper;
import imcode.server.user.UserDomainObject;

import java.util.Arrays;
import java.util.HashSet;

import com.imcode.imcms.api.NoPermissionException;

class SecurityChecker {

    private final static String SUPERADMIN_ROLE = "Superadmin";
    private final static String USER_ADMIN = "Useradmin";

    private DocumentMapper docMapper;
    private imcode.server.user.UserDomainObject accessingUser;
    private HashSet accessorRoles;

    private boolean isSuperAdmin;
    private boolean isUserAdmin;

    SecurityChecker( DocumentMapper docMapper, UserDomainObject accessor, String[] accessorRoles ) {
        this.docMapper = docMapper;
        this.accessingUser = accessor;
        this.accessorRoles = new HashSet( Arrays.asList( accessorRoles ) );

        isSuperAdmin = this.accessorRoles.contains( SUPERADMIN_ROLE );
        isUserAdmin = this.accessorRoles.contains( USER_ADMIN );
    }

    void loggedIn() throws NoPermissionException {
        if( null == accessingUser ) {
            throw new NoPermissionException( "User not logged in" );
        }
    }

    void isSuperAdmin() throws NoPermissionException {
        if( !isSuperAdmin ) {
            throw new NoPermissionException( "User is not " + SUPERADMIN_ROLE );
        }
    }

    void isSuperAdminOrIsUserAdminOrIsSameUser( User userBean ) throws NoPermissionException {
        boolean isSameUser = userBean.getLoginName().equalsIgnoreCase( accessingUser.getLoginName() );
        if( !isSuperAdmin && !isUserAdmin && !isSameUser ) {
            throw new NoPermissionException( "User is not superadmin, useradmin nor the same user." );
        }
    }

    void hasEditPermission( DocumentDomainObject document ) throws NoPermissionException  {
        if( !docMapper.hasAdminPermissions( document, accessingUser ) ) {
            throw new NoPermissionException("The logged in user does not have permission to edit document: " + document.getMetaId() );
        };
    }

    UserDomainObject getCurrentLoggedInUser() {
        return accessingUser;
    }

    void hasDocumentRights( int metaId ) throws NoPermissionException {
        // todo
    }
}
