package com.superman.superman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableEurekaClient
@MapperScan("com.superman.superman.dao")
public class SupermanApplication {
    //    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SupermanApplication.class);
//    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(SupermanApplication.class, args);
    }
}
