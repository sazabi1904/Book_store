package com.library.model;

public class DeXuat {
    private int id;
    private int memberId;
    private String tenSach;
    private String tacGia;
    private String trangThai;
    private String memberName; 

    public DeXuat(int id, int memberId, String tenSach, String tacGia, String trangThai, String memberName) {
        this.id = id;
        this.memberId = memberId;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.trangThai = trangThai;
        this.memberName = memberName;
    }

    public int getId() { return id; }
    public int getMemberId() { return memberId; }
    public String getTenSach() { return tenSach; }
    public String getTacGia() { return tacGia; }
    public String getTrangThai() { return trangThai; }
    public String getMemberName() { return memberName; }
}
