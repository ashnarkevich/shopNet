package com.gmail.petrikov05.app.service.util.converter;

import com.gmail.petrikov05.app.repository.model.Review;
import com.gmail.petrikov05.app.service.model.review.ReviewDTO;

public class ReviewConverter {

    public static ReviewDTO convertObjectToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setText(review.getText());
        reviewDTO.setDateCreate(review.getDateCreate());
        reviewDTO.setIsActive(review.getIsActive());
        String author = review.getAuthor().getUserDetails().getLastName() + " "
                + review.getAuthor().getUserDetails().getFirstName();
        reviewDTO.setAuthor(author);
        return reviewDTO;
    }

}
