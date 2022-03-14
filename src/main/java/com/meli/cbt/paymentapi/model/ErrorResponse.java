package com.meli.cbt.paymentapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    public String error;
    public String message;

}
