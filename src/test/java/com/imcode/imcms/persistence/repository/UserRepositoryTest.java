package com.imcode.imcms.persistence.repository;

import com.imcode.imcms.api.Role;
import com.imcode.imcms.components.datainitializer.UserDataInitializer;
import com.imcode.imcms.config.TestConfig;
import com.imcode.imcms.persistence.entity.User;
import com.imcode.imcms.persistence.entity.User.PasswordReset;
import imcode.server.user.RoleId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserDataInitializer userDataInitializer;

    private List<User> users;

    @Before
    public void createUsers() {
        users = new ArrayList<>(12);
        final List<User> superAdmins = userDataInitializer.createData(5, RoleId.SUPERADMIN_ID);
        final List<User> admins = userDataInitializer.createData(4, RoleId.USERADMIN_ID);
        final List<User> defaultUsers = userDataInitializer.createData(3, RoleId.USERS_ID);

        users.addAll(superAdmins);
        users.addAll(admins);
        users.addAll(defaultUsers);
    }

    @Test
    public void testFindByLogin() {
        assertNotNull(repository.findByLogin("admin"));
    }

    @Test
    public void testFindByLoginIgnoreCase() {
        assertNotNull(repository.findByLoginIgnoreCase("admin"));
    }

    @Test
    public void testFindByEmail() {
        assertNotNull(repository.findByEmail("admin"));
    }

    @Test
    public void testFindById() {
        assertNotNull(repository.findById(1));
    }

    @Test
    public void testFindByPasswordResetId() {
        final User user = users.get(0);
        final PasswordReset passwordReset = new PasswordReset();
        passwordReset.setId("test");
        passwordReset.setTimestamp(new Date().getTime());
        user.setPasswordReset(passwordReset);
        repository.saveAndFlush(user);
        assertNotNull(repository.findByPasswordResetId("test"));
    }

    /**
     * We have predefined super admin user in the database, so we add 1 to actual admin users size.
     */
    @Test
    public void findUsersWithRoleIdsTest() {
        final List<User> admins = repository.findUsersWithRoleIds(Role.SUPERADMIN_ID, RoleId.USERADMIN_ID);
        assertEquals(9 + 1, admins.size());
    }

    @Test
    public void findByIdIn() {

        final List<User> users = this.users.subList(0, 4);

        final Set<Integer> usersIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        final List<User> usersByIds = repository.findByIdIn(usersIds);

        assertEquals(users.size(), usersByIds.size());

        for (int i = 0; i < usersByIds.size(); i++) {
            final User expected = users.get(i);
            final User actual = usersByIds.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getLogin(), actual.getLogin());
        }
    }

}