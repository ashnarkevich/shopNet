package com.gmail.petrikov05.app.service.exception;

import com.gmail.petrikov05.app.service.constant.MessageConstant;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ACCESS_CLOSE;

public class AnonymousUserException extends Throwable {

    public AnonymousUserException() {
        super(MESSAGE_ACCESS_CLOSE);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
