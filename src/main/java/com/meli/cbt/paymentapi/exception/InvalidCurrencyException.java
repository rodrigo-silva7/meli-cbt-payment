package com.meli.cbt.paymentapi.exception;

import com.meli.cbt.paymentapi.constants.Constants;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidCurrencyException extends MeliException {
    public InvalidCurrencyException() {
        super(
            HttpStatus.BAD_REQUEST,
            Constants.ERROR_PAYMENT_REQUEST_INVALID_CURRENCY,
            Constants.ERROR_PAYMENT_REQUEST_INVALID_CURRENCY_MESSAGE);

    }
}
