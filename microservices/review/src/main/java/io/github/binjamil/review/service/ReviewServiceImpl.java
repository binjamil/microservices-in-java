package io.github.binjamil.review.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import io.github.binjamil.api.core.review.*;
import io.github.binjamil.api.exceptions.BadRequestException;
import io.github.binjamil.review.persistence.*;
import io.github.binjamil.util.ServiceUtil;

@RestController
public class ReviewServiceImpl implements ReviewService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ModelMapper mapper;
    private final ReviewRepository repository;
    private final ServiceUtil serviceUtil;

    public ReviewServiceImpl(ModelMapper mapper, ReviewRepository repository, ServiceUtil serviceUtil) {
        this.mapper = mapper;
        this.repository = repository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<ReviewDto> getReviews(int bookId) {
        List<Review> reviews = repository.findByBookId(bookId);

        LOGGER.info("FETCH Reviews[bookId={}]", bookId);
        return reviews.stream()
            .map(entity -> {
                var dto = mapper.map(entity, ReviewDto.class);
                dto.setServiceAddress(serviceUtil.getServiceAddress());
                return dto;
            })
            .toList();
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        if (reviewDto.getId() != 0) {
            throw new BadRequestException("Id is auto-generated so don't provide it in request body");
        }
        if (reviewDto.getBookId() == 0) {
            throw new BadRequestException("Field bookId is required to add a review");
        }

        var entity = mapper.map(reviewDto, Review.class);
        var savedEntity = repository.save(entity);

        var dto = mapper.map(savedEntity, ReviewDto.class);
        dto.setServiceAddress(serviceUtil.getServiceAddress());

        LOGGER.info("CREATE Review[id={}, bookId={}]", dto.getId(), dto.getBookId());
        return dto;
    }

    @Override
    public void deleteReviews(int bookId) {
        List<Review> reviews = repository.findByBookId(bookId);

        LOGGER.info("DELETE Reviews[bookId={}]", bookId);
        reviews.stream().forEach(entity -> repository.delete(entity));
    }
    
}
