package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.client.BCClient;
import com.meli.cbt.paymentapi.model.dto.BCBExchangeRateResponseDTO;
import com.meli.cbt.paymentapi.model.dto.ExchangeRateDTO;
import com.meli.cbt.paymentapi.model.entity.ExchangeRate;
import com.meli.cbt.paymentapi.repository.ExchangeRateRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeRateServiceTest {

    @Mock
    BCClient client;
    @Mock
    ExchangeRateRepository repository;

    @InjectMocks
    ExchangeRateService service;

    BCBExchangeRateResponseDTO bcbExchangeRateResponseDTO;

    @BeforeAll
    void setup() {
        ExchangeRateDTO rate = new ExchangeRateDTO(5.F,5.F,"");
        bcbExchangeRateResponseDTO = new BCBExchangeRateResponseDTO(List.of(rate));
    }

    @Test
    @DisplayName("Deve validar uma taxa de câmbio de 5.0 para USD=2.0 e BRL=12.0")
    void validateExchangeRateScenario1() {

        doReturn(Mono.just(bcbExchangeRateResponseDTO))
                .when(client).getExchangeRate();
        doReturn(Mono.just(ExchangeRate.builder().build()))
                .when(repository).save(any(ExchangeRate.class));

        StepVerifier.create(service.validateExchangeRate(2.F,12.F))
                .consumeNextWith(response -> {
                    Assertions.assertTrue(response.isAcceptableRate());
                })
                .verifyComplete();

        verify(client).getExchangeRate();
        verify(repository).save(any(ExchangeRate.class));
    }

    @Test
    @DisplayName("Deve validar uma taxa de câmbio de 2.0 para USD=2 e BRL=10")
    void validateExchangeRateScenario2() {
        doReturn(Mono.just(bcbExchangeRateResponseDTO))
                .when(client).getExchangeRate();
        doReturn(Mono.just(ExchangeRate.builder().build()))
                .when(repository).save(any(ExchangeRate.class));

        StepVerifier.create(service.validateExchangeRate(2.F,10.F))
                .consumeNextWith(response -> {
                    Assertions.assertTrue(response.isAcceptableRate());
                })
                .verifyComplete();

        verify(client).getExchangeRate();
        verify(repository).save(any(ExchangeRate.class));
    }

    @Test
    @DisplayName("Deve validar uma taxa de câmbio de 2.0 para USD=3 e BRL=6")
    void validateExchangeRateScenario3() {

        doReturn(Mono.just(bcbExchangeRateResponseDTO))
                .when(client).getExchangeRate();
        doReturn(Mono.just(ExchangeRate.builder().build()))
                .when(repository).save(any(ExchangeRate.class));

        StepVerifier.create(service.validateExchangeRate(3.F,6.F))
                .consumeNextWith(response -> {
                    Assertions.assertFalse(response.isAcceptableRate());
                })
                .verifyComplete();

        verify(client).getExchangeRate();
        verify(repository).save(any(ExchangeRate.class));
    }
    @Test
    @DisplayName("Deve validar uma taxa de câmbio de 2.0 para USD=10.0 e BRL=")
    void validateExchangeRateScenario4() {

        doReturn(Mono.just(bcbExchangeRateResponseDTO))
                .when(client).getExchangeRate();
        doReturn(Mono.just(ExchangeRate.builder().build()))
                .when(repository).save(any(ExchangeRate.class));

        StepVerifier.create(service.validateExchangeRate(10.F,1.F))
                .consumeNextWith(response -> {
                    Assertions.assertFalse(response.isAcceptableRate());
                })
                .verifyComplete();

        verify(client).getExchangeRate();
        verify(repository).save(any(ExchangeRate.class));
    }
}