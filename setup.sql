CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    full_name VARCHAR(100),
    birth_date VARCHAR(20),
    gender VARCHAR(10),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    card_id VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS books (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100),
    category VARCHAR(50),
    year INT,
    quantity INT DEFAULT 0
);

INSERT IGNORE INTO members (username, password, full_name, role) 
VALUES ('admin', '123', 'Quản trị viên hệ thống', 'ADMIN');
