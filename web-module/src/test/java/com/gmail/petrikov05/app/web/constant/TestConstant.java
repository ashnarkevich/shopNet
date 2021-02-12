package com.gmail.petrikov05.app.web.constant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface TestConstant {

    //    User
    long VALID_USER_ID = 1L;
    String VALID_USER_LAST_NAME = "TestLastName";
    String VALID_USER_FIRST_NAME = "TestFirstName";
    String VALID_USER_PATRONYMIC = "TestPatronymic";
    String VALID_EMAIL = "test@test.test";
    String VALID_USER_ROLE = "ADMINISTRATOR";
    String VALID_USER_ADDRESS = "Minsk, st.Esenina 23 - 23";
    String VALID_USER_PHONE = "+123 12 1234567";
    String VALID_PASSWORD = "password";

    //    Page
    int VALID_PAGE = 3;
    int VALID_PAGES = 3;

    //    Review
    String VALID_REVIEW_TEXT = "This is the review text.";

    //    Article
    long VALID_ARTICLE_ID = 1L;
    String VALID_ARTICLE_TITLE = "Test article title";
    String VALID_ARTICLE_NUMBER = "09354376-13f9-4e05-b550-1abdddca7bd4";
    LocalDateTime VALID_ARTICLE_DATE = LocalDateTime.of(2020, 12, 15, 00, 00, 00);
    String VALID_ARTICLE_TEXT = "This is the article text.";
    String VALID_ARTICLE_AUTHOR = "BestLastName BestFirstName";
    String VALID_ARTICLE_DATE_PUBLICATION_STR = "2015-02-20 06:30";
    LocalDateTime VALID_ARTICLE_DATE_PUBLICATION =
            LocalDateTime.parse(VALID_ARTICLE_DATE_PUBLICATION_STR, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    //    Comment
    long VALID_COMMENT_ID = 1L;
    String VALID_COMMENT_AUTHOR = "BestLastName BestFirstName";
    String VALID_COMMENT_TEXT = "This is the comment text.";
    LocalDateTime VALID_COMMENT_DATE = LocalDateTime.of(2020, 8, 30, 21, 23, 34);

    String VALID_AUTHOR = VALID_USER_LAST_NAME + " " + VALID_USER_FIRST_NAME;
    boolean VALID_IS_ACTIVE = true;
    boolean VALID_IS_DELETED = true;
    long VALID_ID = 5L;
    LocalDateTime VALID_DATE = LocalDateTime.now();

    long VALID_ORDER_ID = 1L;
    String VALID_ORDER_NUMBER = "5-2021";
    String VALID_ORDER_STATUS = "NEW";
    LocalDateTime VALID_ORDER_DATE = LocalDateTime.of(2020, 8, 22, 8, 25, 30);

    String VALID_ITEM_NAME = "testItemName";
    String VALID_ITEM_NUMBER = "09354376-13f9-4e05-b550-1abdddca7bd4";
    String VALID_ITEM_DESCRIPTION = "Test item description";
    BigDecimal VALID_ITEM_PRICE = BigDecimal.valueOf(15.34);
    int VALID_ITEM_AMOUNT = 45;
    BigDecimal VALID_TOTAL_PRICE = BigDecimal.valueOf(690.3);

}
