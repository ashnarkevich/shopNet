package com.gmail.petrikov05.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.repository.ArticleRepository;
import com.gmail.petrikov05.app.repository.model.Article;
import com.gmail.petrikov05.app.repository.model.Comment;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.impl.ArticleServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.article.UpdateArticleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.petrikov05.app.service.constant.PageConstant.ARTICLE_SUMMARY_LENGTH;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ARTICLE_DATE_PUBLICATION;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ARTICLE_TEXT;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ARTICLE_TITLE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_AUTHOR;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_AUTHOR;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_DATE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COMMENT_TEXT;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_DATE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
class ArticleServiceTest {

    private ArticleService articlesService;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        this.articlesService = new ArticleServiceImpl(articleRepository, userService);
    }

    /* get articles by page */
    @Test
    public void getArticlesByPage_returnArticlesWithPages() {
        List<Article> returnedArticles = getValidArticles();
        when(articleRepository.getArticlesByPage(anyInt(), anyInt())).thenReturn(returnedArticles);
        when(articleRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        PaginationWithEntitiesDTO<ArticlePreviewDTO> actualArticlesByPage = articlesService.getArticlesByPage(VALID_PAGE);
        assertThat(actualArticlesByPage).isNotNull();
        assertThat(actualArticlesByPage.getEntities()).isNotNull();
        assertThat(actualArticlesByPage.getPages()).isEqualTo(VALID_PAGES);
        assertThat(actualArticlesByPage.getEntities().get(0).getId()).isEqualTo(VALID_ID);
        assertThat(actualArticlesByPage.getEntities().get(0).getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(actualArticlesByPage.getEntities().get(0).getDateCreate()).isEqualTo(VALID_DATE);
        assertThat(actualArticlesByPage.getEntities().get(0).getAuthor()).isEqualTo(VALID_AUTHOR);
        assertThat(actualArticlesByPage.getEntities().get(0).getText()).isEqualTo(VALID_ARTICLE_TEXT);
    }

    @Test
    public void getArticlesByPage_returnArticlesValidWithTextMaxLength() {
        List<Article> returnedArticles = getValidArticles();
        returnedArticles.get(0).setText(getValidTextWithLength(201));
        when(articleRepository.getArticlesByPage(anyInt(), anyInt())).thenReturn(returnedArticles);
        PaginationWithEntitiesDTO<ArticlePreviewDTO> actualArticleByPage = articlesService.getArticlesByPage(VALID_PAGE);
        assertThat(actualArticleByPage.getEntities().get(0).getText().length()).isEqualTo(ARTICLE_SUMMARY_LENGTH);
    }

    @Test
    public void getArticlesByPage_returnArticlesWithTextMaxLength() {
        List<Article> returnedArticles = getValidArticles();
        returnedArticles.get(0).setText(getValidTextWithLength(ARTICLE_SUMMARY_LENGTH));
        when(articleRepository.getArticlesByPage(anyInt(), anyInt())).thenReturn(returnedArticles);
        PaginationWithEntitiesDTO<ArticlePreviewDTO> actualArticleByPage = articlesService.getArticlesByPage(VALID_PAGE);
        assertThat(actualArticleByPage.getEntities().get(0).getText()).isEqualTo(returnedArticles.get(0).getText());
    }

    @Test
    public void getArticlesByPage_callLogic() {
        articlesService.getArticlesByPage(VALID_PAGE);
        verify(articleRepository, times(1)).getCountOfEntities();
        verify(articleRepository, times(1)).getArticlesByPage(anyInt(), anyInt());
    }

    /* get article by id */
    @Test
    public void getArticleById_returnValidArticleDTO() {
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        ArticleDTO actualArticleDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleDTO).isNotNull();
        assertThat(actualArticleDTO.getId()).isEqualTo(VALID_ID);
        assertThat(actualArticleDTO.getDateCreate()).isEqualTo(VALID_DATE);
        assertThat(actualArticleDTO.getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(actualArticleDTO.getText()).isEqualTo(VALID_ARTICLE_TEXT);
        assertThat(actualArticleDTO.getAuthor()).isEqualTo(VALID_AUTHOR);
    }

    @Test
    public void getArticleById_returnArticleDTOWithValidComment() {
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        ArticleDTO actualArticleDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleDTO.getComments()).isNotNull();
        assertThat(actualArticleDTO.getComments().get(0).getCommentId()).isEqualTo(VALID_COMMENT_ID);
        assertThat(actualArticleDTO.getComments().get(0).getDate()).isEqualTo(VALID_COMMENT_DATE);
        assertThat(actualArticleDTO.getComments().get(0).getText()).isEqualTo(VALID_COMMENT_TEXT);
        assertThat(actualArticleDTO.getComments().get(0).getAuthor()).isEqualTo(VALID_COMMENT_AUTHOR);
    }

    @Test
    public void getArticleById_returnArticleWithFullText() {
        Article returnedArticle = getValidArticle();
        int validTextLength = 500;
        returnedArticle.setText(getValidTextWithLength(validTextLength));
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        ArticleDTO actualArticleDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleDTO).isNotNull();
        assertThat(actualArticleDTO.getText().length()).isEqualTo(validTextLength);
    }

    @Test
    public void getArticleById_callLogic() {
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        articlesService.getArticleById(VALID_ID);
        verify(articleRepository, times(1)).getObjectByID(anyLong());
    }

    @Test
    public void getNonExistentArticleById_returnNull() {
        when(articleRepository.getObjectByID(anyLong())).thenReturn(null);
        ArticleDTO actualArticleDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleDTO).isNull();
    }

    /* get all articles */
    @Test
    public void getArticles_returnListArticleDTOS() {
        List<Article> returnedArticles = getValidArticles();
        when(articleRepository.getAllObjects()).thenReturn(returnedArticles);
        List<ArticlePreviewDTO> actualArticle = articlesService.getAllArticles();
        assertThat(actualArticle).isNotNull();
        assertThat(actualArticle.get(0).getId()).isEqualTo(VALID_ID);
        assertThat(actualArticle.get(0).getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(actualArticle.get(0).getDateCreate()).isEqualTo(VALID_DATE);
        assertThat(actualArticle.get(0).getDatePublication()).isEqualTo(VALID_ARTICLE_DATE_PUBLICATION);
        assertThat(actualArticle.get(0).getAuthor()).isEqualTo(VALID_AUTHOR);
        assertThat(actualArticle.get(0).getText()).isEqualTo(VALID_ARTICLE_TEXT);
    }

    @Test
    public void getArticles_callLogic() {
        articlesService.getAllArticles();
        verify(articleRepository, times(1)).getAllObjects();
    }

    /* add article */
    @Test
    public void addArticle_returnValidAddedArticles() throws AnonymousUserException {
        User returnedUser = getValidAuthor();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        AddArticleDTO addArticleDTO = getValidAddArticleDTO();
        ArticleDTO addedArticle = articlesService.addArticle(addArticleDTO);
        assertThat(addedArticle).isNotNull();
        assertThat(addedArticle.getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(addedArticle.getText()).isEqualTo(VALID_ARTICLE_TEXT);
        assertThat(addedArticle.getAuthor()).isEqualTo(VALID_AUTHOR);
        assertThat(addedArticle.getDateCreate().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
        assertThat(addedArticle.getDatePublication()).isEqualTo(VALID_ARTICLE_DATE_PUBLICATION);
    }

    @Test
    public void addArticle_callLogic() throws AnonymousUserException {
        User returnedUser = getValidAuthor();
        when(userService.getCurrentUser()).thenReturn(returnedUser);
        AddArticleDTO addArticleDTO = getValidAddArticleDTO();
        articlesService.addArticle(addArticleDTO);
        verify(articleRepository, times(1)).add(any());
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void addArticleWithAnonymousUser_returnAnonymousUserException() throws AnonymousUserException {
        when(userService.getCurrentUser()).thenThrow(new AnonymousUserException());
        AddArticleDTO addArticle = getValidAddArticleDTO();
        assertThatExceptionOfType(AnonymousUserException.class)
                .isThrownBy(() -> articlesService.addArticle(addArticle));
        assertThrows(
                AnonymousUserException.class,
                () -> articlesService.addArticle(addArticle)
        );

    }

    /* delete article by id */
    @Test
    public void deleteArticle_returnTrue() throws ObjectDBException {
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        when(articleRepository.delete(any())).thenReturn(true);
        boolean actualResult = articlesService.deleteById(VALID_ID);
        verify(articleRepository, times(1)).getObjectByID(anyLong());
        verify(articleRepository, times(1)).delete(any(Article.class));
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isTrue();
    }

    @Test
    public void deleteNonExistentArticle_returnObjectDBException() {
        when(articleRepository.getObjectByID(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ObjectDBException.class)
                .isThrownBy(() -> articlesService.deleteById(anyLong()));
        assertThrows(
                ObjectDBException.class,
                () -> articlesService.deleteById(anyLong())
        );
    }
    /* finish delete article */

    /* start update article */
    @Test
    public void updateArticle_returnUpdatedArticle() throws ObjectDBException {
        UpdateArticleDTO updateArticleDTO = getValidUpdateArticleDTO();
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(any())).thenReturn(returnedArticle);
        when(articleRepository.merge(any())).thenReturn(returnedArticle);
        ArticleDTO actualResult = articlesService.updateArticle(VALID_ID, updateArticleDTO);
        verify(articleRepository, times(1)).getObjectByID(anyLong());
        verify(articleRepository, times(1)).merge(any());
        assertThat(actualResult).isNotNull();
    }
    @Test
    public void updateArticle_returnValidUpdatedArticle() throws ObjectDBException {
        String newTitle = "new title";
        String newText = "new text";
        UpdateArticleDTO updateArticleDTO = getValidUpdateArticleDTO();
        updateArticleDTO.setTitle(newTitle);
        updateArticleDTO.setText(newText);
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(any())).thenReturn(returnedArticle);
        when(articleRepository.merge(any())).thenReturn(returnedArticle);
        ArticleDTO actualResult = articlesService.updateArticle(VALID_ID, updateArticleDTO);
        assertThat(actualResult.getId()).isEqualTo(VALID_ID);
        assertThat(actualResult.getTitle()).isEqualTo(newTitle);
        assertThat(actualResult.getText()).isEqualTo(newText);
        assertThat(actualResult.getDatePublication()).isEqualTo(VALID_ARTICLE_DATE_PUBLICATION);
        assertThat(actualResult.getAuthor()).isEqualTo(VALID_AUTHOR);
    }

    @Test
    public void updateNonExistentArticle_returnObjectDBException() {
        when(articleRepository.getObjectByID(VALID_ID)).thenReturn(null);
        UpdateArticleDTO updateArticleDTO = getValidUpdateArticleDTO();
        assertThatExceptionOfType(ObjectDBException.class)
                .isThrownBy(() -> articlesService.updateArticle(VALID_ID, any()));
        assertThrows(ObjectDBException.class,
                () -> articlesService.updateArticle(VALID_ID, any())
        );
    }
    /* finish update article */

    private UpdateArticleDTO getValidUpdateArticleDTO() {
        UpdateArticleDTO article = new UpdateArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
        article.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        return article;
    }

    private AddArticleDTO getValidAddArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
        article.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        return article;
    }

    private List<Article> getValidArticles() {
        return IntStream.of(1)
                .mapToObj(x -> getValidArticle())
                .collect(Collectors.toList());
    }

    private Article getValidArticle() {
        Article article = new Article();
        article.setId(VALID_ID);
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setDateCreate(VALID_DATE);
        article.setDatePublication(VALID_ARTICLE_DATE_PUBLICATION);
        article.setAuthor(getValidAuthor());
        article.setText(VALID_ARTICLE_TEXT);
        article.setComments(getValidComments());
        return article;
    }

    private List<Comment> getValidComments() {
        return IntStream.of(1)
                .mapToObj(x -> getValidComment())
                .collect(Collectors.toList());
    }

    private UserDetails getValidUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(VALID_LAST_NAME);
        userDetails.setFirstName(VALID_FIRST_NAME);
        return userDetails;
    }

    private Comment getValidComment() {
        Comment comment = new Comment();
        comment.setId(VALID_COMMENT_ID);
        comment.setDate(VALID_COMMENT_DATE);
        comment.setText(VALID_COMMENT_TEXT);
        comment.setAuthor(getValidAuthorComment());
        return comment;
    }

    private User getValidAuthorComment() {
        User user = new User();
        user.setUserDetails(getValidCommentUserDetails());
        return user;
    }

    private UserDetails getValidCommentUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(VALID_COMMENT_LAST_NAME);
        userDetails.setFirstName(VALID_COMMENT_FIRST_NAME);
        return userDetails;
    }

    private User getValidAuthor() {
        User user = new User();
        UserDetails userDetails = getValidUserDetails();
        user.setUserDetails(userDetails);
        return user;
    }

    private String getValidTextWithLength(int length) {
        return IntStream.range(0, length)
                .boxed()
                .map(x -> "a")
                .collect(Collectors.joining());
    }

}