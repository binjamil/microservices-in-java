package io.github.binjamil.api.core.review;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewService {
    
    @GetMapping(value = "/review", produces = "application/json")
    public List<ReviewDto> getReviews(@RequestParam(value = "bookId", required = true) int bookId);

    @PostMapping(value = "/review", produces = "application/json")
    public ReviewDto createReview(@RequestBody ReviewDto reviewDto);

    @DeleteMapping(value = "/review")
    public void deleteReviews(@RequestParam(value = "bookId", required = true) int bookId);
}
