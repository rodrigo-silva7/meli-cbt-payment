package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.exception.InvalidCurrencyException;
import com.meli.cbt.paymentapi.exception.InvalidValueException;
import com.meli.cbt.paymentapi.exception.PaymentNotFoundException;
import com.meli.cbt.paymentapi.exception.SameAccountIdException;
import com.meli.cbt.paymentapi.model.ExchangeRateResponse;
import com.meli.cbt.paymentapi.model.dto.PaymentDetailsResponseDTO;
import com.meli.cbt.paymentapi.model.dto.PaymentRequestDTO;
import com.meli.cbt.paymentapi.model.dto.PaymentResponseDTO;
import com.meli.cbt.paymentapi.model.dto.ReportPaymentDTO;
import com.meli.cbt.paymentapi.model.entity.Payment;
import com.meli.cbt.paymentapi.model.entity.Transaction;
import com.meli.cbt.paymentapi.model.enums.Currency;
import com.meli.cbt.paymentapi.model.enums.PaymentStatus;
import com.meli.cbt.paymentapi.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentService {

    TransactionService transactionService;
    ExchangeRateService exchangeRateService;

    PaymentRepository repository;

    public Mono<PaymentResponseDTO> createPayment(PaymentRequestDTO paymentRequest) {
        return validatePayment(paymentRequest)
            .flatMap(p -> transactionService.createTransactions(p.getCreditTransaction(),p.getDebitTransaction())
                .collectList()
                .flatMap(transactions -> repository.save(Payment.from(paymentRequest,transactions)))
                .flatMap(payment -> {
                    log.info("Pagamento registrado - {}", payment);
                    return Mono.just(PaymentResponseDTO.from(payment));
                    }
                )
            );
    }

    public Mono<PaymentDetailsResponseDTO> getPayment(Long paymentId) {
        return repository.findById(paymentId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Pagamento não encontrado - id: [" + paymentId + "]")))
            .flatMap(this::buildPaymentWithTransactions);
    }

    public Mono<PaymentResponseDTO> capturePayment(Long paymentId) {
        return repository.findById(paymentId)
            .switchIfEmpty(Mono.error(new PaymentNotFoundException()))
            .flatMap(payment -> {
                if(payment.getStatus().equals(PaymentStatus.PROCESSED)
                || payment.getStatus().equals(PaymentStatus.FINAL))
                    return buildPaymentWithTransactions(payment);

                return buildPaymentWithTransactions(payment)
                    .flatMap(paymentResponse -> exchangeRateService.validateExchangeRate(
                        paymentResponse.getCreditTransaction().getAmount(),
                        paymentResponse.getDebitTransaction().getAmount())
                    .flatMap(rateResponse -> rateResponse.isAcceptableRate() ?
                            processPayment(payment, rateResponse)
                            : delayPayment(payment)));
                }
            );
    }

    /*public Flux<PaymentDetailsResponseDTO> finishProcessedPayments() {
        List<String> trList = new ArrayList<>();
        return repository.findAllByStatusProcessed()
            .flatMapSequential(payment -> {
                trList.add(payment.getDebitTransactionId());
                trList.add(payment.getCreditTransactionId());
                return Mono.just(payment);
            })
            .collectList()
            .flatMapMany(this::finishAllPayments)
            .collectList()
            .flatMap(list -> Mono.zip(Mono.just(list),transactionService.getTransactions(trList).collectList()))
            .flatMapMany(PaymentService::buildFinishedPaymentsWithTransactions);
    }*/

    public Mono<List<ReportPaymentDTO>> finishProcessedPayments() {
        return repository.findAllByStatusProcessed()
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Não há pagamentos para finalizar.")))
            .collectList()
            .flatMap(payments -> {
                List<String> transactionIdList = new ArrayList<>();
                payments.forEach(p -> {
                    transactionIdList.add(p.getCreditTransactionId());
                    transactionIdList.add(p.getDebitTransactionId());
                });
                return Mono.zip(transactionService.getTransactions(transactionIdList).collectList(), Mono.just(payments))
                    .flatMap(tuple -> {
                        List<Payment> paymentsList = tuple.getT2();
                        List<Transaction> transactions = tuple.getT1();
                        List<ReportPaymentDTO> report = new ArrayList<>();

                        for (Payment p : paymentsList)
                            report.add(ReportPaymentDTO.from(p, transactions));

                        return finishAllPayments(payments)
                            .collectList()
                            .flatMap(response -> {
                                log.info("Pagamentos finalizados - {}", response);
                                return Mono.just(report);
                            });
                    });
            });
    }

    /*private static Publisher<? extends PaymentDetailsResponseDTO> buildFinishedPaymentsWithTransactions(Tuple2<List<Payment>,List<Transaction>> tuple) {
        List<Payment> payments = tuple.getT1();
        List<Transaction> transactions = tuple.getT2();
        List<PaymentDetailsResponseDTO> response = new ArrayList<>();

        for (Payment p : payments) {
            Transaction debit = transactions.stream().filter(t -> t.getTransactionId().equals(p.getDebitTransactionId())).findFirst().get();
            Transaction credit = transactions.stream().filter(t -> t.getTransactionId().equals(p.getCreditTransactionId())).findFirst().get();
            response.add(PaymentDetailsResponseDTO.from(p, List.of(debit, credit)));
        }

        log.info("Pagamentos finalizados - {}", response);
        return Flux.fromIterable(response);
    }*/

    private Mono<PaymentDetailsResponseDTO> buildPaymentWithTransactions(Payment payment) {
        return transactionService.getTransactions(List.of(payment.getDebitTransactionId(), payment.getCreditTransactionId()))
            .collectList()
            .flatMap(transactions -> Mono.just(PaymentDetailsResponseDTO.from(payment,transactions)));
    }

    private Mono<PaymentRequestDTO> validatePayment(PaymentRequestDTO payment) {
        if(payment.getCreditTransaction().getAccountId().equals(payment.getDebitTransaction().getAccountId()))
            throw new SameAccountIdException();

        if(!payment.getCreditTransaction().getCurrency().equals(Currency.USD)
        || !payment.getDebitTransaction().getCurrency().equals(Currency.BRL))
            throw new InvalidCurrencyException();

        if(payment.getDebitTransaction().getAmount() <= 0
        || payment.getCreditTransaction().getAmount() <= 0)
            throw new InvalidValueException();

        return Mono.just(payment);
    }

    private Mono<PaymentResponseDTO> processPayment(Payment payment, ExchangeRateResponse rateResponse) {
        payment.setUsdRate(rateResponse.getRate());
        payment.setRateRecoveredDate(rateResponse.getDate());
        payment.setStatus(PaymentStatus.PROCESSED);

        return repository.save(payment)
            .map(PaymentResponseDTO::from);
    }

    private Mono<PaymentResponseDTO> delayPayment(Payment payment) {
        payment.setStatus(PaymentStatus.AWAITING_NEW_CURRENCY_EVALUATION);
        return repository.save(payment)
            .map(PaymentResponseDTO::from);
    }

    /*private Mono<PaymentResponseDTO> finishPayment(Payment payment) {
        payment.setStatus(PaymentStatus.FINAL);
        return repository.save(payment)
            .map(PaymentResponseDTO::from);
    }*/

    private Flux<Payment> finishAllPayments(List<Payment> payments) {
        payments.forEach(p -> p.setStatus(PaymentStatus.FINAL));
        return repository.saveAll(Flux.fromIterable(payments));
    }
}
