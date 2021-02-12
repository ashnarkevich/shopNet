package com.gmail.petrikov05.app.service.model.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_REGEX_PASSWORD;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.REGEXP_PASSWORD;

public class PasswordDTO {

    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_REGEX_PASSWORD)
    private String newPassword;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Pattern(regexp = REGEXP_PASSWORD, message = MESSAGE_REGEX_PASSWORD)
    private String confirmPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
