package io.github.binjamil.review.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    List<Review> findByBookId(int bookId);
}
