package io.github.binjamil.api.core.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private int id;
    private String title;
    private String author;
    private String serviceAddress;
}
