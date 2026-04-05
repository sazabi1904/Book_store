package com.library.model;

import java.sql.Date;

public class ChiTietPhieuMuon {
    private int id;
    private int phieuMuonId;
    private int bookId;
    private Date ngayTraThucTe;

    public ChiTietPhieuMuon() {}

    public int getId() { return id; }
    public int getPhieuMuonId() { return phieuMuonId; }
    public int getBookId() { return bookId; }
    public Date getNgayTraThucTe() { return ngayTraThucTe; }
}