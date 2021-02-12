package com.gmail.petrikov05.app.web.controller.api.integration;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;
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
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE_PUBLICATION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-integration.properties")
@AutoConfigureMockMvc
@WithMockUser(roles = "SECURE_API_USER")
public class ArticleAPIControllerITTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getArticles_returnArticlePreviewDTOS() throws Exception {
        List<ArticlePreviewDTO> returnedArticles = getValidArticlesPreview();
        String expectedResult = objectMapper.writeValueAsString(returnedArticles);
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getArticleById_returnArticleWithCommentsDTO() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(getValidArticleDTO());
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @WithMockUser(username = "best@best.com", roles = "SECURE_API_USER")
    void addArticle_returnAddedArticle() throws Exception {
        AddArticleDTO addArticle = getAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(containsString(VALID_ARTICLE_TITLE)))
                .andExpect(content().string(containsString(VALID_ARTICLE_TEXT)))
                .andExpect(content().string(containsString(String.valueOf(VALID_ARTICLE_DATE_PUBLICATION))))
                .andExpect(content().string(containsString(VALID_ARTICLE_AUTHOR)))
                .andExpect(content().string(containsString("2")));
    }

    @Test
    void deleteArticleById_returnMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                delete("/api/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(MESSAGE_ARTICLE_DELETED);
    }

    private AddArticleDTO getAddArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
        article.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        return article;
    }

    private ArticleDTO getValidArticleDTO() {
        ArticleDTO article = new ArticleDTO();
        article.setId(VALID_ARTICLE_ID);
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setDateCreate(VALID_ARTICLE_DATE);
        article.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        article.setAuthor(VALID_AUTHOR);
        article.setText(VALID_ARTICLE_TEXT);
        article.setComments(getValidCommentDTOS());
        return article;
    }

    private List<CommentDTO> getValidCommentDTOS() {
        List<CommentDTO> comments = new ArrayList<>();
        comments.add(getValidCommentDTO());
        return comments;
    }

    private CommentDTO getValidCommentDTO() {
        CommentDTO comment = new CommentDTO();
        comment.setCommentId(VALID_COMMENT_ID);
        comment.setDate(VALID_COMMENT_DATE);
        comment.setAuthor(VALID_COMMENT_AUTHOR);
        comment.setText(VALID_COMMENT_TEXT);
        return comment;
    }

    private List<ArticlePreviewDTO> getValidArticlesPreview() {
        List<ArticlePreviewDTO> articles = new ArrayList<>();
        articles.add(getValidArticlePreviewDTO());
        return articles;
    }

    private ArticlePreviewDTO getValidArticlePreviewDTO() {
        ArticlePreviewDTO articlePreviewDTO = new ArticlePreviewDTO();
        articlePreviewDTO.setId(VALID_ARTICLE_ID);
        articlePreviewDTO.setTitle(VALID_ARTICLE_TITLE);
        articlePreviewDTO.setDateCreate(VALID_ARTICLE_DATE);
        articlePreviewDTO.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        articlePreviewDTO.setAuthor(VALID_AUTHOR);
        articlePreviewDTO.setText(VALID_ARTICLE_TEXT);
        return articlePreviewDTO;
    }

}
