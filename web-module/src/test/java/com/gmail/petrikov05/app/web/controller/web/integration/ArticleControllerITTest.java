package com.gmail.petrikov05.app.web.controller.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_COMMENT_DELETED;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE_PUBLICATION_STR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_NUMBER;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = {"CUSTOMER_USER", "SALE_USER"})
class ArticleControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getArticles_returnArticlesWithPagination() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ARTICLE_ID));
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
        assertThat(actualResult).contains(VALID_AUTHOR);
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
        assertThat(actualResult).contains("more");

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getArticle_returnArticleWithComments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
        assertThat(actualResult).contains(VALID_AUTHOR);
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_ID));
        assertThat(actualResult).contains(VALID_COMMENT_AUTHOR);
        assertThat(actualResult).contains(VALID_COMMENT_TEXT);
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_DATE));
    }

    @Test
    @WithMockUser(username = "best@best.com", roles = "SALE_USER")
    public void addArticle_returnAddedArticle() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/articles")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
                .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
        assertThat(actualResult).contains(VALID_ARTICLE_AUTHOR);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteCommentInArticle_returnArticle() throws Exception {
        mockMvc.perform(
                post("/articles/" + VALID_ARTICLE_ID + "/comments/" + VALID_COMMENT_ID + "/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + VALID_ARTICLE_ID))
                .andExpect(flash().attribute("message", MESSAGE_COMMENT_DELETED));
    }

    @Test
    public void deleteArticleById_returnMessage() throws Exception {
        mockMvc.perform(get("/articles/1/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", MESSAGE_ARTICLE_DELETED));
    }

    @Test
    public void showUpdateArticlePage_returnArticleUpdatePage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ARTICLE_ID));
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
    }

    @Test
    public void updateArticle_returnUpdatedArticle() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/articles/" + VALID_ARTICLE_ID + "/update")
                        .param("title", "new title")
                        .param("text", "new text")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ARTICLE_ID));
        assertThat(actualResult).contains("new title");
        assertThat(actualResult).contains("new text");
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
    }

}