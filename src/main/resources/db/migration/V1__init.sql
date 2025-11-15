create table crypto_currency
(
    id        bigserial primary key,
    symbol    varchar(32) not null,
    price_usd decimal
);

create table notify_client
(
    id       bigserial primary key,
    username varchar(128),
    symbol   varchar(32),
    price    decimal
);

insert into crypto_currency
values (90, 'BTC', 32000),
       (80, 'ETH', 4000),
       (48543, 'SOL', 200);
