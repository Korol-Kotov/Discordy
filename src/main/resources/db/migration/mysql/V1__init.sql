-- ===============================
-- Migration: Users table
-- ===============================

SET NAMES utf8mb4;
SET time_zone = '+00:00';

-- -------------------------------
-- users — таблица пользователей
-- -------------------------------
CREATE TABLE users
(
    unique_id   VARCHAR(36) NOT NULL,
    name        VARCHAR(64) NOT NULL,
    discord_id  BIGINT      NOT NULL,
    minutes     INT         NOT NULL DEFAULT 0,
    rewarded_at TIMESTAMP NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (unique_id),

    INDEX       idx_users_discord (discord_id),
    INDEX       idx_users_rewarded (rewarded_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;