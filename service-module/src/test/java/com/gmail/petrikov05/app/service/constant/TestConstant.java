package com.gmail.petrikov05.app.service.constant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.gmail.petrikov05.app.service.util.PageUtil.getStartPosition;

public interface TestConstant {

    String VALID_LAST_NAME = "TestLastName";
    String VALID_FIRST_NAME = "TestFirstName";
    String VALID_PATRONYMIC = "TestPatronymicName";
    String VALID_EMAIL_SUPER_ADMIN = "admin@shop.com";
    String VALID_EMAIL = "Test@test.test";
    String VALID_USER_ROLE = "ADMINISTRATOR";
    String VALID_PASSWORD = "password";
    String VALID_ADDRESS = "Minsk, st.Esenina 23 - 23";
    String VALID_PHONE = "+123 12 1234567";
    Long VALID_USER_ID = 5L;

    int VALID_PAGE = 2;
    int VALID_PAGES = 2;
    int VALID_OBJECT_BY_PAGE = 10;
    int VALID_START_POSITION = getStartPosition(VALID_PAGE, VALID_OBJECT_BY_PAGE);
    Long VALID_ID = 1L;
    Long VALID_COUNT_OF_ENTITIES = 18L;
    Boolean VALID_IS_DELETED = true;
    LocalDateTime VALID_DATE =
            LocalDateTime.of(2020, 10, 15, 12, 59, 41);
    String VALID_AUTHOR = VALID_LAST_NAME + " " + VALID_FIRST_NAME;

    String VALID_REVIEW_TEXT = "test review content";
    Boolean VALID_REVIEW_IS_ACTIVE = true;
    LocalDateTime VALID_REVIEW_DATE_CREATE =
            LocalDateTime.of(2002, 8, 05, 12, 59, 41);

    String VALID_ARTICLE_TEXT = "test article text";
    String VALID_ARTICLE_TITLE = "test title";

    Long VALID_COMMENT_ID = 35L;
    LocalDateTime VALID_COMMENT_DATE =
            LocalDateTime.of(2020, 8, 05, 12, 59, 41);
    String VALID_COMMENT_TEXT = "This is a comment text";
    String VALID_COMMENT_LAST_NAME = "TestAuthor";
    String VALID_COMMENT_FIRST_NAME = "authorTest";
    String VALID_COMMENT_AUTHOR =  VALID_COMMENT_LAST_NAME + " " + VALID_COMMENT_FIRST_NAME;

    Long VALID_ORDER_ID = 2L;
    String VALID_ORDER_NUMBER = "1-2021";
    String VALID_ORDER_STATUS = "IN_PROGRESS";
    LocalDateTime VALID_ORDER_DATE_CREATE = LocalDateTime.of(2020,6,28,23,45,46);

    String VALID_ITEM_NAME = "testItemName";
    String VALID_ITEM_NUMBER = "asd2341";
    int VALID_ITEM_AMOUNT = 36;
    BigDecimal VALID_ITEM_PRICE = BigDecimal.valueOf(4.11);
    BigDecimal VALID_TOTAL_PRICE = BigDecimal.valueOf(1234.23);

}
