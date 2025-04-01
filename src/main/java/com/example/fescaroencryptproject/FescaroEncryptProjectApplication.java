package com.example.fescaroencryptproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EntityScan(basePackages = "com.example.fescaroencryptproject.domain")
@EnableJpaAuditing
public class FescaroEncryptProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FescaroEncryptProjectApplication.class, args);
    }

}
