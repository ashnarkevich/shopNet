package com.gmail.petrikov05.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    private final static Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getItemsByPage(int startPosition, int maxResult) {
        String queryStr = "FROM " + entityClass.getSimpleName() + " i ORDER BY i.name";
        Query query = entityManager.createQuery(queryStr);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    @Override
    public Optional<Item> getItemByNumber(String number) {
        String queryStr = "FROM " + entityClass.getSimpleName() + " i WHERE i.number = :number";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("number", number);
        try {
            return Optional.of((Item) query.getSingleResult());
        } catch (NoResultException e) {
            logger.info("Item with number (" + number + ") not found");
            return Optional.empty();
        }
    }

    @Override
    public Long getCountItemByName(String name) {
        String queryStr = "SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " i WHERE i.name = :name";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("name", name);
        return (Long) query.getSingleResult();
    }

}
