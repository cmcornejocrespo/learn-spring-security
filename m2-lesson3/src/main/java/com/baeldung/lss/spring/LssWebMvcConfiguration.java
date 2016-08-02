package com.baeldung.lss.spring;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@EnableWebMvc
public class LssWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/login").setViewName("loginPage");

        registry.setOrder(HIGHEST_PRECEDENCE);
    }

}