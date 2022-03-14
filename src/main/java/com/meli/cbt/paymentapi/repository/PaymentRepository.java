package com.meli.cbt.paymentapi.repository;

import com.meli.cbt.paymentapi.model.entity.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {
}
