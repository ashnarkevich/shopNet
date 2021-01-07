package com.gmail.petrikov05.app.web.controller.security;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class LoginAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.info(String.format("User with username %s and role %s attempted to access the protected URL: %s",
                    authentication.getName(),
                    authentication.getAuthorities(),
                    httpServletRequest.getRequestURI()));
        }
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/403");
    }

}
