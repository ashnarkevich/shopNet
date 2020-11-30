package com.gmail.petrikov05.app.repository;

public interface GenericRepository<I, T> {

    Long getCountOfEntities();

    void delete(T t);

    void merge(T t);

    void persist(T t);

    T getObjectByID(I id);

}
