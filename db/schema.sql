CREATE TABLE IF NOT EXISTS post (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS candidate (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS regUser (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT CONSTRAINT user_email_key UNIQUE,
    password TEXT
);