package com.gmail.petrikov05.app.service;

public interface MailService {

    boolean sendMail(String email, String message, String userEmail);

}
