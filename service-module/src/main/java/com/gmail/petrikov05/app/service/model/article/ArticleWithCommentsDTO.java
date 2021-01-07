package com.gmail.petrikov05.app.service.model.article;

import java.time.LocalDateTime;
import java.util.List;

import com.gmail.petrikov05.app.service.model.comment.CommentDTO;

public class ArticleWithCommentsDTO {

    private Long id;
    private String title;
    private LocalDateTime date;
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

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
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
