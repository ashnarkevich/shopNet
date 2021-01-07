package com.gmail.petrikov05.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "SECURE_API_USER")
public class ArticleAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ArticleService articleService;

    @Test
    void getArticles_returnStatusOk() throws Exception {
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk());
    }

    @Test
    void getArticles_callBusinessLogin() throws Exception {
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk());
        verify(articleService, times(1)).getAllArticles();
    }

    @Test
    void getArticles_returnValidListArticles() throws Exception {
        List<ArticleDTO> returnedArticles = getValidArticleDTOS();
        when(articleService.getAllArticles()).thenReturn(returnedArticles);
        MvcResult mvcResult = mockMvc.perform(get("/api/articles"))
                .andReturn();
        String expectedResult = objectMapper.writeValueAsString(returnedArticles);
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    /* get article by id */
    @Test
    void getArticle_returnStatusOk() throws Exception {
        mockMvc.perform(
                get("/api/articles/2")
        ).andExpect(status().isOk());
    }

    @Test
    void getArticle_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/api/articles/a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getArticle_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/api/articles/2")
        ).andExpect(status().isOk());
        verify(articleService, times(1)).getArticleById(anyLong());
    }

    @Test
    void getArticle_returnValidArticle() throws Exception {
        ArticleWithCommentsDTO returnedArticle = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                get("/api/articles/2")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(returnedArticle);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void getNonExistentArticle_returnErrorMessage() throws Exception {
        when(articleService.getArticleById(anyLong())).thenReturn(null);
        mockMvc.perform(
                get("/api/articles/55")
        ).andExpect(jsonPath("$", is(MessageConstant.MESSAGE_ARTICLE_NOT_FOUND)).exists());

    }

    /* add article */
    @Test
    void addArticle_returnStatusCreated() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    void addArticleWithEmptyTitle_returnBadRequest() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        addArticle.setTitle("");
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addArticleWithSmallTitle_returnBadRequest() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        addArticle.setTitle(getStringWithLength(1));
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addArticleWithLongTitle_returnBadRequest() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        addArticle.setTitle(getStringWithLength(51));
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addArticleWithEmptyText_returnBadRequest() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        addArticle.setText("");
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addArticleWithSmallText_returnBadRequest() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        addArticle.setText(getStringWithLength(1));
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addArticleWithLongText_returnBadRequest() throws Exception {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        addArticle.setText(getStringWithLength(1001));
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addArticle_callBusinessLogic() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated());
        verify(articleService, times(1)).addArticle(any());
    }

    @Test
    void addArticle_returnAddedArticle() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String expectedResult = objectMapper.writeValueAsString(returnedArticle);
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void addArticleWithAnonymousUser_returnForbidden() throws Exception, AnonymousUserException {
        AddArticleDTO addArticle = getValidAddArticleDTO();
        String content = objectMapper.writeValueAsString(addArticle);
        when(articleService.addArticle(any())).thenThrow(new AnonymousUserException());
        mockMvc.perform(
                post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isForbidden());
    }

    /* delete article by id */
    @Test
    void deleteArticleById_returnStatusOk() throws Exception {
        mockMvc.perform(
                delete("/api/articles/1")
        ).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));
    }

    @Test
    void deleteArticle_returnBadRequest() throws Exception {
        mockMvc.perform(
                delete("/api/articles/a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deleteArticleById_callBusinessLogic() throws Exception {
        mockMvc.perform(
                delete("/api/articles/1")
        ).andExpect(status().isOk());
        verify(articleService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteArticleByValidId_returnMessage() throws Exception {
        when(articleService.deleteById(anyLong())).thenReturn(true);
        MvcResult mvcResult = mockMvc.perform(
                delete("/api/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(MESSAGE_ARTICLE_DELETED);
    }

    @Test
    void deleteArticleByInvalidId_returnMessage() throws Exception {
        when(articleService.deleteById(anyLong())).thenReturn(false);
        MvcResult mvcResult = mockMvc.perform(
                delete("/api/articles/1")
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(MESSAGE_ARTICLE_DELETED_FAIL);
    }

    private String getStringWithLength(int length) {
        return IntStream.range(0, length)
                .mapToObj(x -> "a")
                .collect(Collectors.joining());
    }

    private AddArticleDTO getValidAddArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
        return article;
    }

    private ArticleWithCommentsDTO getValidArticleWithCommentsDTO() {
        ArticleWithCommentsDTO articleWithComments = new ArticleWithCommentsDTO();
        articleWithComments.setId(VALID_ARTICLE_ID);
        articleWithComments.setTitle(VALID_ARTICLE_TITLE);
        articleWithComments.setDate(VALID_ARTICLE_DATE);
        articleWithComments.setAuthor(VALID_AUTHOR);
        articleWithComments.setText(VALID_ARTICLE_TEXT);
        articleWithComments.setComments(getValidComments());
        return articleWithComments;
    }

    private List<CommentDTO> getValidComments() {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        commentDTOS.add(getValidCommentDTO());
        return commentDTOS;
    }

    private CommentDTO getValidCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(VALID_COMMENT_ID);
        commentDTO.setAuthor(VALID_COMMENT_AUTHOR);
        commentDTO.setDate(VALID_COMMENT_DATE);
        commentDTO.setText(VALID_COMMENT_TEXT);
        return commentDTO;
    }

    private List<ArticleDTO> getValidArticleDTOS() {
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        articleDTOS.add(getValidArticleDTO());
        return articleDTOS;
    }

    private ArticleDTO getValidArticleDTO() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(VALID_ID);
        articleDTO.setTitle(VALID_ARTICLE_TITLE);
        articleDTO.setDate(VALID_DATE);
        articleDTO.setAuthor(VALID_AUTHOR);
        articleDTO.setText(VALID_ARTICLE_TEXT);
        return articleDTO;
    }

}
