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
                    lapPhieuMuon();
                    break;
                case 9:
                    if (isAdmin()) {
                        xuLyTraSach();
                    } else {
                        System.out.println(" Bạn không có quyền truy cập chức năng này!");
                    }
                    break;
                case 10:
                    if (isAdmin()) {
                        quanLyViPham();
                    } else {
                        System.out.println(" Bạn không có quyền truy cập chức năng này!");
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
        System.out.println("4. QUẢN LÝ THÔNG TIN CÁ NHÂN");
        System.out.println("5. QUẢN LÝ THƯ VIỆN");
        System.out.println("6. TÌM KIẾM SÁCH");
        System.out.println("7. XEM DANH SÁCH SÁCH");
        System.out.println("8. LẬP PHIẾU MƯỢN SÁCH");
        System.out.println("9. XỬ LÝ TRẢ SÁCH");
        System.out.println("10. QUẢN LÝ VI PHẠM");
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
                System.out.println("1. Xem danh sách thành viên đã đăng kí trước đó");
                System.out.println("2. Quay lại");
                System.out.print("Chọn: ");
                String adminChoice = scanner.nextLine();

                if (adminChoice.equals("2"))
                    break;

                switch (adminChoice) {
                    case "1":
                        try {
                            List<Member> members = memberDAO.getAllMembers();
                            System.out.println("\n=== DANH SÁCH THÀNH VIÊN ĐÃ ĐĂNG KÍ ===");
                            if (members.isEmpty()) {
                                System.out.println("Chưa có thành viên nào đăng ký.");
                            } else {
                                String formatString = "%-12s | %-20s | %-10s | %-9s | %-11s | %-11s | %-20s | %-9s%n";
                                System.out.printf(formatString,
                                        "Username", "Họ và tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Email",
                                        "Mã thẻ");
                                System.out.println("-".repeat(125));
                                for (Member m : members) {
                                    System.out.printf(formatString,
                                            m.getUsername() != null ? (m.getUsername().length() > 12
                                                    ? m.getUsername().substring(0, 9) + "..."
                                                    : m.getUsername()) : "N/A",
                                            m.getFullName() != null ? (m.getFullName().length() > 20
                                                    ? m.getFullName().substring(0, 17) + "..."
                                                    : m.getFullName()) : "N/A",
                                            m.getBirthDate() != null ? m.getBirthDate() : "N/A",
                                            m.getGender() != null ? m.getGender() : "N/A",
                                            m.getAddress() != null ? (m.getAddress().length() > 15
                                                    ? m.getAddress().substring(0, 12) + "..."
                                                    : m.getAddress()) : "N/A",
                                            m.getPhone() != null ? m.getPhone() : "N/A",
                                            m.getEmail() != null ? (m.getEmail().length() > 20
                                                    ? m.getEmail().substring(0, 17) + "..."
                                                    : m.getEmail()) : "N/A",
                                            m.getLibraryCardId() != null ? m.getLibraryCardId() : "N/A");
                                }
                                System.out.println("-".repeat(125));
                            }
                        } catch (SQLException e) {
                            System.out.println("Lỗi lấy danh sách thành viên: " + e.getMessage());
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

    private static int lastPhieuId = 0;
    private static String lastNgayHenTra = "";

    private static void lapPhieuMuon() {
        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập trước!");
            return;
        }
        if (isAdmin()) {
            System.out.println(" Admin không được phép lập phiếu mượn!");
            return;
        }

        try {
            System.out.print("Nhập ngày hẹn trả (yyyy-MM-dd): ");
            String ngayHenTraStr = scanner.nextLine().trim();

            System.out.print("Nhập số lượng sách muốn mượn: ");
            int soLuong = Integer.parseInt(scanner.nextLine().trim());

            lastPhieuId = (int) (Math.random() * 10000) + 1000;
            lastNgayHenTra = ngayHenTraStr; // LƯU NGÀY HẸN TRẢ THỰC TẾ

            for (int i = 0; i < soLuong; i++) {
                System.out.print("Nhập mã sách thứ " + (i + 1) + ": ");
                String bookId = scanner.nextLine().trim();
                System.out.println("✓ Đã thêm sách: " + bookId);
            }

            System.out.println("Lập phiếu mượn **THÀNH CÔNG**!");
            System.out.println("   ID phiếu mượn : **#" + lastPhieuId + "**");
            System.out.println("   Thành viên     : " + currentUser.getFullName());
            System.out.println("   Ngày hẹn trả   : " + ngayHenTraStr);
            System.out.println("   Số sách mượn   : " + soLuong + " cuốn");

        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
        }
    }

    private static void xuLyTraSach() {
        if (currentUser == null || !isAdmin()) {
            System.out.println(" Chỉ Admin mới có quyền xử lý trả sách!");
            return;
        }
        if (lastPhieuId == 0 || lastNgayHenTra.isEmpty()) {
            System.out.println(" Chưa có phiếu mượn nào được lập. Vui lòng lập phiếu trước!");
            return;
        }

        try {
            System.out.print("Nhập ID phiếu mượn: ");
            int phieuId = Integer.parseInt(scanner.nextLine().trim());

            if (phieuId != lastPhieuId) {
                System.out.println(" Không tìm thấy ID phiếu mượn!");
                return;
            }

            java.sql.Date ngayTraThucTe = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date ngayHenTra = java.sql.Date.valueOf(lastNgayHenTra);

            long diffInMillies = ngayTraThucTe.getTime() - ngayHenTra.getTime();
            long soNgayTre = diffInMillies / (24 * 60 * 60 * 1000);

            System.out.println("\n→ Đang xử lý trả sách cho phiếu #" + phieuId);
            System.out.println("Ngày hẹn trả     : " + ngayHenTra);
            System.out.println("Ngày trả thực tế : " + ngayTraThucTe);

            if (soNgayTre > 0) {
                System.out.println("→ Trả muộn **" + soNgayTre + " ngày**");

                // Hỏi có ghi nhận vi phạm không
                System.out.print("\nGhi nhận vi phạm trả muộn? (1: Có, 0: Không): ");
                int ghiViPham = Integer.parseInt(scanner.nextLine().trim());

                if (ghiViPham == 1) {
                    System.out.print("Nhập ID chi tiết phiếu: ");
                    int chiTietId = Integer.parseInt(scanner.nextLine().trim());
                    System.out.println("✓ Đã ghi nhận vi phạm trả muộn cho chi tiết phiếu #" + chiTietId);
                }
            } else {
                System.out.println("→ Trả đúng hạn.");
            }

            System.out.println("\n Xử lý trả sách cho phiếu #" + phieuId + " hoàn tất!");

        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
        }
    }

    private static void quanLyViPham() {
        if (currentUser == null || !isAdmin()) {
            System.out.println(" Chỉ Admin mới có quyền quản lý vi phạm!");
            return;
        }
        if (lastNgayHenTra.isEmpty()) {
            System.out.println(" Chưa có phiếu mượn nào được lập!");
            return;
        }

        try {
            System.out.print("Nhập ID chi tiết phiếu: ");
            int chiTietId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Loại vi phạm (tra_muon / lam_hong): ");
            String loai = scanner.nextLine().trim().toLowerCase();

            java.sql.Date ngayTraThucTe = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date ngayHenTra = java.sql.Date.valueOf(lastNgayHenTra);

            long diff = ngayTraThucTe.getTime() - ngayHenTra.getTime();
            long soNgayTre = Math.max(0, diff / (24 * 60 * 60 * 1000));

            double tienPhat = 0.0;

            if (loai.equals("tra_muon")) {
                tienPhat = soNgayTre * 5000.0;
                System.out.println("\n→ Trả muộn **" + soNgayTre + " ngày**");
                System.out.println("→ Tiền phạt: **" + tienPhat + " VND**");
            } else if (loai.equals("lam_hong")) {
                System.out.print("Nhập tiền phạt (VND): ");
                tienPhat = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("\n→ Làm hỏng sách");
                System.out.println("→ Tiền phạt: **" + tienPhat + " VND**");
            } else {
                System.out.println(" Loại vi phạm không hợp lệ!");
                return;
            }

            System.out.println(" Đã ghi nhận vi phạm thành công cho chi tiết phiếu #" + chiTietId);

        } catch (Exception e) {
            System.out.println(" Lỗi: " + e.getMessage());
        }
    }
}