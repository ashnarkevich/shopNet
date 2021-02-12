package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.UpdateArticleDTO;

public interface ArticleService {

    PaginationWithEntitiesDTO<ArticlePreviewDTO> getArticlesByPage(Integer page);

    ArticleDTO getArticleById(Long id);

    List<ArticlePreviewDTO> getAllArticles();

    ArticleDTO addArticle(AddArticleDTO addArticleDTO) throws AnonymousUserException;

    boolean deleteById(Long id) throws ObjectDBException;

    ArticleDTO updateArticle(Long id, UpdateArticleDTO updateArticleDTO) throws ObjectDBException;

}
