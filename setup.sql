-- Script tạo Database và Bảng cho Hệ thống Thư viện
-- Lưu ý: nếu bạn đã chạy script cũ, hãy xóa database cũ và chạy lại để đồng bộ hoàn toàn.
DROP DATABASE IF EXISTS library_db;
CREATE DATABASE library_db;
USE library_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS vi_pham;
DROP TABLE IF EXISTS chi_tiet_phieu_muon;
DROP TABLE IF EXISTS phieu_muon;
DROP TABLE IF EXISTS de_xuat;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Bảng members
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
    login_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng books
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

-- 3. Bảng phieu_muon (Phiếu mượn)
CREATE TABLE IF NOT EXISTS phieu_muon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    ngay_muon DATE NOT NULL,
    ngay_hen_tra DATE NOT NULL,
    so_luong_sach INT NOT NULL DEFAULT 0,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 4. Bảng chi_tiet_phieu_muon (Chi tiết phiếu mượn tương ứng với mỗi sách mượn)
CREATE TABLE IF NOT EXISTS chi_tiet_phieu_muon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phieu_muon_id INT NOT NULL,
    book_id VARCHAR(50) NOT NULL,
    ngay_tra_thuc_te DATE,
    trang_thai ENUM('dang_muon', 'da_tra', 'lam_hong', 'mat_sach') DEFAULT 'dang_muon',
    FOREIGN KEY (phieu_muon_id) REFERENCES phieu_muon(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- 5. Bảng vi_pham (Quản lý vi phạm)
CREATE TABLE IF NOT EXISTS vi_pham (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chi_tiet_phieu_muon_id INT NOT NULL,
    loai_vi_pham VARCHAR(100) NOT NULL, -- Theo ViPham.java: tra_muon hoặc lam_hong
    tien_phat DECIMAL(10, 2) DEFAULT 0.0,
    ghi_chu VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chi_tiet_phieu_muon_id) REFERENCES chi_tiet_phieu_muon(id) ON DELETE CASCADE
);

-- 6. Bảng de_xuat (Đề xuất sách mới)
CREATE TABLE IF NOT EXISTS de_xuat (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    ten_sach VARCHAR(255) NOT NULL,
    tac_gia VARCHAR(100),
    trang_thai ENUM('cho_duyet', 'da_duyet', 'tu_choi') DEFAULT 'cho_duyet',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- HIỆU CHỈNH DỮ LIỆU ĐỂ KIỂM THỬ (MOCK DATA)

-- Thêm Admin
INSERT INTO members (id, username, password, full_name, card_id, role) 
VALUES (1, 'admin', '123', 'Quản trị viên hệ thống', 'ADMIN-001', 'ADMIN');

-- Thêm Reader
INSERT INTO members (id, username, password, full_name, birth_date, gender, address, phone, email, card_id, role) 
VALUES (2, 'nguyenvana', '123', 'Nguyễn Văn A', '2000-01-01', 'Nam', 'Hà Nội', '0987654321', 'nva@gmail.com', 'READ-001', 'READER'),
       (3, 'tranthib', '123', 'Trần Thị B', '2002-05-15', 'Nữ', 'TP.HCM', '0912345678', 'ttb@gmail.com', 'READ-002', 'READER');

-- Thêm Sách
INSERT INTO books (id, title, author, category, year, quantity, trangThai) 
VALUES ('B001', 'Lập trình Java', 'James Gosling', 'Công nghệ thông tin', 2020, 10, 'available'),
       ('B002', 'Cấu trúc dữ liệu và giải thuật', 'Nguyễn Văn C', 'Giáo trình', 2018, 5, 'available'),
       ('B003', 'Sách Đắc Nhân Tâm', 'Dale Carnegie', 'Tâm lý', 1936, 15, 'available');

-- Thêm Phiếu Mượn cho member 2
INSERT INTO phieu_muon (id, member_id, ngay_muon, ngay_hen_tra, so_luong_sach) 
VALUES (1, 2, '2023-10-01', '2023-10-15', 2);

-- Thêm Chi Tiết Phiếu Mượn
INSERT INTO chi_tiet_phieu_muon (id, phieu_muon_id, book_id, ngay_tra_thuc_te, trang_thai) 
VALUES (1, 1, 'B001', 'dang_muon'),
       (2, 1, 'B002', '2023-10-20', 'da_tra'); -- Sách này đã trả muộn (so với hẹn trả 15/10)

-- Thêm Vi Phạm (Cho chi tiết phiếu mượn trả muộn của sách B002)
INSERT INTO vi_pham (id, chi_tiet_phieu_muon_id, loai_vi_pham, tien_phat, ghi_chu) 
VALUES (1, 2, 'tra_muon', 25000, 'Trả muộn 5 ngày');

-- Thêm Đề Xuất Sách
INSERT INTO de_xuat (id, member_id, ten_sach, tac_gia) 
VALUES (1, 3, 'Clean Code', 'Robert C. Martin');
