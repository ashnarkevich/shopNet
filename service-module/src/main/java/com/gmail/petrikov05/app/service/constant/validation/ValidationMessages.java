package com.gmail.petrikov05.app.service.constant.validation;

import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_REVIEW_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MIN_PATRONYMIC;

public interface ValidationMessages {

    String MESSAGE_NOT_EMPTY = "Field can not be empty.";
    String MESSAGE_USER_ROLE_NOT_NULL = "The role is not selected.";
    String MESSAGE_ORDER_STATUS_NOT_NULL = "The status is not selected.";
    String MESSAGE_ITEM_AMOUNT_NOT_EMPTY = "Did not specify the number of items.";

    String MESSAGE_REGEX_NAME = "Field can be only letters: A-Z, a-z.";
    String MESSAGE_REGEX_EMAIL = "Invalid email address.";
    String MESSAGE_REGEX_PASSWORD = "Password can be only: A-Z, a-z, 0-9, !@#$()_%^&*";

    String MESSAGE_SIZE_LAST_NAME =
            "Last name must be between " + SIZE_MIN_LAST_NAME + " and " + SIZE_MAX_LAST_NAME + " characters.";
    String MESSAGE_SIZE_FIRST_NAME =
            "First name must be between " + SIZE_MIN_FIRST_NAME + " and " + SIZE_MAX_FIRST_NAME + " characters.";
    String MESSAGE_SIZE_PATRONYMIC =
            "Patronymic must be between " + SIZE_MIN_PATRONYMIC
                    + " and " + SIZE_MAX_PATRONYMIC + " characters.";
    String MESSAGE_SIZE_MAX_EMAIL = "Email must be no longer than " + SIZE_MAX_EMAIL + " characters.";
    String MESSAGE_SIZE_MAX_REVIEW_TEXT = "The review must be no longer than " + SIZE_MAX_REVIEW_TEXT + " characters.";

    String MESSAGE_DATE_PUBLICATION_BEFORE = "Вate must be later than now.";
}
