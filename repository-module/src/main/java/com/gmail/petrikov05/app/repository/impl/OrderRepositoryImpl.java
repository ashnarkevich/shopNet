package com.gmail.petrikov05.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.OrderRepository;
import com.gmail.petrikov05.app.repository.model.Order;
import com.gmail.petrikov05.app.repository.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    private final static Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getOrdersByPage(int startPosition, int maxResult) {
        String strQuery = "FROM " + entityClass.getSimpleName() + " o ORDER BY o.dateCreate DESC";
        Query query = entityManager.createQuery(strQuery);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return (List<Order>) query.getResultList();
    }

    @Override
    public Order getOrderByNumber(String number) {
        String strQuery = "FROM " + entityClass.getSimpleName() + " o WHERE o.number = :number";
        Query query = entityManager.createQuery(strQuery);
        query.setParameter("number", number);
        try {
            return (Order) query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("order with number = " + number + " not found");
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getOrdersByPageByUser(int startPosition, int maxResult, User user) {
        String strQuery = "FROM " + entityClass.getSimpleName() + " o WHERE o.customer = :customer ORDER BY o.dateCreate DESC";
        Query query = entityManager.createQuery(strQuery);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        query.setParameter("customer", user);
        return (List<Order>) query.getResultList();
    }

}
