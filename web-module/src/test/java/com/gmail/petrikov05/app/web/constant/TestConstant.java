package com.gmail.petrikov05.app.web.constant;

import java.time.LocalDateTime;

public interface TestConstant {

    //    User
    String VALID_LAST_NAME = "TestLastName";
    String VALID_FIRST_NAME = "TestFirstName";
    String VALID_PATRONYMIC = "TestPatronymic";
    String VALID_EMAIL = "test@test.test";
    String VALID_ROLE = "ADMINISTRATOR";
    String VALID_ADDRESS = "Minsk, st.Esenina 23 - 23";
    String VALID_PHONE = "+123 12 1234567";
    String VALID_PASSWORD = "password";

    //    Page
    Integer VALID_PAGE = 3;
    int VALID_PAGES = 3;

    //    Review
    String VALID_REVIEW_TEXT = "This is the review text.";

    //    Article
    Long VALID_ARTICLE_ID = 1L;
    String VALID_ARTICLE_TITLE = "Test article title";
    LocalDateTime VALID_ARTICLE_DATE = LocalDateTime.of(2020, 12, 15, 00, 00, 00);
    String VALID_ARTICLE_TEXT = "This is the article text.";

    //    Comment
    Long VALID_COMMENT_ID = 1L;
    String VALID_COMMENT_AUTHOR = "BestLastName BestFirstName";
    String VALID_COMMENT_TEXT = "This is the comment text.";
    LocalDateTime VALID_COMMENT_DATE = LocalDateTime.of(2020, 8, 30, 21, 23, 34);

    String VALID_AUTHOR = VALID_LAST_NAME + " " + VALID_FIRST_NAME;
    Boolean VALID_IS_ACTIVE = true;
    Boolean VALID_IS_DELETED = true;
    Long VALID_ID = 5L;
    LocalDateTime VALID_DATE = LocalDateTime.now();

}
