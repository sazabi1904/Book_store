package com.library.model;

import java.sql.Date;

public class PhieuMuon {
    private int id;
    private int memberId;
    private Date ngayMuon;
    private Date ngayHenTra;

    public PhieuMuon() {
    }

    public int getId() {
        return id;
    }

    public int getMemberId() {
        return memberId;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public Date getNgayHenTra() {
        return ngayHenTra;
    }
}