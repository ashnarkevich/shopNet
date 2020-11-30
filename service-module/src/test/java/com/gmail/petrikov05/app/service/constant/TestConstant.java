package com.gmail.petrikov05.app.service.constant;

import java.time.LocalDateTime;

import static com.gmail.petrikov05.app.service.util.PageUtil.getStartPosition;

public interface TestConstant {

    String VALID_LAST_NAME = "TestLastName";
    String VALID_FIRST_NAME = "TestFirstName";
    String VALID_PATRONYMIC = "TestPatronymicName";
    String VALID_EMAIL_SUPER_ADMIN = "admin@shop.com";
    String VALID_EMAIL = "Test@test.test";
    String VALID_USER_ROLE = "ADMINISTRATOR";
    Long VALID_USER_ID = 5L;
    String VALID_PASSWORD = "password";
    Boolean VALID_IS_DELETED = true;

    int VALID_PAGE = 2;
    int VALID_PAGES = 2;
    Long VALID_COUNT_OF_ENTITIES = 18L;
    int VALID_OBJECT_BY_PAGE = 10;
    int VALID_START_POSITION = getStartPosition(VALID_PAGE, VALID_OBJECT_BY_PAGE);

    String VALID_REVIEW_TEXT = "test review content";
    Long VALID_REVIEW_ID = 1L;
    String VALID_REVIEW_AUTHOR = VALID_LAST_NAME + " " + VALID_FIRST_NAME;
    Boolean VALID_REVIEW_IS_ACTIVE = true;
    LocalDateTime VALID_REVIEW_DATE_CREATE =
            LocalDateTime.of(2002, 8, 05, 12, 59, 41);

}
