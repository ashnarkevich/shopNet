package com.gmail.petrikov05.app.web.controller;

import java.lang.invoke.MethodHandles;

import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleWithCommentsDTO;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_NOT_FOUND;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {this.articleService = articleService;}

    @GetMapping
    public String getArticlesByPage(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model
    ) {
        PaginationWithEntitiesDTO<ArticleDTO> articlesByPage = articleService.getArticlesByPage(page);
        model.addAttribute("articles", articlesByPage.getEntities());
        model.addAttribute("pages", articlesByPage.getPages());
        model.addAttribute("page", page);
        logger.trace("show articles page");
        return "articles";
    }

    @GetMapping("/{id}")
    public String getArticleById(
            @PathVariable Long id,
            Model model
    ) {
        ArticleWithCommentsDTO article = articleService.getArticleById(id);
        if (article == null) {
            model.addAttribute("error", MESSAGE_ARTICLE_NOT_FOUND);
        }
        model.addAttribute("article", article);
        return "article";
    }

}
