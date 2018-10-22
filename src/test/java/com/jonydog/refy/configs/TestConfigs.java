package com.jonydog.refy.configs;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(
        basePackages = {
                "com.jonydog.refy.controllers",
                "com.jonydog.refy.business",
                "com.jonydog.refy.daos",
                "com.jonydog.refy.statesources",
                "com.jonydog.refy.configs"
        }
)
public class TestConfigs {

}
