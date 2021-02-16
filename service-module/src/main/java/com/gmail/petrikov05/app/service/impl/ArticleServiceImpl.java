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
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.article.UpdateArticleDTO;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.converter.ArticleConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.PageConstant.ARTICLE_SUMMARY_LENGTH;
import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_ARTICLE_BY_PAGE;
import static com.gmail.petrikov05.app.service.util.PageUtil.getCountOfPage;
import static com.gmail.petrikov05.app.service.util.converter.ArticleConverter.convertAddDTOToObject;
import static com.gmail.petrikov05.app.service.util.converter.ArticleConverter.convertObjectToDTO;

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
    public PaginationWithEntitiesDTO<ArticlePreviewDTO> getArticlesByPage(Integer page) {
        int startPosition = PageUtil.getStartPosition(page, COUNT_OF_ARTICLE_BY_PAGE);
        List<Article> articles = articleRepository.getArticlesByPage(startPosition, COUNT_OF_ARTICLE_BY_PAGE);
        List<ArticlePreviewDTO> articlePreviewDTOS = articles.stream()
                .map(ArticleConverter::convertObjectToPreviewDTO)
                .map(this::correctTextLengthForSummary)
                .collect(Collectors.toList());
        int pages = getPages();
        return new PaginationWithEntitiesDTO<>(articlePreviewDTOS, pages);
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.getObjectByID(id);
        if (article == null) {
            return null;
        }
        return convertObjectToDTO(article);
    }

    @Override
    @Transactional
    public List<ArticlePreviewDTO> getAllArticles() {
        List<Article> articles = articleRepository.getAllObjects();
        return articles.stream()
                .map(ArticleConverter::convertObjectToPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArticleDTO addArticle(AddArticleDTO addArticleDTO) throws AnonymousUserException {
        User user = userService.getCurrentUser();
        Article article = convertAddDTOToObject(addArticleDTO);
        article.setAuthor(user);
        article.setDateCreate(LocalDateTime.now());
        articleRepository.add(article);
        return convertObjectToDTO(article);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) throws ObjectDBException {
        Optional<Article> article = Optional.ofNullable(articleRepository.getObjectByID(id));
        if (!article.isPresent()) {
            logger.info("tried to get non-existent article");
            throw new ObjectDBException("Article not found");
        }
        return articleRepository.delete(article.get());
    }

    @Override
    @Transactional
    public ArticleDTO updateArticle(Long id, UpdateArticleDTO articleDTO) throws ObjectDBException {
        Article article = getArticleForUpdate(id, articleDTO);
        return convertObjectToDTO(article);
    }

    private Article getArticleForUpdate(Long id, UpdateArticleDTO articleDTO) throws ObjectDBException {
        Article article = articleRepository.getObjectByID(id);
        if (article == null) {
            logger.info("The article (id=" + id + ") not found");
            throw new ObjectDBException("Article not found");
        }
        article.setTitle(articleDTO.getTitle());
        article.setText(articleDTO.getText());
        article.setDatePublication(articleDTO.getDatePublication());
        return article;
    }

    private int getPages() {
        Long countOfEntities = articleRepository.getCountOfEntities();
        return getCountOfPage(countOfEntities, COUNT_OF_ARTICLE_BY_PAGE);
    }

    private ArticlePreviewDTO correctTextLengthForSummary(ArticlePreviewDTO article) {
        int textLength = article.getText().length();
        if (textLength > ARTICLE_SUMMARY_LENGTH) {
            String correctText = article.getText().substring(0, ARTICLE_SUMMARY_LENGTH);
            article.setText(correctText);
        }
        return article;
    }

}
