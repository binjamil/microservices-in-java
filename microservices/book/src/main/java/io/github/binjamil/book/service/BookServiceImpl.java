package io.github.binjamil.book.service;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import io.github.binjamil.api.core.book.BookDto;
import io.github.binjamil.api.core.book.BookService;
import io.github.binjamil.book.persistence.Book;
import io.github.binjamil.book.persistence.BookRepository;
import io.github.binjamil.util.ServiceUtil;

@RestController
public class BookServiceImpl implements BookService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);
    
    private final ModelMapper mapper;
    private final BookRepository repository;
    private final ServiceUtil serviceUtil;

    public BookServiceImpl(ModelMapper mapper, BookRepository repository, ServiceUtil serviceUtil) {
        this.mapper = mapper;
        this.repository = repository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public BookDto getBook(int bookId) {
        LOGGER.info("Will get book info for id={}", bookId);
        var entity = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException());
        var dto = mapper.map(entity, BookDto.class);
        dto.setServiceAddress(this.serviceUtil.getServiceAddress());
        return dto;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        LOGGER.info("Will create new book info with title={}", bookDto.getTitle());
        var entity = mapper.map(bookDto, Book.class);
        var savedEntity = repository.save(entity);
        var dto = mapper.map(savedEntity, BookDto.class);
        dto.setServiceAddress(this.serviceUtil.getServiceAddress());
        return dto;
    }    
}
