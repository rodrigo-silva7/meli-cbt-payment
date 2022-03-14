package com.meli.cbt.paymentapi.client;

import com.meli.cbt.paymentapi.config.properties.BCBProperties;
import com.meli.cbt.paymentapi.model.dto.BCBExchangeRateResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@AllArgsConstructor
@Component
public class BCClient extends BaseClient {

    private final WebClient client;
    private BCBProperties configurationProperties;

    public Mono<BCBExchangeRateResponseDTO> getExchangeRate() {
        return client
                .get()
                .uri(buildExchangeRateQueryForPresentDay())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handle4xxError)
                .onStatus(HttpStatus::is5xxServerError, this::handle5xxError)
                .bodyToMono(BCBExchangeRateResponseDTO.class);
    }

    private URI buildExchangeRateQueryForPresentDay() {
        return URI.create(configurationProperties.getUrl()
                + "CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao=%27"
                + findLastValidDay()
                +"%27&$top=100&$format=json");
    }

    private String findLastValidDay() {
        LocalDate presentDay = LocalDate.now();
        if(presentDay.getDayOfWeek().equals(DayOfWeek.SATURDAY))
            return LocalDate.now().minusDays(1).format(getDateFormatter());
        if(presentDay.getDayOfWeek().equals(DayOfWeek.SUNDAY))
            return LocalDate.now().minusDays(2).format(getDateFormatter());
        return presentDay.format(getDateFormatter());
    }

    private DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }
}



