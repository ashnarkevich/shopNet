package com.gmail.petrikov05.app.service.model.article;

import java.time.LocalDateTime;
import java.util.List;

import com.gmail.petrikov05.app.service.model.comment.CommentDTO;
import org.springframework.format.annotation.DateTimeFormat;

public class ArticleDTO {

    private Long id;
    private String title;
    private LocalDateTime dateCreate;
    private LocalDateTime datePublication;
    private String author;
    private String text;
    private List<CommentDTO> comments;

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

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
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

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

}
