package com.gmail.petrikov05.app.service.model.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_REGEX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_REGEX_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_USER_ROLE_NOT_NULL;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_MAX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.REGEXP_EMAIL;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.REGEXP_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_PATRONYMIC;

public class AddUserDTO {

    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_LAST_NAME, max = SIZE_MAX_LAST_NAME, message = MESSAGE_SIZE_LAST_NAME)
    @Pattern(regexp = REGEXP_NAME , message = MESSAGE_REGEX_NAME)
    private String lastName;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_FIRST_NAME, max = SIZE_MAX_FIRST_NAME, message = MESSAGE_SIZE_FIRST_NAME)
    @Pattern(regexp = REGEXP_NAME , message = MESSAGE_REGEX_NAME)
    private String firstName;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_PATRONYMIC, max = SIZE_MAX_PATRONYMIC, message = MESSAGE_SIZE_PATRONYMIC)
    @Pattern(regexp = REGEXP_NAME , message = MESSAGE_REGEX_NAME)
    private String patronymic;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(max = SIZE_MAX_EMAIL, message = MESSAGE_SIZE_MAX_EMAIL)
    @Pattern(regexp = REGEXP_EMAIL, message = MESSAGE_REGEX_EMAIL)
    private String email;
    @NotNull(message = MESSAGE_USER_ROLE_NOT_NULL)
    private UserRoleDTOEnum role;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRoleDTOEnum getRole() {
        return role;
    }

    public void setRole(UserRoleDTOEnum role) {
        this.role = role;
    }

}
