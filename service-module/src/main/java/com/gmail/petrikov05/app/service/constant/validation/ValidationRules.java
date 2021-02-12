package com.gmail.petrikov05.app.service.constant.validation;

public interface ValidationRules {

    int SIZE_MIN_LAST_NAME = 2;
    int SIZE_MAX_LAST_NAME = 40;
    int SIZE_MIN_FIRST_NAME = 2;
    int SIZE_MAX_FIRST_NAME = 20;
    int SIZE_MIN_PATRONYMIC = 2;
    int SIZE_MAX_PATRONYMIC = 40;
    int SIZE_MAX_EMAIL = 50;
    int SIZE_MAX_REVIEW_TEXT = 200;

    String REGEXP_NAME = "^[a-zA-Z]+$";
    String REGEXP_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    String REGEXP_PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_]{1,}$";

}
