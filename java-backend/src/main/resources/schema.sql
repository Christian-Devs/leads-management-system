CREATE TABLE IF NOT EXISTS leads (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    source VARCHAR(100),
    created_at TIMESTAMP NOT NULL
);
