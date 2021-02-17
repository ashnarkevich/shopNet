package com.gmail.petrikov05.app.web.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE_PUBLICATION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class ArticleAPIControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    /* get "/api/articles" */
    @Test
    public void getArticlesUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/articles")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getArticlesAuthorizedUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getArticlesLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/articles")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getArticlesLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/articles")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getArticlesLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/articles")
        ).andExpect(status().isForbidden());
    }

    /* get "/api/articles/{id}" */
    @Test
    public void getArticleLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/articles/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getArticleLikeApiUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/articles/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getArticleLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/articles/1")
        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getArticleLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/articles/1")
        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getArticleLikeXUSTOMERUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(get("/api/articles/1")
        ).andExpect(status().isForbidden());
    }


    /* post "/api/articles" */
    @Test
    public void addArticleUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/api/articles")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "SECURE_API_USER")
    public void addArticleAuthorizedUser_returnStatusCreate() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addArticleLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/articles")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addArticleLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/articles")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void addArticleLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/articles")
        ).andExpect(status().isForbidden());
    }

    /* delete "/api/articles" */
    @Test
    public void deleteArticleByIdUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/articles/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void deleteArticleByIdAuthorizedUser_returnStatusOk() throws Exception {
        mockMvc.perform(delete("/api/articles/1")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteArticleLikeAdministratorUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(delete("/api/articles/1")
        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteArticleLikeSaleUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(delete("/api/articles/1")
        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteArticleLikeCustomerUser_returnStatusForbidden() throws Exception {
        mockMvc.perform(delete("/api/articles/1")
        ).andExpect(status().isForbidden());
    }

    private AddArticleDTO getValidAddArticleDTO() {
        AddArticleDTO addArticle = new AddArticleDTO();
        addArticle.setTitle(VALID_ARTICLE_TITLE);
        addArticle.setText(VALID_ARTICLE_TEXT);
        addArticle.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        return addArticle;
    }

}