-- Script tạo Database và Bảng cho Hệ thống Thư viện
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- 1. Bảng thành viên (members)
CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE,
    gender VARCHAR(20),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    card_id VARCHAR(50) UNIQUE NOT NULL,
    role ENUM('ADMIN', 'READER') DEFAULT 'READER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng sách (books)
CREATE TABLE IF NOT EXISTS books (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100),
    category VARCHAR(100),
    year INT,
    quantity INT DEFAULT 1,
    trangThai ENUM('available', 'borrowed') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Thêm tài khoản Admin mặc định
-- Chú ý: username 'admin' được dùng để nhận diện quyền Quản trị viên
INSERT IGNORE INTO members (username, password, full_name, role) 
VALUES ('admin', '123', 'Quản trị viên hệ thống', 'ADMIN');
