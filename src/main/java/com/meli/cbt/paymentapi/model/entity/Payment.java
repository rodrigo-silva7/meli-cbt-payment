package com.meli.cbt.paymentapi.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.dto.PaymentRequestDTO;
import com.meli.cbt.paymentapi.model.enums.PaymentStatus;
import com.meli.cbt.paymentapi.model.enums.TransactionType;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table("tb_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String debitTransactionId;

    private String creditTransactionId;

    private float amountInUsd;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private float usdRate;

    private String rateRecoveredDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Payment from(PaymentRequestDTO paymentRequestDTO, List<Transaction> transactions) {
        Optional<Transaction> creditTransaction = transactions.stream().filter(tr -> tr.getType().equals(TransactionType.CREDIT)).findFirst();
        Optional<Transaction> debitTransaction = transactions.stream().filter(tr -> tr.getType().equals(TransactionType.DEBIT)).findFirst();

        return Payment.builder()
                .amountInUsd(paymentRequestDTO.getCreditTransaction().getAmount())
                .creditTransactionId(creditTransaction.map(Transaction::getTransactionId).orElse(null))
                .debitTransactionId(debitTransaction.map(Transaction::getTransactionId).orElse(null))
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("debitTransactionId", debitTransactionId)
                .append("creditTransactionId", creditTransactionId)
                .append("amountInUsd", amountInUsd)
                .append("status", status)
                .append("usdRate", usdRate)
                .append("rateRecoveredDate", rateRecoveredDate)
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .toString();
    }
}

