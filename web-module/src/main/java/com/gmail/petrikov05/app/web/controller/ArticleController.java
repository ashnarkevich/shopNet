package com.gmail.petrikov05.app.web.controller;

import java.lang.invoke.MethodHandles;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.ArticleService;
import com.gmail.petrikov05.app.service.CommentService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.article.AddArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticleDTO;
import com.gmail.petrikov05.app.service.model.article.ArticlePreviewDTO;
import com.gmail.petrikov05.app.service.model.article.UpdateArticleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_ARTICLE_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_COMMENT_DELETED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_COMMENT_DELETED_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_USER_NOT_FOUND;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(
            ArticleService articleService,
            CommentService commentService
    ) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping
    public String getArticlesByPage(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model
    ) {
        PaginationWithEntitiesDTO<ArticlePreviewDTO> articlesByPage = articleService.getArticlesByPage(page);
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
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            model.addAttribute("error", MESSAGE_ARTICLE_NOT_FOUND);
        }
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/add")
    public String showAddArticlePage(
            Model model
    ) {
        model.addAttribute("article", new AddArticleDTO());
        return "article_add";
    }

    @PostMapping
    public String addArticle(
            @ModelAttribute(name = "article") @Valid AddArticleDTO addArticleDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", addArticleDTO);
            return "article_add";
        }
        try {
            ArticleDTO addedArticle = articleService.addArticle(addArticleDTO);
            model.addAttribute("article", addedArticle);
            return "article";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", MESSAGE_USER_NOT_FOUND);
            return "redirect:/";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteArticleById(
            @RequestParam(name = "page") Integer page,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            boolean isDeleted = articleService.deleteById(id);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_DELETED);
            } else {
                redirectAttributes.addFlashAttribute("error", MESSAGE_ARTICLE_DELETED_FAIL);
            }
        } catch (ObjectDBException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/articles?page=" + page;
    }

    @PostMapping("/{articleId}/comments/{commentId}/delete")
    public String deleteCommentById(
            @PathVariable(required = true, name = "articleId") Long articleId,
            @PathVariable(required = true, name = "commentId") Long commentId,
            RedirectAttributes redirectAttributes
    ) {
        boolean isDeleted = commentService.deleteById(commentId);
        if (isDeleted) {
            logger.info("comment with id (" + commentId + ") deleted");
            redirectAttributes.addFlashAttribute("message", MESSAGE_COMMENT_DELETED);
        } else {
            logger.info("comment with id (" + commentId + ") did not delete");
            redirectAttributes.addFlashAttribute("error", MESSAGE_COMMENT_DELETED_FAIL);
        }
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/{id}/update")
    public String showUpdateArticlePage(
            @PathVariable Long id,
            Model model
    ) {
        ArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article_update";
    }

    @PostMapping("/{id}/update")
    public String updateArticleByNumber(
            @PathVariable Long id,
            @ModelAttribute(name = "article") @Valid UpdateArticleDTO updateArticleDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", updateArticleDTO);
            model.addAttribute("id", id);
            return "article_update";
        }
        try {
            ArticleDTO updatedArticle = articleService.updateArticle(id, updateArticleDTO);
            model.addAttribute("article", updatedArticle);
            return "article";
        } catch (ObjectDBException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/articles";
        }
    }

}
