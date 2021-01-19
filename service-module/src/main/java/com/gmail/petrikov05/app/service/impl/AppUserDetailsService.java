package com.gmail.petrikov05.app.service.impl;

import java.lang.invoke.MethodHandles;

import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.model.user.AppUser;
import com.gmail.petrikov05.app.service.model.user.LoginUserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_USER_WITH_USERNAME_NOT_FOUND;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public AppUserDetailsService(UserService userService) {this.userService = userService;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserDTO user = userService.getUserByEmail(username);
        if (user == null) {
            logger.info("user equals null");
            throw new UsernameNotFoundException(MESSAGE_USER_WITH_USERNAME_NOT_FOUND);
        }
        return new AppUser(user);
    }

}
