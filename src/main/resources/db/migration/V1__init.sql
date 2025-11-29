create table crypto_currency
(
    id          bigserial primary key,
    external_id integer     not null,
    symbol      varchar(32) not null,
    price_usd   decimal     not null
);

create table notify_client
(
    id       bigserial primary key,
    username varchar(128) not null,
    symbol   varchar(32)  not null,
    price    decimal      not null
);
