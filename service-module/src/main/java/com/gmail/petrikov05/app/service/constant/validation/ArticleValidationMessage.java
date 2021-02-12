package com.gmail.petrikov05.app.service.constant.validation;

import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MAX_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MAX_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MIN_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MIN_ARTICLE_TITLE;

public interface ArticleValidationMessage {

    String MESSAGE_TITLE_NOT_EMPTY = "Title can not be empty.";
    String MESSAGE_SIZE_ARTICLE_TITLE =
            "Article title must be between " + SIZE_MIN_ARTICLE_TITLE + " and "
                    + SIZE_MAX_ARTICLE_TITLE + " characters.";
    String MESSAGE_SIZE_ARTICLE_TEXT =
            "Article text must be between " + SIZE_MIN_ARTICLE_TEXT + " and "
                    + SIZE_MAX_ARTICLE_TEXT + " characters.";

}
