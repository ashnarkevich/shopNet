package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;

public interface ReviewService {

    PaginationWithEntitiesDTO<ReviewDTO> getReviewsByPage(Integer page);

    List<ReviewDTO> deletedReviews(List<Long> ids);

}
