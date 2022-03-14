package com.meli.cbt.paymentapi.client;

import com.meli.cbt.paymentapi.constants.Constants;
import com.meli.cbt.paymentapi.exception.ExternalClientException;
import com.meli.cbt.paymentapi.exception.MeliException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
public abstract class BaseClient {

    protected Mono<MeliException> handle4xxError(final ClientResponse response) {
        return response
                .bodyToMono(String.class)
                .doOnSuccess(errorResponse -> log.error("## Erro na requisição ao client externo - statusCode: [{}] - response: {}", response.statusCode(), errorResponse))
                .flatMap(error -> Mono.error(new ExternalClientException(response.statusCode(), Constants.ERROR_CLIENT_REQUEST, error)));
    }

    protected Mono<MeliException> handle5xxError(final ClientResponse response) {
        return response
                .bodyToMono(String.class)
                .doOnSuccess(errorResponse -> log.error("## Erro interno no client externo - statusCode: [{}] - response: {}", response.statusCode(), errorResponse))
                .flatMap(errorResponse -> Mono.error(new ExternalClientException(response.statusCode(), Constants.ERROR_CLIENT_INTERNAL, errorResponse)));
    }
}
