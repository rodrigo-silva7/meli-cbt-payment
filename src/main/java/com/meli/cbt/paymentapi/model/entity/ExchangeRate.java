package com.meli.cbt.paymentapi.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.ExchangeRateResponse;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table("tb_exchange_rate")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float rate;
    private LocalDateTime consultedAt;

    public static ExchangeRate from(ExchangeRateResponse rateResponse) {
        return ExchangeRate.builder()
                .rate(rateResponse.getRate())
                .consultedAt(LocalDateTime.now())
                .build();
    }
}
