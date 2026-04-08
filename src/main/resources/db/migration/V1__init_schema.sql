-- MSSQL Compatible Migration

-- 1. Users Table
CREATE TABLE users (
                       id BIGINT IDENTITY(1,1) PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100)
);

-- 2. Customers Table
CREATE TABLE customers (
                           id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           first_name VARCHAR(100) NOT NULL,
                           last_name VARCHAR(100) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           company VARCHAR(255),
                           status VARCHAR(50) DEFAULT 'New'
);

-- =============================================
-- DEMO DATA
-- =============================================

-- DEV-ONLY: {noop} stores passwords in plain text for demo convenience.
-- Spring Security's DelegatingPasswordEncoder supports this prefix.
-- All passwords created via /api/auth/register are BCrypt-encoded automatically.
-- Replace these with BCrypt hashes (e.g., {bcrypt}$2a$10$...) before production use.
INSERT INTO users (email, password, role, first_name, last_name)
VALUES
    ('admin@saaskit.com', '{noop}Admin1234!', 'ADMIN', 'Alex', 'Carter'),
    ('user@saaskit.com', '{noop}User1234!', 'USER', 'John', 'Tester');

INSERT INTO customers (first_name, last_name, email, company, status)
VALUES
    ('Elon', 'Musk', 'elon@tesla.com', 'Tesla', 'Active'),
    ('Jeff', 'Bezos', 'jeff@amazon.com', 'Amazon', 'Lead'),
    ('Bill', 'Gates', 'bill@microsoft.com', 'Microsoft', 'Inactive');