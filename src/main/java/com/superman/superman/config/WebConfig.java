package com.superman.superman.config;

import com.superman.superman.req.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by liujupeng on 2018/11/21.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
    }
    @Bean
    public  AuthenticationInterceptor authenticationInterceptor(){
        return new AuthenticationInterceptor();
    }
}
