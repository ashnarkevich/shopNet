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
import com.gmail.petrikov05.app.service.impl.ArticleServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.gmail.petrikov05.app.service.constant.PageConstant.ARTICLE_SUMMARY_LENGTH;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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
        PaginationWithEntitiesDTO<ArticleDTO> actualArticlesByPage = articlesService.getArticlesByPage(VALID_PAGE);
        assertThat(actualArticlesByPage).isNotNull();
        assertThat(actualArticlesByPage.getEntities()).isNotNull();
        assertThat(actualArticlesByPage.getPages()).isEqualTo(VALID_PAGES);
        assertThat(actualArticlesByPage.getEntities().get(0).getId()).isEqualTo(VALID_ID);
        assertThat(actualArticlesByPage.getEntities().get(0).getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(actualArticlesByPage.getEntities().get(0).getDate()).isEqualTo(VALID_DATE);
        assertThat(actualArticlesByPage.getEntities().get(0).getAuthor()).isEqualTo(VALID_AUTHOR);
        assertThat(actualArticlesByPage.getEntities().get(0).getText()).isEqualTo(VALID_ARTICLE_TEXT);
    }

    @Test
    public void getArticlesByPage_returnArticlesValidWithTextMaxLength() {
        List<Article> returnedArticles = getValidArticles();
        returnedArticles.get(0).setText(getValidTextWithLength(201));
        when(articleRepository.getArticlesByPage(anyInt(), anyInt())).thenReturn(returnedArticles);
        PaginationWithEntitiesDTO<ArticleDTO> actualArticleByPage = articlesService.getArticlesByPage(VALID_PAGE);
        assertThat(actualArticleByPage.getEntities().get(0).getText().length()).isEqualTo(ARTICLE_SUMMARY_LENGTH);
    }

    @Test
    public void getArticlesByPage_returnArticlesWithTextMaxLength() {
        List<Article> returnedArticles = getValidArticles();
        returnedArticles.get(0).setText(getValidTextWithLength(ARTICLE_SUMMARY_LENGTH));
        when(articleRepository.getArticlesByPage(anyInt(), anyInt())).thenReturn(returnedArticles);
        PaginationWithEntitiesDTO<ArticleDTO> actualArticleByPage = articlesService.getArticlesByPage(VALID_PAGE);
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
        ArticleWithCommentsDTO actualArticleWithCommentsDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleWithCommentsDTO).isNotNull();
        assertThat(actualArticleWithCommentsDTO.getId()).isEqualTo(VALID_ID);
        assertThat(actualArticleWithCommentsDTO.getDate()).isEqualTo(VALID_DATE);
        assertThat(actualArticleWithCommentsDTO.getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(actualArticleWithCommentsDTO.getText()).isEqualTo(VALID_ARTICLE_TEXT);
        assertThat(actualArticleWithCommentsDTO.getAuthor()).isEqualTo(VALID_AUTHOR);
    }

    @Test
    public void getArticleById_returnArticleDTOWithValidComment() {
        Article returnedArticle = getValidArticle();
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        ArticleWithCommentsDTO actualArticleWithCommentsDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleWithCommentsDTO.getComments()).isNotNull();
        assertThat(actualArticleWithCommentsDTO.getComments().get(0).getId()).isEqualTo(VALID_COMMENT_ID);
        assertThat(actualArticleWithCommentsDTO.getComments().get(0).getDate()).isEqualTo(VALID_COMMENT_DATE);
        assertThat(actualArticleWithCommentsDTO.getComments().get(0).getText()).isEqualTo(VALID_COMMENT_TEXT);
        assertThat(actualArticleWithCommentsDTO.getComments().get(0).getAuthor()).isEqualTo(VALID_COMMENT_AUTHOR);
    }

    @Test
    public void getArticleById_returnArticleWithFullText() {
        Article returnedArticle = getValidArticle();
        int validTextLength = 500;
        returnedArticle.setText(getValidTextWithLength(validTextLength));
        when(articleRepository.getObjectByID(anyLong())).thenReturn(returnedArticle);
        ArticleWithCommentsDTO actualArticleWithCommentsDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleWithCommentsDTO).isNotNull();
        assertThat(actualArticleWithCommentsDTO.getText().length()).isEqualTo(validTextLength);
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
        ArticleWithCommentsDTO actualArticleWithCommentsDTO = articlesService.getArticleById(VALID_ID);
        assertThat(actualArticleWithCommentsDTO).isNull();
    }

    /* get all articles */
    @Test
    public void getArticles_returnListArticleDTOS() {
        List<Article> returnedArticles = getValidArticles();
        when(articleRepository.getAllObjects()).thenReturn(returnedArticles);
        List<ArticleDTO> actualArticle = articlesService.getAllArticles();
        assertThat(actualArticle).isNotNull();
        assertThat(actualArticle.get(0).getId()).isEqualTo(VALID_ID);
        assertThat(actualArticle.get(0).getTitle()).isEqualTo(VALID_ARTICLE_TITLE);
        assertThat(actualArticle.get(0).getDate()).isEqualTo(VALID_DATE);
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
        assertThat(addedArticle.getDate().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
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

    /* delete article by id */
    @Test
    public void deleteArticle_returnTrue() {
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
    public void deleteNotExistArticle_returnFalse() {
        when(articleRepository.getObjectByID(anyLong())).thenReturn(null);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        boolean actualResult = articlesService.deleteById(VALID_ID);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isFalse();
        verify(SecurityContextHolder.getContext(), times(1)).getAuthentication();
    }

    private AddArticleDTO getValidAddArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setTitle(VALID_ARTICLE_TITLE);
        article.setText(VALID_ARTICLE_TEXT);
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
        article.setDate(VALID_DATE);
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