package com.meli.cbt.paymentapi.controller;

import com.meli.cbt.paymentapi.constants.Constants;
import com.meli.cbt.paymentapi.exception.MeliException;
import com.meli.cbt.paymentapi.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MeliException ex) {
        log.error("## Ocorreu um erro - status: [{}] - error: {} - message: {}", ex.getHttpStatus().value(), ex.getError(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus())
            .body(ErrorResponse.builder()
                .error(ex.getError())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler
    public ResponseEntity<Void> handle(EntityNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle (WebExchangeBindException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .error(Constants.ERROR_PAYMENT_REQUEST_INVALID_INPUT)
                        .message(Constants.ERROR_PAYMENT_REQUEST_INVALID_INPUT_MESSAGE)
                        .build());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.info("## Ocorreu um erro: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.builder()
                .error(Constants.ERROR_INTERNAL)
                .message(Constants.ERROR_INTERNAL_MESSAGE)
                .build());
    }
}