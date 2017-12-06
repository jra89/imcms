package com.imcode.imcms.domain.service.api;

import com.imcode.imcms.components.datainitializer.TemplateDataInitializer;
import com.imcode.imcms.config.TestConfig;
import com.imcode.imcms.config.WebTestConfig;
import com.imcode.imcms.domain.dto.TemplateGroupDTO;
import com.imcode.imcms.domain.service.TemplateGroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class, WebTestConfig.class})
public class TemplateGroupServiceTest {

    @Autowired
    private TemplateDataInitializer dataInitializer;

    @Autowired
    private TemplateGroupService templateGroupService;

    @Before
    public void setUp() throws Exception {
        dataInitializer.cleanRepositories();
    }

    @Test
    public void getAll_When_templateGroupsWithTemplates_Expect_theyAllPersisted() {
        int i = 1;
        final List<TemplateGroupDTO> expected = Arrays.asList(dataInitializer.createData("test " + i++, i++, false),
                dataInitializer.createData("test " + i++, i++, false),
                dataInitializer.createData("test " + i++, i++, false),
                dataInitializer.createData("test " + i++, i++, false),
                dataInitializer.createData("test " + i++, i, false)
        );

        assertTrue(templateGroupService.getAll().containsAll(expected));
    }

    @Test
    public void get() {
        final String name = "TEST";
        final TemplateGroupDTO test = dataInitializer.createData(name, 5, false);
        final TemplateGroupDTO persistedByName = templateGroupService.get(name);

        assertEquals(test, persistedByName);
    }

    @Test
    public void save() {
        final TemplateGroupDTO test = dataInitializer.createData("test", 5, true);

        templateGroupService.save(test);

        final TemplateGroupDTO persisted = templateGroupService.get("test");
        persisted.setId(null);
        assertEquals(test, persisted);
    }
}