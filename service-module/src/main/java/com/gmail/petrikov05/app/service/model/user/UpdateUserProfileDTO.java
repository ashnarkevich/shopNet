package com.gmail.petrikov05.app.service.model.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_REGEX_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.REGEXP_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_PATRONYMIC;

public class UpdateUserProfileDTO {

    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_LAST_NAME, max = SIZE_MAX_LAST_NAME, message = MESSAGE_SIZE_LAST_NAME)
    @Pattern(regexp = REGEXP_NAME, message = MESSAGE_REGEX_NAME)
    private String lastName;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_FIRST_NAME, max = SIZE_MAX_FIRST_NAME, message = MESSAGE_SIZE_FIRST_NAME)
    @Pattern(regexp = REGEXP_NAME, message = MESSAGE_REGEX_NAME)
    private String firstName;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_PATRONYMIC, max = SIZE_MAX_PATRONYMIC, message = MESSAGE_SIZE_PATRONYMIC)
    @Pattern(regexp = REGEXP_NAME, message = MESSAGE_REGEX_NAME)
    private String patronymic;
    private String address;
    private String phone;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
