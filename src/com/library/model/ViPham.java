package com.library.model;

public class ViPham {
    private int id;
    private int chiTietPhieuMuonId;
    private String loaiViPham; // tra_muon hoặc lam_hong
    private double tienPhat;

    public ViPham() {
    }

    public int getId() {
        return id;
    }

    public int getChiTietPhieuMuonId() {
        return chiTietPhieuMuonId;
    }

    public String getLoaiViPham() {
        return loaiViPham;
    }

    public double getTienPhat() {
        return tienPhat;
    }
}