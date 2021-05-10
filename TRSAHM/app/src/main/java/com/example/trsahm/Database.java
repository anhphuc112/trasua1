package com.example.trsahm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // truy vấn k trà kết quà create insert update delete
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    // truy vấn trả kết quả select
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    public void INSERT_SP(String ten,int gia,int loai,byte[] hinh){
        SQLiteDatabase database = getWritableDatabase();
        String sql="INSERT INTO SanPham VALUES(null,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,ten);
        statement.bindLong(2,gia);
        statement.bindLong(3,loai);
        statement.bindBlob(4,hinh);

        statement.executeInsert();
    }
    public void EDIT_SP(int masp,String ten,int gia,int loai,byte[] hinh){
        SQLiteDatabase database = getWritableDatabase();
        String sql="UPDATE SanPham SET TenSP=?, Gia=?, MaLoai=?, HinhAnh=? WHERE MaSP=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,ten);
        statement.bindLong(2,gia);
        statement.bindLong(3,loai);
        statement.bindBlob(4,hinh);
        statement.bindLong(5,masp);

        statement.executeInsert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
