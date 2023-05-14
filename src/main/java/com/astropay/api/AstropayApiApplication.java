package com.astropay.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AstropayApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AstropayApiApplication.class, args);
    }
}
