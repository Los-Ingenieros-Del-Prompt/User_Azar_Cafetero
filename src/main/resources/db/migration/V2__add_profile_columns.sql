-- V2__add_profile_columns.sql
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS username VARCHAR(20) UNIQUE,
    ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS last_name_changed_at TIMESTAMP;

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username ON users(username);