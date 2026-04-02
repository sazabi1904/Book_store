package com.library.model;

public class Member extends Account {
    private int id; // From database
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

    // Constructor with ID from DB
    public Member(int id, String username, String password, String fullName, String birthDate,
                  String gender, String address, String phone, String email, String libraryCardId) {
        this(username, password, fullName, birthDate, gender, address, phone, email, libraryCardId);
        this.id = id;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getLibraryCardId() { return libraryCardId; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    public void showProfile(){
        System.out.println("====== Hồ sơ cá nhân ======");
        System.out.println("Tên: " + fullName);
        System.out.println("Ngày sinh: " + birthDate);
        System.out.println("Giới tính: " + gender);
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
