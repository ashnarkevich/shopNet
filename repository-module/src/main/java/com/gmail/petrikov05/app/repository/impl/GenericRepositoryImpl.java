package com.gmail.petrikov05.app.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.GenericRepository;
import com.gmail.petrikov05.app.repository.model.Article;

public abstract class GenericRepositoryImpl<L, T> implements GenericRepository<L, T> {

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperClass.getActualTypeArguments()[1];
    }

    @Override
    public Long getCountOfEntities() {
        String queryString = "SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " c";
        Query query = entityManager.createQuery(queryString);
        return (Long) query.getSingleResult();
    }

    @Override
    public boolean delete(T t) {
        entityManager.remove(t);
        return true;
    }

    @Override
    public void merge(T t) {
        entityManager.merge(t);
    }

    @Override
    public void add(T t) {
        entityManager.persist(t);
    }

    @Override
    public T getObjectByID(L id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getAllObjects() {
        String queryString = "FROM " + entityClass.getSimpleName() + " c";
        Query query = entityManager.createQuery(queryString);
        return (List<Article>) query.getResultList();
    }

}