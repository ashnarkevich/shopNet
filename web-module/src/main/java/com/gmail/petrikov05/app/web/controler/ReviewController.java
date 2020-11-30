package com.gmail.petrikov05.app.web.controler;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.gmail.petrikov05.app.service.ReviewService;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
