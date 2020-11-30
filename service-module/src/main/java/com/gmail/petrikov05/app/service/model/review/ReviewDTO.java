package com.gmail.petrikov05.app.service.model.review;

import java.time.LocalDateTime;

public class ReviewDTO {

    private Long id;
    private String author;
    private LocalDateTime dateCreate;
    private String text;
    private Boolean isActive;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

}
