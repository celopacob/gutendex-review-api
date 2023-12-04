package com.example.demo.controllers;

import com.example.demo.dtos.BookDto;
import com.example.demo.dtos.GutendexApiDto;
import com.example.demo.dtos.ReviewDto;
import com.example.demo.entities.Review;
import com.example.demo.services.BookService;
import com.example.demo.services.GutendexService;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Books", description = "Books API")
@RestController
@RequestMapping("/api/books/")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private GutendexService gutendexService;


    @GetMapping
    public ResponseEntity<GutendexApiDto> getBooks(
        @RequestParam("title") Optional<String> title) throws InterruptedException, ExecutionException {

        GutendexApiDto gutendexApiDto = gutendexService.getBooks(title);
        return ResponseEntity.status(HttpStatus.OK).body(gutendexApiDto);
    }

    @GetMapping("/{bookId}/")
    public ResponseEntity<?> getBookById(@PathVariable("bookId") int bookId) {

        try {
            BookDto bookDto = gutendexService.getBookById(bookId);
            return ResponseEntity.status(HttpStatus.OK).body(bookDto);

        } catch(Exception e) {
            return new ResponseEntity<>(
                "No book found with the id: " + bookId,
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping("/reviews/")
    public ResponseEntity<Review> create(@Valid @RequestBody ReviewDto reviewDto) {
        Review bookReview = bookService.create(reviewDto.toBookReview());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookReview);
    }

    // Get the top 3 books by overall rating (if we have 3 or more books already rated)
    @GetMapping("/top-reviews/")
    public ResponseEntity<GutendexApiDto> getTopReviews() {
        GutendexApiDto gutendexApiDto = gutendexService.getTopBookReviews();
        return ResponseEntity.status(HttpStatus.OK).body(gutendexApiDto);
    }

    // Controller based exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}