CREATE TABLE IF NOT EXISTS persons(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE
);