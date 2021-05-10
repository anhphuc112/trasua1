package com.example.trsahm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MSanPhamAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<SanPham> lis_sp;
    NumberFormat formatter = new DecimalFormat("#,###đ");

    public MSanPhamAdapter(Context context,int layout,ArrayList<SanPham> lis_sp) {
        this.context = context;
        this.layout = layout;
        this.lis_sp = lis_sp;
    }
    @Override
    public int getCount() {
        return lis_sp.size();
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
        TextView txtTTSP;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder holder ;

        if(convertView == null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            holder.txtTTSP = convertView.findViewById(R.id.textViewTTSP);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        SanPham sp = lis_sp.get(position);
        holder.txtTTSP.setText(sp.getTenSP()+" - "+formatter.format(sp.getGia()));

        return  convertView;
    }
}
