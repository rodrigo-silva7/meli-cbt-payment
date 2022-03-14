package com.meli.cbt.paymentapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MeliException extends RuntimeException {
    private HttpStatus httpStatus;
    private String error;
    private String message;

    public MeliException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
