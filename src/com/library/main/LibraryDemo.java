package com.library.main;

import java.util.*;

class Account {
    protected String username;
    protected String password;
    protected String role;

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String user, String pass) {
        return this.username.equals(user) && this.password.equals(pass);
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}

class Member extends Account {
    private String fullName;
    private String birthDate;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String libraryCardId;

    public Member(String username, String password, String fullName, String birthDate,
            String gender, String address, String phone, String email, String libraryCardId) {
        super(username, password, "READER");
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.libraryCardId = libraryCardId;
    }

    public void showProfile() {
        System.out.println("====== Hồ sơ cá nhân ======");
        System.out.println("Tên: " + fullName);
        System.out.println("Ngày sinh: " + birthDate);
        System.out.println("Giới Tính: " + gender);
        System.out.println("Địa chỉ: " + address);
        System.out.println("Số điện thoại: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Mã thẻ thư viện: " + libraryCardId);
    }

    public void updateProfile(String address, String phone, String email, String libraryCardId) {
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.libraryCardId = libraryCardId;
        System.out.println("Thông tin cá nhân được cập nhật!");
    }
}

class LibrarySystem {
    private List<Account> accounts = new ArrayList<>();
    private Account currentUser = null;

    public void addAccount(Account acc) {
        accounts.add(acc);
    }

    public void login(String user, String pass) {
        if (this.currentUser != null) {
            System.out.println("LỖI: Tài khoản [" + currentUser.getUsername() + "] đang sử dụng hệ thống.");
            System.out.println("Vui lòng Đăng xuất trước khi đăng nhập tài khoản khác!");
            return;
        }
        for (Account acc : accounts) {
            if (acc.login(user, pass)) {
                currentUser = acc;
                System.out.println("Đăng nhập thành công! Xin chào " + acc.getUsername());
                return;
            }
        }
        System.out.println("Sai tên đăng nhập hoặc mật khẩu! Vui lòng thử lại.");
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("ĐÃ ĐĂNG XUẤT THÀNH CÔNG! TẠM BIỆT " + currentUser.getUsername());
            currentUser = null;
        } else {
            System.out.println("CHƯA CÓ NGƯỜI DÙNG NÀO ĐĂNG NHẬP!");
        }
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public void deleteMember(String username) {
        for (Account acc : accounts) {
            if (acc instanceof Member && acc.getUsername().equals(username)) {
                accounts.remove(acc);
                System.out.println("ĐÃ XÓA THÀNH VIÊN: " + username);
                return;
            }
        }
        System.out.println("KHÔNG TÌM THẤY THÀNH VIÊN CÓ TÊN ĐĂNG NHẬP: " + username);
    }
}

public class LibraryDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LibrarySystem system = new LibrarySystem();
        system.addAccount(new Account("admin", "123", "ADMIN"));

        while (true) {
            System.out.println("\n===== Hệ thống thư viện =====");
            System.out.println("1. ĐĂNG NHẬP");
            System.out.println("2. ĐĂNG XUẤT");
            System.out.println("3. QUẢN LÍ THÔNG TIN CÁ NHÂN");
            System.out.println("4. THOÁT");
            System.out.print("CHỌN: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("TÊN ĐĂNG NHẬP: ");
                    String user = sc.nextLine();
                    System.out.print("MẬT KHẨU: ");
                    String pass = sc.nextLine();
                    system.login(user, pass);
                    break;
                case 2:
                    system.logout();
                    break;
                case 3:
                    if (system.getCurrentUser() == null) {
                        System.out.println("Bạn cần đăng nhập trước!");
                        break;
                    }

                    Account current = system.getCurrentUser();

                    if ("ADMIN".equals(current.getRole())) {
                        // Menu cho Admin
                        System.out.println("=== Quản lý thông tin cá nhân ===");
                        System.out.println("1. Thêm thành viên mới");
                        System.out.println("2. Xóa thành viên");
                        System.out.println("3. Quay lại");
                        System.out.print("Chọn: ");
                        int adminChoice = sc.nextInt();
                        sc.nextLine();

                        switch (adminChoice) {
                            case 1:
                                System.out.print("TÊN ĐĂNG NHẬP: ");
                                String uname = sc.nextLine();
                                System.out.print("MẬT KHẨU: ");
                                String pwd = sc.nextLine();
                                System.out.print("HỌ VÀ TÊN: ");
                                String name = sc.nextLine();
                                System.out.print("NGÀY SINH: ");
                                String birth = sc.nextLine();
                                System.out.print("GIỚI TÍNH: ");
                                String gender = sc.nextLine();
                                System.out.print("ĐỊA CHỈ: ");
                                String address = sc.nextLine();
                                System.out.print("SỐ ĐIỆN THOẠI: ");
                                String phone = sc.nextLine();
                                System.out.print("EMAIL: ");
                                String email = sc.nextLine();
                                System.out.print("MÃ THẺ THƯ VIỆN: ");
                                String card = sc.nextLine();

                                system.addAccount(
                                        new Member(uname, pwd, name, birth, gender, address, phone, email, card));
                                System.out.println("ĐĂNG KÍ THÀNH VIÊN THÀNH CÔNG!");
                                break;
                            case 2:
                                System.out.print("NHẬP TÊN ĐĂNG NHẬP CỦA THÀNH VIÊN CẦN XÓA: ");
                                String delUser = sc.nextLine();
                                system.deleteMember(delUser);
                                break;
                            case 3:
                                break;
                            default:
                                System.out.println("Lựa chọn không hợp lệ!");
                        }
                    } else if ("READER".equals(current.getRole())) {
                        // Menu cho Reader
                        System.out.println("=== Quản lý thông tin cá nhân ===");
                        System.out.println("1. Xem hồ sơ");
                        System.out.println("2. Cập nhật hồ sơ");
                        System.out.println("3. Quay lại");
                        System.out.print("Chọn: ");
                        int readerChoice = sc.nextInt();
                        sc.nextLine();

                        Member m = (Member) current;
                        switch (readerChoice) {
                            case 1:
                                m.showProfile();
                                break;
                            case 2:
                                System.out.print("CẬP NHẬT ĐỊA CHỈ MỚI: ");
                                String newAddress = sc.nextLine();
                                System.out.print("CẬP NHẬT SỐ ĐIỆN THOẠI MỚI: ");
                                String newPhone = sc.nextLine();
                                System.out.print("CẬP NHẬT EMAIL MỚI: ");
                                String newEmail = sc.nextLine();
                                System.out.print("CẬP NHẬT MÃ THẺ THƯ VIỆN MỚI: ");
                                String newCard = sc.nextLine();
                                m.updateProfile(newAddress, newPhone, newEmail, newCard);
                                break;
                            case 3:
                                break;
                            default:
                                System.out.println("Lựa chọn không hợp lệ!");
                        }
                    }
                    break;
                case 4:
                    System.out.println("THOÁT CHƯƠNG TRÌNH...");
                    return;
                default:
                    System.out.println("LỰA CHỌN KHÔNG HỢP LỆ!");
            }
        }
    }
}
