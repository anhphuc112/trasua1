package com.example.trsahm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LoaiSPAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<LoaiSP> list_loaisp;

    public LoaiSPAdapter(Context context, int layout, ArrayList<LoaiSP> list_loaisp) {
        this.context = context;
        this.layout = layout;
        this.list_loaisp = list_loaisp;
    }

    @Override
    public int getCount() {
        return list_loaisp.size();
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
        TextView txtName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if(convertView == null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            holder.txtName = convertView.findViewById(R.id.textviewLoaiSP);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }


        LoaiSP loai=list_loaisp.get(position);

        holder.txtName.setText(loai.getTenLoai());

        return  convertView;
    }
}
