package com.springbootproject.movie;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class TestConfig {
    @Bean

    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}