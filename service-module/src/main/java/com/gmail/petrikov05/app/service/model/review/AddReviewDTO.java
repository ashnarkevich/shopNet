package com.gmail.petrikov05.app.service.model.review;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_SIZE_MAX_REVIEW_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationRules.SIZE_MAX_REVIEW_TEXT;

public class AddReviewDTO {

    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(max = SIZE_MAX_REVIEW_TEXT, message = MESSAGE_SIZE_MAX_REVIEW_TEXT)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
