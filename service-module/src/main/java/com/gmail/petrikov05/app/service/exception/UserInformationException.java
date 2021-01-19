package com.gmail.petrikov05.app.service.exception;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_USER_WITHOUT_INFORMATION;

public class UserInformationException extends Throwable {

    public UserInformationException() {
        super(MESSAGE_USER_WITHOUT_INFORMATION);
    }

}
