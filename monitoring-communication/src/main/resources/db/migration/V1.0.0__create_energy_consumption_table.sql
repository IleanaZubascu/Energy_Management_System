CREATE TABLE energy_consumption (

    id SERIAL PRIMARY KEY,
    device_id INTEGER NOT NULL,
    timestamp  BIGINT NOT NULL,
    hourly_consumption DECIMAL(10, 2) NOT NULL
);