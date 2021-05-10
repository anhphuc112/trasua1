package com.example.trsahm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    Database database;
    ArrayList<LoaiSP> list_loaisp;
    ListView lvLoaiSP;
    LoaiSPAdapter adapter;

    ArrayList<SanPham> list_fullSP;
    ArrayList<SanPham> list_sp;
    GridView gvSP;
    SanPhamAdapter adaptersp;

    TextView txtThongTin;
    Ban ban;

    ArrayList<CTHD> list_cthd;

    int soluong=0;
    int vitri=0;
    int REQUEST_CODE_EXIT = 123;
    NumberFormat formatter = new DecimalFormat("#,###đ");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        lvLoaiSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_sp.clear();
                for(SanPham sp:list_fullSP){
                    if(sp.getMaloai()==position+1){
                        list_sp.add(sp);
                    }
                }
                adaptersp.notifyDataSetChanged();
            }
        });
        gvSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CTHD cthd=new CTHD();
                cthd.setMaSP(list_sp.get(position).getMaSP());
                cthd.setMaHD(ban.getMaHD());
                checkSP(cthd);
                if(soluong==0){
                    cthd.setSoLuong(1);
                    list_cthd.add(cthd);
                    database.QueryData("INSERT INTO CTHD VALUES('"+cthd.getMaHD()+"',"+cthd.getMaSP()+",1)");
                }
                else{
                    cthd.setSoLuong(soluong+1);
                    list_cthd.set(vitri, cthd);
                    database.QueryData("UPDATE CTHD SET SoLuong="+list_cthd.get(vitri).getSoLuong()+" WHERE MaHD='"+cthd.getMaHD()+"' AND MaSP="+cthd.getMaSP());
                }
                txtThongTin.setText("   "+ban.getTenBan()+" - "+ban.getMaHD()+" - "+formatter.format(tinhtongtien(ban.getMaHD())));
                soluong=0;
                vitri=0;
            }
        });
    }
    private void init(){
        //LoaiSP
        database = new Database(this,"trasua.sqlite", null, 1);
        lvLoaiSP=findViewById(R.id.listviewLoaiSP);
        list_loaisp=new ArrayList<>();
        Cursor dataLoaiSP = database.GetData("SELECT * FROM LoaiSP");
        while (dataLoaiSP.moveToNext()){
            list_loaisp.add(new LoaiSP(dataLoaiSP.getInt(0),dataLoaiSP.getString(1)));
        }
        adapter=new LoaiSPAdapter(OrderActivity.this,R.layout.dong_loaisp,list_loaisp);
        lvLoaiSP.setAdapter(adapter);
        //SP
        gvSP=findViewById(R.id.listviewSanPham);
        list_sp=new ArrayList<>();
        list_fullSP=new ArrayList<>();
        Cursor dataSP = database.GetData("SELECT * FROM SanPham");
        while (dataSP.moveToNext()){
            SanPham sp=new SanPham(dataSP.getInt(0),dataSP.getString(1),dataSP.getInt(2),dataSP.getInt(3),dataSP.getBlob(4));
            list_fullSP.add(sp);
            if(sp.getMaloai()==1){
                list_sp.add(sp);
            }
        }
        adaptersp=new SanPhamAdapter(OrderActivity.this,R.layout.dong_sanpham,list_sp);
        gvSP.setAdapter(adaptersp);
        ban=new Ban();
        txtThongTin=findViewById(R.id.textviewThongTin);
        Intent intent=getIntent();
        ban = (Ban) intent.getSerializableExtra("dulieu");
        txtThongTin.setText("   "+ban.getTenBan()+" - "+ban.getMaHD()+" - "+formatter.format(tinhtongtien(ban.getMaHD())));

        list_cthd=new ArrayList<>();
        Cursor dataCTHD = database.GetData("SELECT * FROM CTHD");
        while (dataCTHD.moveToNext()){
            CTHD cthd=new CTHD(dataCTHD.getString(0),dataCTHD.getInt(1),dataCTHD.getInt(2));
            list_cthd.add(cthd);
        }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuXoaBan:
                XacNhanXoa();
                break;
            case R.id.menuGioHang:
                Intent intent=new Intent(OrderActivity.this,BillActivity.class);
                intent.putExtra("dulieuban", ban);
                startActivityForResult(intent, REQUEST_CODE_EXIT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void XoaBan(Ban ban){
        database.QueryData("DELETE FROM HoaDon WHERE MaHD='"+ban.getMaHD()+"'");
        database.QueryData("UPDATE Ban SET MaHD=null, TrangThai='Còn trống' WHERE TenBan='"+ban.getTenBan()+"'");
        database.QueryData("DELETE FROM CTHD WHERE MaHD='"+ban.getMaHD()+"'");
    }
    private void XacNhanXoa(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_xoaban);
        final TextView txtNoiDung= dialog.findViewById(R.id.textViewNoidungxoaban);
        final EditText txtPassword= dialog.findViewById(R.id.edittextPassword);
        int tongtien=tinhtongtien(ban.getMaHD());
        txtNoiDung.setText("Bạn có chắc xóa bàn "+ban.getTenBan()+"("+ban.getMaHD()+" - "+formatter.format(tongtien)+") không ?");
        Button btnLXoaBan=dialog.findViewById(R.id.buttonXoaBan);
        Button btnExit=dialog.findViewById(R.id.buttonExit);
        btnLXoaBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=txtPassword.getText().toString().trim();
                if(pass.equals("123456")){
                    XoaBan(ban);
                    dialog.dismiss();
                    Toast.makeText(OrderActivity.this,"Đã xóa "+ban.getTenBan(),Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(OrderActivity.this,"Bạn nhập sai mật khẩu",Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void checkSP(CTHD cthd){
        for(int i=0;i<list_cthd.size();i++){
            if(list_cthd.get(i).getMaSP()==cthd.getMaSP() && list_cthd.get(i).getMaHD().equals(cthd.getMaHD())){
                soluong=list_cthd.get(i).getSoLuong();
                vitri=i;
            }
        }
    }
    public SanPham GetSP(int masp){
        SanPham sp=new SanPham();
        Cursor dataSP = database.GetData("SELECT * FROM SanPham WHERE MaSP="+masp);
        while (dataSP.moveToNext()){
            sp=new SanPham(dataSP.getInt(0),dataSP.getString(1),dataSP.getInt(2),dataSP.getInt(3));
        }
        return sp;
    }
    public int tinhtongtien(String mahd){
        int tong=0;
        Cursor dataHD = database.GetData("SELECT * FROM CTHD WHERE MaHD='"+mahd+"'");
        while (dataHD.moveToNext()){
            SanPham sp=GetSP(dataHD.getInt(1));
            tong+=sp.getGia()*dataHD.getInt(2);
        }
        return tong;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_EXIT && data!=null && resultCode==RESULT_OK){
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }
}
