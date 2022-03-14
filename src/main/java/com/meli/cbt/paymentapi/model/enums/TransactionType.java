package com.meli.cbt.paymentapi.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    CREDIT("CREDIT"),
    DEBIT("DEBIT");



    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
