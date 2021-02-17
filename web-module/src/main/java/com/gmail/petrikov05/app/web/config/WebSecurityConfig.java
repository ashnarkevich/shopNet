package com.gmail.petrikov05.app.web.config;

import com.gmail.petrikov05.app.web.security.LoginAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.ADMINISTRATOR;
import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.CUSTOMER_USER;
import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.SALE_USER;

@Configuration
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(
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
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").not().fullyAuthenticated()
                // articles
                .antMatchers(HttpMethod.GET, "/articles").hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers(HttpMethod.POST, "/articles").hasRole(SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/articles/add").hasRole(SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/articles/*").hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/articles/*/update").hasRole(SALE_USER.name())
                .antMatchers(HttpMethod.POST, "/articles/*/update").hasRole(SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/articles/*/delete").hasRole(SALE_USER.name())
                .antMatchers(HttpMethod.POST, "/articles/*/comments/*/delete").hasRole(SALE_USER.name())
                // items
                .antMatchers(HttpMethod.GET, "/items").hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/items/*").hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/items/*/delete").hasRole(SALE_USER.name())
                .antMatchers(HttpMethod.GET, "/items/*/copy").hasRole(SALE_USER.name())
                // orders
                .antMatchers(HttpMethod.GET, "/orders").hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers(HttpMethod.POST, "/orders").hasRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.GET, "/orders/*").hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers(HttpMethod.POST, "/orders/*/update").hasRole(SALE_USER.name())
                // profile
                .antMatchers(HttpMethod.GET, "/profile").hasRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.GET, "/profile/update").hasRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.POST, "/profile/update").hasRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.GET, "/profile/changePass").hasRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.POST, "/profile/changePass").hasRole(CUSTOMER_USER.name())
                // reviews
                .antMatchers(HttpMethod.GET, "/reviews").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.POST, "/reviews/delete").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/reviews/add").hasRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.POST, "/reviews/add").hasRole(CUSTOMER_USER.name())
                // user
                .antMatchers(HttpMethod.GET, "/users").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.POST, "/users/delete").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/users/*/updateRole").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/users/*/updatePass").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/users/add").hasRole(ADMINISTRATOR.name())
                .antMatchers(HttpMethod.POST, "/users/add").hasRole(ADMINISTRATOR.name())
                //                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout().permitAll().logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

}
