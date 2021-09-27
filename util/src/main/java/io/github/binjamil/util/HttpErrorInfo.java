package io.github.binjamil.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpErrorInfo {
    
    private HttpStatus httpStatus;
    private String path;
    private String message;

    @Builder.Default
    private String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
}
