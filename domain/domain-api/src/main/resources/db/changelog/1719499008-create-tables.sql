--liquibase formatted sql

--changeset miaskor:1
create table client
(
    id       bigserial
        primary key,
    blocked  boolean not null,
    bot_id   bigint,
    email    varchar(255),
    login    varchar(255),
    password varchar(255),
    type     varchar(255)
);

--changeset miaskor:2
create table task
(
    id         bigserial
        primary key,
    date       date,
    task_name  varchar(255),
    task_state varchar(255),
    client_id  bigint not null
        constraint fk_task_client
            references client
);