package com.gmail.petrikov05.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.GenericRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GenericRepositoryImpl<L, T> implements GenericRepository<L, T> {

    private final static Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

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
        try {
            entityManager.remove(t);
            return true;
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    @Override
    public T merge(T t) {
        entityManager.merge(t);
        return t;
    }

    @Override
    public T add(T t) {
        entityManager.persist(t);
        return t;
    }

    @Override
    public T getObjectByID(L id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllObjects() {
        String queryString = "FROM " + entityClass.getSimpleName() + " c";
        Query query = entityManager.createQuery(queryString);
        return (List<T>) query.getResultList();
    }

}