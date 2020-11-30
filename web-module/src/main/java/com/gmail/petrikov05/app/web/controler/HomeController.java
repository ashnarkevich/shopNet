package com.gmail.petrikov05.app.web.controler;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/home")
    public String getHomePage() {
        logger.info("show home page. redirect /..");
        return "redirect:/";
    }

}
