package com.meli.cbt.paymentapi.model.dto;

import com.meli.cbt.paymentapi.model.entity.Payment;
import com.meli.cbt.paymentapi.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    public static PaymentResponseDTO from(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("paymentId", id)
                .append("status", status)
                .append("createdAt", createdAt)
                .toString();
    }
}
