package io.github.binjamil.review.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    
    @Id @GeneratedValue
    private int id;

    private int bookId;
    private String content;
    private String reviewer;
}
