CREATE TABLE IF NOT EXISTS tb_transaction (
    id              BIGINT      PRIMARY KEY AUTO_INCREMENT,
    transaction_id   VARCHAR(36) NOT NULL,
    type            VARCHAR(12) NOT NULL,
    amount          FLOAT       NOT NULL,
    account_id      INTEGER     NOT NULL,
    currency        VARCHAR(3)  NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_payment (
    id                      BIGINT      PRIMARY KEY AUTO_INCREMENT,
    debit_transaction_id    VARCHAR(36) NOT NULL,
    credit_transaction_id   VARCHAR(36) NOT NULL,
    amount_in_usd           FLOAT       NOT NULL,
    status                  VARCHAR(36) NOT NULL,
    usd_rate                FLOAT       DEFAULT NULL,
    rate_recovered_date     VARCHAR(48) DEFAULT NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_exchange_rate (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    rate        FLOAT  NOT NULL,
    consulted_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_transaction_id ON tb_transaction(transaction_id);
ALTER TABLE tb_payment ADD CONSTRAINT FOREIGN KEY(debit_transaction_id) REFERENCES tb_transaction(transaction_id);
ALTER TABLE tb_payment ADD CONSTRAINT FOREIGN KEY(credit_transaction_id) REFERENCES tb_transaction(transaction_id);