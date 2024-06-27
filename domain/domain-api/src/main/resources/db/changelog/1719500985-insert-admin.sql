--liquibase formatted sql

--changeset miaskor:3
INSERT INTO client(blocked, bot_id, email, login, password, type)
VALUES (false, null, 'admin@admin.com', 'admin', 'admin', 'ADMIN');