package com.gmail.petrikov05.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.gmail.petrikov05.app.repository.ReviewRepository;
import com.gmail.petrikov05.app.repository.model.Review;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.service.constant.MessageConstant;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.impl.ReviewServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.AddReviewDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ACCESS_CLOSE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_OBJECT_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_DATE_CREATE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_IS_ACTIVE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_REVIEW_TEXT;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_START_POSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        this.reviewService = new ReviewServiceImpl(reviewRepository, userService);
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
        assertThat(actualReviewsByPage.getEntities().get(0).getId()).isEqualTo(VALID_ID);
        assertThat(actualReviewsByPage.getEntities().get(0).getText()).isEqualTo(VALID_REVIEW_TEXT);
        assertThat(actualReviewsByPage.getEntities().get(0).getAuthor()).isEqualTo(VALID_AUTHOR);
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
        assertThat(actualReviewsDTO.get(0).getId()).isEqualTo(VALID_ID);
        assertThat(actualReviewsDTO.get(0).getAuthor()).isEqualTo(VALID_AUTHOR);
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

    /* add review */
    @Test
    void addReview_returnAddedReview() throws AnonymousUserException {
        AddReviewDTO addReviewDTO = getValidAddReviewDTO();
        User returnedUser = getValidUser();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        Review returnedReview = getValidReview();
        when(reviewRepository.add(any())).thenReturn(returnedReview);
        ReviewDTO actualResult = reviewService.addReview(addReviewDTO);
        verify(userService, times(1)).getCurrentUser();
        verify(reviewRepository, times(1)).add(any());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(VALID_ID);
        assertThat(actualResult.getAuthor()).isEqualTo(VALID_AUTHOR);
        assertThat(actualResult.getDateCreate()).isEqualTo(VALID_REVIEW_DATE_CREATE);
        assertThat(actualResult.getText()).isEqualTo(VALID_REVIEW_TEXT);
    }

    @Test
    void addReviewWithAnonymousUser_returnAnonymousUserException() throws AnonymousUserException {
        AddReviewDTO addReviewDTO = getValidAddReviewDTO();
        when(userService.getCurrentUser()).thenThrow(new AnonymousUserException());
        assertThatExceptionOfType(AnonymousUserException.class)
                .isThrownBy(() -> reviewService.addReview(addReviewDTO));
        assertThrows(
                AnonymousUserException.class,
                () -> reviewService.addReview(addReviewDTO),
                MESSAGE_ACCESS_CLOSE
        );
    }

    private AddReviewDTO getValidAddReviewDTO() {
        AddReviewDTO review = new AddReviewDTO();
        review.setText(VALID_REVIEW_TEXT);
        return review;
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
        review.setId(VALID_ID);
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