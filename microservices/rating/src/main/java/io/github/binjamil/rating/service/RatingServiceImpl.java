package io.github.binjamil.rating.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import io.github.binjamil.api.core.rating.*;
import io.github.binjamil.api.exceptions.BadRequestException;
import io.github.binjamil.rating.persistence.*;
import io.github.binjamil.util.ServiceUtil;

@RestController
public class RatingServiceImpl implements RatingService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final ModelMapper mapper;
    private final RatingRepository repository;
    private final ServiceUtil serviceUtil;

    public RatingServiceImpl(ModelMapper mapper, RatingRepository repository, ServiceUtil serviceUtil) {
        this.mapper = mapper;
        this.repository = repository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<RatingDto> getRatings(int bookId) {
        List<Rating> ratings = repository.findByBookId(bookId);

        LOGGER.info("FETCH Ratings[bookId={}]", bookId);
        return ratings.stream()
            .map(entity -> {
                var dto = mapper.map(entity, RatingDto.class);
                dto.setServiceAddress(serviceUtil.getServiceAddress());
                return dto;
            })
            .toList();
    }

    @Override
    public RatingDto createRating(RatingDto ratingDto) {
        if (ratingDto.getId() != 0) {
            throw new BadRequestException("Id is auto-generated so don't provide it in request body");
        }
        if (ratingDto.getBookId() == 0) {
            throw new BadRequestException("Field bookId is required to add a rating");
        }

        var entity = mapper.map(ratingDto, Rating.class);
        var savedEntity = repository.save(entity);

        var dto = mapper.map(savedEntity, RatingDto.class);
        dto.setServiceAddress(serviceUtil.getServiceAddress());

        LOGGER.info("CREATE Rating[id={}, bookId={}]", dto.getId(), dto.getBookId());
        return dto;
    }

    @Override
    public void deleteRatings(int bookId) {
        List<Rating> ratings = repository.findByBookId(bookId);

        LOGGER.info("DELETE Ratings[bookId={}]", bookId);
        ratings.stream().forEach(entity -> repository.delete(entity));
    }
}
