CREATE TABLE "user"
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    password         VARCHAR(60) NOT NULL,
    authority         VARCHAR(50),
    CONSTRAINT ux_user_login UNIQUE (name)
);