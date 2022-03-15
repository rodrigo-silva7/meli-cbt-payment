package com.meli.cbt.paymentapi.repository;

import com.meli.cbt.paymentapi.model.entity.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long> {
}
