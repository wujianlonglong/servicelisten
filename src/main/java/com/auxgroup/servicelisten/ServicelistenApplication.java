package com.auxgroup.servicelisten;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableScheduling
@SpringBootApplication
public class ServicelistenApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicelistenApplication.class, args);
    }
}
