package com.imcode.imcms.domain.service.core;

import com.imcode.imcms.domain.dto.DocumentRoles;
import com.imcode.imcms.domain.service.DocumentRolesService;
import com.imcode.imcms.model.RestrictedPermission;
import com.imcode.imcms.persistence.entity.DocumentRole;
import com.imcode.imcms.persistence.entity.Meta;
import com.imcode.imcms.persistence.entity.Meta.Permission;
import com.imcode.imcms.persistence.entity.RestrictedPermissionJPA;
import com.imcode.imcms.security.AccessType;
import imcode.server.Imcms;
import imcode.server.user.UserDomainObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAccessServiceTest {

    private static final int userId = 1;
    private static final int documentId = 1001;

    @Mock
    private DocumentRolesService documentRolesService;

    @InjectMocks
    private DefaultAccessService accessService;

    @Before
    public void setUp() {
        final UserDomainObject user = new UserDomainObject();
        Imcms.setUser(user);
    }

    @After
    public void tearDown() {
        Imcms.removeUser();
    }

    @Test
    public void hasUserEditAccess_When_DocumentNotExist_Expect_False() {
        testHasUserEditAccessWhenDocumentRolesListIsEmpty();
    }

    @Test
    public void hasUserEditAccess_When_UserHasNoRoleForEditAccess_Expect_False() {
        testHasUserEditAccessWhenDocumentRolesListIsEmpty();
    }

    @Test
    public void hasUserEditAccess_When_UserHasEditAccess_Expect_True() {
        final DocumentRole documentRole = new DocumentRole();
        documentRole.setPermission(Permission.EDIT);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), null);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasViewPermission_Expect_False() {
        final DocumentRole documentRole = new DocumentRole();
        documentRole.setPermission(Permission.VIEW);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), null);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertFalse(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasNonePermission_Expect_False() {
        final DocumentRole documentRole = new DocumentRole();
        documentRole.setPermission(Permission.NONE);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), null);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertFalse(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasEditAndNonePermissions_Expect_True() {
        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setPermission(Permission.EDIT);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setPermission(Permission.NONE);

        final DocumentRoles documentRoles = new DocumentRoles(
                Arrays.asList(firstDocumentRole, secondDocumentRole),
                null
        );
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditImageAccessAndDocumentDoesNot_Expect_False() {
        final RestrictedPermissionJPA restrictedPermission = new RestrictedPermissionJPA();
        restrictedPermission.setPermission(Permission.RESTRICTED_2);
        restrictedPermission.setEditImage(true);

        final Meta meta = new Meta();
        meta.setRestrictedPermissions(new HashSet<>(Collections.singletonList(restrictedPermission)));

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertFalse(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditImageAccessAndDocumentToo_Expect_True() {
        final RestrictedPermissionJPA restrictedPermission1 = new RestrictedPermissionJPA();
        restrictedPermission1.setPermission(Permission.RESTRICTED_1);
        restrictedPermission1.setEditImage(true);

        final RestrictedPermissionJPA restrictedPermission2 = new RestrictedPermissionJPA();
        restrictedPermission2.setPermission(Permission.RESTRICTED_2);
        restrictedPermission2.setEditImage(true);

        final Set<RestrictedPermissionJPA> restrictedPermissions = new HashSet<>();
        restrictedPermissions.add(restrictedPermission1);
        restrictedPermissions.add(restrictedPermission2);

        final Meta meta = new Meta();
        meta.setRestrictedPermissions(restrictedPermissions);

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditImageAccessAndDocumentAnother_Expect_False() {
        final RestrictedPermissionJPA restrictedPermission1 = new RestrictedPermissionJPA();
        restrictedPermission1.setPermission(Permission.RESTRICTED_1);
        restrictedPermission1.setEditImage(false);
        restrictedPermission1.setEditMenu(true);

        final RestrictedPermissionJPA restrictedPermission2 = new RestrictedPermissionJPA();
        restrictedPermission2.setPermission(Permission.RESTRICTED_2);
        restrictedPermission2.setEditImage(true);

        final Set<RestrictedPermissionJPA> restrictedPermissions = new HashSet<>();
        restrictedPermissions.add(restrictedPermission1);
        restrictedPermissions.add(restrictedPermission2);

        final Meta meta = new Meta();
        meta.setRestrictedPermissions(restrictedPermissions);

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertFalse(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditMenuAccessAndDocumentToo_Expect_True() {
        final RestrictedPermissionJPA restrictedPermission1 = new RestrictedPermissionJPA();
        restrictedPermission1.setPermission(Permission.RESTRICTED_1);
        restrictedPermission1.setEditMenu(true);

        final RestrictedPermissionJPA restrictedPermission2 = new RestrictedPermissionJPA();
        restrictedPermission2.setPermission(Permission.RESTRICTED_2);
        restrictedPermission2.setEditImage(true);

        final Set<RestrictedPermissionJPA> restrictedPermissions = new HashSet<>();
        restrictedPermissions.add(restrictedPermission1);
        restrictedPermissions.add(restrictedPermission2);

        final Meta meta = new Meta();
        meta.setId(documentId);
        meta.setRestrictedPermissions(restrictedPermissions);

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.MENU);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditLoopAccessAndDocumentToo_Expect_True() {
        final RestrictedPermissionJPA restrictedPermission1 = new RestrictedPermissionJPA();
        restrictedPermission1.setPermission(Permission.RESTRICTED_1);
        restrictedPermission1.setEditLoop(true);

        final RestrictedPermissionJPA restrictedPermission2 = new RestrictedPermissionJPA();
        restrictedPermission2.setPermission(Permission.RESTRICTED_2);
        restrictedPermission2.setEditImage(true);

        final Set<RestrictedPermissionJPA> restrictedPermissions = new HashSet<>();
        restrictedPermissions.add(restrictedPermission1);
        restrictedPermissions.add(restrictedPermission2);

        final Meta meta = new Meta();
        meta.setId(documentId);
        meta.setRestrictedPermissions(restrictedPermissions);

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.LOOP);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditTextAccessAndDocumentToo_Expect_True() {
        final RestrictedPermissionJPA restrictedPermission1 = new RestrictedPermissionJPA();
        restrictedPermission1.setPermission(Permission.RESTRICTED_1);
        restrictedPermission1.setEditText(true);

        final RestrictedPermissionJPA restrictedPermission2 = new RestrictedPermissionJPA();
        restrictedPermission2.setPermission(Permission.RESTRICTED_2);
        restrictedPermission2.setEditImage(true);

        final Set<RestrictedPermissionJPA> restrictedPermissions = new HashSet<>();
        restrictedPermissions.add(restrictedPermission1);
        restrictedPermissions.add(restrictedPermission2);

        final Meta meta = new Meta();
        meta.setId(documentId);
        meta.setRestrictedPermissions(restrictedPermissions);

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.TEXT);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void hasUserEditAccess_When_UserHasRestrictedEditDocInfoAccessAndDocumentToo_Expect_True() {
        final RestrictedPermissionJPA restrictedPermission1 = new RestrictedPermissionJPA();
        restrictedPermission1.setPermission(Permission.RESTRICTED_1);
        restrictedPermission1.setEditDocInfo(true);

        final RestrictedPermissionJPA restrictedPermission2 = new RestrictedPermissionJPA();
        restrictedPermission2.setPermission(Permission.RESTRICTED_2);
        restrictedPermission2.setEditImage(true);

        final Set<RestrictedPermissionJPA> restrictedPermissions = new HashSet<>();
        restrictedPermissions.add(restrictedPermission1);
        restrictedPermissions.add(restrictedPermission2);

        final Meta meta = new Meta();
        meta.setId(documentId);
        meta.setRestrictedPermissions(restrictedPermissions);

        final DocumentRole documentRole = new DocumentRole();
        documentRole.setDocument(meta);
        documentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRoles documentRoles = new DocumentRoles(Collections.singletonList(documentRole), meta);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.DOC_INFO);

        assertTrue(hasUserEditAccess);
    }

    @Test
    public void getEditPermission_When_UserHasNoRoles_Expect_ViewPermission() {
        testWhenDocumentRolesListIsEmpty();
    }

    @Test
    public void getEditPermission_When_DocumentHasNotRoles_Expect_ViewPermission() {
        testWhenDocumentRolesListIsEmpty();
    }

    @Test
    public void getEditPermission_When_UserAndDocumentHaveEditPermission_Expect_EditPermission() {
        final Permission editPermissionEnum = Permission.EDIT;

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setPermission(Permission.VIEW);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setPermission(editPermissionEnum);

        final DocumentRole thirdDocumentRole = new DocumentRole();
        thirdDocumentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRole fourthDocumentRole = new DocumentRole();
        fourthDocumentRole.setPermission(Permission.RESTRICTED_2);

        final List<DocumentRole> documentRoleList = Arrays.asList(
                firstDocumentRole, secondDocumentRole, thirdDocumentRole, fourthDocumentRole
        );

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, null);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(editPermissionEnum));
        assertTrue(editPermission.isEditText());
        assertTrue(editPermission.isEditMenu());
        assertTrue(editPermission.isEditImage());
        assertTrue(editPermission.isEditLoop());
        assertTrue(editPermission.isEditDocInfo());
    }

    @Test
    public void getEditPermission_When_UserAndDocumentHaveOnlyViewPermission_Expect_ViewPermissionReturned() {
        final Permission viewPermission = Permission.VIEW;

        final Meta document = new Meta();
        document.setRestrictedPermissions(Collections.emptySet());

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(viewPermission);

        final List<DocumentRole> documentRoleList = Collections.singletonList(firstDocumentRole);

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(viewPermission));
        assertFalse(editPermission.isEditText());
        assertFalse(editPermission.isEditMenu());
        assertFalse(editPermission.isEditImage());
        assertFalse(editPermission.isEditLoop());
        assertFalse(editPermission.isEditDocInfo());
    }

    @Test
    public void getEditPermission_When_UserAndDocumentHaveRestricted1AndRestricted2_Expect_UnionOfThem() {
        final Permission restricted1 = Permission.RESTRICTED_1;
        final Permission restricted2 = Permission.RESTRICTED_2;

        final Meta document = new Meta();

        final RestrictedPermissionJPA firstPermission = new RestrictedPermissionJPA();
        firstPermission.setPermission(restricted1);
        firstPermission.setEditText(true);
        firstPermission.setEditMenu(false);
        firstPermission.setEditImage(true);
        firstPermission.setEditLoop(false);
        firstPermission.setEditDocInfo(false);

        final RestrictedPermissionJPA secondPermission = new RestrictedPermissionJPA();
        secondPermission.setPermission(restricted2);
        secondPermission.setEditText(true);
        secondPermission.setEditMenu(false);
        secondPermission.setEditImage(false);
        secondPermission.setEditLoop(true);
        secondPermission.setEditDocInfo(false);

        document.setRestrictedPermissions(new HashSet<>(Arrays.asList(firstPermission, secondPermission)));

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(Permission.VIEW);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setDocument(document);
        secondDocumentRole.setPermission(restricted1);

        final DocumentRole thirdDocumentRole = new DocumentRole();
        thirdDocumentRole.setDocument(document);
        thirdDocumentRole.setPermission(restricted2);

        final List<DocumentRole> documentRoleList = Arrays.asList(
                firstDocumentRole, secondDocumentRole, thirdDocumentRole
        );

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(restricted1));
        assertTrue(editPermission.isEditText());
        assertFalse(editPermission.isEditMenu());
        assertTrue(editPermission.isEditImage());
        assertTrue(editPermission.isEditLoop());
        assertFalse(editPermission.isEditDocInfo());
    }

    @Test
    public void getEditPermission_When_UserAndDocumentHaveRestricted1Permission_Expect_Permission1Returned() {
        testRestrictedPermissionWhenUserAndDocumentHasCorrespondingOne(Permission.RESTRICTED_1);
    }

    @Test
    public void getEditPermission_When_UserAndDocumentHaveRestricted2Permission_Expect_Permission2Returned() {
        testRestrictedPermissionWhenUserAndDocumentHasCorrespondingOne(Permission.RESTRICTED_2);
    }

    @Test
    public void getEditPermission_When_UserHasRestricted1AndRestricted2ButDocumentDoesNotBoth_Expect_ViewPermission() {
        final Permission viewPermission = Permission.VIEW;

        final Meta document = new Meta();
        document.setRestrictedPermissions(Collections.emptySet());

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(viewPermission);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setDocument(document);
        secondDocumentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRole thirdDocumentRole = new DocumentRole();
        thirdDocumentRole.setDocument(document);
        thirdDocumentRole.setPermission(Permission.RESTRICTED_2);

        final List<DocumentRole> documentRoleList = Arrays.asList(
                firstDocumentRole, secondDocumentRole, thirdDocumentRole
        );

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(viewPermission));
        assertFalse(editPermission.isEditText());
        assertFalse(editPermission.isEditMenu());
        assertFalse(editPermission.isEditImage());
        assertFalse(editPermission.isEditLoop());
        assertFalse(editPermission.isEditDocInfo());
    }

    @Test
    public void getEditPerm_When_UserHasRestricted1AndRestricted2ButDocumentHasOnlyRestricted1_Expect_Restricted1() {
        testWhenUserHasAllOfThemButDocumentHasOnlySpecified(Permission.RESTRICTED_1);
    }

    @Test
    public void getEditPerm_When_UserHasRestricted1AndRestricted2ButDocumentHasOnlyRestricted2_Expect_Restricted2() {
        testWhenUserHasAllOfThemButDocumentHasOnlySpecified(Permission.RESTRICTED_2);
    }

    @Test
    public void getEditPermission_When_UserHasRestricted1ButDocumentHasRestricted1AndRestricted2_Expect_Restricted1() {
        testWhenDocumentHasAllOfThemButUserHasOnlySpecified(Permission.RESTRICTED_1);
    }

    @Test
    public void getEditPermission_When_UserHasRestricted2ButDocumentHasRestricted1AndRestricted2_Expect_Restricted2() {
        testWhenDocumentHasAllOfThemButUserHasOnlySpecified(Permission.RESTRICTED_2);
    }

    @Test
    public void getEditPermission_When_UserHasRestricted1ButDocumentDoesNotHaveBoth_Expect_View() {
        testWhenUserHasOneSpecifiedButDocumentDoesNotHaveBoth(Permission.RESTRICTED_1);
    }

    @Test
    public void getEditPermission_When_UserHasRestricted2ButDocumentDoesNotHaveBoth_Expect_View() {
        testWhenUserHasOneSpecifiedButDocumentDoesNotHaveBoth(Permission.RESTRICTED_2);
    }

    private void testWhenUserHasOneSpecifiedButDocumentDoesNotHaveBoth(Permission permissionEnum) {
        final Meta document = new Meta();
        document.setRestrictedPermissions(Collections.emptySet());

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(Permission.VIEW);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setDocument(document);
        secondDocumentRole.setPermission(permissionEnum);

        final List<DocumentRole> documentRoleList = Arrays.asList(
                firstDocumentRole, secondDocumentRole
        );

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(Permission.VIEW));
        assertFalse(editPermission.isEditText());
        assertFalse(editPermission.isEditMenu());
        assertFalse(editPermission.isEditImage());
        assertFalse(editPermission.isEditLoop());
        assertFalse(editPermission.isEditDocInfo());
    }

    private void testWhenDocumentHasAllOfThemButUserHasOnlySpecified(Permission permissionEnum) {
        final Meta document = new Meta();

        final Map<Permission, RestrictedPermissionJPA> permissionMap = new HashMap<>();

        final RestrictedPermissionJPA firstPermission = new RestrictedPermissionJPA();
        firstPermission.setPermission(Permission.RESTRICTED_1);
        firstPermission.setEditText(true);
        firstPermission.setEditMenu(false);
        firstPermission.setEditImage(true);
        firstPermission.setEditLoop(false);
        firstPermission.setEditDocInfo(false);

        final RestrictedPermissionJPA secondPermission = new RestrictedPermissionJPA();
        secondPermission.setPermission(Permission.RESTRICTED_2);
        secondPermission.setEditText(true);
        secondPermission.setEditMenu(false);
        secondPermission.setEditImage(false);
        secondPermission.setEditLoop(true);
        secondPermission.setEditDocInfo(false);

        permissionMap.put(Permission.RESTRICTED_1, firstPermission);
        permissionMap.put(Permission.RESTRICTED_2, secondPermission);

        document.setRestrictedPermissions(new HashSet<>(permissionMap.values()));

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(Permission.VIEW);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setDocument(document);
        secondDocumentRole.setPermission(permissionEnum);

        final List<DocumentRole> documentRoleList = Arrays.asList(
                firstDocumentRole, secondDocumentRole
        );

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        final RestrictedPermissionJPA actualRestrictedPermission = permissionMap.get(permissionEnum);

        assertThat(editPermission.getPermission(), is(permissionEnum));
        assertThat(editPermission.isEditText(), is(actualRestrictedPermission.isEditText()));
        assertThat(editPermission.isEditMenu(), is(actualRestrictedPermission.isEditMenu()));
        assertThat(editPermission.isEditImage(), is(actualRestrictedPermission.isEditImage()));
        assertThat(editPermission.isEditLoop(), is(actualRestrictedPermission.isEditLoop()));
        assertThat(editPermission.isEditDocInfo(), is(actualRestrictedPermission.isEditDocInfo()));
    }

    private void testWhenUserHasAllOfThemButDocumentHasOnlySpecified(Permission permissionEnum) {
        final Meta document = new Meta();

        final RestrictedPermissionJPA firstPermission = new RestrictedPermissionJPA();
        firstPermission.setPermission(permissionEnum);
        firstPermission.setEditText(true);
        firstPermission.setEditMenu(false);
        firstPermission.setEditImage(true);
        firstPermission.setEditLoop(false);
        firstPermission.setEditDocInfo(false);

        document.setRestrictedPermissions(new HashSet<>(Collections.singletonList(firstPermission)));

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(Permission.VIEW);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setDocument(document);
        secondDocumentRole.setPermission(Permission.RESTRICTED_1);

        final DocumentRole thirdDocumentRole = new DocumentRole();
        thirdDocumentRole.setDocument(document);
        thirdDocumentRole.setPermission(Permission.RESTRICTED_2);

        final List<DocumentRole> documentRoleList = Arrays.asList(
                firstDocumentRole, secondDocumentRole, thirdDocumentRole
        );

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(permissionEnum));
        assertThat(editPermission.isEditText(), is(firstPermission.isEditText()));
        assertThat(editPermission.isEditMenu(), is(firstPermission.isEditMenu()));
        assertThat(editPermission.isEditImage(), is(firstPermission.isEditImage()));
        assertThat(editPermission.isEditLoop(), is(firstPermission.isEditLoop()));
        assertThat(editPermission.isEditDocInfo(), is(firstPermission.isEditDocInfo()));
    }

    private void testRestrictedPermissionWhenUserAndDocumentHasCorrespondingOne(Permission permissionEnum) {
        final Meta document = new Meta();

        final RestrictedPermissionJPA permissionJPA = new RestrictedPermissionJPA();
        permissionJPA.setPermission(permissionEnum);
        permissionJPA.setEditText(true);
        permissionJPA.setEditMenu(false);
        permissionJPA.setEditImage(true);
        permissionJPA.setEditLoop(false);
        permissionJPA.setEditDocInfo(false);

        document.setRestrictedPermissions(new HashSet<>(Collections.singletonList(permissionJPA)));

        final DocumentRole firstDocumentRole = new DocumentRole();
        firstDocumentRole.setDocument(document);
        firstDocumentRole.setPermission(Permission.VIEW);

        final DocumentRole secondDocumentRole = new DocumentRole();
        secondDocumentRole.setDocument(document);
        secondDocumentRole.setPermission(permissionEnum);

        final List<DocumentRole> documentRoleList = Arrays.asList(firstDocumentRole, secondDocumentRole);

        final DocumentRoles documentRoles = new DocumentRoles(documentRoleList, document);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(permissionEnum));
        assertThat(editPermission.isEditText(), is(permissionJPA.isEditText()));
        assertThat(editPermission.isEditMenu(), is(permissionJPA.isEditMenu()));
        assertThat(editPermission.isEditImage(), is(permissionJPA.isEditImage()));
        assertThat(editPermission.isEditLoop(), is(permissionJPA.isEditLoop()));
        assertThat(editPermission.isEditDocInfo(), is(permissionJPA.isEditDocInfo()));
    }

    private void testWhenDocumentRolesListIsEmpty() {
        final DocumentRoles documentRoles = new DocumentRoles(Collections.emptyList(), null);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final RestrictedPermission editPermission = accessService.getEditPermission(userId, documentId);

        assertThat(editPermission.getPermission(), is(Permission.VIEW));
        assertFalse(editPermission.isEditText());
        assertFalse(editPermission.isEditMenu());
        assertFalse(editPermission.isEditImage());
        assertFalse(editPermission.isEditLoop());
        assertFalse(editPermission.isEditDocInfo());
    }

    private void testHasUserEditAccessWhenDocumentRolesListIsEmpty() {
        final DocumentRoles documentRoles = new DocumentRoles(Collections.emptyList(), null);
        when(documentRolesService.getDocumentRoles(documentId, userId)).thenReturn(documentRoles);

        final boolean hasUserEditAccess = accessService.hasUserEditAccess(userId, documentId, AccessType.IMAGE);

        assertFalse(hasUserEditAccess);
    }
}
