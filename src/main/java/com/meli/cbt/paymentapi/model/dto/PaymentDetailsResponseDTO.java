package com.meli.cbt.paymentapi.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.entity.Payment;
import com.meli.cbt.paymentapi.model.entity.Transaction;
import com.meli.cbt.paymentapi.model.enums.TransactionType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDetailsResponseDTO extends PaymentResponseDTO {

    private Transaction debitTransaction;
    private Transaction creditTransaction;
    private float amountInUsd;
    private LocalDateTime updatedAt;

    public static PaymentDetailsResponseDTO from(Payment payment, List<Transaction> transactions) {
        Optional<Transaction> creditTransaction = transactions.stream().filter(tr -> tr.getType().equals(TransactionType.CREDIT)).findFirst();
        Optional<Transaction> debitTransaction = transactions.stream().filter(tr -> tr.getType().equals(TransactionType.DEBIT)).findFirst();

        return PaymentDetailsResponseDTO.builder()
            .id(payment.getId())
            .status(payment.getStatus())
            .createdAt(payment.getCreatedAt())
            .debitTransaction(debitTransaction.orElse(null))
            .creditTransaction(creditTransaction.orElse(null))
            .amountInUsd(payment.getAmountInUsd())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", super.getId())
                .append("debitTransaction", debitTransaction)
                .append("creditTransaction", creditTransaction)
                .append("amountInUsd", amountInUsd)
                .append("status", super.getStatus())
                .append("createdAt", super.getCreatedAt())
                .append("updatedAt", updatedAt)
                .toString();
    }
}
