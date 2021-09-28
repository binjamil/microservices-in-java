package io.github.binjamil.composite.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.github.binjamil.api.composite.CompositeDto;
import io.github.binjamil.api.composite.CompositeService;
import io.github.binjamil.api.composite.CompositeDto.ReviewSummary;
import io.github.binjamil.api.composite.CompositeDto.ServiceAddresses;
import io.github.binjamil.api.core.book.BookDto;
import io.github.binjamil.api.core.rating.RatingDto;
import io.github.binjamil.api.core.review.ReviewDto;
import io.github.binjamil.api.exceptions.*;
import io.github.binjamil.util.HttpErrorInfo;
import io.github.binjamil.util.ServiceUtil;

@RestController
public class CompositeServiceImpl implements CompositeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CompositeServiceImpl.class);

    private final ModelMapper mapper;
    private final ServiceUtil serviceUtil;
    private final RestTemplate client;
    private final String bookUrl;
    private final String ratingUrl;
    private final String reviewUrl;
    
    @Autowired
    public CompositeServiceImpl(
        ModelMapper mapper,
        ServiceUtil serviceUtil,
        RestTemplate client,
        @Value("${app.book-service.host}") String bookHost,
        @Value("${app.book-service.port}") int bookPort,
        @Value("${app.rating-service.host}") String ratingHost,
        @Value("${app.rating-service.port}") int ratingPort,
        @Value("${app.review-service.host}") String reviewHost,
        @Value("${app.review-service.port}") int reviewPort
    ) {
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
        this.client = client;
        this.bookUrl = "http://" + bookHost + ":" + bookPort + "/book";
        this.ratingUrl = "http://" + ratingHost + ":" + ratingPort + "/rating";
        this.reviewUrl = "http://" + reviewHost + ":" + reviewPort + "/review";
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        LOGGER.info("Creating new Book...");
        try {
            String url = bookUrl;
            LOGGER.info("Sending POST request to {}", url);
            var responseDto = client.postForObject(url, bookDto, BookDto.class);
            return responseDto;
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public RatingDto createRating(RatingDto ratingDto) {
        LOGGER.info("Creating new Rating...");
        try {
            String url = bookUrl + "/" + ratingDto.getBookId();
            LOGGER.info("Sending GET request to {}", url);
            client.getForObject(url, BookDto.class);

            // If bookId is valid
            url = ratingUrl;
            LOGGER.info("Sending POST request to {}", url);
            var responseDto = client.postForObject(url, ratingDto, RatingDto.class);
            return responseDto;
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        LOGGER.info("Creating new Review...");
        try {
            String url = bookUrl + "/" + reviewDto.getBookId();
            LOGGER.info("Sending GET request to {}", url);
            client.getForObject(url, BookDto.class);

            // If bookId is valid
            url = reviewUrl;
            LOGGER.info("Sending POST request to {}", url);
            var responseDto = client.postForObject(url, reviewDto, ReviewDto.class);
            return responseDto;
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public CompositeDto getBookComposite(int bookId) {
        LOGGER.info("Fetching composite Book[id={}]", bookId);
        try {
            String url = bookUrl + "/" + bookId;
            LOGGER.info("Sending GET request to {}", url);
            var bookDto = client.getForObject(url, BookDto.class);

            url = ratingUrl + "?bookId=" + bookId;
            LOGGER.info("Sending GET request to {}", url);
            var ratings = client
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RatingDto>>() {})
                .getBody();

            url = reviewUrl + "?bookId=" + bookId;
            LOGGER.info("Sending GET request to {}", url);
            var reviews = client
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ReviewDto>>() {})
                .getBody();

            return createCompositeDto(bookDto, ratings, reviews);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteBookComposite(int bookId) {
        LOGGER.info("Deleting composite Book[id={}]", bookId);
        try {
            String url = bookUrl + "/" + bookId;
            LOGGER.info("Sending DELETE request to {}", url);
            client.delete(url);

            url = ratingUrl + "?bookId=" + bookId;
            LOGGER.info("Sending DELETE request to {}", url);
            client.delete(url);

            url = reviewUrl + "?bookId=" + bookId;
            LOGGER.info("Sending DELETE request to {}", url);
            client.delete(url);
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }
    
    private CompositeDto createCompositeDto(
        BookDto bookDto,
        List<RatingDto> ratings,
        List<ReviewDto> reviews
    ) {
        // 1. Set up Book info
        int bookId = bookDto.getId();
        String title = bookDto.getTitle();
        String author = bookDto.getAuthor();

        // 2. Set up Rating
        double rating = ratings.stream()
            .mapToDouble(RatingDto::getStars)
            .average()
            .orElse(0.0);

        // 3. Set up ReviewSummary list
        List<ReviewSummary> reviewSummaries = reviews.stream()
            .map(r -> new ReviewSummary(r.getId(), r.getContent(), r.getReviewer()))
            .toList();

        // 4. Set up ServiceAdresses
        String compositeAddr = serviceUtil.getServiceAddress();
        String bookAddr = bookDto.getServiceAddress();
        String ratingAddr = (ratings.size() > 0) ? ratings.get(0).getServiceAddress() : "";
        String reviewAddr = (reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(compositeAddr, bookAddr, ratingAddr, reviewAddr);

        return new CompositeDto(bookId, title, author, rating, reviewSummaries, serviceAddresses);
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {
        case NOT_FOUND:
            return new NotFoundException(getErrorMessage(ex));
    
        case BAD_REQUEST:
            return new BadRequestException(getErrorMessage(ex));
    
        default:
            LOGGER.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOGGER.warn("Error body: {}", ex.getResponseBodyAsString());
            return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            var errorNode = new ObjectMapper().readTree(ex.getResponseBodyAsString());
            var error = mapper.map(errorNode, HttpErrorInfo.class);
            return error.getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
