package com.example.trsahm;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int MaSP;
    private String TenSP;
    private int gia;
    private int maloai;
    private byte[] imgHinh;

    public SanPham() {
    }

    public SanPham(int maSP, String tenSP, int gia, int maloai) {
        MaSP = maSP;
        TenSP = tenSP;
        this.gia = gia;
        this.maloai = maloai;
    }

    public SanPham(int maSP, String tenSP, int gia, int maloai, byte[] imgHinh) {
        MaSP = maSP;
        TenSP = tenSP;
        this.gia = gia;
        this.maloai = maloai;
        this.imgHinh = imgHinh;
    }

    public byte[] getImgHinh() {
        return imgHinh;
    }

    public void setImgHinh(byte[] imgHinh) {
        this.imgHinh = imgHinh;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int maSP) {
        MaSP = maSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }
}
