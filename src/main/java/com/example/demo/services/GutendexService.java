package com.example.demo.services;

import com.example.demo.dtos.BookDto;
import com.example.demo.dtos.GutendexApiDto;
import com.example.demo.entities.Review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GutendexService {

    @Autowired
    private BookService bookService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String EXTERNAL_API_URL = "https://gutendex.com/books";


    private ResponseEntity<String> getRestResponseTemplate(String url) {
        return restTemplate
            .getForEntity(url, String.class);
    }

    @Cacheable("allBooks")
    public ArrayList<BookDto> getAllBooks() {

        ArrayList<BookDto> allBooks = new ArrayList<>();
        int endPage = 5;

        for (int page = 1; page <= endPage; page++) {
            ArrayList<BookDto> pageOfBooks = getBooksByPage(page);

            if (pageOfBooks.isEmpty()) {
                break;
            }
            allBooks.addAll(pageOfBooks);
        }
        return allBooks;
    }

    @Cacheable("booksByPage")
    private ArrayList<BookDto> getBooksByPage(int page) {
        String apiUrl = EXTERNAL_API_URL + "?page=" + page;

        try {

            ResponseEntity<String> responseTemplate = getRestResponseTemplate(apiUrl);

            JsonNode books = objectMapper.readTree(responseTemplate.getBody())
                .path("results");

            ArrayList<BookDto> bookList = objectMapper
                .readerFor(new TypeReference<List<BookDto>>() {})
                .readValue(books.traverse());

            return bookList;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Cacheable("books")
    public GutendexApiDto getBooks(Optional<String> title) {

        GutendexApiDto gutendexApiDto = new GutendexApiDto();

        try {

            ArrayList<BookDto> bookList = getAllBooks();
            ArrayList<BookDto> filteredResultList = new ArrayList<BookDto>();

            String bookTitle = title.isPresent() ? title.get() : null;

            for (BookDto bookDto : bookList) {

                getBookReviews(bookDto);

                if (bookTitle != null) {
                    if(bookDto.getTitle().toLowerCase().contains(bookTitle.toLowerCase())){
                        filteredResultList.add(bookDto);
                    }
                }
            }

            if(bookTitle != null) {
                gutendexApiDto.setBooks(filteredResultList);
            } else {
                gutendexApiDto.setBooks(bookList);
            }

        } catch(Exception e) {
            System.out.println(e);
        }
        return gutendexApiDto;
    }

    @Cacheable("bookTopReviews")
    public GutendexApiDto getTopBookReviews() {

        GutendexApiDto gutendexApiDto = new GutendexApiDto();

        try {

            ArrayList<BookDto> bookList = getAllBooks();

            for (BookDto bookDto : bookList) {
                getBookReviews(bookDto);
            }

            List<BookDto> topThreeBooksByReviews = bookList.stream()
                .sorted((b1, b2) -> Double.compare(b2.getRating(), b1.getRating()))
                .limit(3)
                .collect(Collectors.toList());

            gutendexApiDto.setBooks((ArrayList<BookDto>) topThreeBooksByReviews);

        } catch(Exception e) {
            System.out.println(e);
        }

        return gutendexApiDto;
    }

    public BookDto getBookById(int bookId) throws Exception {

        try {

            String url = EXTERNAL_API_URL + "/" + bookId + "/";

            ResponseEntity<String> responseTemplate = getRestResponseTemplate(url);

            JsonNode book = objectMapper
                .readTree(responseTemplate.getBody());

            BookDto bookDto = objectMapper
                .readerFor(new TypeReference<BookDto>() {})
                .readValue(book.traverse());

            getBookReviews(bookDto);

            return bookDto;

        } catch(Exception e) {
            throw e;
        }
    }

    @Cacheable("reviewsByBook")
    private void getBookReviews(BookDto bookDto) {

        // Checking reviews/ratings and adding them
        // to the resulting json
        List<Review> bookReviewList = bookService
            .findReviews(bookDto);

        OptionalDouble totalRating = bookReviewList
            .stream()
            .mapToDouble(bookReviewItem -> bookReviewItem.getRating()).average();

        ArrayList<String> reviewList = new ArrayList<String>();

        bookReviewList.stream().map(o -> o.getReview())
            .collect(Collectors.toList()).forEach(reviewList::add);

        bookDto.setRating(totalRating.isPresent()?totalRating.getAsDouble():0.0);
        bookDto.setReviews(reviewList);
    }

    // TO-DO: Implement logging and improve the error handling on the app.
}