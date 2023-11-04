package com.technoverse.platformManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlatformManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformManagerApplication.class, args);
    }


}
