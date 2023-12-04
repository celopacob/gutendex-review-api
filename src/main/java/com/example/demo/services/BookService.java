package com.example.demo.services;

import com.example.demo.dtos.BookDto;
import com.example.demo.entities.Review;
import com.example.demo.repositories.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findAll() {
        List<Review> books = new ArrayList<>();
        reviewRepository.findAll().forEach(books::add);

        return books;
    }

    public List<Review> findReviews(BookDto bookDto) {
        List<Review> bookReviewList = new ArrayList<>();
        reviewRepository.findAll().stream()
        .filter(book -> book.getBookId() == bookDto.getId())
        .collect(Collectors.toList()).forEach(bookReviewList::add);

        return bookReviewList;
    }
}
