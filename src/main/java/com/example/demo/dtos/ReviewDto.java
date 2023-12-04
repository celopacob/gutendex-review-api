package com.example.demo.dtos;

import com.example.demo.entities.Review;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReviewDto {

    @NotNull(message = "BookId is required.")
    private Integer bookId;

    @NotNull(message = "Rating is required.")
    @DecimalMax("5.0")
    @DecimalMin("0.0")
    private Double rating;

    @NotBlank(message = "Review is required.")
    private String review;

    public Review toBookReview() {
        Review bookReview = new Review();
        bookReview.setBookId(bookId);
        bookReview.setRating(rating);
        bookReview.setReview(review);

        return bookReview;
    }
}
