package com.imcode.imcms.domain.service.api;

import com.imcode.imcms.config.TestConfig;
import com.imcode.imcms.config.WebTestConfig;
import com.imcode.imcms.domain.dto.ImageFolderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class, WebTestConfig.class})
@WebAppConfiguration
@Transactional
public class ImageFolderServiceTest {

    @Autowired
    private ImageFolderService imageFolderService;

    @Test
    public void getImageFolder() {
        final ImageFolderDTO imageFolder = imageFolderService.getImageFolder();

        assertNotNull(imageFolder);
    }

}
