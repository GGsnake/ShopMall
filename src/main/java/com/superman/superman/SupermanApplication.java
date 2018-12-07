package com.superman.superman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableCaching
@EnableAsync
@MapperScan("com.superman.superman.dao")
public class SupermanApplication  {
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SupermanApplication.class);
//    }


    public static void main(String[] args) {
        SpringApplication.run(SupermanApplication.class, args);
    }
}
