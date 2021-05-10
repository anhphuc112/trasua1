package com.example.trsahm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BanAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Ban> list_ban;

    public BanAdapter(Context context, int layout, ArrayList<Ban> list_ban) {
        this.context = context;
        this.layout = layout;
        this.list_ban = list_ban;
    }

    @Override
    public int getCount() {
        return list_ban.size();
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
        TextView txtTenBan, txtTrangThai;
        ImageView imgBan;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if(convertView == null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            holder.txtTenBan = convertView.findViewById(R.id.textViewTenBan);
            holder.txtTrangThai = convertView.findViewById(R.id.textViewTrangThai);
            holder.imgBan = convertView.findViewById(R.id.imageViewBan);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }


        Ban ban=list_ban.get(position);

        holder.txtTenBan.setText(ban.getTenBan());
        holder.txtTrangThai.setText(ban.getTrangThai());
        holder.imgBan.setImageResource(ban.getHinhBan());
        if(ban.getTrangThai().equals("Còn trống")){
            holder.txtTrangThai.setTextColor(Color.RED);
        }
        else{
            holder.txtTrangThai.setTextColor(Color.rgb(15, 171, 46));
        }

        return  convertView;
    }
}
