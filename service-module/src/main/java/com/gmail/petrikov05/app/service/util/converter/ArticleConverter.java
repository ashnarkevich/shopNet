package com.gmail.petrikov05.app.service.util.converter;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Article;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;

public class ArticleConverter {

    public static ArticlePreviewDTO convertObjectToPreviewDTO(Article article) {
        ArticlePreviewDTO articlePreviewDTO = new ArticlePreviewDTO();
        articlePreviewDTO.setId(article.getId());
        articlePreviewDTO.setTitle(article.getTitle());
        articlePreviewDTO.setDateCreate(article.getDateCreate());
        articlePreviewDTO.setDatePublication(article.getDatePublication());
        articlePreviewDTO.setText(article.getText());
        UserDetails userDetails = article.getAuthor().getUserDetails();
        String author = userDetails.getLastName() + " " + userDetails.getFirstName();
        articlePreviewDTO.setAuthor(author);
        return articlePreviewDTO;
    }

    public static ArticleDTO convertObjectToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setDateCreate(article.getDateCreate());
        articleDTO.setDatePublication(article.getDatePublication());
        articleDTO.setAuthor(article.getAuthor().getUserDetails().getLastName() + " "
                + article.getAuthor().getUserDetails().getFirstName());
        articleDTO.setText(article.getText());
        List<CommentDTO> commentDTOS = CommentConverter.getCommentDTOS(article.getComments());
        articleDTO.setComments(commentDTOS);
        return articleDTO;
    }

    public static Article convertAddDTOToObject(AddArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        article.setDatePublication(articleDTO.getDatePublication());
        return article;
    }

}
