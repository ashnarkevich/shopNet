package com.gmail.petrikov05.app.web.controller;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/")
    public String getIndexPage() {
        logger.info("show index page");
        return "index";
    }

}
