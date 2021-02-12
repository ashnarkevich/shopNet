package com.gmail.petrikov05.app.repository.impl;

import java.util.List;

import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.ArticleRepository;
import com.gmail.petrikov05.app.repository.model.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getArticlesByPage(int startPosition, int countOfArticleByPage) {
        String queryStr = "FROM " + entityClass.getSimpleName() + " a ORDER BY a.dateCreate desc";
        Query query = entityManager.createQuery(queryStr);
        query.setFirstResult(startPosition);
        query.setMaxResults(countOfArticleByPage);
        return (List<Article>) query.getResultList();
    }

}
