CREATE TABLE balances
(
    `account_id`        BIGINT          NOT NULL    PRIMARY KEY,
    `balance`           DECIMAL(15,2)   NOT NULL
)
ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;
