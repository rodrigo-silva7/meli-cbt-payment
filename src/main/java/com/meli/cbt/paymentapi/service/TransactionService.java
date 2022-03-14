package com.meli.cbt.paymentapi.service;

import com.meli.cbt.paymentapi.model.dto.TransactionDTO;
import com.meli.cbt.paymentapi.model.entity.Transaction;
import com.meli.cbt.paymentapi.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    TransactionRepository repository;

    public Flux<Transaction> createTransactions(TransactionDTO creditTransaction ,TransactionDTO debitTransaction) {
        log.info("Iniciando registro de transações - debito: {} - credito: {}", debitTransaction,creditTransaction);
        return repository.saveAll(Flux.just(Transaction.from(creditTransaction), Transaction.from(debitTransaction)))
                .flatMap(tr -> {
                    log.info("Transação registrada - {}", tr);
                    return Mono.just(tr);
                });
    }

    public Flux<Transaction> getTransactions(List<String> transactions) {
        log.info("Iniciando busca de transações - id: [{}]", transactions);
        return repository.findByTransactionIdIn(transactions)
                .flatMap(tr -> {
                    log.info("Transação encontrada - {}", tr);
                    return Mono.just(tr);
                });
    }

}
