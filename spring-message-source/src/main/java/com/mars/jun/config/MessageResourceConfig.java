package com.mars.jun.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author hejun
 * @date 2019/04/25
 */
@Configuration
public class MessageResourceConfig {
    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("exception");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
//        resourceBundleMessageSource.setFallbackToSystemLocale(false);
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        return resourceBundleMessageSource;
    }
}
