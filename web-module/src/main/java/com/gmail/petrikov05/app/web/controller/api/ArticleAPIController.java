package com.gmail.petrikov05.app.web.controller.api;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_NOT_FOUND;

@RestController
@RequestMapping("/api/articles")
public class ArticleAPIController {

    private final ArticleService articleService;

    public ArticleAPIController(ArticleService articleService) {this.articleService = articleService;}

    @GetMapping
    public List<ArticlePreviewDTO> getArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Object getArticleById(
            @PathVariable Long id
    ) {
        Optional<ArticleDTO> article = Optional.ofNullable(articleService.getArticleById(id));
        if (article.isPresent()) {
            return article.get();
        }
        return MESSAGE_ARTICLE_NOT_FOUND;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ArticleDTO addArticle(
            @RequestBody @Valid AddArticleDTO addArticleDTO
    ) throws AnonymousUserException {
        return articleService.addArticle(addArticleDTO);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteArticleById(
            @PathVariable Long id
    ) {
        try {
            boolean isDeleted = articleService.deleteById(id);
            if (isDeleted) {
                return MESSAGE_ARTICLE_DELETED;
            } else {
                return MESSAGE_ARTICLE_DELETED_FAIL;
            }
        } catch (ObjectDBException e) {
            return e.getMessage();
        }
    }

}
