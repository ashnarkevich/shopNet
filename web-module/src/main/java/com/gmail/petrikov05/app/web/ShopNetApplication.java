package com.gmail.petrikov05.app.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication(
        exclude = UserDetailsServiceAutoConfiguration.class,
        scanBasePackages = {
                "com.gmail.petrikov05.app.web",
                "com.gmail.petrikov05.app.service",
                "com.gmail.petrikov05.app.repository"
        }
)
public class ShopNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopNetApplication.class, args);
    }

}
