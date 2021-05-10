package com.example.trsahm;

import java.io.Serializable;

public class Ban implements Serializable {
    private String TenBan;
    private String MaHD;
    private String TrangThai;
    private int HinhBan;

    public Ban() {
    }

    public Ban(String tenBan, String maHD, String trangThai) {
        TenBan = tenBan;
        MaHD = maHD;
        TrangThai = trangThai;
    }

    public Ban(String tenBan, String maHD, String trangThai, int hinhBan) {
        TenBan = tenBan;
        MaHD = maHD;
        TrangThai = trangThai;
        HinhBan = hinhBan;
    }

    public String getTenBan() {
        return TenBan;
    }

    public void setTenBan(String tenBan) {
        TenBan = tenBan;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public int getHinhBan() {
        return HinhBan;
    }

    public void setHinhBan(int hinhBan) {
        HinhBan = hinhBan;
    }
}
