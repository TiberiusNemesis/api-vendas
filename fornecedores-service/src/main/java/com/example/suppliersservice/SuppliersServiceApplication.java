package com.example.suppliersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SuppliersServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SuppliersServiceApplication.class, args);
    }
}
