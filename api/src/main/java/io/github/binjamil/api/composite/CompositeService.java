package io.github.binjamil.api.composite;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.binjamil.api.core.book.BookDto;
import io.github.binjamil.api.core.rating.RatingDto;
import io.github.binjamil.api.core.review.ReviewDto;

public interface CompositeService {
    
    @PostMapping(value = "/book-composite/book", produces = "application/json")
    public BookDto createBook(@RequestBody BookDto bookDto);

    @PostMapping(value = "/book-composite/rating", produces = "application/json")
    public RatingDto createRating(@RequestBody RatingDto ratingDto);

    @PostMapping(value = "/book-composite/review", produces = "application/json")
    public ReviewDto createReview(@RequestBody ReviewDto reviewDto);

    @GetMapping(value = "/book-composite/{bookId}", produces = "application/json")
    public CompositeDto getBookComposite(@PathVariable int bookId);

    @DeleteMapping(value = "/book-composite/{bookId}")
    public void deleteBookComposite(@PathVariable int bookId);
}
