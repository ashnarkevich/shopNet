package com.gmail.petrikov05.app.web.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.ReviewService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.AddReviewDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.gmail.petrikov05.app.service.constant.MessageConstant.MESSAGE_ACCESS_CLOSE;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @GetMapping
    public String showReviewsPage(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        PaginationWithEntitiesDTO<ReviewDTO> reviewsByPage = reviewService.getReviewsByPage(page);
        model.addAttribute("reviews", reviewsByPage.getEntities());
        model.addAttribute("pages", reviewsByPage.getPages());
        model.addAttribute("page", page);
        logger.trace("show reviews page");
        return "reviews";
    }

    @GetMapping("/add")
    public String showAddReviewPage(
            Model model
    ) {
        model.addAttribute("review", new AddReviewDTO());
        return "review_add";
    }

    @PostMapping("/add")
    public String addReview(
            @ModelAttribute @Valid AddReviewDTO addReviewDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("review", addReviewDTO);
            return "review_add";
        }
        try {
            ReviewDTO addedReview = reviewService.addReview(addReviewDTO);
            String message = addedReview.getAuthor() + " Your review was send.";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", MESSAGE_ACCESS_CLOSE);
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteReviews(
            @RequestParam int page,
            @RequestParam(name = "id", defaultValue = "") List<Long> ids,
            RedirectAttributes redirect
    ) {
        reviewService.deletedReviews(ids);
        redirect.addFlashAttribute("message", "Reviews deleted");
        return "redirect:/reviews?page=" + page;
    }

}
