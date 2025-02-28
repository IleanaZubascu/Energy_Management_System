CREATE TABLE device (
        id          SERIAL PRIMARY KEY,
        description VARCHAR(400),
        address     VARCHAR(255),
        max_energy  INTEGER,
        user_id     INTEGER
);