CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users
(
    id             UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    email          VARCHAR(254) NOT NULL UNIQUE,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    email_verified BOOLEAN,
    enabled        BOOLEAN,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);