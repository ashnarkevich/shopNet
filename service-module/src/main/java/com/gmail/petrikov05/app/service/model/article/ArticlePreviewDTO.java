package com.gmail.petrikov05.app.service.model.article;

import java.time.LocalDateTime;

public class ArticlePreviewDTO {

    private Long id;
    private String title;
    private LocalDateTime dateCreate;
    private String author;
    private String text;
    private LocalDateTime datePublication;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }

}
