package com.example.trsahm;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class SanPhamAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<SanPham> list_sp;
    NumberFormat formatter;

    public SanPhamAdapter(Context context, int layout, ArrayList<SanPham> list_sp) {
        this.context = context;
        this.layout = layout;
        this.list_sp = list_sp;
    }

    @Override
    public int getCount() {
        return list_sp.size();
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
        ImageView imgHinh;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if(convertView == null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            holder.txtTenSP = convertView.findViewById(R.id.textviewTenSP);
            holder.txtGia = convertView.findViewById(R.id.textviewGiaSP);
            holder.imgHinh = convertView.findViewById(R.id.imageviewHinh);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }


        SanPham sp=list_sp.get(position);

        formatter = new DecimalFormat("#,###đ");
        long myNumber = sp.getGia();
        String formattedNumber = formatter.format(myNumber);

        holder.txtTenSP.setText(sp.getTenSP());
        holder.txtGia.setText(formattedNumber);

        byte[] hinhAnh=sp.getImgHinh();
        holder.imgHinh.setImageBitmap(BitmapFactory.decodeByteArray(hinhAnh,0,hinhAnh.length));

        return  convertView;
    }
}
