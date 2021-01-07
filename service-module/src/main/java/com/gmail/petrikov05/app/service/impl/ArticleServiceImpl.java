package com.gmail.petrikov05.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.ArticleRepository;
import com.gmail.petrikov05.app.repository.model.Article;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.converter.ArticleConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.PageConstant.ARTICLE_SUMMARY_LENGTH;
import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_ARTICLE_BY_PAGE;
import static com.gmail.petrikov05.app.service.util.PageUtil.getCountOfPage;
import static com.gmail.petrikov05.app.service.util.converter.ArticleConverter.convertAddDTOToObject;
import static com.gmail.petrikov05.app.service.util.converter.ArticleConverter.convertObjectToDTO;
import static com.gmail.petrikov05.app.service.util.converter.ArticleConverter.converterObjectToDTOWithComments;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            UserService userService
    ) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public PaginationWithEntitiesDTO<ArticleDTO> getArticlesByPage(Integer page) {
        int pages = getPages();
        int startPosition = PageUtil.getStartPosition(page, COUNT_OF_ARTICLE_BY_PAGE);
        List<Article> articles = articleRepository.getArticlesByPage(startPosition, COUNT_OF_ARTICLE_BY_PAGE);
        List<ArticleDTO> articleWithCommentsDTOS = articles.stream()
                .map(ArticleConverter::convertObjectToDTO)
                .map(this::correctTextLengthForSummary)
                .collect(Collectors.toList());
        PaginationWithEntitiesDTO<ArticleDTO> articlesWithPage = new PaginationWithEntitiesDTO<>();
        articlesWithPage.setEntities(articleWithCommentsDTOS);
        articlesWithPage.setPages(pages);
        return articlesWithPage;
    }

    @Override
    @Transactional
    public ArticleWithCommentsDTO getArticleById(Long id) {
        Article article = articleRepository.getObjectByID(id);
        if (article == null) {
            return null;
        }
        return converterObjectToDTOWithComments(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.getAllObjects();
        return articles.stream()
                .map(ArticleConverter::convertObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArticleDTO addArticle(AddArticleDTO addArticleDTO) throws AnonymousUserException {
        User user = userService.getCurrentUser();
        Article article = convertAddDTOToObject(addArticleDTO);
        article.setAuthor(user);
        article.setDate(LocalDateTime.now());
        articleRepository.add(article);
        return convertObjectToDTO(article);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Optional<Article> article = Optional.ofNullable(articleRepository.getObjectByID(id));
        if (!article.isPresent()) {
            logger.info(getAuthentication().getName() + " tried to get non-existent article");
            return false;
        }
        return articleRepository.delete(article.get());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private int getPages() {
        Long countOfEntities = articleRepository.getCountOfEntities();
        return getCountOfPage(countOfEntities, COUNT_OF_ARTICLE_BY_PAGE);
    }

    private ArticleDTO correctTextLengthForSummary(ArticleDTO article) {
        int textLength = article.getText().length();
        if (textLength > ARTICLE_SUMMARY_LENGTH) {
            String correctText = article.getText().substring(0, ARTICLE_SUMMARY_LENGTH);
            article.setText(correctText);
        }
        return article;
    }

}
