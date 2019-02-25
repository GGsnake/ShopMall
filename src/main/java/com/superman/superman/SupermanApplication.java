package com.superman.superman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableCaching
@EnableAsync
//@EnableDiscoveryClient
@MapperScan("com.superman.superman.dao")
public class SupermanApplication {

    public static void main(String[] args) {

        SpringApplication.run(SupermanApplication.class, args);
    }
}
