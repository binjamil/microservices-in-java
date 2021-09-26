package io.github.binjamil.api.core.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    
    private int id;
    private int bookId;
    private int stars;
    private String serviceAddress;
}
