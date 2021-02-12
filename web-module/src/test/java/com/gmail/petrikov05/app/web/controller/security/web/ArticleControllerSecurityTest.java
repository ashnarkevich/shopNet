package com.gmail.petrikov05.app.web.controller.security.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class ArticleControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/articles" */
    @Test
    public void getArticlesLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/articles")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getArticlesLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getArticlesLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getArticlesLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getArticlesLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/articles/{id} */
    @Test
    public void getArticleLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/articles/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getArticleLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getArticleLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getArticleLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getArticleLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/articles/1/delete" */
    @Test
    public void deleteArticleByIdLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/articles/1/delete")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteArticleLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteArticleLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles/1/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles?page=" + VALID_PAGE));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteArticleLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void deleteArticleLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/articles/add" */
    @Test
    public void showAddArticlePageLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/articles/add")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void showAddArticlePageLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void showAddArticlePageLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles/add")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void showAddArticlePageLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void showAddArticlePageLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/articles" */
    @Test
    public void addArticleLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/articles")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void addArticleLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(username = "best@best.com", roles = "SALE_USER")
    public void addArticleLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/articles")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addArticleLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void addArticleLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/articles/{articleId}/comments/{commentId}/delete" */
    @Test
    public void deleteCommentLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/articles/1/comments/1/delete")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteCommentLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles/1/comments/1/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteCommentLikeSaleUser_returnStatusRedirectArticle() throws Exception {
        mockMvc.perform(post("/articles/1/comments/1/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/1"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteCommentLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles/1/comments/1/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void deleteCommentLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles/1/comments/1/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/articles/{articleId}/update" */
    @Test
    public void showUpdateArticlePageLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/articles/1/update")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void showUpdateArticlePageLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void showUpdateArticlePageLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles/1/update")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void showUpdateArticlePageLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void showUpdateArticlePageLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/articles/1/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/articles/{articleId}/update" */
    @Test
    public void updateArticleLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/articles/1/update")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void updateArticleLikeCustomerUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles/1/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void updateArticleLikeSaleUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/articles/1/update")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void updateArticleLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles/1/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void updateArticleLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/articles/1/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

}