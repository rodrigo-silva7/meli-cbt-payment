package com.meli.cbt.paymentapi.repository;

import com.meli.cbt.paymentapi.model.entity.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String> {

    //@Query("SELECT * FROM tb_transaction WHERE transaction_id IN({transactions})")
    Flux<Transaction> findByTransactionIdIn(List<String> transactions);
}
