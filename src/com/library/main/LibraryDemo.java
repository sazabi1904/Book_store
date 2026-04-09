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
    private static PhieuMuonDAO phieuMuonDao = new PhieuMuonDAO();
    private static ViPhamDAO viPhamDao = new ViPhamDAO();
    private static Member currentUser = null;
    // Các đối tượng DAO dành cho chức năng của bạn
    static ReaderDAO readerDao = new ReaderDAO();
    static StatisticDAO statDao = new StatisticDAO();
    private static DeXuatDAO deXuatDao = new DeXuatDAO();

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
                    taoPhieuMuon();
                    break;
                case 9:
                    if (isAdmin()) {
                        xuLyTraSach();
                    } else {
                        System.out.println("Tài khoản không có quyền truy cập!");
                    }
                    break;
                case 10:
                    if (isAdmin()) {
                        quanLyViPham();
                    } else {
                        System.out.println("Tài khoản không có quyền truy cập!");
                    }
                    break;
                case 11:
                    deXuatSach();
                    break;
                case 12:
                    if (isAdmin()) {
                        viewStatistics();
                    } else {
                        System.out.println("Tài khoản không có quyền truy cập!");
                    }
                    break;
                case 13:
                    if (isAdmin()) {
                        viewBorrowTracking();
                    } else {
                        System.out.println("Tài khoản không có quyền truy cập!");
                    }
                    break;
                case 0:
                    System.out.println("Tạm biệt!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n===== HỆ THỐNG THƯ VIỆN =====");

        if (currentUser != null) {
            String roleText = isAdmin() ? "ADMIN" : "READER";
            System.out.println("Xin chào: " + currentUser.getFullName() + " (" + roleText + ")");
        }

        System.out.println("1. ĐĂNG NHẬP");
        System.out.println("2. ĐĂNG XUẤT");
        System.out.println("3. ĐĂNG KÍ THÀNH VIÊN MỚI");
        System.out.println("4. QUẢN LÝ ĐĂNG NHẬP");
        System.out.println("5. QUẢN LÝ THƯ VIỆN");
        System.out.println("6. TÌM KIẾM SÁCH");
        System.out.println("7. XEM DANH SÁCH SÁCH");
        System.out.println("8. TẠO PHIẾU MƯỢN SÁCH");
        System.out.println("9. XỬ LÝ TRẢ SÁCH");
        System.out.println("10. QUẢN LÝ VI PHẠM");
        System.out.println("11. ĐỀ XUẤT SÁCH MỚI");
        System.out.println("12. THỐNG KÊ MƯỢN SÁCH");
        System.out.println("13. THEO DÕI MƯỢN / TRẢ");
        System.out.println("0. THOÁT");
    }

    // Hàm kiểm tra quyền Admin
    private static boolean isAdmin() {
        if (currentUser == null)
            return false;
        return currentUser.getUsername().equals("admin") ||
                "Quản trị viên".equals(currentUser.getFullName());
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
                memberDAO.incrementLoginCount(member.getId());
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
        if (currentUser == null
                || (!currentUser.getUsername().equals("admin") && !"Quản trị viên".equals(currentUser.getFullName()))) {
            System.out.println("Quyền truy cập bị từ chối! Chỉ Admin mới có quyền đăng ký thành viên mới.");
            return;
        }
        System.out.println("=== Đăng ký thành viên mới ===");
        System.out.print("TÊN ĐĂNG NHẬP: ");
        String user = scanner.nextLine();
        System.out.print("MẬT KHẨU: ");
        String pass = scanner.nextLine();
        System.out.print("HỌ VÀ TÊN: ");
        String name = scanner.nextLine();
        System.out.print("NGÀY SINH: ");
        String birth = scanner.nextLine();
        String gender = "";
        while (true) {
            System.out.println("GIỚI TÍNH:");
            System.out.println("1. Nam");
            System.out.println("2. Nữ");
            System.out.println("3. Khác");
            System.out.print("Chọn: ");
            String gChoice = scanner.nextLine();
            if (gChoice.equals("1")) {
                gender = "Nam";
                break;
            } else if (gChoice.equals("2")) {
                gender = "Nữ";
                break;
            } else if (gChoice.equals("3")) {
                gender = "Khác";
                break;
            } else {
                System.out.println("Lỗi: Lựa chọn không hợp lệ! Vui lòng chọn lại.");
            }
        }
        System.out.print("ĐỊA CHỈ: ");
        String address = scanner.nextLine();

        String phone = "";
        while (true) {
            System.out.print("SỐ ĐIỆN THOẠI: ");
            phone = scanner.nextLine();
            if (phone.matches("\\d+")) {
                break;
            } else {
                System.out.println("Lỗi số điện thoại không đúng dạng! Vui lòng nhập lại");
            }
        }
        String email = "";
        while (true) {
            System.out.print("EMAIL: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Lỗi: Định dạng Email không hợp lệ. Vui lòng nhập lại.");
            }
        }

        String card = "";
        while (true) {
            System.out.print("MÃ THẺ THƯ VIỆN: ");
            card = scanner.nextLine();
            try {
                if (memberDAO.isCardIdExists(card)) {
                    System.out.println("Lỗi: Mã thẻ thư viện đã tồn tại trong hệ thống! Vui lòng nhập lại.");
                } else {
                    break;
                }
            } catch (SQLException e) {
                System.out.println("Lỗi kiểm tra mã thẻ: " + e.getMessage());
                return;
            }
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
                System.out.println("1. Xem số lần đăng nhập Reader");
                System.out.println("2. Quay lại");
                System.out.print("Chọn: ");
                String adminChoice = scanner.nextLine();

                if (adminChoice.equals("2"))
                    break;

                switch (adminChoice) {
                    case "1":
                        try {
                            List<Member> members = memberDAO.getAllMembers();
                            System.out.println("\n=== SỐ LẦN ĐĂNG NHẬP CỦA READER ===");
                            if (members.isEmpty()) {
                                System.out.println("Chưa có thành viên nào đăng ký.");
                            } else {
                                String formatString = "%-15s | %-20s | %-20s%n";
                                System.out.printf(formatString, "Username", "Họ và tên", "Số lần đăng nhập");
                                System.out.println("-".repeat(65));
                                for (Member m : members) {
                                    if (!m.getUsername().equals("admin")) {
                                        System.out.printf(formatString,
                                                m.getUsername(),
                                                m.getFullName(),
                                                m.getLoginCount() + " lần");
                                    }
                                }
                                System.out.println("-".repeat(65));
                            }
                        } catch (SQLException e) {
                            System.out.println("Lỗi lấy thông tin: " + e.getMessage());
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
                        String newPhone = "";
                        while (true) {
                            System.out.print("CẬP NHẬT SỐ ĐIỆN THOẠI MỚI: ");
                            newPhone = scanner.nextLine();
                            if (newPhone.isEmpty() || newPhone.matches("\\d+")) {
                                break;
                            } else {
                                System.out.println("Lỗi: Số điện thoại chỉ được chứa số! Vui lòng nhập lại.");
                            }
                        }
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
                        String newCard = "";
                        boolean sqlError = false;
                        while (true) {
                            System.out.print("CẬP NHẬT MÃ THẺ THƯ VIỆN MỚI: ");
                            newCard = scanner.nextLine();
                            try {
                                if (!newCard.isEmpty() && memberDAO.isCardIdExists(newCard, currentUser.getId())) {
                                    System.out.println(
                                            "Lỗi: Mã thẻ thư viện đã được sử dụng bởi người khác! Vui lòng nhập lại.");
                                } else {
                                    break;
                                }
                            } catch (SQLException e) {
                                System.out.println("Lỗi kiểm tra mã thẻ: " + e.getMessage());
                                sqlError = true;
                                break;
                            }
                        }
                        if (sqlError)
                            break;

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
            System.out.println("1. Quản lý sách");
            System.out.println("2. Quản lý thành viên");
            System.out.println("3. Quay lại");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if (choice.equals("3"))
                break;

            switch (choice) {
                case "1":
                    manageBooks();
                    break;
                case "2":
                    manageMembersAdmin();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void manageBooks() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ SÁCH ---");
            System.out.println("1. Thêm sách");
            System.out.println("2. Xóa sách (theo mã)");
            System.out.println("3. Xóa TOÀN BỘ sách");
            System.out.println("4. Sửa thông tin sách");
            System.out.println("5. Quay lại");
            System.out.print("Chọn: ");
            String sub = scanner.nextLine();

            if (sub.equals("5")) {
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

                    // PHẦN THÊM VÀO: Hiện bảng phân loại sách (chỉ hiện tên sách)
                    displayBookCategories();
                    // ===========================================
                } catch (SQLException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            } else if (sub.equals("2")) {
                try {
                    List<Book> allBooks = bookDAO.getAllBooks();
                    if (allBooks.isEmpty()) {
                        System.out.println("Thư viện hiện đang trống, không có sách để xóa!");
                        continue;
                    }
                    displayBooks(allBooks);
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
                    List<Book> allBooks = bookDAO.getAllBooks();
                    if (allBooks.isEmpty()) {
                        System.out.println("Thư viện hiện đang trống, không có gì để sửa!");
                        continue;
                    }
                    displayBooks(allBooks);
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
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void manageMembersAdmin() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ THÀNH VIÊN ---");
            System.out.println("1. Xem danh sách thành viên");
            System.out.println("2. Xóa thành viên");
            System.out.println("3. Xóa TOÀN BỘ thành viên");
            System.out.println("4. Quay lại");
            System.out.print("Chọn: ");
            String sub = scanner.nextLine();

            if (sub.equals("4")) {
                break;
            } else if (sub.equals("1")) {
                viewAllMembers();
            } else if (sub.equals("2")) {
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
            } else if (sub.equals("3")) {
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
            System.out.println("1. Nhập từ khóa tìm kiếm (Mã/Tiêu đề/Tác giả/Thể loại)");
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
        System.out.printf("%-10s | %-30s | %-20s | %-15s | %-5s\n", "Mã", "Tiêu đề", "Tác giả", "Thể loại", "SL");
        System.out.println(
                "---------------------------------------------------------------------------------------------");
        for (Book b : books) {
            System.out.printf("%-10s | %-30s | %-20s | %-15s | %-5d\n", b.getBookId(), b.getTitle(), b.getAuthor(),
                    b.getCategory(),
                    b.getQuantity());
        }
    }

    // PHẦN THÊM VÀO: Hàm hiển thị bảng thể loại
    private static void displayBookCategories() {
        try {
            List<BookCategoryModel> categories = bookDAO.getBooksCategorized();
            if (categories.isEmpty())
                return;

            System.out.println("\n--- BẢNG THỂ LOẠI SÁCH ---");
            System.out.printf("%-20s | %-50s\n", "Thể loại", "Tên sách");
            System.out.println("-------------------------------------------------------------------------");
            for (BookCategoryModel catModel : categories) {
                String catName = catModel.getCategoryName();
                List<String> titles = catModel.getBookTitles();
                for (int i = 0; i < titles.size(); i++) {
                    if (i == 0) {
                        System.out.printf("%-20s | %-50s\n", catName, titles.get(i));
                    } else {
                        System.out.printf("%-20s | %-50s\n", "", titles.get(i));
                    }
                }
                System.out.println("-".repeat(73));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi hiển thị bảng thể loại: " + e.getMessage());
        }
    }
    // ========================================================

    private static void taoPhieuMuon() {
        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập trước!");
            return;
        }

        try {
            int memberId = currentUser.getId();
            if (memberId <= 0) {
                System.out.print("Nhập mã thẻ thư viện của độc giả mượn: ");
                String cardId = scanner.nextLine().trim();
                Member borrower = memberDAO.getByLibraryCardId(cardId);
                if (borrower == null) {
                    System.out.println("Không tìm thấy độc giả với mã thẻ này.");
                    return;
                }
                memberId = borrower.getId();
            }

            System.out.print("Nhập ngày hẹn trả (yyyy-MM-dd): ");
            String ngayHenTraStr = scanner.nextLine().trim();
            java.sql.Date ngayHenTra = java.sql.Date.valueOf(ngayHenTraStr);

            java.sql.Date ngayMuon = new java.sql.Date(System.currentTimeMillis());

            // Kiểm tra ngày hẹn trả phải lớn hơn ngày mượn
            if (ngayHenTra.before(ngayMuon) || ngayHenTra.equals(ngayMuon)) {
                System.out.println("Lỗi không hợp lệ!");
                return;
            }

            System.out.print("Nhập số lượng sách muốn mượn: ");
            int soLuong = Integer.parseInt(scanner.nextLine().trim());
            if (soLuong <= 0) {
                System.out.println("Số lượng sách phải lớn hơn 0.");
                return;
            }
            java.util.List<String> bookIds = new java.util.ArrayList<>();
            for (int i = 0; i < soLuong; i++) {
                System.out.print("Nhập mã sách thứ " + (i + 1) + ": ");
                String bookId = scanner.nextLine().trim();
                if (bookDAO.getByBookId(bookId) == null) {
                    System.out.println("Không tìm thấy sách với mã: " + bookId);
                    return;
                }
                bookIds.add(bookId);
            }

            int phieuId = phieuMuonDao.createPhieuMuon(memberId, ngayMuon, ngayHenTra, soLuong);
            if (phieuId < 0) {
                System.out.println("Không thể tạo phiếu mượn. Vui lòng thử lại sau.");
                return;
            }

            int successCount = 0;
            for (String bookId : bookIds) {
                try {
                    phieuMuonDao.addChiTietPhieuMuon(phieuId, bookId);
                    bookDAO.updateTrangThai(bookId, "borrowed");
                    successCount++;
                } catch (SQLException e) {
                    System.out.println("   ⚠ Lỗi lưu sách " + bookId + ": " + e.getMessage());
                }
            }

            if (successCount > 0) {
                System.out.println("--- TẠO PHIẾU MƯỢN THÀNH CÔNG! ---");
                System.out.println("   ID phiếu mượn : #" + phieuId);
                System.out.println("   Thành viên     : " + currentUser.getFullName());
                System.out.println("   Ngày hẹn trả   : " + ngayHenTraStr);
                System.out.println("   Số sách đã lưu : " + successCount + "/" + soLuong + " cuốn");
            } else {
                System.out.println("Không thể lưu bất kỳ sách nào vào phiếu mượn #" + phieuId);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Định dạng ngày không hợp lệ. Vui lòng nhập lại theo yyyy-MM-dd.");
        } catch (SQLException e) {
            System.out.println("Lỗi database khi tạo phiếu mượn: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void xuLyTraSach() {
        if (currentUser == null || !isAdmin()) {
            System.out.println("Chỉ Admin mới có quyền xử lý trả sách!");
            return;
        }

        while (true) {
            System.out.println("\n--- XỬ LÝ TRẢ SÁCH ---");
            System.out.println("1. XEM DANH SÁCH PHIẾU MƯỢN");
            System.out.println("2. THỰC HIỆN TRẢ SÁCH");
            System.out.println("0. QUAY LẠI");
            System.out.print("CHỌN: ");

            int sub = -1;
            try {
                sub = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Vui lòng nhập số!");
                continue;
            }

            try {
                java.util.List<PhieuMuonDAO.LoanInfo> list = phieuMuonDao.getAllBorrowSlips();
                if (list.isEmpty()) {
                    System.out.println("Chưa có phiếu mượn nào được tạo!");
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Lỗi truy vấn phiếu mượn: " + e.getMessage());
                return;
            }

            if (sub == 0)
                return;
            if (sub == 1)
                xemTatCaPhieuMuon();
            else if (sub == 2)
                thucHienTraSach();
            else
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private static void xemTatCaPhieuMuon() {
        System.out.println("\n--- DANH SÁCH PHIẾU MƯỢN ---");
        try {
            java.util.List<PhieuMuonDAO.LoanInfo> list = phieuMuonDao.getAllBorrowSlips();
            if (list.isEmpty()) {
                System.out.println("Chưa có phiếu mượn nào được tạo!");
                return;
            }
            for (PhieuMuonDAO.LoanInfo info : list) {
                System.out.println("ID Phiếu     : #" + info.getId());
                System.out.println("Thành viên   : " + info.getMemberName());
                System.out.println("Số sách mượn : " + info.getBookCount() + " cuốn");
                System.out.println("Tên sách     : " + info.getBookTitles());
                System.out.println("Ngày hẹn trả : " + info.getNgayHenTra());
                System.out.println("Trạng thái   : " + info.getStatus());
                System.out.println("-".repeat(40));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi hiển thị phiếu mượn: " + e.getMessage());
        }
    }

    private static void thucHienTraSach() {
        try {
            System.out.print("Nhập ID phiếu mượn: #");
            int phieuId = Integer.parseInt(scanner.nextLine().trim());

            java.sql.Date ngayHenTra = phieuMuonDao.getDueDate(phieuId);
            if (ngayHenTra == null) {
                System.out.println("Không tìm thấy phiếu mượn #" + phieuId);
                return;
            }

            java.sql.Date ngayTraThucTe = new java.sql.Date(System.currentTimeMillis());
            int updatedCount = phieuMuonDao.returnBooks(phieuId, ngayTraThucTe);
            if (updatedCount == 0) {
                System.out.println("Không có sách nào đang mượn cho phiếu này hoặc phiếu đã được trả.");
                return;
            }

            long soNgayTre = (ngayTraThucTe.getTime() - ngayHenTra.getTime()) / (24 * 60 * 60 * 1000);

            System.out.println("\n--- Đang thực hiện trả sách cho phiếu #" + phieuId + " ---");
            System.out.println("Ngày hẹn trả     : " + ngayHenTra);
            System.out.println("Ngày trả thực tế : " + ngayTraThucTe);

            if (soNgayTre > 0) {
                System.out.println("→ Trả muộn: " + soNgayTre + " ngày");
                System.out.print("\nGhi nhận vi phạm trả muộn? (1: Có, 0: Không): ");
                if (Integer.parseInt(scanner.nextLine().trim()) == 1) {
                    int chiTietId = phieuMuonDao.getFirstChiTietIdForPhieu(phieuId);
                    if (chiTietId <= 0) {
                        System.out.println("✗ Không tìm thấy chi tiết phiếu mượn để ghi vi phạm.");
                    } else {
                        try {
                            String info = String.format("Phiếu #%d | Trả muộn %d ngày | Ngày trả: %s", phieuId, soNgayTre, ngayTraThucTe);
                            viPhamDao.insertViolation(chiTietId, "tra_muon", soNgayTre * 5000.0, info);
                            System.out.println("✓ Đã ghi nhận vi phạm trả muộn vào database!");
                        } catch (SQLException sqlEx) {
                            System.out.println("✗ Lỗi lưu vi phạm vào database: " + sqlEx.getMessage());
                            sqlEx.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("→ Trả đúng hạn.");
            }

            System.out.println("\n✓ Đã thực hiện trả sách thành công cho phiếu #" + phieuId);

        } catch (SQLException e) {
            System.out.println("✗ Lỗi database khi xử lý trả sách: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void quanLyViPham() {
        if (currentUser == null || !isAdmin()) {
            System.out.println("Chỉ Admin mới có quyền quản lý vi phạm!");
            return;
        }

        while (true) {
            System.out.println("\n--- QUẢN LÝ VI PHẠM ---");
            System.out.println("1. XEM DANH SÁCH VI PHẠM");
            System.out.println("2. THỰC HIỆN XỬ LÝ VI PHẠM");
            System.out.println("0. QUAY LẠI");
            System.out.print("CHỌN: ");

            int sub = -1;
            try {
                sub = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Vui lòng nhập số!");
                continue;
            }

            if (sub == 0)
                return;
            if (sub == 1)
                xemTatCaViPham();
            else if (sub == 2)
                thucHienXuLyViPham();
            else
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private static void xemTatCaViPham() {
        System.out.println("\n--- DANH SÁCH VI PHẠM ---");
        try {
            java.util.List<ViPhamDAO.ViPhamInfo> list = viPhamDao.getAllViolations();
            if (list.isEmpty()) {
                System.out.println("Chưa có vi phạm nào được ghi nhận.");
                return;
            }
            System.out.printf("%-5s | %-10s | %-10s | %-12s | %-12s | %-30s\n", "ID", "Phiếu", "Chi tiết", "Loại", "Tiền phạt", "Ghi chú");
            System.out.println("-".repeat(90));
            for (ViPhamDAO.ViPhamInfo info : list) {
                System.out.printf("%-5d | %-10d | %-10d | %-12s | %-12.0f | %-30s\n",
                        info.getId(), info.getPhieuMuonId(), info.getChiTietId(), info.getLoaiViPham(), info.getTienPhat(), info.getGhiChu());
            }
        } catch (SQLException e) {
            System.out.println("Lỗi hiển thị vi phạm: " + e.getMessage());
        }
    }

    private static void thucHienXuLyViPham() {
        try {
            System.out.print("Nhập ID phiếu mượn: #");
            int phieuId = Integer.parseInt(scanner.nextLine().trim());

            java.sql.Date ngayHenTra = phieuMuonDao.getDueDate(phieuId);
            if (ngayHenTra == null) {
                System.out.println("Không tìm thấy phiếu mượn #" + phieuId);
                return;
            }

            System.out.println("\nChọn loại vi phạm:");
            System.out.println("1. Trả muộn");
            System.out.println("2. Làm hỏng");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            String loai;
            double tienPhat = 0.0;
            String ghiChu = "";

            if (choice == 1) {
                loai = "tra_muon";
                java.sql.Date ngayTra = new java.sql.Date(System.currentTimeMillis());
                long soNgayTre = Math.max(0, (ngayTra.getTime() - ngayHenTra.getTime()) / (24 * 60 * 60 * 1000));
                tienPhat = soNgayTre * 5000.0;
                ghiChu = String.format("Trả muộn %d ngày | Ngày trả: %s", soNgayTre, ngayTra);
                System.out.println("→ Trả muộn: " + soNgayTre + " ngày");
                System.out.println("→ Tiền phạt: " + tienPhat + " VND");
            } else if (choice == 2) {
                loai = "lam_hong";
                System.out.print("Nhập tiền phạt (VND): ");
                tienPhat = Double.parseDouble(scanner.nextLine().trim());
                ghiChu = String.format("Làm hỏng | Phạt: %.0f VND", tienPhat);
                System.out.println("→ Làm hỏng");
                System.out.println("→ Tiền phạt: " + tienPhat + " VND");
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
                return;
            }

            int chiTietId = phieuMuonDao.getFirstChiTietIdForPhieu(phieuId);
            if (chiTietId <= 0) {
                System.out.println("✗ Không tìm thấy chi tiết phiếu mượn để ghi vi phạm.");
                return;
            }

            try {
                viPhamDao.insertViolation(chiTietId, loai, tienPhat, ghiChu);
                System.out.println("✓ Đã ghi nhận vi phạm thành công vào database cho phiếu #" + phieuId);
            } catch (SQLException sqlEx) {
                System.out.println("✗ Lỗi lưu vi phạm vào database: " + sqlEx.getMessage());
                sqlEx.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("✗ Lỗi database khi xử lý vi phạm: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
// ========================================================
    private static void deXuatSach() {
        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập để sử dụng tính năng này!");
            return;
        }

        if (isAdmin()) {
            while (true) {
                System.out.println("\n--- QUẢN LÝ ĐỀ XUẤT SÁCH MỚI ---");
                try {
                    java.util.List<com.library.model.DeXuat> list = deXuatDao.getAllDeXuat();
                    if (list.isEmpty()) {
                        System.out.println("Hiện chưa có đề xuất nào từ độc giả.");
                    } else {
                        System.out.printf("%-5s | %-15s | %-30s | %-20s | %-15s\n", "ID", "Độc giả", "Tên Sách",
                                "Tác Giả", "Trạng Thái");
                        System.out.println("-".repeat(95));
                        for (com.library.model.DeXuat d : list) {
                            System.out.printf("%-5d | %-15s | %-30s | %-20s | %-15s\n",
                                    d.getId(), d.getMemberName(), d.getTenSach(),
                                    (d.getTacGia() == null ? "N/A" : d.getTacGia()), d.getTrangThai());
                        }
                    }
                    System.out.println("\n1. Xóa toàn bộ đề xuất");
                    System.out.println("0. Quay lại");
                    System.out.print("Chọn: ");
                    String choice = scanner.nextLine();
                    if (choice.equals("1")) {
                        deXuatDao.clearAllDeXuat();
                        System.out.println("Đã xóa toàn bộ danh sách đề xuất trong CSDL.");
                    } else if (choice.equals("0")) {
                        break;
                    } else {
                        System.out.println("Lựa chọn không hợp lệ!");
                    }
                } catch (java.sql.SQLException e) {
                    System.out.println("Lỗi truy xuất CSDL: " + e.getMessage());
                }
            }
        } else {
            System.out.println("\n--- GỬI ĐỀ XUẤT SÁCH MỚI ---");
            System.out.print("Nhập tên sách bạn muốn thư viện bổ sung (hoặc Enter để Hủy): ");
            String tenSach = scanner.nextLine();
            if (tenSach.trim().isEmpty()) {
                System.out.println("Đã hủy thao tác.");
                return;
            }
            System.out.print("Nhập tên tác giả (Có thể để trống): ");
            String tacGia = scanner.nextLine();

            try {
                // Assuming currentUser is an instance of Member from DB with properly populated
                // ID field!
                deXuatDao.addDeXuat(currentUser.getId(), tenSach, tacGia);
                System.out.println("Cảm ơn! Đề xuất của bạn đã được ghi nhận vào CSDL.");
            } catch (java.sql.SQLException e) {
                System.out.println("Lỗi CSDL khi lưu đề xuất: " + e.getMessage());
            }
        }
    }

    private static void viewStatistics() {
        System.out.println("\n--- Thống Kê Mượn Sách ---");
        System.out.println("SÁCH MƯỢN NHIỀU NHẤT:");
        List<BookStatistic> stats = statDao.getTopBorrowedBooks();
        System.out.printf("%-40s %-15s\n", "Tên Sách", "Số Lần Mượn");
        for (BookStatistic s : stats) {
            System.out.printf("%-40s %-15d\n", s.getTitle(), s.getBorrowCount());
        }

        System.out.println("\nTÌNH TRẠNG KHO SÁCH:");
        List<String> inventory = statDao.getInventoryStatus();
        for (String status : inventory) {
            System.out.println(status);
        }
    }

    private static void viewBorrowTracking() {
        System.out.println("\n--- Theo Dõi Mượn Trả ---");
        System.out.println("DANH SÁCH ĐANG MƯỢN / QUÁ HẠN:");
        List<BorrowedBook> overdueBooks = statDao.getOverdueBooks();
        System.out.printf("%-25s %-40s %-15s %-15s\n", "Người Mượn", "Tên Sách", "Hạn Trả", "Tình Trạng");
        for (BorrowedBook b : overdueBooks) {
            System.out.printf("%-25s %-40s %-15s %-15s\n", b.getReaderName(), b.getBookTitle(), b.getReturnDate(),
                    b.getStatus());
        }
    }
}