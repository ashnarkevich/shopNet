package com.gmail.petrikov05.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.gmail.petrikov05.app.repository.ReviewRepository;
import com.gmail.petrikov05.app.repository.model.Review;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.service.impl.ReviewServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_OBJECT_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_AUTHOR;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_DATE_CREATE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_IS_ACTIVE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_TEXT;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_START_POSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void setup() {
        this.reviewService = new ReviewServiceImpl(reviewRepository);
    }

    /* get user by page */
    @Test
    void getReviewsWithPagination_returnPaginationWithReviews() {
        when(reviewRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        List<Review> returnedReviews = getValidReviews();
        when(reviewRepository.getReviewsByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE))
                .thenReturn(returnedReviews);
        PaginationWithEntitiesDTO<ReviewDTO> actualReviewsByPage = reviewService.getReviewsByPage(VALID_PAGE);
        assertThat(actualReviewsByPage).isNotNull();
        assertThat(actualReviewsByPage.getEntities().get(0).getId()).isEqualTo(VALID_REVIEW_ID);
        assertThat(actualReviewsByPage.getEntities().get(0).getText()).isEqualTo(VALID_REVIEW_TEXT);
        assertThat(actualReviewsByPage.getEntities().get(0).getAuthor()).isEqualTo(VALID_REVIEW_AUTHOR);
        assertThat(actualReviewsByPage.getEntities().get(0).getDateCreate())
                .isEqualTo(VALID_REVIEW_DATE_CREATE);
        assertThat(actualReviewsByPage.getEntities().get(0).getIsActive())
                .isEqualTo(VALID_REVIEW_IS_ACTIVE);
    }

    @Test
    void getReviewsWithPagination_callLogic() {
        when(reviewRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        List<Review> returnedReviews = getValidReviews();
        when(reviewRepository.getReviewsByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE))
                .thenReturn(returnedReviews);
        PaginationWithEntitiesDTO<ReviewDTO> actualReviewsByPage = reviewService.getReviewsByPage(VALID_PAGE);
        verify(reviewRepository, Mockito.times(1)).getCountOfEntities();
        verify(reviewRepository, times(1))
                .getReviewsByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE);
    }

    @Test
    void getReviewsWithPagination_returnEmptyCollection() {
        when(reviewRepository.getCountOfEntities()).thenReturn(0L);
        PaginationWithEntitiesDTO<ReviewDTO> actualReviewsByPage = reviewService.getReviewsByPage(VALID_PAGE);
        assertThat(actualReviewsByPage.getEntities()).isEmpty();
    }

    /* deleted reviews */
    @Test
    void deleteReviews_returnListOfDeletedReviews() {
        Review returnedReview = getValidReview();
        when(reviewRepository.getObjectByID(anyLong())).thenReturn(returnedReview);
        List<Long> ids = getValidIds(1);
        List<ReviewDTO> actualReviewsDTO = reviewService.deletedReviews(ids);
        assertThat(actualReviewsDTO).isNotEmpty();
        assertThat(actualReviewsDTO.get(0).getId()).isEqualTo(VALID_REVIEW_ID);
        assertThat(actualReviewsDTO.get(0).getAuthor()).isEqualTo(VALID_REVIEW_AUTHOR);
        assertThat(actualReviewsDTO.get(0).getText()).isEqualTo(VALID_REVIEW_TEXT);
        assertThat(actualReviewsDTO.get(0).getDateCreate()).isEqualTo(VALID_REVIEW_DATE_CREATE);
        assertThat(actualReviewsDTO.get(0).getIsActive()).isEqualTo(VALID_REVIEW_IS_ACTIVE);
    }

    @Test
    void deleteReviews_callLogicOneTime() {
        Review returnedReview = getValidReview();
        when(reviewRepository.getObjectByID(anyLong())).thenReturn(returnedReview);
        List<Long> ids = getValidIds(1);
        reviewService.deletedReviews(ids);
        verify(reviewRepository, times(1)).getObjectByID(anyLong());
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    void deleteReviews_callLogicFourTime() {
        Review returnedReview = getValidReview();
        when(reviewRepository.getObjectByID(anyLong())).thenReturn(returnedReview);
        List<Long> ids = getValidIds(4);
        reviewService.deletedReviews(ids);
        verify(reviewRepository, times(4)).getObjectByID(anyLong());
        verify(reviewRepository, times(4)).delete(any(Review.class));
    }

    private List<Long> getValidIds(int length) {
        return LongStream.range(1, length + 1).boxed()
                .collect(Collectors.toList());
    }

    private List<Review> getValidReviews() {
        List<Review> reviews = new ArrayList<>();
        Review review = getValidReview();
        reviews.add(review);
        return reviews;
    }

    private Review getValidReview() {
        Review review = new Review();
        review.setId(VALID_REVIEW_ID);
        review.setText(VALID_REVIEW_TEXT);
        review.setDateCreate(VALID_REVIEW_DATE_CREATE);
        review.setIsActive(VALID_REVIEW_IS_ACTIVE);
        review.setAuthor(getValidUser());
        return review;
    }

    private User getValidUser() {
        User user = new User();
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(VALID_LAST_NAME);
        userDetails.setFirstName(VALID_FIRST_NAME);
        user.setUserDetails(userDetails);
        return user;
    }

}