package com.gmail.petrikov05.app.web.controler;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/login")
    public String getLoginPage() {
        logger.info("LoginController: show login page");
        return "login";
    }

    @GetMapping("/403")
    public String getAccessDeniedPage() {
        logger.info("trying to access a protected resource");
        return "403";
    }

}
