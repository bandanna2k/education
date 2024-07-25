CREATE TABLE properties
(
    `key`       varchar(64)     PRIMARY KEY,
    `value`     varchar(256)
)
ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO properties (`key`, `value`) VALUES ("kafka.offset", "from-beginning");