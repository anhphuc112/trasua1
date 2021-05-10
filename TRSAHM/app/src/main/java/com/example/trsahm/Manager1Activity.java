package com.example.trsahm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Manager1Activity extends AppCompatActivity {
    ListView lvSanPham;
    ArrayList<SanPham> list_sp;
    MSanPhamAdapter adapter;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager1);
        init();
        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
                Intent intent = new Intent(Manager1Activity.this, EditSPActivity.class);
                intent.putExtra("dataSP", list_sp.get(position));
                startActivity(intent);
            }
        });
    }
    private void init() {
        lvSanPham=findViewById(R.id.listviewSanPham_M);
        list_sp = new ArrayList<>();
        adapter = new MSanPhamAdapter(this,R.layout.dong_spmanager,list_sp);
        lvSanPham.setAdapter(adapter);
        database = new Database(this,"trasua.sqlite", null, 1);
        Cursor cursor = database.GetData("SELECT * FROM SanPham");
        while (cursor.moveToNext()){
            list_sp.add(new SanPham(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getBlob(4)));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addsp_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAddSP:
                startActivity(new Intent(Manager1Activity.this,AddSPActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }


}
