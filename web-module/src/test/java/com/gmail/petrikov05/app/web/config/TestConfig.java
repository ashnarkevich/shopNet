package com.gmail.petrikov05.app.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

public class TestConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return mock(PasswordEncoder.class);
    }

}
