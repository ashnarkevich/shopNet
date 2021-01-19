package com.gmail.petrikov05.app.web.controller.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.ReviewService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.AddReviewDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.controller.ReviewController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ACCESS_CLOSE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_IS_ACTIVE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_REVIEW_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ReviewController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "ADMINISTRATOR")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewsService;

    /* shop reviews page*/
    @Test
    public void showReviewsPage_returnReviewsPage() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        mockMvc.perform(
                get("/reviews")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(view().name("reviews"));
    }

    @Test
    public void showReviewsWithInvalidPage_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/reviews")
                        .param("page", "a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void showReviews_callBusinessLogic() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        mockMvc.perform(
                get("/reviews")
        ).andExpect(status().isOk());
        verify(reviewsService, Mockito.times(1)).getReviewsByPage(anyInt());
    }

    @Test
    public void showReviewsWithPage_returnReviewsPageWithValidPage() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        mockMvc.perform(
                get("/reviews")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(model().attribute("page", VALID_PAGE));
    }

    @Test
    public void showReviews_returnReviewsWithValidPages() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        mockMvc.perform(
                get("/reviews")
        ).andExpect(status().isOk())
                .andExpect(model().attribute("pages", VALID_PAGES));
    }

    @Test
    public void showReviews_returnReviewsWithValidIdReview() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        MvcResult mvcResult = mockMvc.perform(
                get("/reviews")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult)
                .contains(String.valueOf(returnedReviews.getEntities().get(0).getId()));
    }

    @Test
    public void showReviews_returnReviewsWithValidAuthorName() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        MvcResult mvcResult = mockMvc.perform(
                get("/reviews")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult)
                .contains(String.valueOf(returnedReviews.getEntities().get(0).getAuthor()));
    }

    @Test
    public void showReviews_returnReviewsWithValidText() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        MvcResult mvcResult = mockMvc.perform(
                get("/reviews")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult)
                .contains(String.valueOf(returnedReviews.getEntities().get(0).getText()));
    }

    @Test
    public void showReviews_returnReviewsWithValidDateCteate() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        MvcResult mvcResult = mockMvc.perform(
                get("/reviews")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult)
                .contains(String.valueOf(returnedReviews.getEntities().get(0).getDateCreate()));
    }

    @Test
    public void showReviews_returnReviewsWithValidActive() throws Exception {
        PaginationWithEntitiesDTO<ReviewDTO> returnedReviews = getPaginationWithEntitiesDTO();
        when(reviewsService.getReviewsByPage(anyInt())).thenReturn(returnedReviews);
        MvcResult mvcResult = mockMvc.perform(
                get("/reviews")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult)
                .contains("<span>show</span>");
    }

    /* delete reviews */
    @Test
    public void deleteReviews_redirectReviewsWithPage() throws Exception {
        mockMvc.perform(
                post("/reviews/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews?page=" + VALID_PAGE));
    }

    @Test
    public void deletedReviews_returnBadRequest() throws Exception {
        mockMvc.perform(
                post("/reviews/delete")
                        .param("page", "a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deletedReviews_callBusinessLogic() throws Exception {
        mockMvc.perform(
                post("/reviews/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", "3")
                        .param("id", "5")
        ).andExpect(status().is3xxRedirection());
        verify(reviewsService, times(1)).deletedReviews(anyList());
    }

    @Test
    public void deletedReviews_redirectReviewsWithPositiveMessage() throws Exception {
        mockMvc.perform(
                post("/reviews/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", "2")
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "Reviews deleted"));
    }

    /* show add review page */
    @Test
    public void showAddReviewPage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/reviews/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"));
    }

    /* add review */
    @Test
    public void addReview_redirectIndex() throws Exception, AnonymousUserException {
        ReviewDTO returnedReview = getValidReviewDTO();
        when(reviewsService.addReview(any())).thenReturn(returnedReview);
        mockMvc.perform(post("/reviews/add")
                .param("text", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void addReviewWithInvalidText_returnStatusOkWithError() throws Exception {
        mockMvc.perform(post("/reviews/add")
        ).andExpect(status().isOk())
                .andExpect(view().name("review_add"));
    }

    @Test
    public void addReviewWithEmptyText_returnStatusOkWithError() throws Exception {
        mockMvc.perform(post("/reviews/add")
                .param("text", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("review_add"));
    }

    @Test
    public void addReviewWithLongText_returnStatusOkWithError() throws Exception {
        mockMvc.perform(post("/reviews/add")
                .param("text", getStringByLength(201))
        ).andExpect(status().isOk())
                .andExpect(view().name("review_add"));
    }

    private String getStringByLength(int length) {
        return IntStream.range(0, length)
                .boxed()
                .map(x -> "a")
                .collect(Collectors.joining());
    }

    @Test
    public void addReview_callBusinessLogic() throws Exception, AnonymousUserException {
        ReviewDTO returnedReview = getValidReviewDTO();
        when(reviewsService.addReview(any())).thenReturn(returnedReview);
        mockMvc.perform(post("/reviews/add")
                .param("text", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection());
        verify(reviewsService, times(1)).addReview(any(AddReviewDTO.class));
    }

    @Test
    public void addReview_redirectIndexWithMessage() throws Exception, AnonymousUserException {
        ReviewDTO returnedReview = getValidReviewDTO();
        when(reviewsService.addReview(any())).thenReturn(returnedReview);
        mockMvc.perform(post("/reviews/add")
                .param("text", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "TestLastName TestFirstName Your review was send."));
    }

    @Test
    public void addReviewWIthAnonymousUser_returnAnonymousUserException() throws Exception, AnonymousUserException {
        when(reviewsService.addReview(any())).thenThrow(new AnonymousUserException());
        mockMvc.perform(post("/reviews/add")
                .param("text", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", MESSAGE_ACCESS_CLOSE));

    }

    private PaginationWithEntitiesDTO<ReviewDTO> getPaginationWithEntitiesDTO() {
        List<ReviewDTO> reviewDTOS = getValidReviewDTOS();
        return new PaginationWithEntitiesDTO<>(reviewDTOS, VALID_PAGES);
    }

    private List<ReviewDTO> getValidReviewDTOS() {
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        ReviewDTO reviewDTO = getValidReviewDTO();
        reviewDTOS.add(reviewDTO);
        return reviewDTOS;
    }

    private ReviewDTO getValidReviewDTO() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(VALID_ID);
        reviewDTO.setAuthor(VALID_USER_LAST_NAME + " " + VALID_USER_FIRST_NAME);
        reviewDTO.setText(VALID_REVIEW_TEXT);
        reviewDTO.setDateCreate(VALID_DATE);
        reviewDTO.setIsActive(VALID_IS_ACTIVE);
        return reviewDTO;
    }

}