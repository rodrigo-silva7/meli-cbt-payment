package com.meli.cbt.paymentapi.controller;

import com.meli.cbt.paymentapi.constants.Constants;
import com.meli.cbt.paymentapi.model.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(Constants.VERSION_1_PATH + Constants.TRANSACTION_CONTEXT_PATH)
public class TransactionController {



}
