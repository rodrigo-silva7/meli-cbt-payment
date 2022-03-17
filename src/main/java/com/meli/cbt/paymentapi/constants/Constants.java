package com.meli.cbt.paymentapi.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public final String VERSION_1_PATH = "/v1";

    public final String PAYMENT_CONTEXT_PATH = "/payment";
    public final String TRANSACTION_CONTEXT_PATH = "/transaction";

    public final String ERROR_PAYMENT_REQUEST_SAME_ACCOUNT_ID = "error.request.invalid_account_id";
    public final String ERROR_PAYMENT_REQUEST_INVALID_VALUE = "error.request.invalid_value";
    public final String ERROR_PAYMENT_REQUEST_INVALID_CURRENCY = "error.request.invalid_currency";
    public final String ERROR_PAYMENT_REQUEST_INVALID_INPUT = "error.request.invalid_request";
    public final String ERROR_PAYMENT_NOT_FOUND = "error.payment.not_found";
    public final String ERROR_CLIENT_INTERNAL= "error.client.internal";
    public final String ERROR_CLIENT_REQUEST = "error.client.request";
    public final String ERROR_INTERNAL = "error.internal";

    public final String ERROR_PAYMENT_REQUEST_SAME_ACCOUNT_ID_MESSAGE = "Operações de débito e crédito realizadas a mesma conta! Pagamento não permitido.";
    public final String ERROR_PAYMENT_REQUEST_INVALID_VALUE_MESSAGE = "Valor de transação inválido! O valor deve ser maior que zero.";
    public final String ERROR_PAYMENT_REQUEST_INVALID_CURRENCY_MESSAGE = "Moeda de transação inválida! A moeda deve ser BRL para operações de débito e USD para operações de crédito.";
    public final String ERROR_PAYMENT_REQUEST_INVALID_INPUT_MESSAGE = "Dados inválidos.";
    public final String ERROR_PAYMENT_NOT_FOUND_MESSAGE = "Pagamento não encontrado.";
    public final String ERROR_INTERNAL_MESSAGE = "Ocorreu um erro interno.";


}
