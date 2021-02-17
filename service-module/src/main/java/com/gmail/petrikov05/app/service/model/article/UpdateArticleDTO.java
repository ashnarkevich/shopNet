package com.gmail.petrikov05.app.service.model.article;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_ARTICLE_DATE_PUBLICATION_BEFORE;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_SIZE_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_SIZE_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_TITLE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MAX_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MAX_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MIN_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationRules.SIZE_MIN_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

public class UpdateArticleDTO {

    @NotEmpty(message = MESSAGE_TITLE_NOT_EMPTY)
    @Size(min = SIZE_MIN_ARTICLE_TITLE,
            max = SIZE_MAX_ARTICLE_TITLE,
            message = MESSAGE_SIZE_ARTICLE_TITLE)
    private String title;
    @NotEmpty(message = MESSAGE_NOT_EMPTY)
    @Size(min = SIZE_MIN_ARTICLE_TEXT,
            max = SIZE_MAX_ARTICLE_TEXT,
            message = MESSAGE_SIZE_ARTICLE_TEXT)
    private String text;
    @NotNull(message = MESSAGE_NOT_EMPTY)
    @DateTimeFormat(iso = DATE_TIME)
    @FutureOrPresent(message = MESSAGE_ARTICLE_DATE_PUBLICATION_BEFORE)
    private LocalDateTime datePublication;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateArticleDTO that = (UpdateArticleDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(text, that.text) && Objects.equals(
                datePublication,
                that.datePublication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text, datePublication);
    }

}
