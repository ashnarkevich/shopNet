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

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "CUSTOMER_USER")
class ArticleControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getArticles_returnArticlesWithPagination() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains("1");
        assertThat(actualResult).contains("article title");
        assertThat(actualResult).contains(String.valueOf(VALID_ARTICLE_DATE));
        assertThat(actualResult).contains(VALID_AUTHOR);
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
        assertThat(actualResult).contains("more");

    }

    @Test
    public void getArticle_returnArticleWithComments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
        assertThat(actualResult).contains(String.valueOf(VALID_ARTICLE_DATE));
        assertThat(actualResult).contains(VALID_AUTHOR);
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_ID));
        assertThat(actualResult).contains(VALID_COMMENT_AUTHOR);
        assertThat(actualResult).contains(VALID_COMMENT_TEXT);
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_DATE));
    }

}