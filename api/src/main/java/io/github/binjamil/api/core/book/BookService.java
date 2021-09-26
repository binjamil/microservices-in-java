package io.github.binjamil.api.core.book;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BookService {
    
    @GetMapping(value = "/book/{bookId}", produces = "application/json")
    public BookDto getBook(@PathVariable int bookId);

    @PostMapping(value = "/book", produces = "application/json")
    public BookDto createBook(@RequestBody BookDto bookDto);

    @DeleteMapping(value = "/book/{bookId}")
    public void deleteBook(@PathVariable int bookId);
}
