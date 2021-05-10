package com.example.trsahm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddSPActivity extends AppCompatActivity {
    ImageView imgHinhAnh;
    ImageButton btnCam, btnFolder;
    EditText edtTenSP,edtGiaSP;
    Spinner spinnerLoaiSP;
    Database database;
    Button btnAdd;
    final int REQUEST_CODE_CAMERA=123;
    final int REQUEST_CODE_FOLDER=456;

    ArrayList<LoaiSP> list_loaisp;
    ArrayList<String> tenlsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_s_p);
        init();
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddSPActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
            }
        });
        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddSPActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_FOLDER);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable= (BitmapDrawable) imgHinhAnh.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
                byte[] hinhanh=byteArray.toByteArray();
                String tensp=edtTenSP.getText().toString().trim();
                int giasp=Integer.parseInt(String.valueOf(edtGiaSP.getText()));
                int maloaisp=spinnerLoaiSP.getSelectedItemPosition()+1;
                if(tensp.equals("") || giasp <= 0 || maloaisp <= 0 || hinhanh==null){
                    Toast.makeText(AddSPActivity.this, "Dữ liệu không hợp lệ",Toast.LENGTH_SHORT).show();
                }
                else{
                    database.INSERT_SP(
                            edtTenSP.getText().toString().trim(),
                            Integer.parseInt(String.valueOf(edtGiaSP.getText())),
                            spinnerLoaiSP.getSelectedItemPosition()+1,
                            hinhanh
                    );
                    Toast.makeText(AddSPActivity.this,"Đã thêm",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void init() {
        database = new Database(this,"trasua.sqlite", null, 1);
        imgHinhAnh = findViewById(R.id.imageViewSanPham_ADDSP);
        btnAdd = findViewById(R.id.buttonAdd_ADDSP);
        btnCam = findViewById(R.id.imageButtonCamera_ADDSP);
        btnFolder = findViewById(R.id.imageButtonFolder_ADDSP);
        edtTenSP = findViewById(R.id.editTextTenSP_ADDSP);
        edtGiaSP = findViewById(R.id.editTextGiaSP_ADDSP);
        spinnerLoaiSP = findViewById(R.id.spinnerLoaiSP_ADDSP);
        list_loaisp=new ArrayList<>();
        tenlsp=new ArrayList<>();
        Cursor cursor = database.GetData("SELECT * FROM LoaiSP");
        while (cursor.moveToNext()){
            list_loaisp.add(new LoaiSP(cursor.getInt(0),cursor.getString(1)));
            tenlsp.add(cursor.getString(1));
        }
        SpinnerAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,tenlsp);
        spinnerLoaiSP.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CODE_CAMERA);
                }
                break;
            case REQUEST_CODE_FOLDER:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,REQUEST_CODE_FOLDER);
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_CAMERA && resultCode==RESULT_OK && data!=null){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imgHinhAnh.setImageBitmap(bitmap);
        }
        if(requestCode==REQUEST_CODE_FOLDER && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imgHinhAnh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
