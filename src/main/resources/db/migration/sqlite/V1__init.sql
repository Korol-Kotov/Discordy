-- ===============================
-- Migration: Users table (SQLite)
-- ===============================

PRAGMA foreign_keys = ON;

-- -------------------------------
-- users — таблица пользователей
-- -------------------------------
CREATE TABLE users
(
    unique_id   TEXT PRIMARY KEY,
    name        TEXT     NOT NULL,
    discord_id  INTEGER  NOT NULL,
    minutes     INTEGER  NOT NULL DEFAULT 0,
    rewarded_at DATETIME,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_discord
    ON users (discord_id);

CREATE INDEX idx_users_rewarded
    ON users (rewarded_at);