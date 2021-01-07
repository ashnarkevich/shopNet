package com.gmail.petrikov05.app.repository;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Article;

public interface GenericRepository<L, T> {

    Long getCountOfEntities();

    boolean delete(T t);

    void merge(T t);

    void add(T t);

    T getObjectByID(L id);

    List<Article> getAllObjects();

}
