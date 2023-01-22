-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE statistic_request
(
    id    varchar(255) primary key,
    date  timestamp with time zone not null,
    secid varchar(255)             not null,
    price decimal                  not null
);