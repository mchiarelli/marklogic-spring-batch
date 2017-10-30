package com.marklogic.spring.batch.test;

import com.marklogic.client.ext.helper.DatabaseClientProvider;
import com.marklogic.junit.NamespaceProvider;
import com.marklogic.junit.spring.AbstractSpringTest;
import com.marklogic.spring.batch.config.MarkLogicBatchConfiguration;
import com.marklogic.xcc.template.XccTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

/**
 * Base class for any "core" test.
 */
@ContextConfiguration(classes = { MarkLogicBatchConfiguration.class } )
public abstract class AbstractSpringBatchTest extends AbstractSpringTest {

    protected ApplicationContext applicationContext;

    @Override
    protected NamespaceProvider getNamespaceProvider() {
        return new SpringBatchNamespaceProvider();
    }

    @Override
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setDatabaseClientProvider(applicationContext.getBean("databaseClientProvider", DatabaseClientProvider.class));
        setXccTemplate(applicationContext.getBean("xccTemplate", XccTemplate.class));
    }

    @Configuration
    @ComponentScan(basePackages= { "com.marklogic.spring.batch"} )
    @PropertySource(value = "classpath:job.properties")
    public static class TestConfig {



    }


}
