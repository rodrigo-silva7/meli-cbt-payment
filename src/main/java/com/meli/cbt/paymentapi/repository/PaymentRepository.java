package com.meli.cbt.paymentapi.repository;

import com.meli.cbt.paymentapi.model.entity.Payment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {
    @Query("SELECT * FROM tb_payment WHERE status = 'PROCESSED'")
    public Flux<Payment> findAllByStatusProcessed();
}
