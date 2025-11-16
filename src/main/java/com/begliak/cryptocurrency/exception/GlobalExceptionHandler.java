package com.begliak.cryptocurrency.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(CurrencyNotFoundException exception) {
    log.warn("During handling a request was caught exception: {}", exception.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .message(exception.getMessage())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
    return createResponseEntity(errorResponse);
  }

  private ResponseEntity<ErrorResponse> createResponseEntity(ErrorResponse errorResponse) {
    return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
  }
}
