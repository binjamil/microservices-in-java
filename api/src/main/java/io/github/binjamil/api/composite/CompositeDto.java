package io.github.binjamil.api.composite;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositeDto {
    
    private int bookId;
    private String title;
    private String author;
    private double rating;
    private List<ReviewSummary> reviews;

    public record ReviewSummary(
        int id,
        String content,
        String reviewer
    ) {}

    public record ServiceAddresses
    (
        String compositeAddr,
        String bookAddr,
        String ratingAddr,
        String reviewAddr
    ) {}
}
