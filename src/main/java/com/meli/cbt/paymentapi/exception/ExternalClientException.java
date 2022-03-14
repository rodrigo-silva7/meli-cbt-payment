package com.meli.cbt.paymentapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExternalClientException extends MeliException {
    public ExternalClientException(HttpStatus status, String error, String message) {
        super(status,error,message);
    }
}
