package com.meli.cbt.paymentapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.enums.Currency;
import com.meli.cbt.paymentapi.model.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
    @NotNull(message = "Tipo da Transação não pode ser nulo.")
    private TransactionType type;
    private float amount;
    @NotNull(message = "Número da conta não pode ser nulo.")
    private Integer accountId;
    @NotNull(message = "Moeda não pode ser nula.")
    private Currency currency;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("type", type)
                .append("currency", currency)
                .append("amount", amount)
                .append("accountId", accountId)
                .toString();
    }
}
