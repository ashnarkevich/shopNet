package com.gmail.petrikov05.app.service.constant;

import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_REVIEW_CONTENT;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_PATRONYMIC;

public interface ValidationMessages {

    String MESSAGE_NOT_EMPTY = "Field can't be empty.";

    String MESSAGE_ROLE_NOT_NULL = "The role is not selected.";

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
    String MESSAGE_SIZE_MAX_REVIEW_CONTENT = "must be no longer than "
            + SIZE_MAX_REVIEW_CONTENT + " characters.";

    String MESSAGE_NOT_VALID_NUMBER = "Not valid number.";

    String MESSAGE_SIZE_ARTICLE_TITLE =
            "Article title must be between " + SIZE_MIN_ARTICLE_TITLE + " and "
                    + SIZE_MAX_ARTICLE_TITLE + " characters.";
    String MESSAGE_SIZE_ARTICLE_TEXT =
            "Article text must be between " + SIZE_MIN_ARTICLE_TEXT + " and "
                    + SIZE_MAX_ARTICLE_TEXT + " characters.";

}
