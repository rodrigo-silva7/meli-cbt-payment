package com.meli.cbt.paymentapi.exception;

import com.meli.cbt.paymentapi.constants.Constants;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentNotFoundException extends MeliException {
    public PaymentNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constants.ERROR_PAYMENT_NOT_FOUND,Constants.ERROR_PAYMENT_NOT_FOUND_MESSAGE);
    }
}
