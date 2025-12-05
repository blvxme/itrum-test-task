create table if not exists wallets
(
    id      uuid primary key,
    balance decimal(15, 2) not null
);
