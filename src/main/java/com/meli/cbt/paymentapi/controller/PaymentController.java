package com.meli.cbt.paymentapi.controller;

import com.meli.cbt.paymentapi.constants.Constants;
import com.meli.cbt.paymentapi.model.dto.PaymentDetailsResponseDTO;
import com.meli.cbt.paymentapi.model.dto.PaymentRequestDTO;
import com.meli.cbt.paymentapi.model.dto.PaymentResponseDTO;
import com.meli.cbt.paymentapi.model.dto.ReportPaymentDTO;
import com.meli.cbt.paymentapi.model.enums.PaymentStatus;
import com.meli.cbt.paymentapi.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@Tag(name = "Pagamentos", description="Gerencia os recursos de pagamento")
@RequestMapping(Constants.VERSION_1_PATH + Constants.PAYMENT_CONTEXT_PATH)
public class PaymentController {

    private PaymentService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaymentResponseDTO> createPayment(@RequestBody @Valid PaymentRequestDTO payment) {
        log.info("Iniciando a criação de um pagamento - {}", payment);
        return service.createPayment(payment)
            .doOnSuccess(paymentResponse -> log.info("Pagamento criado com sucesso - response: {}", paymentResponse));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaymentDetailsResponseDTO> getPayment(@PathVariable(value = "paymentId") Long paymentId) {
        log.info("Iniciando a busca por um pagamento - id: [{}]", paymentId);
        return service.getPayment(paymentId)
            .doOnSuccess(paymentResponse -> log.info("Pagamento encontrado com sucesso - response: {}", paymentResponse));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/{paymentId}/capture", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaymentResponseDTO> capturePayment(ServerHttpResponse response, @PathVariable(value = "paymentId") Long paymentId) {
        log.info("Iniciando o processamento de pagamento - id: [{}]", paymentId);
        return service.capturePayment(paymentId)
            .doOnSuccess(paymentResponse -> {
                if(paymentResponse.getStatus().equals(PaymentStatus.AWAITING_NEW_CURRENCY_EVALUATION)) {
                    log.info("Pagamento não processado, aguardando nova cotação - response: {}", paymentResponse);
                    response.setStatusCode(HttpStatus.ACCEPTED);
                } else
                    log.info("Pagamento processado com sucesso - response: {}", paymentResponse);
            });
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ReportPaymentDTO>> finishProcessedPayments(ServerHttpResponse response) {
        log.info("Iniciando a recuperação de pagamentos processados");
        return service.finishProcessedPayments();
    }
}
