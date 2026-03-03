CREATE TABLE trips
(
    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    user_id    UUID         NOT NULL,
    title      VARCHAR(200) NOT NULL,
    start_date DATE,
    end_date   DATE,
    notes      TEXT,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_trips_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
)