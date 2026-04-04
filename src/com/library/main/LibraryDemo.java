package com.library.main;

import com.library.model.*;
import com.library.dao.*;
import java.util.Scanner;
import java.util.List;
import java.sql.SQLException;

public class LibraryDemo {
    private static Scanner scanner = new Scanner(System.in);
    private static MemberDAO memberDAO = new MemberDAO();
    private static BookDAO bookDAO = new BookDAO();
    private static Member currentUser = null;

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            System.out.print("CHỌN: ");
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
                continue;
            }

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    logout();
                    break;
                case 3:
                    register();
                    break;
                case 4:
                    managePersonalInfo();
                    break;
                case 5:
                    manageLibraryAdmin();
                    break;
                case 6:
                    searchBooks();
                    break;
                case 7:
                    viewAllBooks();
                    break;
                case 8:
                    System.out.println("Tạm biệt!");
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n===== HỆ THỐNG THƯ VIỆN =====");
        if (currentUser != null) {
            System.out.println("Xin chào: " + currentUser.getFullName());
        }
        System.out.println("1. ĐĂNG NHẬP");
        System.out.println("2. ĐĂNG XUẤT");
        System.out.println("3. ĐĂNG KÍ THÀNH VIÊN MỚI");
        System.out.println("4. QUẢN LÝ THÔNG TIN CÁ NHÂN");
        System.out.println("5. QUẢN LÝ THƯ VIỆN ");
        System.out.println("6. TÌM KIẾM SÁCH");
        System.out.println("7. XEM DANH SÁCH SÁCH");
        System.out.println("8. THOÁT");
    }

    private static void login() {
        if (currentUser != null) {
            System.out.println("Bạn đã đăng nhập rồi!");
            return;
        }
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        try {
            Member member = memberDAO.login(user, pass);
            if (member != null) {
                currentUser = member;
                System.out.println("Đăng nhập thành công! Xin chào " + currentUser.getUsername());
            } else if (user.equals("admin") && pass.equals("123")) {
                currentUser = new Member("admin", "123", "Quản trị viên", "", "Khác", "", "", "", "ADMIN-DEF");
                System.out.println("Đăng nhập ADMIN mặc định thành công!");
            } else {
                System.out.println("Sai tên đăng nhập hoặc mật khẩu! Vui lòng thử lại.");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi database: " + e.getMessage());
        }
    }

    private static void logout() {
        if (currentUser != null) {
            System.out.println("ĐÃ ĐĂNG XUẤT THÀNH CÔNG! TẠM BIỆT " + currentUser.getUsername());
            currentUser = null;
        } else {
            System.out.println("CHƯA CÓ NGƯỜI DÙNG NÀO ĐĂNG NHẬP!");
        }
    }

    private static void register() {
        System.out.println("--- Đăng ký thành viên mới ---");
        System.out.print("TÊN ĐĂNG NHẬP: ");
        String user = scanner.nextLine();
        System.out.print("MẬT KHẨU: ");
        String pass = scanner.nextLine();
        System.out.print("HỌ VÀ TÊN: ");
        String name = scanner.nextLine();
        System.out.print("NGÀY SINH: ");
        String birth = scanner.nextLine();
        System.out.print("GIỚI TÍNH: ");
        String gender = scanner.nextLine();
        System.out.print("ĐỊA CHỈ: ");
        String address = scanner.nextLine();
        System.out.print("SỐ ĐIỆN THOẠI: ");
        String phone = scanner.nextLine();
        String email = "";
        while (true) {
            System.out.print("EMAIL: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Lỗi: Định dạng Email không hợp lệ (ví dụ: abc@gmail.com)! Vui lòng nhập lại.");
            }
        }

        System.out.print("MÃ THẺ THƯ VIỆN: ");
        String card = scanner.nextLine();
        try {
            if (memberDAO.isCardIdExists(card)) {
                System.out.println("Lỗi: Mã thẻ thư viện đã tồn tại trong hệ thống!");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra mã thẻ: " + e.getMessage());
            return;
        }

        Member m = new Member(user, pass, name, birth, gender, address, phone, email, card);
        try {
            memberDAO.addMember(m);
            System.out.println("ĐĂNG KÍ THÀNH VIÊN THÀNH CÔNG!");
        } catch (SQLException e) {
            System.out.println("Lỗi đăng ký: " + e.getMessage());
        }

        while (true) {
            System.out.println("1. Quay lại");
            System.out.print("Chọn: ");
            if (scanner.nextLine().equals("1")) {
                break;
            }
        }
    }

    private static void managePersonalInfo() {
        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập trước!");
            return;
        }

        if (currentUser.getUsername().equals("admin") || "Quản trị viên".equals(currentUser.getFullName())) {
            // Menu cho Admin
            while (true) {
                System.out.println("=== Quản lý thông tin cá nhân ===");
                System.out.println("1. Xóa thành viên");
                System.out.println("2. Quay lại");
                System.out.print("Chọn: ");
                String adminChoice = scanner.nextLine();

                if (adminChoice.equals("2"))
                    break;

                switch (adminChoice) {
                    case "1":
                        System.out.print("NHẬP TÊN ĐĂNG NHẬP CỦA THÀNH VIÊN CẦN XÓA: ");
                        String delUser = scanner.nextLine();
                        try {
                            memberDAO.deleteMember(delUser);
                            System.out.println("ĐÃ XÓA THÀNH VIÊN: " + delUser);
                        } catch (SQLException e) {
                            System.out.println("Lỗi xóa: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            }
        } else {
            // Menu cho Reader
            while (true) {
                System.out.println("=== Quản lý thông tin cá nhân ===");
                System.out.println("1. Xem hồ sơ");
                System.out.println("2. Cập nhật hồ sơ");
                System.out.println("3. Quay lại");
                System.out.print("Chọn: ");
                String readerChoice = scanner.nextLine();

                if (readerChoice.equals("3"))
                    break;

                switch (readerChoice) {
                    case "1":
                        currentUser.showProfile();
                        break;
                    case "2":
                        System.out.print("CẬP NHẬT ĐỊA CHỈ MỚI: ");
                        String newAddress = scanner.nextLine();
                        System.out.print("CẬP NHẬT SỐ ĐIỆN THOẠI MỚI: ");
                        String newPhone = scanner.nextLine();
                        String newEmail = "";
                        while (true) {
                            System.out.print("CẬP NHẬT EMAIL MỚI: ");
                            newEmail = scanner.nextLine();
                            if (newEmail.isEmpty() || isValidEmail(newEmail)) {
                                break;
                            } else {
                                System.out.println("Lỗi: Định dạng Email không hợp lệ! Vui lòng nhập lại.");
                            }
                        }
                        System.out.print("CẬP NHẬT MÃ THẺ THƯ VIỆN MỚI: ");
                        String newCard = scanner.nextLine();
                        try {
                            if (!newCard.isEmpty() && memberDAO.isCardIdExists(newCard, currentUser.getId())) {
                                System.out.println("Lỗi: Mã thẻ thư viện đã được sử dụng bởi người khác!");
                                break;
                            }
                        } catch (SQLException e) {
                            System.out.println("Lỗi kiểm tra mã thẻ: " + e.getMessage());
                            break;
                        }

                        if (!newAddress.isEmpty())
                            currentUser.setAddress(newAddress);
                        if (!newPhone.isEmpty())
                            currentUser.setPhone(newPhone);
                        if (!newEmail.isEmpty())
                            currentUser.setEmail(newEmail);
                        if (!newCard.isEmpty())
                            currentUser.setLibraryCardId(newCard);

                        try {
                            memberDAO.updateMember(currentUser);
                            System.out.println("Cập nhật thông tin thành công!");
                        } catch (SQLException e) {
                            System.out.println("Lỗi cập nhật DB: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            }
        }
    }

    private static void manageLibraryAdmin() {
        if (currentUser == null || !currentUser.getUsername().equals("admin")) {
            System.out.println("Quyền truy cập bị từ chối! Chỉ Admin mới có quyền này.");
            return;
        }

        while (true) {
            System.out.println("\n--- QUẢN LÝ THƯ VIỆN ---");
            System.out.println("1. Thêm sách");
            System.out.println("2. Xóa sách (theo mã)");
            System.out.println("3. Xóa TOÀN BỘ sách");
            System.out.println("4. Sửa thông tin sách");
            System.out.println("5. Xem danh sách thành viên");
            System.out.println("6. Xóa thành viên ");
            System.out.println("7. Xóa TOÀN BỘ thành viên");
            System.out.println("8. Quay lại");
            System.out.print("Chọn: ");
            String sub = scanner.nextLine();

            if (sub.equals("8")) {
                break;
            } else if (sub.equals("1")) {
                System.out.print("Mã sách: ");
                String id = scanner.nextLine();
                System.out.print("Tiêu đề: ");
                String title = scanner.nextLine();
                System.out.print("Tác giả: ");
                String author = scanner.nextLine();
                System.out.print("Thể loại: ");
                String cat = scanner.nextLine();
                System.out.print("Năm: ");
                int year = Integer.parseInt(scanner.nextLine());
                System.out.print("Số lượng: ");
                int qty = Integer.parseInt(scanner.nextLine());
                try {
                    bookDAO.addBook(new Book(id, title, author, cat, year, qty));
                    System.out.println("Thêm sách thành công!");
                } catch (SQLException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            } else if (sub.equals("2")) {
                try {
                    if (bookDAO.getAllBooks().isEmpty()) {
                        System.out.println("Thư viện hiện đang trống, không có sách để xóa!");
                        continue;
                    }
                    System.out.print("Nhập mã sách cần xóa: ");
                    String id = scanner.nextLine();
                    bookDAO.deleteBook(id);
                    System.out.println("Đã xóa sách.");
                } catch (SQLException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            } else if (sub.equals("3")) {
                try {
                    if (bookDAO.getAllBooks().isEmpty()) {
                        System.out.println("Thư viện hiện đang trống, không còn gì để xóa!");
                        continue;
                    }
                    System.out.print("CẢNH BÁO: Bạn có chắc muốn xóa TOÀN BỘ sách? (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        bookDAO.deleteAllBooks();
                        System.out.println("Đã xóa toàn bộ sách trong thư viện.");
                    } else {
                        System.out.println("Đã hủy thao tác.");
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            } else if (sub.equals("4")) {
                try {
                    if (bookDAO.getAllBooks().isEmpty()) {
                        System.out.println("Thư viện hiện đang trống, không có gì để sửa!");
                        continue;
                    }
                    System.out.print("Nhập mã sách cần sửa: ");
                    String id = scanner.nextLine();
                    List<Book> books = bookDAO.searchBooks(id);
                    Book target = null;
                    for (Book b : books) {
                        if (b.getBookId().equals(id)) {
                            target = b;
                            break;
                        }
                    }

                    if (target == null) {
                        System.out.println("Không tìm thấy sách có mã: " + id);
                    } else {
                        System.out.println("Nhập thông tin mới (để trống nếu không muốn đổi):");
                        System.out.print("Tiêu đề [" + target.getTitle() + "]: ");
                        String title = scanner.nextLine();
                        if (!title.isEmpty())
                            target.setTitle(title);

                        System.out.print("Tác giả [" + target.getAuthor() + "]: ");
                        String author = scanner.nextLine();
                        if (!author.isEmpty())
                            target.setAuthor(author);

                        System.out.print("Thể loại [" + target.getCategory() + "]: ");
                        String cat = scanner.nextLine();
                        if (!cat.isEmpty())
                            target.setCategory(cat);

                        System.out.print("Năm [" + target.getYear() + "]: ");
                        String yearStr = scanner.nextLine();
                        if (!yearStr.isEmpty())
                            target.setYear(Integer.parseInt(yearStr));

                        System.out.print("Số lượng [" + target.getQuantity() + "]: ");
                        String qtyStr = scanner.nextLine();
                        if (!qtyStr.isEmpty())
                            target.setQuantity(Integer.parseInt(qtyStr));

                        bookDAO.updateBook(target);
                        System.out.println("Cập nhật sách thành công!");
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            } else if (sub.equals("5")) {
                viewAllMembers();
            } else if (sub.equals("6")) {
                try {
                    System.out.print("Nhập username thành viên cần xóa: ");
                    String user = scanner.nextLine();
                    if (user.equals("admin")) {
                        System.out.println("Không thể xóa tài khoản Admin hệ thống!");
                    } else {
                        memberDAO.deleteMember(user);
                        System.out.println("Đã xóa thành viên.");
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            } else if (sub.equals("7")) {
                System.out.print("CẢNH BÁO: Bạn có chắc muốn xóa TOÀN BỘ thành viên? (Y/N): ");
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("Y")) {
                    try {
                        memberDAO.deleteAllMembers();
                        System.out.println("Đã xóa toàn bộ thành viên (ngoại trừ Admin).");
                    } catch (SQLException e) {
                        System.out.println("Lỗi: " + e.getMessage());
                    }
                } else {
                    System.out.println("Đã hủy thao tác.");
                }
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void viewAllMembers() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            System.out.println("\n--- DANH SÁCH THÀNH VIÊN ---");
            if (members.isEmpty()) {
                System.out.println("Chưa có thành viên nào đăng ký.");
            } else {
                System.out.printf("%-15s | %-20s | %-15s | %-20s\n", "Username", "Họ tên", "SĐT", "Email");
                System.out.println("-----------------------------------------------------------------------------");
                for (Member m : members) {
                    System.out.printf("%-15s | %-20s | %-15s | %-20s\n",
                            m.getUsername(), m.getFullName(), m.getPhone(), m.getEmail());
                }
            }

            while (true) {
                System.out.println("1. Quay lại");
                System.out.print("Chọn: ");
                if (scanner.nextLine().equals("1")) {
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void searchBooks() {
        try {
            if (bookDAO.getAllBooks().isEmpty()) {
                System.out.println("\nThư viện hiện đang trống. Không có sách nào để tìm kiếm!");
                while (true) {
                    System.out.println("1. Quay lại");
                    System.out.print("Chọn: ");
                    if (scanner.nextLine().equals("1")) {
                        break;
                    }
                }
                return;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra dữ liệu: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\n--- TÌM KIẾM SÁCH ---");
            System.out.println("1. Nhập từ khóa tìm kiếm (Mã/Tiêu đề/Tác giả)");
            System.out.println("2. Quay lại");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if (choice.equals("2")) {
                break;
            } else if (choice.equals("1")) {
                System.out.print("Nhập từ khóa: ");
                String key = scanner.nextLine();
                try {
                    List<Book> books = bookDAO.searchBooks(key);
                    System.out.println("\n--- KẾT QUẢ TÌM KIẾM ---");
                    if (books.isEmpty()) {
                        System.out.println("Không tìm thấy sách nào khớp với từ khóa: " + key);
                    } else {
                        displayBooks(books);
                    }
                } catch (SQLException e) {
                    System.out.println("Lỗi tìm kiếm: " + e.getMessage());
                }
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void viewAllBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            System.out.println("\n--- DANH SÁCH SÁCH ---");
            if (books.isEmpty()) {
                System.out.println("Thư viện hiện chưa có sách nào.");
            } else {
                displayBooks(books);
            }

            while (true) {
                System.out.println("1. Quay lại");
                System.out.print("Chọn: ");
                if (scanner.nextLine().equals("1")) {
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private static void displayBooks(List<Book> books) {
        System.out.printf("%-10s | %-30s | %-20s | %-5s\n", "Mã", "Tiêu đề", "Tác giả", "SL");
        System.out.println("-------------------------------------------------------------------------");
        for (Book b : books) {
            System.out.printf("%-10s | %-30s | %-20s | %-5d\n", b.getBookId(), b.getTitle(), b.getAuthor(),
                    b.getQuantity());
        }
    }
}
