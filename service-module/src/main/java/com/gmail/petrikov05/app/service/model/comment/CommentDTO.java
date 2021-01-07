package com.gmail.petrikov05.app.service.model.comment;

import java.time.LocalDateTime;

public class CommentDTO {

    private Long id;
    private String author;
    private LocalDateTime date;
    private String text;

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

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
