-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE statistic_request
(
    id    uuid primary key            not null,
    date  timestamp with time zone not null,
    secid varchar(255)                not null,
    price varchar(255)
);