package com.gmail.petrikov05.app.repository.impl;

import java.util.List;
import javax.persistence.Query;

import com.gmail.petrikov05.app.repository.ReviewRepository;
import com.gmail.petrikov05.app.repository.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Review> getReviewsByPage(int startPosition, int countOfEntities) {
        String queryString = "FROM " + entityClass.getSimpleName() + " r";
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(countOfEntities);
        return (List<Review>) query.getResultList();
    }

}
