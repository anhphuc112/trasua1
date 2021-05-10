package com.example.trsahm;

public class HoaDon {
    private String MaHD;
    private String GioBan;
    private int TongTien;

    public HoaDon() {
    }

    public HoaDon(String maHD, String gioBan, int tongTien) {
        MaHD = maHD;
        GioBan = gioBan;
        TongTien = tongTien;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public String getGioBan() {
        return GioBan;
    }

    public void setGioBan(String gioBan) {
        GioBan = gioBan;
    }

    public int getTongTien() {
        return TongTien;
    }

    public void setTongTien(int tongTien) {
        TongTien = tongTien;
    }
}
