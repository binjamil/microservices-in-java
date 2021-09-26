package io.github.binjamil.api.core.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    
    private int id;
    private int bookId;
    private String content;
    private String reviewer;
    private String serviceAddress;
}
