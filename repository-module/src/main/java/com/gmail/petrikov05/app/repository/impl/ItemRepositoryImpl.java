package com.gmail.petrikov05.app.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
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
        String strQuery = "FROM " + entityClass.getSimpleName() + " i ORDER BY i.name";
        Query query = entityManager.createQuery(strQuery);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Item getItemsByNumber(String number) {
        String strQuery = "FROM " + entityClass.getSimpleName() + " i WHERE i.number = :number";
        Query query = entityManager.createQuery(strQuery);
        query.setParameter("number", number);
        try {
            return (Item) query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("item with number ( " + number + " ) not found");
            return null;
        }
    }

}
