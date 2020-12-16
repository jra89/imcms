package com.imcode.imcms.domain.service.api;

import com.imcode.imcms.WebAppSpringTestConfig;
import com.imcode.imcms.api.exception.DataIsNotValidException;
import com.imcode.imcms.components.datainitializer.UserDataInitializer;
import com.imcode.imcms.components.datainitializer.UserPropertyDataInitializer;
import com.imcode.imcms.domain.dto.UserPropertyDTO;
import com.imcode.imcms.domain.service.UserPropertyService;
import com.imcode.imcms.model.Roles;
import com.imcode.imcms.model.UserProperty;
import com.imcode.imcms.persistence.entity.User;
import imcode.server.Imcms;
import imcode.server.user.UserDomainObject;
import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class UserPropertyServiceTest extends WebAppSpringTestConfig {

    @Autowired
    private UserPropertyService userPropertyService;

    @Autowired
    private UserDataInitializer userDataInitializer;

    @Autowired
    private UserPropertyDataInitializer userPropertyDataInitializer;

    private User user;
    private int userId;
    private final String keyName = "keyName";
    private final String value = "value";
    private UserProperty userProperty;

    @BeforeEach
    public void setUp() {
        userDataInitializer.cleanRepositories();
        userPropertyDataInitializer.cleanRepositories();

        user = userDataInitializer.createData("test");
        userId = user.getId();
        userProperty = userPropertyDataInitializer.createData(userId, keyName, value);

        final UserDomainObject userSuperAdmin = new UserDomainObject(1);
        userSuperAdmin.setLanguageIso639_2("eng");
        userSuperAdmin.addRoleId(Roles.SUPER_ADMIN.getId());
        Imcms.setUser(userSuperAdmin);
    }

    @Test
    public void getAll_When_UserPropertyExist_Expected_CorrectResult() {
        assertNotNull(userProperty);
        final UserProperty userProperty2 = userPropertyDataInitializer.createData(userId, "keyName2", "value");

        List<UserProperty> expectedUserPropertyList = userPropertyService.getAll();

        assertFalse(expectedUserPropertyList.isEmpty());
        assertEquals(userProperty, expectedUserPropertyList.get(0));
        assertEquals(userProperty2, expectedUserPropertyList.get(1));
    }


    @Test
    public void getAll_When_UserPropertyNotExist_Expected_EmptyResult() {
        userPropertyService.deleteById(userProperty.getId());
        List<UserProperty> expectedUserPropertyList = userPropertyService.getAll();
        assertTrue(expectedUserPropertyList.isEmpty());
    }

    @Test
    public void getAll_When_UserIsNotSuperAdmin_Expected_CorrectException() {
        setCommonUser();
        assertThrows(AccessDeniedException.class, () -> userPropertyService.getAll());
    }

    @Test
    public void getByUserId_When_UserPropertyExist_Expected_CorrectResult() {
        assertNotNull(userProperty);

        List<UserProperty> expectedUserPropertyList = userPropertyService.getByUserId(userId);

        assertFalse(expectedUserPropertyList.isEmpty());
        assertEquals(1, expectedUserPropertyList.size());

        UserProperty actualUserProperty = expectedUserPropertyList.get(0);
        assertEquals(userProperty.getId(), actualUserProperty.getId());
        assertEquals(userProperty.getUserId(), actualUserProperty.getUserId());
        assertEquals(userProperty.getKeyName(), actualUserProperty.getKeyName());
        assertEquals(userProperty.getValue(), actualUserProperty.getValue());

    }

    @Test
    public void getByUserId_When_UserPropertyNotExist_Expected_EmptyResult() {
        userPropertyService.deleteById(userProperty.getId());
        List<UserProperty> expectedUserPropertyList = userPropertyService.getByUserId(userId);
        assertTrue(expectedUserPropertyList.isEmpty());
    }

    @Test
    public void getByUserId_When_UserNotSuperAdmin_Expected_CorrectException() {
        setCommonUser();
        assertThrows(AccessDeniedException.class, () -> userPropertyService.getByUserId(userId));
    }

    @Test
    public void getByUserIdAndKeyName_When_UserPropertyExist_Expected_CorrectData() {
        assertNotNull(userProperty);

        Optional<UserProperty> expectedUserProperty = userPropertyService.getByUserIdAndKeyName(userId, keyName);
        assertTrue(expectedUserProperty.isPresent());
        assertEquals(userProperty, expectedUserProperty.get());
    }

    @Test
    public void getByUserIdAndKeyName_When_UserPropertyNotExist_Expected_EmptyData() {
        int nonExistentId = 1000;
        Optional<UserProperty> expectedUserProperty = userPropertyService.getByUserIdAndKeyName(nonExistentId, keyName);
        assertFalse(expectedUserProperty.isPresent());
        Optional<UserProperty> expectedUserProperty2 = userPropertyService.getByUserIdAndKeyName(userId, "nonExistentKeyName");
        assertFalse(expectedUserProperty2.isPresent());
    }


    @Test
    public void getByUserIdAndKeyName_When_UserNotSuperAdmin_Expected_CorrectException() {
        assertNotNull(userProperty);

        setCommonUser();
        assertThrows(AccessDeniedException.class, () -> userPropertyService.getByUserIdAndKeyName(userId, keyName).get());
    }

    @Test
    public void getByUserIdAndValue_When_userPropertyExist_Expected_CorrectResult() {
        List<UserProperty> userPropertyList = userPropertyService.getByUserIdAndValue(userId, value);

        assertFalse(userPropertyList.isEmpty());
        assertEquals(1, userPropertyList.size());

        UserProperty actualUserProperty = userPropertyList.get(0);
        assertEquals(userProperty.getId(), actualUserProperty.getId());
        assertEquals(userProperty.getUserId(), actualUserProperty.getUserId());
        assertEquals(userProperty.getKeyName(), actualUserProperty.getKeyName());
        assertEquals(userProperty.getValue(), actualUserProperty.getValue());
    }

    @Test
    public void getByUserIdAndValue_When_userPropertyNotExist_Expected_CorrectResult() {
        int nonExistentId = 1000;
        Optional<UserProperty> expectedUserProperty = userPropertyService.getByUserIdAndKeyName(nonExistentId, value);
        assertFalse(expectedUserProperty.isPresent());
        List<UserProperty> userPropertyList = userPropertyService.getByUserIdAndValue(userId, "nonExistentValue");
        assertTrue(userPropertyList.isEmpty());
    }


    @Test
    public void getByUserIdAndValue_When_UserNotSuperAdmin_Expected_CorrectException(){
        assertNotNull(userProperty);

        setCommonUser();
        assertThrows(AccessDeniedException.class, () -> userPropertyService.getByUserIdAndValue(userId, value));
    }

    @Test
    public void create_When_UserPropertyIsCorrect_Expected_CorrectUserProperty() {
        final UserProperty userProperty = new UserPropertyDTO(null, userId, keyName, value);
        UserProperty expectedUserProperty = userPropertyService.create(userProperty);

        assertNotNull(expectedUserProperty);
        assertEquals(userProperty.getUserId(), expectedUserProperty.getUserId());
        assertEquals(userProperty.getKeyName(), expectedUserProperty.getKeyName());
        assertEquals(userProperty.getValue(), expectedUserProperty.getValue());
    }

    @Test
    public void create_When_KeyNameOrValueIsEmpty_Expected_CorrectException() {
        UserProperty nonExistentUserProperty = new UserPropertyDTO(1000, 1000, "",  "");
        assertThrows(DataIsNotValidException.class, () -> userPropertyService.create(nonExistentUserProperty));
    }

    @Test
    public void create_When_UserNotSuperAdmin_Expected_CorrectException() {
        setCommonUser();
        assertThrows(AccessDeniedException.class, () -> userPropertyService.create(userProperty));
    }

    @Test
    public void update_When_UserPropertyExist_Expected_CorrectData() {
        assertNotNull(userProperty);

        userProperty.setKeyName("keyName2");
        UserProperty expectedUserProperty = userPropertyService.update(userProperty);
        assertNotNull(expectedUserProperty);
        assertEquals(userProperty, expectedUserProperty);

        userProperty.setValue("value2");
        expectedUserProperty = userPropertyService.update(userProperty);
        assertNotNull(expectedUserProperty);
        assertEquals(userProperty, expectedUserProperty);

        userProperty.setKeyName("keyName3");
        userProperty.setValue("value3");
        expectedUserProperty = userPropertyService.update(userProperty);
        assertNotNull(expectedUserProperty);
        assertEquals(userProperty, expectedUserProperty);
    }

    @Test
    public void update_When_UserPropertyNotExist_Expected_CorrectException() {
        assertNotNull(userProperty);

        UserProperty nonExistentUserProperty = new UserPropertyDTO(1000, 1000, keyName,  value);
        assertThrows(EntityNotFoundException.class, () -> userPropertyService.update(nonExistentUserProperty));
    }

    @Test
    public void update_When_KeyNameOrValueIsEmpty_Expected_CorrectException() {
        assertNotNull(userProperty);

        UserProperty userPropertyWithEmptyValue = userProperty;
        userPropertyWithEmptyValue.setValue("");
        assertThrows(DataIsNotValidException.class, () -> userPropertyService.update(userPropertyWithEmptyValue));
        assertNotNull(userPropertyService.getByUserIdAndValue(userId, value));
    }

    @Test
    public void update_When_UserNotSuperAdmin_Expected_CorrectException() {
        assertNotNull(userProperty);

        setCommonUser();
        userProperty.setKeyName("keyName2");
        assertThrows(AccessDeniedException.class, () -> userPropertyService.update(userProperty));
    }

    @Test
    public void delete_When_UserPropertyExist_Expected_EmptyResult() {
        userPropertyService.deleteById(userProperty.getId());
        assertTrue(userPropertyService.getAll().isEmpty());
    }

    @Test
    public void delete_When_UserPropertyNotExist_Expected_CorrectException() {
        int nonExistenceId = 1000;
        assertThrows(EmptyResultDataAccessException.class, ()-> userPropertyService.deleteById(nonExistenceId));
    }

    @Test
    public void delete_When_UserPropertyExistNotSuperAdmin_Expected_CorrectException() {
        assertNotNull(userProperty);

        setCommonUser();
        assertThrows(AccessDeniedException.class, () -> userPropertyService.deleteById(userProperty.getId()));
    }

    private void setCommonUser() {
        final UserDomainObject commonUser = new UserDomainObject(2);
        commonUser.setLanguageIso639_2("eng");
        commonUser.addRoleId(Roles.USER.getId());
        Imcms.setUser(commonUser);
    }
}