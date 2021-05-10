package com.example.trsahm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Database database;
    ArrayList<Ban> list_ban;
    GridView gvBan;
    BanAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        database.QueryData("DROP TABLE LoaiSP");
//        database.QueryData("UPDATE Ban SET MaHD=null, TrangThai='Còn trống' WHERE 1");
//        database.QueryData("DELETE FROM HoaDon WHERE 1");
//        database.QueryData("DELETE FROM CTHD WHERE 1");
//        database.QueryData("DELETE FROM LoaiSP WHERE 1");
//        database.QueryData("DELETE FROM San WHERE 1");


        gvBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ban ban=list_ban.get(position);
                if(ban.getTrangThai().equals("Còn trống")){
                    ThemBan(position);
                }
                else{
                    Intent intent=new Intent(MainActivity.this,OrderActivity.class);
                    intent.putExtra("dulieu",ban);
                    startActivity(intent);
                }
            }
        });




        //tao bảng
        database.QueryData("CREATE TABLE IF NOT EXISTS LoaiSP(MaLoai INTEGER PRIMARY KEY AUTOINCREMENT, TenLoai NVARCHAR(50))");
        //insert
//        database.QueryData("INSERT INTO LoaiSP VALUES(null, 'Trà sữa')");
//        database.QueryData("INSERT INTO LoaiSP VALUES(null, 'Trà trái cây')");
//        database.QueryData("INSERT INTO LoaiSP VALUES(null, 'Ăn vặt')");
        database.QueryData("CREATE TABLE IF NOT EXISTS SanPham(MaSP INTEGER PRIMARY KEY AUTOINCREMENT, TenSP NVARCHAR(200), Gia INTEGER, MaLoai INTEGER, HinhAnh BLOB, FOREIGN KEY (MaLoai) REFERENCES LoaiSP (MaLoai))");
        database.QueryData("CREATE TABLE IF NOT EXISTS HoaDon(MaHD VARCHAR(20) PRIMARY KEY, GioBan DATETIME, TongTien INTEGER)");
        database.QueryData("CREATE TABLE IF NOT EXISTS CTHD(MaHD VARCHAR(20), MaSP INTEGER, SoLuong INTEGER, FOREIGN KEY (MaHD) REFERENCES HoaDon (MaHD), FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP))");
        database.QueryData("CREATE TABLE IF NOT EXISTS Ban(TenBan NVARCHAR(10) PRIMARY KEY, MaHD VARCHAR(20), TrangThai NVARCHAR(30), FOREIGN KEY (MaHD) REFERENCES HoaDon (MaHD))");

//        database.QueryData("INSERT INTO SanPham VALUES(1, 'Tra sua truyen thong', 15000, 1)");
//        database.QueryData("INSERT INTO SanPham VALUES(2, 'Tra sua thai', 15000, 1)");
//        database.QueryData("INSERT INTO SanPham VALUES(3, 'Tra dao', 15000, 2)");
//        database.QueryData("INSERT INTO SanPham VALUES(4, 'Tra vai', 15000, 2)");
//        database.QueryData("INSERT INTO SanPham VALUES(5, 'Tra oi', 15000, 2)");
//        database.QueryData("INSERT INTO SanPham VALUES(6, 'Banh trang muoi', 10000, 3)");
//        database.QueryData("INSERT INTO SanPham VALUES(7, 'Banh trang mo hanh', 10000, 3)");

//        database.QueryData("INSERT INTO Ban VALUES('Bàn 1', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 2', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 3', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 4', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 5', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 6', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 7', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 8', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 9', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 10', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 11', null, 'Còn trống')");
//        database.QueryData("INSERT INTO Ban VALUES('Bàn 12', null, 'Còn trống')");





        //select
//        String dulieu="";
//        Cursor dataLoaiSP = database.GetData("SELECT * FROM LoaiSP");
//        while(dataLoaiSP.moveToNext()){
//            dulieu+=dataLoaiSP.getString(1)+"\n";
//        }
//        Toast.makeText(MainActivity.this,dulieu,Toast.LENGTH_SHORT).show();
    }
    private void init(){
        list_ban=new ArrayList<>();
        gvBan=findViewById(R.id.gridviewBan);
        database = new Database(this,"trasua.sqlite", null, 1);
        adapter=new BanAdapter(MainActivity.this,R.layout.dong_ban,list_ban);
        Cursor dataBan=database.GetData("SELECT * FROM Ban");
        while (dataBan.moveToNext()){
            Ban ban=new Ban(dataBan.getString(0),dataBan.getString(1),dataBan.getString(2));
            if(ban.getTrangThai().equals("Còn trống")){
                ban.setHinhBan(R.drawable.banvang);
            }
            else{
                ban.setHinhBan(R.drawable.banxanh);
            }
            list_ban.add(ban);
        }
        gvBan.setAdapter(adapter);
    }
    private void ThemBan(final int position){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Thông báo");
        alertDialog.setIcon(R.drawable.thongbaothem);
        alertDialog.setMessage("Bạn có muốn thêm "+list_ban.get(position).getTenBan()+" không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int dem=1;
                Cursor dataHD = database.GetData("SELECT * FROM HoaDon");
                while(dataHD.moveToNext()){
                    dem++;
                }
                String mahd="HD"+(position+1);
                database.QueryData("INSERT INTO HoaDon VALUES('"+mahd+"', null, 0)");
                database.QueryData("UPDATE Ban SET MaHD='"+mahd+"', TrangThai='Có khách' WHERE TenBan='"+list_ban.get(position).getTenBan()+"'");
                list_ban.set(position, new Ban(list_ban.get(position).getTenBan(),mahd,"Có khách",R.drawable.banxanh));
                Intent intent=new Intent(MainActivity.this,OrderActivity.class);
                intent.putExtra("dulieu",list_ban.get(position));
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

        @Override
        protected void onStart() {
            super.onStart();

        }

        @Override
        protected void onStop() {
            super.onStop();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
        }

        @Override
        protected void onPause() {
            super.onPause();
        }

        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        protected void onRestart() {
            init();
            super.onRestart();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuQLSP:
                startActivity(new Intent(MainActivity.this,Manager1Activity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
