package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;

public interface ArticleService {

    PaginationWithEntitiesDTO<ArticleDTO> getArticlesByPage(Integer page);

    ArticleWithCommentsDTO getArticleById(Long id);

    List<ArticleDTO> getAllArticles();

    ArticleDTO addArticle(AddArticleDTO addArticleDTO) throws AnonymousUserException;

    boolean deleteById(Long id);

}
