package com.gmail.petrikov05.app.service.util;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final JavaMailSender javaMailSender;

    public MailUtil(JavaMailSender javaMailSender) {this.javaMailSender = javaMailSender;}

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
            logger.info(to + ": message send");
        } catch (MailException e) {
            logger.info(to + ": message not send");
        }
    }

}
