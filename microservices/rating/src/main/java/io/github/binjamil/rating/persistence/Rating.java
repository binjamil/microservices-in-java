package io.github.binjamil.rating.persistence;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    
    @Id @GeneratedValue
    private int id;

    private int bookId;
    private int stars;
}
