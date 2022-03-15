package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.model.dto.TransactionDTO;
import com.meli.cbt.paymentapi.model.entity.Transaction;
import com.meli.cbt.paymentapi.model.enums.Currency;
import com.meli.cbt.paymentapi.model.enums.TransactionType;
import com.meli.cbt.paymentapi.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTest {

    @Mock
    TransactionRepository repository;

    @InjectMocks
    TransactionService service;

    @Test
    @DisplayName("Deve criar uma transação com sucesso")
    void createTransactions() {
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
                .accountId(112345)
                .build();

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(repository).saveAll(any(Flux.class));

        StepVerifier.create(service.createTransactions(credit,debit))
                .consumeNextWith(t -> {
                })
                .consumeNextWith(t -> {
                })
                .verifyComplete();

        verify(repository).saveAll(any(Flux.class));
    }

    @Test
    @DisplayName("Deve buscar duas transações com sucesso")
    void getTransactions() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        List<String> transactions = List.of(uuid1,uuid2);

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
                .accountId(112345)
                .build();

        doReturn(Flux.just(Transaction.from(credit),Transaction.from(debit)))
                .when(repository).findByTransactionIdIn(transactions);

        StepVerifier.create(service.getTransactions(transactions))
                .consumeNextWith(t -> {})
                .consumeNextWith(t -> {
                })
                .verifyComplete();

        verify(repository).findByTransactionIdIn(transactions);
    }
}