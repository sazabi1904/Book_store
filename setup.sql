-- ==================== TẠO DATABASE library_db ====================

CREATE DATABASE library_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE library_db;

-- 1. Bảng members (có cột role)
CREATE TABLE members (
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

-- 2. Bảng books
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bookId VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    category VARCHAR(100),
    year INT,
    quantity INT DEFAULT 1,
    trangThai ENUM('available', 'borrowed') DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==================== DỮ LIỆU TEST ====================

-- Thêm Admin
INSERT INTO members (username, password, full_name, role, card_id)
VALUES ('admin', '123', 'Quản trị viên hệ thống', 'ADMIN', 'ADMIN001');

-- Thêm thành viên test
INSERT INTO members (username, password, full_name, birth_date, gender, address, phone, email, card_id, role)
VALUES 
('reader1', '123456', 'Nguyễn Văn A', '2002-05-15', 'Nam', 'Hà Nội', '0987654321', 'vana@gmail.com', 'TV001', 'READER'),
('reader2', '123456', 'Trần Thị B', '2003-08-20', 'Nữ', 'Hà Nội', '0912345678', 'thib@gmail.com', 'TV002', 'READER'),
('reader3', '123456', 'Lê Văn C', '2001-12-10', 'Nam', 'Hải Phòng', '0978123456', 'levanc@gmail.com', 'TV003', 'READER'),
('reader4', '123456', 'Phạm Thị D', '2004-03-25', 'Nữ', 'Đà Nẵng', '0933456789', 'phamthid@gmail.com', 'TV004', 'READER'),
('reader5', '123456', 'Hoàng Văn E', '2002-07-30', 'Nam', 'Hà Nội', '0966123456', 'hoangve@gmail.com', 'TV005', 'READER');

-- Thêm sách test
INSERT INTO books (bookId, title, author, category, year, quantity, trangThai)
VALUES 
('B001', 'Java Cơ Bản', 'Nguyễn Văn C', 'Công nghệ', 2022, 5, 'available'),
('B002', 'Cơ sở dữ liệu', 'Trần Thị D', 'Công nghệ', 2021, 3, 'available'),
('B003', 'Lập trình Web', 'Lê Văn E', 'Công nghệ', 2023, 4, 'available'),
('B004', 'Python cho người mới', 'Phạm Thị F', 'Công nghệ', 2024, 6, 'available'),
('B005', 'C++ Nâng cao', 'Hoàng Văn G', 'Công nghệ', 2020, 2, 'available'),
('B006', 'Clean Code', 'Robert Martin', 'Công nghệ', 2019, 3, 'available'),
('B007', 'Harry Potter và Hòn đá phù thủy', 'J.K. Rowling', 'Văn học', 2001, 5, 'available'),
('B008', 'Dế Mèn Phiêu Lưu Ký', 'Tô Hoài', 'Văn học', 1941, 8, 'available'),
('B009', 'To Kill a Mockingbird', 'Harper Lee', 'Văn học', 1960, 4, 'available'),
('B010', 'Sapiens: Lược sử loài người', 'Yuval Noah Harari', 'Khoa học', 2014, 2, 'available');

-- ==================== KIỂM TRA ====================
SELECT '✅ Database library_db đã được tạo lại thành công!' AS ThongBao;

SELECT 'Số thành viên:' AS Info, COUNT(*) FROM members;
SELECT 'Số sách:' AS Info, COUNT(*) FROM books;

SELECT id, username, full_name, card_id, role FROM members ORDER BY role DESC;
SELECT bookId, title, author, quantity, trangThai FROM books LIMIT 5;