package com.gmail.petrikov05.app.service.model.article;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_SIZE_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_SIZE_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MAX_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.ValidationRules.SIZE_MIN_ARTICLE_TITLE;

public class AddArticleDTO {

    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_ARTICLE_TITLE,
            max = SIZE_MAX_ARTICLE_TITLE,
            message = MESSAGE_SIZE_ARTICLE_TITLE)
    private String title;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_ARTICLE_TEXT,
            max = SIZE_MAX_ARTICLE_TEXT,
            message = MESSAGE_SIZE_ARTICLE_TEXT)
    private String text;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
