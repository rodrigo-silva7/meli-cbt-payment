package com.meli.cbt.paymentapi.config.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient(final WebClient.Builder builder, final ClientHttpConnector httpClient) {
        return builder.clientConnector(httpClient)
            .filter((request, next) -> {
                log.info("Requisição externa - [{}] - [{}]", request.method(),request.url().getPath());
                return next.exchange(request)
                    .doOnSuccess(response -> log.info("Resposta da requisição externa - [{}]", response.rawStatusCode()));
            })
            .build();
    }
}
