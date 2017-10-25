package com.imcode.imcms.config;

import com.imcode.db.Database;
import com.imcode.imcms.api.DocumentLanguages;
import com.imcode.imcms.mapping.DocumentLanguageMapper;
import com.imcode.imcms.util.l10n.CachingLocalizedMessageProvider;
import com.imcode.imcms.util.l10n.ImcmsPrefsLocalizedMessageProvider;
import com.imcode.imcms.util.l10n.LocalizedMessageProvider;
import imcode.server.Config;
import imcode.server.DefaultImcmsServices;
import imcode.server.Imcms;
import imcode.server.ImcmsServices;
import imcode.util.CachingFileLoader;
import imcode.util.io.FileUtility;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan({
        "com.imcode.imcms.servlet.apis",
        "com.imcode.imcms.controller",
        "com.imcode.imcms.mapping",
        "imcode.server"
})
public class WebConfig {

    private static final String DEFAULT_SOLR_HOME = "WEB-INF/solr";
    private static final Logger LOG = Logger.getLogger(WebConfig.class);

    @Bean
    public File realPath(ServletContext servletContext) {
        return new File(servletContext.getRealPath("/"));
    }

    @Bean
    public Properties imcmsProperties(StandardEnvironment env, File realPath) {
        final Properties imcmsProperties = (Properties) env.getPropertySources().get("imcms.properties").getSource();
        final String solrHome = new File(realPath.getAbsolutePath(), DEFAULT_SOLR_HOME).getAbsolutePath();
        imcmsProperties.setProperty("SolrHome", solrHome);
        return imcmsProperties;
    }

    @Bean
    public LocalizedMessageProvider createLocalizedMessageProvider() {
        return new CachingLocalizedMessageProvider(new ImcmsPrefsLocalizedMessageProvider());
    }

    @Bean
    public DocumentLanguages createDocumentLanguages(DocumentLanguageMapper languageMapper, Properties imcmsProperties) {
        return DocumentLanguages.create(languageMapper, imcmsProperties);
    }

    @Bean
    public CachingFileLoader createCachingFileLoader() {
        return new CachingFileLoader();
    }

    @Bean
    public Config createConfigFromProperties(Properties imcmsProperties) {
        class WebappRelativeFileConverter implements Converter {
            @SuppressWarnings("unchecked")
            public File convert(Class type, Object value) {
                return FileUtility.getFileFromWebappRelativePath((String) value);
            }
        }

        Config config = new Config();
        ConvertUtils.register(new WebappRelativeFileConverter(), File.class);
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(config);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (null == propertyDescriptor.getWriteMethod()) {
                continue;
            }
            String uncapitalizedPropertyName = propertyDescriptor.getName();
            String capitalizedPropertyName = StringUtils.capitalize(uncapitalizedPropertyName);
            String propertyValue = imcmsProperties.getProperty(capitalizedPropertyName);
            if (null != propertyValue) {
                try {
                    BeanUtils.setProperty(config, uncapitalizedPropertyName, propertyValue);
                } catch (Exception e) {
                    LOG.error("Failed to set property " + capitalizedPropertyName, e.getCause());
                    continue;
                }
            }
            try {
                String setPropertyValue = BeanUtils.getProperty(config, uncapitalizedPropertyName);
                if (null != setPropertyValue) {
                    LOG.info(capitalizedPropertyName + " = " + setPropertyValue);
                } else {
                    LOG.warn(capitalizedPropertyName + " not set.");
                }
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }
        return config;
    }

    @Bean
    public ImcmsServices createServices(Properties imcmsProperties, Database database, DocumentLanguages languages,
                                        LocalizedMessageProvider localizedMessageProvider, Config config,
                                        ApplicationContext applicationContext, CachingFileLoader fileLoader) {

        return new DefaultImcmsServices(
                database,
                imcmsProperties,
                localizedMessageProvider,
                fileLoader,
                applicationContext,
                config,
                languages);
    }

    @Bean
    public Imcms imcms(ServletContext servletContext,
                       @Qualifier("dataSourceWithAutoCommit") DataSource dataSource,
                       ImcmsServices imcmsServices,
                       Properties imcmsProperties) {

        return new Imcms(servletContext, dataSource, imcmsServices, imcmsProperties);
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(Properties imcmsProperties) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(Long.parseLong(imcmsProperties.getProperty("ImageArchiveMaxImageUploadSize")));
        return multipartResolver;
    }

    @Bean
    public ViewResolver templateViewResolver() {
        return instantiateJspViewResolver("/WEB-INF/templates/text/");
    }

    @Bean
    public ViewResolver internalViewResolver() {
        return instantiateJspViewResolver("/WEB-INF/jsp/imcms/views/");
    }

    private ViewResolver instantiateJspViewResolver(String prefix) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(prefix);
        viewResolver.setSuffix(".jsp");
        viewResolver.setExposedContextBeanNames("loopService", "imageService", "menuService");
        return viewResolver;
    }

}
