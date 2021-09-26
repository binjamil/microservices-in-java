package io.github.binjamil.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.github.binjamil.api.exceptions.BadRequestException;
import io.github.binjamil.api.exceptions.NotFoundException;

@RestControllerAdvice
public class GlobalControllerAdvice {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFound(WebRequest request, NotFoundException ex) {
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequest(WebRequest request, BadRequestException ex) {
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus status, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        final String message = ex.getMessage();

        LOGGER.info("Returning HTTP status: {} for path: {}, message: {}", status, path, message);
        return HttpErrorInfo.builder()
            .httpStatus(status)
            .path(path)
            .message(message)
            .build();
    }
}
