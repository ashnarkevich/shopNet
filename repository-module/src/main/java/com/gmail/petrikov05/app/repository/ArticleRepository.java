package com.gmail.petrikov05.app.repository;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Article;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    List<Article> getArticlesByPage(int startPosition, int countOfArticleByPage);

}
