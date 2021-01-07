package com.gmail.petrikov05.app.web.controller.web;

import java.util.ArrayList;
import java.util.List;

import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import com.gmail.petrikov05.app.service.model.comment.CommentDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import com.gmail.petrikov05.app.web.controller.ArticleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_COMMENT_TEXT;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_DATE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_AUTHOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ArticleController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "CUSTOMER_USER")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleService articleService;

    /* get articles by page */
    @Test
    public void getArticles_returnIsOk() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(model().attribute("page", VALID_PAGE));
    }

    @Test
    public void getArticlesWithoutPage_returnIsOk() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(model().attribute("page", 1));
    }

    @Test
    public void getArticles_callBusinessLogic() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk());
        verify(articleService, times(1)).getArticlesByPage(anyInt());
    }

    @Test
    public void getArticles_returnArticlesWithValidPages() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(model().attribute("pages", VALID_PAGES));
    }

    @Test
    public void getArticles_returnValidArticles() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    public void getArticles_returnArticlesWithValidId() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getArticles_returnArticlesWithValidTitle() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
    }

    @Test
    public void getArticles_returnArticlesWithValidDate() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_DATE));
    }

    @Test
    public void getArticles_returnArticlesWithValidAuthor() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_AUTHOR);
    }

    @Test
    public void getArticles_returnArticlesWithValidText() throws Exception {
        PaginationWithEntitiesDTO<ArticleDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
    }

    /* get article */
    @Test
    public void getArticle_returnStatusIsOk() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk());
    }

    @Test
    public void getArticle_returnStatusBadRequest() throws Exception {
        mockMvc.perform(
                get("/articles/a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getArticle_callBusinessLogic() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk());
        verify(articleService, times(1)).getArticleById(anyLong());
    }

    @Test
    public void getArticle_returnValidArticle() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("article"));
    }

    @Test
    public void getArticle_returnArticleWithValidId() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getArticle_returnArticleWithValidTitle() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
    }

    @Test
    public void getArticle_returnArticleWithValidAuthor() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_AUTHOR));
    }

    @Test
    public void getArticle_returnArticleWithValidDate() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_DATE));
    }

    @Test
    public void getArticle_returnCommentWithValidId() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_ID));
    }

    @Test
    public void getArticle_returnCommentWithValidAuthor() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_COMMENT_AUTHOR);
    }

    @Test
    public void getArticle_returnCommentWithValidDate() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_DATE));
    }

    @Test
    public void getArticle_returnCommentWithValidText() throws Exception {
        ArticleWithCommentsDTO returnedArticleWithCommentsDTO = getValidArticleWithCommentsDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleWithCommentsDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_COMMENT_TEXT);
    }

    @Test
    public void getNonExistentArticle_returnArticlePageWithError() throws Exception {
        when(articleService.getArticleById(any())).thenReturn(null);
        mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk())
                .andExpect(view().name("article"))
                .andExpect(model().attribute("error", MESSAGE_ARTICLE_NOT_FOUND));
    }

    private ArticleWithCommentsDTO getValidArticleWithCommentsDTO() {
        ArticleWithCommentsDTO articleWithCommentsDTO = new ArticleWithCommentsDTO();
        articleWithCommentsDTO.setId(VALID_ID);
        articleWithCommentsDTO.setTitle(VALID_ARTICLE_TITLE);
        articleWithCommentsDTO.setDate(VALID_DATE);
        articleWithCommentsDTO.setAuthor(VALID_AUTHOR);
        articleWithCommentsDTO.setText(VALID_ARTICLE_TEXT);
        articleWithCommentsDTO.setComments(getValidComments());
        return articleWithCommentsDTO;
    }

    private List<CommentDTO> getValidComments() {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        CommentDTO commentDTO = getValidCommentDTO();
        commentDTOS.add(commentDTO);
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

    private PaginationWithEntitiesDTO<ArticleDTO> getValidArticlesWithPagination() {
        PaginationWithEntitiesDTO<ArticleDTO> articlesWithPagination = new PaginationWithEntitiesDTO<>();
        articlesWithPagination.setPages(VALID_PAGES);
        articlesWithPagination.setEntities(getValidArticles());
        return articlesWithPagination;
    }

    private List<ArticleDTO> getValidArticles() {
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        ArticleDTO articleDTO = getValidArticleDTO();
        articleDTOS.add(articleDTO);
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