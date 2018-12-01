package com.superman.superman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableCaching
@EnableAsync
@MapperScan("com.superman.superman.dao")
public class SupermanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupermanApplication.class, args);
    }
}
