package com.gmail.petrikov05.app.web.controller.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_REVIEW_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "ADMINISTRATOR")
class ReviewControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showReviewsPage_returnReviewsWithPagination() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/reviews")
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attributeExists("pages"))
                .andExpect(model().attributeExists("page"))
                .andReturn();
        String actualContent = mvcResult.getResponse().getContentAsString();
        assertThat(actualContent).contains(VALID_REVIEW_TEXT);
        assertThat(actualContent).contains(VALID_AUTHOR);
        assertThat(actualContent).contains("<span>show</span>");
        assertThat(actualContent).contains("<td>2020-05-03T00:00</td>");
    }

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
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void addReview_redirectIndexWithMessage() throws Exception {
        mockMvc.perform(
                post("/reviews/add")
                        .param("text", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "TestLastName TestFirstName Your review was send."));
    }

}