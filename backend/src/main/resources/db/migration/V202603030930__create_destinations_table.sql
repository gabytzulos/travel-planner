CREATE TABLE destinations
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    city    VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,

    CONSTRAINT uq_destination_city_country UNIQUE (city, country)
);

CREATE TABLE trip_destinations
(
    trip_id        UUID NOT NULL,
    destination_id UUID NOT NULL,
    position       INT,

    CONSTRAINT pk_trip_destinations
        PRIMARY KEY (trip_id, destination_id),

    CONSTRAINT fk_td_trip
        FOREIGN KEY (trip_id)
            REFERENCES trips (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_td_destination
        FOREIGN KEY (destination_id)
            REFERENCES destinations (id)
            ON DELETE CASCADE
);