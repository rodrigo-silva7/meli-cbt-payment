package com.meli.cbt.paymentapi.exception;

import com.meli.cbt.paymentapi.constants.Constants;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SameAccountIdException extends MeliException {
    public SameAccountIdException() {
        super(
                HttpStatus.BAD_REQUEST,
                Constants.ERROR_PAYMENT_REQUEST_SAME_ACCOUNT_ID,
                Constants.ERROR_PAYMENT_REQUEST_SAME_ACCOUNT_ID_MESSAGE);
    }
}
