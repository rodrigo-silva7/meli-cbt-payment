package com.meli.cbt.paymentapi.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.dto.TransactionDTO;
import com.meli.cbt.paymentapi.model.enums.Currency;
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
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table("tb_transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private float amount;

    private int accountId;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime createdAt;


    public static Transaction from(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .amount(transactionDTO.getAmount())
                .accountId(transactionDTO.getAccountId())
                .currency(transactionDTO.getCurrency())
                .type(transactionDTO.getType())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("transactionId", transactionId)
                .append("amount", amount)
                .append("accountId", accountId)
                .append("currency", currency)
                .append("type", type)
                .append("createdAt", createdAt)
                .toString();
    }

}
