package com.superman.superman.config;

import com.superman.superman.req.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by snake on 2018/11/21.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/team/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/member/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/collect/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/other/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/Shop/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/oder/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/taobao/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/score/**");
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/wx/**");
    }
    @Bean
    public  AuthenticationInterceptor authenticationInterceptor(){
        return new AuthenticationInterceptor();
    }
}
