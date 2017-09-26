package com.imcode.imcms.mapping.jpa;

import com.imcode.imcms.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional(Transactional.TxType.SUPPORTS)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void testFindAll() throws Exception {
        repository.findAll(true, true);
        repository.findAll(true, false);
        repository.findAll(false, false);
        repository.findAll(false, true);
    }

    @Test
    public void testFindByLogin() throws Exception {
        repository.findByLogin("admin");
    }

    @Test
    public void testFindByLoginIgnoreCase() throws Exception {
        repository.findByLoginIgnoreCase("admin");
    }

    @Test
    public void testFindByEmail() throws Exception {
        repository.findByEmail("admin");
    }

    @Test
    public void testFindByEmailUnique() throws Exception {
        repository.findByEmailUnique("admin");
    }

    @Test
    public void testFindById() throws Exception {
        repository.findById(1);
    }

    @Test
    public void testFindByPasswordResetId() throws Exception {
        repository.findByPasswordResetId("0");
    }

    @Test
    public void findByNamePrefix() throws Exception {
        repository.findByNamePrefix("prefix", true);
        repository.findByNamePrefix("prefix", false);
    }
}
