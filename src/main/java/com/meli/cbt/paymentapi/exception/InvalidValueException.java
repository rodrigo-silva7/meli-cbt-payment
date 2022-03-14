package com.meli.cbt.paymentapi.exception;

import com.meli.cbt.paymentapi.constants.Constants;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidValueException extends MeliException {
    public InvalidValueException() {
        super(HttpStatus.BAD_REQUEST, Constants.ERROR_PAYMENT_REQUEST_INVALID_VALUE, Constants.ERROR_PAYMENT_REQUEST_INVALID_VALUE_MESSAGE);
    }
}
