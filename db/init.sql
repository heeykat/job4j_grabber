CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    title TEXT,
    link TEXT UNIQUE,
    description TEXT,
    created TIMESTAMP WITHOUT TIME ZONE
);
