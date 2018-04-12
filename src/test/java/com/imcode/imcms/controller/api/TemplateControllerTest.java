package com.imcode.imcms.controller.api;

import com.imcode.imcms.components.datainitializer.TemplateDataInitializer;
import com.imcode.imcms.controller.AbstractControllerTest;
import com.imcode.imcms.model.Template;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class TemplateControllerTest extends AbstractControllerTest {

    @Autowired
    private TemplateDataInitializer dataInitializer;
    private List<Template> templatesExpected;

    @Override
    protected String controllerPath() {
        return "/templates";
    }

    @Before
    public void setUp() throws Exception {
        dataInitializer.cleanRepositories();
        templatesExpected = dataInitializer.createData(5);
    }

    @Test
    public void getTemplatesTest() throws Exception {
        final String templatesJson = asJson(templatesExpected);
        getAllExpectedOkAndJsonContentEquals(templatesJson);
    }

}