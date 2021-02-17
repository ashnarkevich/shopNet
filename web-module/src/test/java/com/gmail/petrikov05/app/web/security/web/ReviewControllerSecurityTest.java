package com.gmail.petrikov05.app.web.security.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_REVIEW_TEXT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class ReviewControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/reviews" */
    @Test
    public void getReviewsLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/reviews")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void getReviewsLikeAdministratorUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/reviews")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getReviewsLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(get("/reviews")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getReviewsLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(get("/reviews")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getReviewsLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/reviews")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/reviews/delete*/
    @Test
    public void deleteReviewLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/reviews/delete")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteReviewLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/reviews/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews?page=" + VALID_PAGE));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteReviewLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(post("/reviews/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteReviewLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(post("/reviews/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void deleteReviewLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/reviews/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/reviews/add*/
    @Test
    public void showAddReviewPageLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/reviews/add")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void showAddReviewPageLikeAdministratorUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/reviews/add")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void showAddReviewPageLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(get("/reviews/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void showAddReviewPageLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(get("/reviews/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void showAddReviewPageLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/reviews/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }


    /* post "/reviews/add*/
    @Test
    public void addReviewLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/reviews/add")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "best@best.com", roles = "CUSTOMER_USER")
    public void addReviewLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/reviews/add")
                .param("text", VALID_REVIEW_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addReviewLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(post("/reviews/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addReviewLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(post("/reviews/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void addReviewLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/reviews/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

}