package com.gmail.petrikov05.app.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.ReviewRepository;
import com.gmail.petrikov05.app.repository.model.Review;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.service.ReviewService;
import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.AddReviewDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.converter.ReviewConverter;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_REVIEW_BY_PAGE;
import static com.gmail.petrikov05.app.service.util.PageUtil.getCountOfPage;
import static com.gmail.petrikov05.app.service.util.converter.ReviewConverter.convertObjectToDTO;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserService userService
    ) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public PaginationWithEntitiesDTO<ReviewDTO> getReviewsByPage(Integer page) {
        int startPosition = PageUtil.getStartPosition(page, COUNT_OF_REVIEW_BY_PAGE);
        List<Review> reviews = reviewRepository.getReviewsByPage(startPosition, COUNT_OF_REVIEW_BY_PAGE);
        List<ReviewDTO> reviewDTOS = convertListObjectToDTO(reviews);
        int pages = getPages();
        return new PaginationWithEntitiesDTO<>(reviewDTOS, pages);
    }

    @Override
    @Transactional
    public List<ReviewDTO> deletedReviews(List<Long> ids) {
        List<Review> reviews = new ArrayList<>();
        for (Long id : ids) {
            Review review = reviewRepository.getObjectByID(id);
            if (review != null) {
                reviewRepository.delete(review);
                reviews.add(review);
            }
        }
        return convertListObjectToDTO(reviews);
    }

    @Override
    @Transactional
    public ReviewDTO addReview(AddReviewDTO reviewDTO) throws AnonymousUserException {
        Review review = ReviewConverter.convertAddDTOToObject(reviewDTO);
        User user = userService.getCurrentUser();
        review.setAuthor(user);
        review = reviewRepository.add(review);
        return convertObjectToDTO(review);
    }

    private List<ReviewDTO> convertListObjectToDTO(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewConverter::convertObjectToDTO)
                .collect(Collectors.toList());
    }

    private int getPages() {
        Long countOfEntities = reviewRepository.getCountOfEntities();
        return getCountOfPage(countOfEntities, COUNT_OF_REVIEW_BY_PAGE);
    }

}
