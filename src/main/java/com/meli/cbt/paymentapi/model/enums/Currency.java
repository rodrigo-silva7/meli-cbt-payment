package com.meli.cbt.paymentapi.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    BRL("BRL"),
    USD("USD");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
