# Hướng dẫn chạy ứng dụng Thư viện (CLI + MySQL)

## 1. Chuẩn bị Cơ sở dữ liệu
- Mở MySQL Workbench hoặc công cụ quản lý MySQL của bạn.
- Chạy nội dung file `setup.sql` để tạo database `library_db` và các bảng cần thiết.
- Đảm bảo MySQL đang chạy tại `localhost:3306` (User: `root`, Password: `123456`). Nếu mật khẩu khác, hãy cập nhật trong `DBConnection.java`.

## 2. Thư viện cần thiết
Dự án sử dụng MySQL Connector để kết nối database:
- `lib/mysql-connector-j-8.3.0.jar`

## 3. Cách chạy
Dự án chạy trên giao diện dòng lệnh (CLI). Đảm bảo bạn đang ở thư mục gốc của dự án.

```powershell
# Biên dịch
javac -cp ".;lib/*" LibraryDemo.java

# Chạy
java -cp ".;lib/*" LibraryDemo
```

## 4. Các chức năng
- **Đăng nhập/Đăng xuất**: Quản lý phiên làm việc.
- **Đăng ký**: Tạo tài khoản người đọc mới.
- **Quản lý Sách (Admin)**: Thêm/Xóa sách (yêu cầu login user `admin`).
- **Xem sách**: Hiển thị danh sách sách từ database.
