package com.gmail.petrikov05.app.web.controller.constant;

import java.time.LocalDateTime;

public interface TestUserConstant {

    //    User
    String VALID_LAST_NAME = "TestLastName";
    String VALID_FIRST_NAME = "TestFirstName";
    String VALID_PATRONYMIC = "TestPatronymic";
    String VALID_EMAIL = "test@test.test";
    String VALID_ROLE = "ADMINISTRATOR";
    Long VALID_ID = 5l;
    Boolean VALID_IS_DELETED = true;

    //    Page
    Integer VALID_PAGE = 3;
    int VALID_PAGES = 3;

    //    Review
    LocalDateTime VALID_REVIEW_DATE_CREATE = LocalDateTime.now();
    String VALID_REVIEW_AUTHOR = VALID_LAST_NAME + " " + VALID_FIRST_NAME;
    String VALID_REVIEW_TEXT = "This is the review text.";
    Boolean VALID_IS_ACTIVE = true;

}
