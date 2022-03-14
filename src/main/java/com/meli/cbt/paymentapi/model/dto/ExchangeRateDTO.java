package com.meli.cbt.paymentapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDTO {
    private float cotacaoCompra;
    private float cotacaoVenda;
    private String dataHoraCotacao;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("buyRate", cotacaoCompra)
                .append("saleRate", cotacaoVenda)
                .append("consultedAt", dataHoraCotacao)
                .toString();
    }
}
