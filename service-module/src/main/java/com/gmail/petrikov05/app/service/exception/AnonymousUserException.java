package com.gmail.petrikov05.app.service.exception;

public class AnonymousUserException extends Throwable {

    private final static String MESSAGE = "You do not have access.";

    public AnonymousUserException() {
        super(MESSAGE);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
