package com.example.trsahm;

public class CTHD {
    private String MaHD;
    private int MaSP;
    private int SoLuong;

    public CTHD() {
    }

    public CTHD(String maHD, int maSP, int soLuong) {
        MaHD = maHD;
        MaSP = maSP;
        SoLuong = soLuong;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int maSP) {
        MaSP = maSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

}
