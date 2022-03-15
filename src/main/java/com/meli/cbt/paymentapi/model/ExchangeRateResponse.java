package com.meli.cbt.paymentapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.cbt.paymentapi.model.dto.BCBExchangeRateResponseDTO;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRateResponse {

    private float USDAmount;
    private float BRLAmount;
    private float rate;
    private boolean acceptableRate;
    private String date;

    public static ExchangeRateResponse from(BCBExchangeRateResponseDTO exchangeRate, float USDAmount, float BRLAmount, boolean acceptableRate) {
        return ExchangeRateResponse.builder()
                .USDAmount(USDAmount)
                .BRLAmount(BRLAmount)
                .rate(exchangeRate.getValue().get(0).getCotacaoCompra())
                .acceptableRate(acceptableRate)
                .date(exchangeRate.getValue().get(0).getDataHoraCotacao())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("USDAmount", USDAmount)
                .append("BRLAmount", BRLAmount)
                .append("rate", rate)
                .append("acceptableRate", acceptableRate)
                .append("date", date)
                .toString();
    }
}
