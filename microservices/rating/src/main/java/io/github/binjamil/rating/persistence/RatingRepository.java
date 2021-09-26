package io.github.binjamil.rating.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Integer> {
    
    List<Rating> findByBookId(int bookId);
}
