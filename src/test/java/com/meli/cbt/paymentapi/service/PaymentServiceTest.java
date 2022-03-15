package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.exception.InvalidCurrencyException;
import com.meli.cbt.paymentapi.exception.InvalidValueException;
import com.meli.cbt.paymentapi.exception.PaymentNotFoundException;
import com.meli.cbt.paymentapi.exception.SameAccountIdException;
import com.meli.cbt.paymentapi.model.ExchangeRateResponse;
import com.meli.cbt.paymentapi.model.dto.PaymentDetailsResponseDTO;
import com.meli.cbt.paymentapi.model.dto.PaymentRequestDTO;
import com.meli.cbt.paymentapi.model.dto.TransactionDTO;
import com.meli.cbt.paymentapi.model.entity.Payment;
import com.meli.cbt.paymentapi.model.entity.Transaction;
import com.meli.cbt.paymentapi.model.enums.Currency;
import com.meli.cbt.paymentapi.model.enums.PaymentStatus;
import com.meli.cbt.paymentapi.model.enums.TransactionType;
import com.meli.cbt.paymentapi.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentServiceTest {

    @Mock
    TransactionService transactionService;
    @Mock
    ExchangeRateService exchangeRateService;
    @Mock
    PaymentRepository repository;

    @InjectMocks
    PaymentService service;

    @Test
    @DisplayName("Deve criar um pagamento com sucesso")
    void createPayment_SUCESS() {
        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112345)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .creditTransaction(credit)
                .debitTransaction(debit)
                .build();

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).createTransactions(credit,debit);

        doReturn(Mono.just(Payment.from(request, List.of(Transaction.from(credit),Transaction.from(debit)))))
                .when(repository).save(any(Payment.class));

        StepVerifier.create(service.createPayment(request))
                .consumeNextWith(t -> {
                    Assertions.assertEquals(PaymentStatus.PENDING,t.getStatus());
                })
                .verifyComplete();

        verify(transactionService).createTransactions(any(),any());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao validar um pagamento - mesma conta")
    void validatePayment_SameAccount() {
        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112355)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .creditTransaction(credit)
                .debitTransaction(debit)
                .build();
        try {
            StepVerifier.create(service.validatePayment(request))
                    .verifyComplete();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof SameAccountIdException);
        }
    }

    @Test
    @DisplayName("Deve falhar ao validar um pagamento - moeda inválida")
    void validatePayment_InvalidCurrency() {
        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.USD)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .creditTransaction(credit)
                .debitTransaction(debit)
                .build();
        try {
            StepVerifier.create(service.validatePayment(request))
                    .verifyComplete();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidCurrencyException);
        }
    }

    @Test
    @DisplayName("Deve falhar ao validar um pagamento - amount negativo")
    void validatePayment_InvalidValue() {
        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(-23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .creditTransaction(credit)
                .debitTransaction(debit)
                .build();
        try {
            StepVerifier.create(service.validatePayment(request))
                    .verifyComplete();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof InvalidValueException);
        }
    }

    @Test
    @DisplayName("Deve buscar um pagamento - Sucesso")
    void getPayment() {
        Payment payment = Payment.builder()
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .debitTransactionId("")
                .creditTransactionId("")
                .amountInUsd(12.F)
                .id(1L)
                .rateRecoveredDate("")
                .usdRate(5.F)
                .build();

        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        doReturn(Mono.just(payment))
                .when(repository).findById(anyLong());

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).getTransactions(anyList());

        StepVerifier.create(service.getPayment(1L))
                .consumeNextWith(p -> {
                    Assertions.assertEquals(PaymentDetailsResponseDTO.from(payment, List.of(Transaction.from(credit),Transaction.from(debit))).getId(), p.getId());
                })
                .verifyComplete();

        verify(repository).findById(anyLong());
        verify(transactionService).getTransactions(anyList());

    }

    @Test
    @DisplayName("Deve não encontrar um pagamento e lançar exceção")
    void getPayment_NotFound() {

        doReturn(Mono.empty())
                .when(repository).findById(anyLong());

        StepVerifier.create(service.getPayment(1L))
                    .verifyError(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção para pagamento não encontrado")
    void capturePayment_NotFound() {
        doReturn(Mono.empty())
                .when(repository).findById(anyLong());

        StepVerifier.create(service.capturePayment(1L))
                .verifyError(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("Deve ser idempotente para um pagamento já processado")
    void capturePayment_Processed() {
        Payment payment = Payment.builder()
                .status(PaymentStatus.PROCESSED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .debitTransactionId("")
                .creditTransactionId("")
                .amountInUsd(12.F)
                .id(1L)
                .rateRecoveredDate("")
                .usdRate(5.F)
                .build();

        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        doReturn(Mono.just(payment))
                .when(repository).findById(anyLong());

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).getTransactions(anyList());

        StepVerifier.create(service.capturePayment(1L))
                .consumeNextWith(p -> {
                    Assertions.assertEquals(PaymentDetailsResponseDTO.from(payment, List.of(Transaction.from(credit),Transaction.from(debit))).getId(), p.getId());
                })
                .verifyComplete();

        verifyNoInteractions(exchangeRateService);
    }

    @Test
    @DisplayName("Deve ser idempotente para um pagamento já finalizado")
    void capturePayment_Final() {
        Payment payment = Payment.builder()
                .status(PaymentStatus.FINAL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .debitTransactionId("")
                .creditTransactionId("")
                .amountInUsd(12.F)
                .id(1L)
                .rateRecoveredDate("")
                .usdRate(5.F)
                .build();

        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        doReturn(Mono.just(payment))
                .when(repository).findById(anyLong());

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).getTransactions(anyList());

        StepVerifier.create(service.capturePayment(1L))
                .consumeNextWith(p -> {
                    Assertions.assertEquals(PaymentDetailsResponseDTO.from(payment, List.of(Transaction.from(credit),Transaction.from(debit))).getId(), p.getId());
                })
                .verifyComplete();

        verifyNoInteractions(exchangeRateService);
    }

    @Test
    @DisplayName("Deve atrasar um pagamento para uma taxa de câmbio não vantajosa")
    void capturePayment_Delay() {
        Payment payment = Payment.builder()
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .debitTransactionId("")
                .creditTransactionId("")
                .amountInUsd(12.F)
                .id(1L)
                .rateRecoveredDate("")
                .usdRate(5.F)
                .build();

        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        ExchangeRateResponse rate = ExchangeRateResponse.builder()
                .acceptableRate(false)
                .BRLAmount(1F)
                .USDAmount(2F)
                .date("")
                .rate(5.4F)
                .build();

        doReturn(Mono.just(payment))
                .when(repository).findById(anyLong());

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).getTransactions(anyList());

        doReturn(Mono.just(payment))
                .when(repository).save(any(Payment.class));

        doReturn(Mono.just(rate))
                .when(exchangeRateService).validateExchangeRate(credit.getAmount(), debit.getAmount());

        StepVerifier.create(service.capturePayment(1L))
                .consumeNextWith(p -> {
                    Assertions.assertEquals(PaymentStatus.AWAITING_NEW_CURRENCY_EVALUATION, p.getStatus());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Deve processar um pagamento para uma taxa de câmbio vantajosa")
    void capturePayment_Process() {
        Payment payment = Payment.builder()
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .debitTransactionId("")
                .creditTransactionId("")
                .amountInUsd(12.F)
                .id(1L)
                .rateRecoveredDate("")
                .usdRate(5.F)
                .build();

        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        ExchangeRateResponse rate = ExchangeRateResponse.builder()
                .acceptableRate(true)
                .BRLAmount(1F)
                .USDAmount(2F)
                .date("")
                .rate(5.4F)
                .build();

        doReturn(Mono.just(payment))
                .when(repository).findById(anyLong());

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).getTransactions(anyList());

        doReturn(Mono.just(payment))
                .when(repository).save(any(Payment.class));

        doReturn(Mono.just(rate))
                .when(exchangeRateService).validateExchangeRate(credit.getAmount(), debit.getAmount());

        StepVerifier.create(service.capturePayment(1L))
                .consumeNextWith(p -> {
                    Assertions.assertEquals(PaymentStatus.PROCESSED, p.getStatus());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Deve não encontrar pagamentos para finalizar")
    void finishProcessedPayments_NotFound() {
        doReturn(Flux.empty())
                .when(repository).findAllByStatusProcessed();

        StepVerifier.create(service.finishProcessedPayments())
                .verifyError(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("Deve finalizar pagamentos processados")
    void finishProcessedPayments_Sucess(){
        Payment payment = Payment.builder()
                .status(PaymentStatus.PROCESSED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .debitTransactionId("")
                .creditTransactionId("")
                .amountInUsd(12.F)
                .id(1L)
                .rateRecoveredDate("")
                .usdRate(5.F)
                .build();

        TransactionDTO debit = TransactionDTO.builder()
                .type(TransactionType.DEBIT)
                .amount(23.F)
                .currency(Currency.BRL)
                .accountId(112325)
                .build();

        TransactionDTO credit = TransactionDTO.builder()
                .type(TransactionType.CREDIT)
                .amount(10.F)
                .currency(Currency.USD)
                .accountId(112355)
                .build();

        doReturn(Flux.just(payment))
                .when(repository).findAllByStatusProcessed();

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(transactionService).getTransactions(anyList());

        doReturn(Flux.just(payment))
                .when(repository).saveAll(any(Flux.class));

        StepVerifier.create(service.finishProcessedPayments())
                .consumeNextWith(p -> {
                    Assertions.assertEquals(payment.getId(), p.get(0).getPaymentId());
                })
                .verifyComplete();
    }
}