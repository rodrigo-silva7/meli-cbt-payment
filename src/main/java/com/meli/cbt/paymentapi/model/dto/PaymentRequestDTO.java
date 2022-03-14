package com.meli.cbt.paymentapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequestDTO {

    @Valid
    @NotNull
    private TransactionDTO debitTransaction;
    @Valid
    @NotNull
    private TransactionDTO creditTransaction;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("debitTransaction", debitTransaction)
                .append("creditTransaction", creditTransaction)
                .toString();
    }
}
