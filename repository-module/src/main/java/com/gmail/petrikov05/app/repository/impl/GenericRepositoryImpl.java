package com.gmail.petrikov05.app.repository.impl;

import java.lang.reflect.ParameterizedType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.GenericRepository;

public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

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
    public void delete(T t) {
        entityManager.remove(t);
    }

    @Override
    public void merge(T t) {
        entityManager.merge(t);
    }

    @Override
    public void persist(T t) {
        entityManager.persist(t);
    }

    @Override
    public T getObjectByID(I i) {
        return entityManager.find(entityClass, i);
    }

}