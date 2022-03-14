package com.meli.cbt.paymentapi.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table("tb_exchange_rate")
public class ExchangeRate {
    private float rate;
    private LocalDateTime consultedAt;
}
