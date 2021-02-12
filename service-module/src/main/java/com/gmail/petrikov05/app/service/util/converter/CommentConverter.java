package com.gmail.petrikov05.app.service.util.converter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.petrikov05.app.repository.model.Comment;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;

public class CommentConverter {

    public static CommentDTO convertObjectToDTO( Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getId());
        commentDTO.setDate(comment.getDate());
        commentDTO.setText(comment.getText());
        String author = comment.getAuthor().getUserDetails().getLastName() + " "
                + comment.getAuthor().getUserDetails().getFirstName();
        commentDTO.setAuthor(author);
        return commentDTO;
    }

    public static List<CommentDTO> getCommentDTOS(List<Comment> comments) {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getDate).reversed())
                .map(CommentConverter::convertObjectToDTO)
                .collect(Collectors.toList());
    }

}
