package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.client.BCClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class ExchangeRateService {

    private BCClient client;

    public Mono<Boolean> validateExchangeRate(float USDAmount, float BRLAmount) {
        return client.getExchangeRate()
                .map(exchangeRate -> {
                    log.info("Taxa de Câmbio encontrada - {}", exchangeRate.getValue());
                    return USDAmount * exchangeRate.getValue().get(0).getCotacaoCompra() <= BRLAmount;
                });
    }
}


