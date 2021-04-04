CREATE TABLE IF NOT EXISTS configuration
(
    id    uuid    NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    key   varchar NOT NULL,
    value varchar,
    UNIQUE (key)
);
