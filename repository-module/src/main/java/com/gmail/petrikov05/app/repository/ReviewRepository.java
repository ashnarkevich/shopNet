package com.gmail.petrikov05.app.repository;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.Review;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    List<Review> getReviewsByPage(int startPosition, int countOfEntities);

}
