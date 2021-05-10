package com.example.trsahm;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class HoaDonAdapter extends BaseAdapter {
    private BillActivity context;
    private int layout;
    private ArrayList<CTHD> list_cthd;
    NumberFormat formatter = new DecimalFormat("#,###đ");

    public HoaDonAdapter(BillActivity context, int layout, ArrayList<CTHD> list_cthd) {
        this.context = context;
        this.layout = layout;
        this.list_cthd = list_cthd;
    }

    @Override
    public int getCount() {
        return list_cthd.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTenSP, txtGia;
        TextView edtSoLuong;
        Button btnCong,btnTru;
        ImageView imgXoaSP;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder ;

        if(convertView == null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            holder.txtTenSP = convertView.findViewById(R.id.textViewCTHD_TenSP);
            holder.txtGia = convertView.findViewById(R.id.textViewCTHD_Gia);
            holder.edtSoLuong = convertView.findViewById(R.id.editTextCTHD_SoLuong);
            holder.btnCong = convertView.findViewById(R.id.buttonCong1SP);
            holder.btnTru = convertView.findViewById(R.id.buttonTru1SP);
            holder.imgXoaSP = convertView.findViewById(R.id.imageViewXoaSP);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }


        final CTHD cthd=list_cthd.get(position);

        final Database database = new Database(this.context,"trasua.sqlite", null, 1);
        SanPham sp = new SanPham();
        Cursor dataSP = database.GetData("SELECT * FROM SanPham WHERE MaSP="+cthd.getMaSP());
        while (dataSP.moveToNext()){
            sp=new SanPham(dataSP.getInt(0),dataSP.getString(1),dataSP.getInt(2),dataSP.getInt(3));
        }
        holder.txtTenSP.setText(sp.getTenSP());
        holder.txtGia.setText(formatter.format(sp.getGia()));
        holder.edtSoLuong.setText(cthd.getSoLuong()+"");
        holder.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtSoLuong.setText((cthd.getSoLuong()+1)+"");
                database.QueryData("UPDATE CTHD SET SoLuong="+(cthd.getSoLuong()+1)+" WHERE MaHD='"+cthd.getMaHD()+"' AND MaSP='"+cthd.getMaSP()+"'");
                cthd.setSoLuong(cthd.getSoLuong()+1);
                list_cthd.set(position, cthd);
            }
        });
        holder.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cthd.getSoLuong()!=1){
                    holder.edtSoLuong.setText((cthd.getSoLuong()-1)+"");
                    database.QueryData("UPDATE CTHD SET SoLuong="+(cthd.getSoLuong()-1)+" WHERE MaHD='"+cthd.getMaHD()+"' AND MaSP='"+cthd.getMaSP()+"'");
                    cthd.setSoLuong(cthd.getSoLuong()-1);
                    list_cthd.set(position, cthd);
                }
            }
        });
        holder.imgXoaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogXoaSP(position);
            }
        });
        return  convertView;
    }
}
