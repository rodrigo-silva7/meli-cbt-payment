package com.meli.cbt.paymentapi.controller;

import com.meli.cbt.paymentapi.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(Constants.VERSION_1_PATH + Constants.TRANSACTION_CONTEXT_PATH)
public class TransactionController {
}
