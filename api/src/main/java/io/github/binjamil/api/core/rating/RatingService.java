package io.github.binjamil.api.core.rating;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface RatingService {
    
    @GetMapping(value = "/rating", produces = "application/json")
    public RatingDto getRating(@RequestParam(value = "bookId", required = true) int bookId);

    @PostMapping(value = "/rating", produces = "application/json")
    public RatingDto createRating(@RequestBody RatingDto ratingDto);

    @DeleteMapping(value = "rating")
    public void deleteRating(@RequestParam(value = "bookId", required = true) int bookId);
}
