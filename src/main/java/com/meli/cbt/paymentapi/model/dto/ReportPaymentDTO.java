package com.meli.cbt.paymentapi.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.entity.Payment;
import com.meli.cbt.paymentapi.model.entity.Transaction;
import com.meli.cbt.paymentapi.model.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportPaymentDTO {

    private Long paymentId;
    private String creditTransactionId;
    private float BRLamount;
    private float USDAmount;
    private float sellerAccountId;
    private float rate;
    private LocalDateTime processingDate;

    public static ReportPaymentDTO from(Payment payment, List<Transaction> transactions) {
        Transaction creditTransaction = transactions.stream().filter(tr -> tr.getType().equals(TransactionType.CREDIT)).findFirst().orElse(null);
        Transaction debitTransaction = transactions.stream().filter(tr -> tr.getType().equals(TransactionType.DEBIT)).findFirst().orElse(null);

        return ReportPaymentDTO.builder()
                .paymentId(payment.getId())
                .creditTransactionId(creditTransaction.getTransactionId())
                .BRLamount(debitTransaction.getAmount())
                .USDAmount(creditTransaction.getAmount())
                .sellerAccountId(creditTransaction.getAccountId())
                .rate(payment.getUsdRate())
                .processingDate(payment.getUpdatedAt())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("paymentId", paymentId)
                .append("creditTransactionId", creditTransactionId)
                .append("BRLAmount", BRLamount)
                .append("USDAmount", USDAmount)
                .append("sellerAccountId", sellerAccountId)
                .append("rate", rate)
                .append("processingDate", processingDate)
                .toString();
    }
}
