CREATE TABLE file_inbox (
    id BIGSERIAL PRIMARY KEY,
    idempotent_key VARCHAR(255) UNIQUE NOT NULL,
    payload TEXT NOT NULL,
    occured_on TIMESTAMP,
    processed_date TIMESTAMP,
    status VARCHAR(30) NOT NULL 
);