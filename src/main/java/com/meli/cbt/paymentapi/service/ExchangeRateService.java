package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.client.BCClient;
import com.meli.cbt.paymentapi.model.ExchangeRateResponse;
import com.meli.cbt.paymentapi.model.entity.ExchangeRate;
import com.meli.cbt.paymentapi.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class ExchangeRateService {

    private BCClient client;

    private ExchangeRateRepository repository;

    public Mono<ExchangeRateResponse> validateExchangeRate(float USDAmount, float BRLAmount) {
        return client.getExchangeRate()
            .map(e -> ExchangeRateResponse.from(e, USDAmount, BRLAmount,
    USDAmount * e.getValue().get(0).getCotacaoCompra() <= BRLAmount))
            .flatMap(rateResponse -> {
                log.info("Taxa de Câmbio encontrada - {}", rateResponse);
                return repository.save(ExchangeRate.from(rateResponse))
                        .flatMap(__ -> Mono.just(rateResponse));
            });
    }
}
