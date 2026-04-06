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

    // Các đối tượng DAO dành cho chức năng của bạn
    static ReaderDAO readerDao = new ReaderDAO();
    static StatisticDAO statDao = new StatisticDAO();

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
                    if (isAdmin()) {
                        manageReaders();
                    } else {
                        System.out.println("Tài khoản không có quyền truy cập!");
                    }
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
        System.out.println("11. QUẢN LÝ ĐỘC GIẢ");
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

    private static java.util.List<Integer> listPhieuId = new java.util.ArrayList<>();
    private static java.util.List<String> listNgayHenTra = new java.util.ArrayList<>();
    private static java.util.List<String> listMemberName = new java.util.ArrayList<>();
    private static java.util.List<String> danhSachViPham = new java.util.ArrayList<>();

    private static void taoPhieuMuon() {
        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập trước!");
            return;
        }

        try {
            System.out.print("Nhập ngày hẹn trả (yyyy-MM-dd): ");
            String ngayHenTraStr = scanner.nextLine().trim();

            System.out.print("Nhập số lượng sách muốn mượn: ");
            int soLuong = Integer.parseInt(scanner.nextLine().trim());

            int phieuId = (int) (Math.random() * 10000) + 1000;

            // Lưu thông tin phiếu
            listPhieuId.add(phieuId);
            listNgayHenTra.add(ngayHenTraStr);
            listMemberName.add(currentUser.getFullName());

            for (int i = 0; i < soLuong; i++) {
                System.out.print("Nhập mã sách thứ " + (i + 1) + ": ");
                String bookId = scanner.nextLine().trim();
                System.out.println("✓ Đã thêm sách: " + bookId);
            }
            System.out.println("--- TẠO PHIẾU MƯỢN THÀNH CÔNG! ---");
            System.out.println("   ID phiếu mượn : #" + phieuId);
            System.out.println("   Thành viên     : " + currentUser.getFullName());
            System.out.println("   Ngày hẹn trả   : " + ngayHenTraStr);
            System.out.println("   Số sách mượn   : " + soLuong + " cuốn");
        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
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

            if (listPhieuId.isEmpty()) {
                System.out.println("Chưa có phiếu mượn nào được tạo!");
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
        if (listPhieuId.isEmpty()) {
            System.out.println("Chưa có phiếu mượn nào được tạo!");
            return;
        }
        for (int i = 0; i < listPhieuId.size(); i++) {
            System.out.println("ID Phiếu     : #" + listPhieuId.get(i));
            System.out.println("Thành viên   : " + listMemberName.get(i));
            System.out.println("Ngày hẹn trả : " + listNgayHenTra.get(i));
            System.out.println("Trạng thái   : Đang mượn");
        }
    }

    private static void thucHienTraSach() {
        try {
            System.out.print("Nhập ID phiếu mượn: #");
            int phieuId = Integer.parseInt(scanner.nextLine().trim());

            int index = listPhieuId.indexOf(phieuId);
            if (index == -1) {
                System.out.println("Không tìm thấy phiếu mượn #" + phieuId);
                return;
            }

            String ngayHenTraStr = listNgayHenTra.get(index);
            String memberName = listMemberName.get(index);

            java.sql.Date ngayTraThucTe = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date ngayHenTra = java.sql.Date.valueOf(ngayHenTraStr);

            long soNgayTre = (ngayTraThucTe.getTime() - ngayHenTra.getTime()) / (24 * 60 * 60 * 1000);

            System.out.println("\n--- Đang thực hiện trả sách cho phiếu #" + phieuId + " ---");
            System.out.println("Thành viên       : " + memberName);
            System.out.println("Ngày hẹn trả     : " + ngayHenTra);
            System.out.println("Ngày trả thực tế : " + ngayTraThucTe);

            if (soNgayTre > 0) {
                System.out.println("→ Trả muộn: " + soNgayTre + " ngày");

                System.out.print("\nGhi nhận vi phạm trả muộn? (1: Có, 0: Không): ");
                if (Integer.parseInt(scanner.nextLine().trim()) == 1) {
                    String info = String.format("Phiếu #%d | Trả muộn %d ngày | Ngày trả: %s",
                            phieuId, soNgayTre, ngayTraThucTe);
                    danhSachViPham.add(info);
                    System.out.println("✓ Đã ghi nhận vi phạm trả muộn!");
                }
            } else {
                System.out.println("→ Trả đúng hạn.");
            }

            System.out.println("\nĐã thực hiện trả sách thành công cho phiếu #" + phieuId);

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
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
        if (danhSachViPham.isEmpty()) {
            System.out.println("Chưa có vi phạm nào được ghi nhận.");
            return;
        }
        for (int i = 0; i < danhSachViPham.size(); i++) {
            System.out.println((i + 1) + ". " + danhSachViPham.get(i));
        }
    }

    private static void thucHienXuLyViPham() {
        try {
            System.out.print("Nhập ID phiếu mượn: #");
            int phieuId = Integer.parseInt(scanner.nextLine().trim());

            int index = listPhieuId.indexOf(phieuId);
            if (index == -1) {
                System.out.println("Không tìm thấy phiếu mượn #" + phieuId);
                return;
            }

            String ngayHenTraStr = listNgayHenTra.get(index);

            System.out.println("\nChọn loại vi phạm:");
            System.out.println("1. Trả muộn");
            System.out.println("2. Làm hỏng");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            String loai = "";
            double tienPhat = 0.0;

            if (choice == 1) {
                loai = "tra_muon";
                java.sql.Date ngayTra = new java.sql.Date(System.currentTimeMillis());
                java.sql.Date ngayHen = java.sql.Date.valueOf(ngayHenTraStr);
                long soNgayTre = Math.max(0, (ngayTra.getTime() - ngayHen.getTime()) / (24 * 60 * 60 * 1000));

                tienPhat = soNgayTre * 5000.0;
                System.out.println("→ Trả muộn: " + soNgayTre + " ngày");
                System.out.println("→ Tiền phạt: " + tienPhat + " VND");
            } else if (choice == 2) {
                loai = "lam_hong";
                System.out.print("Nhập tiền phạt (VND): ");
                tienPhat = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("→ Làm hỏng");
                System.out.println("→ Tiền phạt: " + tienPhat + " VND");
            } else {
                System.out.println("Lựa chọn không hợp lệ!");
                return;
            }

            String info = String.format("Phiếu #%d | Loại: %s | Phạt: %.0f VND", phieuId, loai, tienPhat);
            danhSachViPham.add(info);

            System.out.println("Đã ghi nhận vi phạm thành công cho phiếu #" + phieuId);

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void manageReaders() {
        int choice = -1;
        do {
            System.out.println("\n--- Quản Lý Độc Giả ---");
            System.out.println("1. Xem danh sách độc giả");
            System.out.println("2. Thêm độc giả");
            System.out.println("3. Cập nhật độc giả");
            System.out.println("4. Xóa độc giả");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
                continue;
            }

            switch (choice) {
                case 1:
                    List<Reader> readers = readerDao.getAll();
                    System.out.println("DANH SÁCH ĐỘC GIẢ:");
                    System.out.printf("%-5s %-25s %-15s\n", "ID", "Họ và Tên", "Số điện thoại");
                    for (Reader r : readers) {
                        System.out.printf("%-5d %-25s %-15s\n", r.getId(), r.getName(), r.getPhone());
                    }
                    break;
                case 2:
                    System.out.print("Nhập tên độc giả: ");
                    String name = scanner.nextLine();
                    System.out.print("Nhập số điện thoại: ");
                    String phone = scanner.nextLine();
                    readerDao.addReader(new Reader(name, phone));
                    break;
                case 3:
                    System.out.print("Nhập ID độc giả cần cập nhật: ");
                    try {
                        int updateId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập tên mới: ");
                        String newName = scanner.nextLine();
                        System.out.print("Nhập số điện thoại mới: ");
                        String newPhone = scanner.nextLine();
                        readerDao.updateReader(new Reader(updateId, newName, newPhone));
                    } catch (NumberFormatException e) {
                        System.out.println("ID phải là số!");
                    }
                    break;
                case 4:
                    System.out.print("Nhập ID độc giả cần xóa: ");
                    try {
                        int deleteId = Integer.parseInt(scanner.nextLine());
                        readerDao.deleteReader(deleteId);
                    } catch (NumberFormatException e) {
                        System.out.println("ID phải là số!");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
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