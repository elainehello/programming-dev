drop table if exists customer;
CREATE TABLE IF NOT EXISTS customer (
id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL
);