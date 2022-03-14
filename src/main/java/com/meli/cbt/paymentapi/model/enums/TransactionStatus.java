package com.meli.cbt.paymentapi.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionStatus {
    PENDING("PENDING"),
    AWAITING_NEW_CURRENCY_EVALUATION("AWAITING_NEW_CURRENCY_EVALUATION"),
    PROCESSED("PROCESSED");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
