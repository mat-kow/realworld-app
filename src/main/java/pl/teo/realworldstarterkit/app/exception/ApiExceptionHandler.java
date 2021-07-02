package pl.teo.realworldstarterkit.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiNotFoundException.class)
    public ResponseEntity<Object> handleApiNotFoundException(ApiNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = ApiForbiddenException.class)
    public ResponseEntity<Object> handleApiForbiddenException(ApiForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = ApiValidationException.class)
    public ResponseEntity<Object> handleApiValidationException(ApiValidationException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiException apiException = new ApiException( // TODO: 02.07.2021 payload
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, status);
    }
}
