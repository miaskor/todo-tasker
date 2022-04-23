package com.miaskor.todo.spring.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
  //Redundant, but if you wanna set particular URL, must used this bean
    /*@Bean
    public FilterRegistrationBean<MainFilter> filterRegistrationBean(){
        FilterRegistrationBean<MainFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new MainFilter());
        filterBean.addUrlPatterns("*");
        return filterBean;
    }*/
}
