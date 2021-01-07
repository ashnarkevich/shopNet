package com.gmail.petrikov05.app.web.controller.api.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void getArticles_returnArticleDTOS() throws Exception {
        List<ArticleDTO> returnedArticles = getValidArticles();
        String expectedResult = objectMapper.writeValueAsString(returnedArticles);
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void getArticleById_returnArticleWithCommentsDTO() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(getArticleWithCommentDTOS());
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @WithMockUser(username = VALID_EMAIL, roles = "SECURE_API_USER")
    void addArticle_returnAddedArticle() throws Exception {
        AddArticleDTO addArticle = getAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("2L")).exists())
                .andExpect(jsonPath("$.title", is(VALID_ARTICLE_TITLE)).exists())
                .andExpect(jsonPath("$.text", is(VALID_ARTICLE_TEXT)).exists())
                .andExpect(jsonPath("$.author", is(VALID_AUTHOR)).exists());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteArticleById_returnMessage() throws Exception {
        mockMvc.perform(
                delete("/api/articles/2")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", is(MESSAGE_ARTICLE_DELETED)).exists());
    }

    private AddArticleDTO getAddArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
        return article;
    }

    private ArticleWithCommentsDTO getArticleWithCommentDTOS() {
        ArticleWithCommentsDTO article = new ArticleWithCommentsDTO();
        article.setId(VALID_ARTICLE_ID);
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setDate(VALID_ARTICLE_DATE);
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
        comment.setId(VALID_COMMENT_ID);
        comment.setDate(VALID_COMMENT_DATE);
        comment.setAuthor(VALID_COMMENT_AUTHOR);
        comment.setText(VALID_COMMENT_TEXT);
        return comment;
    }

    private List<ArticleDTO> getValidArticles() {
        List<ArticleDTO> articles = new ArrayList<>();
        articles.add(getValidArticle());
        return articles;
    }

    private ArticleDTO getValidArticle() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(VALID_ARTICLE_ID);
        articleDTO.setTitle(VALID_ARTICLE_TITLE);
        articleDTO.setDate(VALID_ARTICLE_DATE);
        articleDTO.setAuthor(VALID_AUTHOR);
        articleDTO.setText(VALID_ARTICLE_TEXT);
        return articleDTO;
    }

}
