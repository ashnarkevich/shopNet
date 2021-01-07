package com.gmail.petrikov05.app.service.util.converter;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Article;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;

public class ArticleConverter {

    public static ArticleDTO convertObjectToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setDate(article.getDate());
        articleDTO.setText(article.getText());
        UserDetails userDetails = article.getAuthor().getUserDetails();
        String author = userDetails.getLastName() + " " + userDetails.getFirstName();
        articleDTO.setAuthor(author);
        return articleDTO;
    }

    public static ArticleWithCommentsDTO converterObjectToDTOWithComments(Article article) {
        ArticleWithCommentsDTO articleWithCommentsDTO = new ArticleWithCommentsDTO();
        articleWithCommentsDTO.setId(article.getId());
        articleWithCommentsDTO.setTitle(article.getTitle());
        articleWithCommentsDTO.setDate(article.getDate());
        articleWithCommentsDTO.setAuthor(article.getAuthor().getUserDetails().getLastName() + " "
                + article.getAuthor().getUserDetails().getFirstName());
        articleWithCommentsDTO.setText(article.getText());
        List<CommentDTO> commentDTOS = CommentConverter.getCommentDTOS(article.getComments());
        articleWithCommentsDTO.setComments(commentDTOS);
        return articleWithCommentsDTO;
    }

    public static Article convertAddDTOToObject(AddArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        return article;
    }

}
