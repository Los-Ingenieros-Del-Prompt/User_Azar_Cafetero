-- V1__create_users_table.sql
CREATE TABLE IF NOT EXISTS users (
    id                  VARCHAR(36)  PRIMARY KEY,
    username            VARCHAR(20)  UNIQUE,
    avatar_url          VARCHAR(512),
    last_name_changed_at TIMESTAMP
);