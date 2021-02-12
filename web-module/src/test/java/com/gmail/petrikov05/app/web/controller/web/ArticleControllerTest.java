package com.gmail.petrikov05.app.web.controller.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.CommentService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.article.UpdateArticleDTO;
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

import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_SIZE_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_SIZE_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.validation.ArticleValidationMessage.MESSAGE_TITLE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_COMMENT_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_COMMENT_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE_PUBLICATION;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ARTICLE_DATE_PUBLICATION_STR;
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
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ArticleController.class)
@Import(TestConfig.class)
@WithMockUser(roles = {"CUSTOMER_USER", "SALE_USER"})
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private CommentService commentService;

    /* get articles by page */
    @Test
    public void getArticles_returnIsOk() throws Exception {
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
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
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(model().attribute("page", 1));
    }

    @Test
    public void getArticles_callBusinessLogic() throws Exception {
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk());
        verify(articleService, times(1)).getArticlesByPage(anyInt());
    }

    @Test
    public void getArticles_returnArticlesWithValidPages() throws Exception {
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(model().attribute("pages", VALID_PAGES));
    }

    @Test
    public void getArticles_returnValidArticles() throws Exception {
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
        when(articleService.getArticlesByPage(anyInt())).thenReturn(returnedArticlesByPage);
        mockMvc.perform(
                get("/articles")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    public void getArticles_returnArticlesWithValidId() throws Exception {
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
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
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
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
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
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
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
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
        PaginationWithEntitiesDTO<ArticlePreviewDTO> returnedArticlesByPage = getValidArticlesWithPagination();
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
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
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
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk());
        verify(articleService, times(1)).getArticleById(anyLong());
    }

    @Test
    public void getArticle_returnValidArticle() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        mockMvc.perform(
                get("/articles/1")
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("article"));
    }

    @Test
    public void getArticle_returnArticleWithValidId() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getArticle_returnArticleWithValidTitle() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
    }

    @Test
    public void getArticle_returnArticleWithValidAuthor() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_AUTHOR));
    }

    @Test
    public void getArticle_returnArticleWithValidDate() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_DATE));
    }

    @Test
    public void getArticle_returnCommentWithValidId() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_ID));
    }

    @Test
    public void getArticle_returnCommentWithValidAuthor() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_COMMENT_AUTHOR);
    }

    @Test
    public void getArticle_returnCommentWithValidDate() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/articles/1")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_COMMENT_DATE));
    }

    @Test
    public void getArticle_returnCommentWithValidText() throws Exception {
        ArticleDTO returnedArticleDTO = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticleDTO);
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

    /* start show add_article page */
    @Test
    public void showAddArticlePage_returnStatusOk() throws Exception {
        mockMvc.perform(get("/articles/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_add"))
                .andExpect(model().attributeExists("article"));
    }
    /* finish show add_article page */

    /* start add article */
    @Test
    public void addArticle_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andExpect(status().isOk())
                .andExpect(view().name("article"));
    }

    @Test
    public void addInvalidArticle_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/articles")
        ).andExpect(status().isOk())
                .andExpect(view().name("article_add"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    public void addArticleWithEmptyTitle_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(MESSAGE_NOT_EMPTY)));
    }

    @Test
    public void addArticleWithSmallTitle_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", getValidStringByLength(1))
                        .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TITLE)));
    }

    @Test
    public void addArticleWithLongTitle_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", getValidStringByLength(51))
                        .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TITLE)));
    }

    @Test
    public void addArticleWithEmptyText_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", "")
        ).andExpect(content().string(containsString(MESSAGE_NOT_EMPTY)));
    }

    @Test
    public void addArticleWithSmallText_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", getValidStringByLength(1))
        ).andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TEXT)));
    }

    @Test
    public void addArticleWithLongText_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", getValidStringByLength(1001))
        ).andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TEXT)));
    }

    @Test
    public void addArticleWithInvalidDatePublication_returnOkWithErrorMessage() throws Exception {
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", " ")
        ).andExpect(content().string(containsString(MESSAGE_NOT_EMPTY)));
    }

    @Test
    public void addArticle_callBusinessLogic() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andExpect(status().isOk());
        verify(articleService, times(1)).addArticle(any());
    }

    @Test
    public void addValidArticle_returnAddedArticle() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andExpect(model().attributeExists("article"));
    }

    @Test
    public void addValidArticle_returnArticleWithValidId() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains("<li>1</li>");
    }

    @Test
    public void addValidArticle_returnArticleWithValidTitle() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
    }

    @Test
    public void addValidArticle_returnArticleWithValidDatePublication() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
    }

    @Test
    public void addValidArticle_returnArticleWithValidAuthor() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_AUTHOR);
    }

    @Test
    public void addValidArticle_returnArticleWithValidText() throws Exception, AnonymousUserException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.addArticle(any())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
    }

    @Test
    public void addArticleWithNonExistentUser_redirectIndexWithMessage() throws Exception, AnonymousUserException {
        when(articleService.addArticle(any())).thenThrow(new AnonymousUserException());
        mockMvc.perform(
                post("/articles")
                        .param("title", VALID_ARTICLE_TITLE)
                        .param("text", VALID_ARTICLE_TEXT)
                        .param("datePublication", VALID_ARTICLE_DATE_PUBLICATION_STR)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", MessageConstant.MESSAGE_USER_NOT_FOUND));
    }

    /* finish add article */

    /* start delete article */
    @Test
    public void deleteArticleById_redirectArticles() throws Exception {
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles?page=" + VALID_PAGE));
    }

    @Test
    public void deleteArticleById_returnBadRequest() throws Exception {
        mockMvc.perform(get("/articles/q/delete")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteArticle_callBusinessLogic() throws Exception, ObjectDBException {
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection());
        verify(articleService, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteArticleById_redirectWithSuccessfulMessage() throws Exception, ObjectDBException {
        when(articleService.deleteById(anyLong())).thenReturn(true);
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", MESSAGE_ARTICLE_DELETED));
    }

    @Test
    public void deleteInvalidArticleById_redirectWithErrorMessage() throws Exception, ObjectDBException {
        when(articleService.deleteById(anyLong())).thenReturn(false);
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", MESSAGE_ARTICLE_DELETED_FAIL));
    }

    @Test
    public void deleteNonExistentArticle_redirectWithErrorMessage() throws ObjectDBException, Exception {
        when((articleService.deleteById(anyLong()))).thenThrow(new ObjectDBException("Article not found"));
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/delete")
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Article not found"));
    }
    /* finish delete article */

    /* start delete an article comment*/
    @Test
    public void deleteArticleComment_redirectArticle() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/comments/" + VALID_COMMENT_ID + "/delete")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteArticleCommentWithInvalidArticleId_returnStatusBadRequest() throws Exception {
        mockMvc.perform(post("/articles/i/comments/" + VALID_COMMENT_ID + "/delete")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteArticleCommentWithInvalidCommentId_returnStatusBadRequest() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/comments/i/delete")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteArticleComment_calBusinessLogic() throws Exception {
        when(commentService.deleteById(anyLong())).thenReturn(true);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/comments/" + VALID_COMMENT_ID + "/delete")
        ).andExpect(status().is3xxRedirection());
        verify(commentService, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteValidArticleComment_redirectArticleWithMessage() throws Exception {
        when(commentService.deleteById(anyLong())).thenReturn(true);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/comments/" + VALID_COMMENT_ID + "/delete")
        ).andExpect(flash().attribute("message", MESSAGE_COMMENT_DELETED));
    }

    @Test
    public void deleteInvalidArticleComment_redirectArticleWithMessage() throws Exception {
        when(commentService.deleteById(anyLong())).thenReturn(false);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/comments/" + VALID_COMMENT_ID + "/delete")
        ).andExpect(flash().attribute("error", MESSAGE_COMMENT_DELETED_FAIL));
    }
    /* finish delete an article comment*/

    /* start show update article page */
    @Test
    public void showUpdateArticlePage_returnStatusOk() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andExpect(status().isOk());
    }

    @Test
    public void showUpdateArticlePage_returnStatusBadRequest() throws Exception {
        mockMvc.perform(get("/articles/i/update")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void showUpdateArticlePage_callBusinessLogic() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andExpect(status().isOk());
        verify(articleService, times(1)).getArticleById(anyLong());
    }

    @Test
    public void showUpdateArticlePage_returnArticle() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andExpect(model().attributeExists("article"));
    }

    @Test
    public void showUpdateArticlePage_returnArticleWithValidId() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ARTICLE_ID));
    }

    @Test
    public void showUpdateArticlePage_returnArticleWithValidTitle() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TITLE);
    }

    @Test
    public void showUpdateArticlePage_returnArticleWithValidText() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_TEXT);
    }

    @Test
    public void showUpdateArticlePage_returnArticleWithValidDatePublication() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(get("/articles/" + VALID_ARTICLE_ID + "/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_ARTICLE_DATE_PUBLICATION_STR);
    }
    /* finish show update article page */

    /* start update article*/
    @Test
    public void updateArticle_returnStatusOk() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(status().isOk())
                .andExpect(view().name("article"));
    }

    @Test
    public void updateArticle_returnStatusBadRequest() throws Exception {
        mockMvc.perform(post("/articles/q/update")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithEmptyTitle_returnArticleUpdatePage() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("article_update"))
                .andExpect(content().string(containsString(MESSAGE_TITLE_NOT_EMPTY)));
    }

    @Test
    public void updateArticleWithSmallTitle_returnArticleUpdatePage() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", getValidStringByLength(1))
        ).andExpect(status().isOk())
                .andExpect(view().name("article_update"))
                .andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TITLE)));
    }

    @Test
    public void updateArticleWithLongTitle_returnArticleUpdatePage() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", getValidStringByLength(51))
        ).andExpect(status().isOk())
                .andExpect(view().name("article_update"))
                .andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TITLE)));
    }

    @Test
    public void updateArticleWithEmptyText_returnArticleUpdatePage() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("article_update"))
                .andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TEXT)));
    }

    @Test
    public void updateArticleWithSmallText_returnArticleUpdatePage() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", getValidStringByLength(1))
        ).andExpect(status().isOk())
                .andExpect(view().name("article_update"))
                .andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TEXT)));
    }

    @Test
    public void updateArticleWithLongText_returnArticleUpdatePage() throws Exception {
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", getValidStringByLength(1001))
        ).andExpect(status().isOk())
                .andExpect(view().name("article_update"))
                .andExpect(content().string(containsString(MESSAGE_SIZE_ARTICLE_TEXT)));
    }

    @Test
    public void updateArticle_callBusinessLogic() throws Exception, ObjectDBException {
        UpdateArticleDTO updateArticle = new UpdateArticleDTO();
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.updateArticle(VALID_ARTICLE_ID, updateArticle)).thenReturn(returnedArticle);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(status().isOk());
        verify(articleService, times(1)).updateArticle(anyLong(), any());
    }

    @Test
    public void updateArticle_returnArticle() throws Exception, ObjectDBException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.updateArticle(any(), any())).thenReturn(returnedArticle);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(model().attributeExists("article"));
    }

    @Test
    public void updateArticle_returnArticleWithValidId() throws Exception, ObjectDBException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.updateArticle(any(), any())).thenReturn(returnedArticle);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(String.valueOf(VALID_ARTICLE_ID))));
    }

    @Test
    public void updateArticle_returnArticleWithValidTitle() throws Exception, ObjectDBException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.updateArticle(any(), any())).thenReturn(returnedArticle);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(VALID_ARTICLE_TITLE)));
    }

    @Test
    public void updateArticle_returnArticleWithValidText() throws Exception, ObjectDBException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.updateArticle(any(), any())).thenReturn(returnedArticle);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(VALID_ARTICLE_TEXT)));
    }

    @Test
    public void updateArticle_returnArticleWithValidDatePublication() throws Exception, ObjectDBException {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.updateArticle(any(), any())).thenReturn(returnedArticle);
        mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(content().string(containsString(VALID_ARTICLE_TEXT)));
    }

    @Test
    public void updateNonExistentArticle_returnWithError() throws ObjectDBException, Exception {
        when(articleService.updateArticle(any(), any())).thenThrow(new ObjectDBException("article not found"));
        mockMvc.perform(post("/articles/12/update")
                .param("title", VALID_ARTICLE_TITLE)
                .param("text", VALID_ARTICLE_TEXT)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles"))
                .andExpect(flash().attribute("error", "article not found"));
    }

    @Test
    public void updateInvalidArticle_returnError() throws Exception {
        ArticleDTO returnedArticle = getValidArticleDTO();
        when(articleService.getArticleById(anyLong())).thenReturn(returnedArticle);
        MvcResult mvcResult = mockMvc.perform(post("/articles/" + VALID_ARTICLE_ID + "/update")
                .param("title", "")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_TITLE_NOT_EMPTY);
    }

    /* finish post update article*/

    private UpdateArticleDTO getValidUpdateArticleDTO() {
        UpdateArticleDTO article = new UpdateArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
        article.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        return article;
    }

    private String getValidStringByLength(int length) {
        return IntStream.range(0, length)
                .mapToObj(x -> "a")
                .collect(Collectors.joining());
    }

    private ArticleDTO getValidArticleDTO() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(VALID_ID);
        articleDTO.setTitle(VALID_ARTICLE_TITLE);
        articleDTO.setDateCreate(VALID_DATE);
        articleDTO.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        articleDTO.setAuthor(VALID_AUTHOR);
        articleDTO.setText(VALID_ARTICLE_TEXT);
        articleDTO.setComments(getValidComments());
        return articleDTO;
    }

    private List<CommentDTO> getValidComments() {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        CommentDTO commentDTO = getValidCommentDTO();
        commentDTOS.add(commentDTO);
        return commentDTOS;
    }

    private CommentDTO getValidCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(VALID_COMMENT_ID);
        commentDTO.setAuthor(VALID_COMMENT_AUTHOR);
        commentDTO.setDate(VALID_COMMENT_DATE);
        commentDTO.setText(VALID_COMMENT_TEXT);
        return commentDTO;
    }

    private PaginationWithEntitiesDTO<ArticlePreviewDTO> getValidArticlesWithPagination() {
        List<ArticlePreviewDTO> articlePreviewDTOS = getValidArticles();
        return new PaginationWithEntitiesDTO<>(articlePreviewDTOS, VALID_PAGES);
    }

    private List<ArticlePreviewDTO> getValidArticles() {
        List<ArticlePreviewDTO> articlePreviewDTOS = new ArrayList<>();
        ArticlePreviewDTO articlePreviewDTO = getValidArticlePreviewDTO();
        articlePreviewDTOS.add(articlePreviewDTO);
        return articlePreviewDTOS;
    }

    private ArticlePreviewDTO getValidArticlePreviewDTO() {
        ArticlePreviewDTO articlePreviewDTO = new ArticlePreviewDTO();
        articlePreviewDTO.setId(VALID_ID);
        articlePreviewDTO.setTitle(VALID_ARTICLE_TITLE);
        articlePreviewDTO.setDateCreate(VALID_DATE);
        articlePreviewDTO.setAuthor(VALID_AUTHOR);
        articlePreviewDTO.setText(VALID_ARTICLE_TEXT);
        return articlePreviewDTO;
    }

}