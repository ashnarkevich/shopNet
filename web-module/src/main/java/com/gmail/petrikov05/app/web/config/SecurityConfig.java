package com.gmail.petrikov05.app.web.config;

import com.gmail.petrikov05.app.web.controller.security.LoginAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.ADMINISTRATOR;
import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.CUSTOMER_USER;
import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.SALE_USER;
import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.SECURE_API_USER;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {return new LoginAccessDeniedHandler();}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").not().fullyAuthenticated()
                .antMatchers("/users").hasRole(ADMINISTRATOR.name())
                .antMatchers("/reviews").hasRole(ADMINISTRATOR.name())
                .antMatchers("/orders").hasAnyRole(SALE_USER.name(), CUSTOMER_USER.name())
                .antMatchers("/articles").hasRole(CUSTOMER_USER.name())
                .antMatchers("/profile").hasRole(CUSTOMER_USER.name())
                .antMatchers("/items").hasRole(CUSTOMER_USER.name())
                .antMatchers("/api/*").hasRole(SECURE_API_USER.name())
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout().permitAll().logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf().disable();
    }

}
