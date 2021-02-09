CREATE TABLE account
(
    id             VARCHAR(36),
    name           VARCHAR(200),
    balance        INTEGER,
    pin            VARCHAR(6),
    account_number VARCHAR(6),
    CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE balance_history
(
    id VARCHAR(36),
    current_balance INTEGER,
    credited_balance INTEGER,
    debited_balance INTEGER,
    source_id VARCHAR(36),
    CONSTRAINT pk_balance_history PRIMARY KEY (id)
);

