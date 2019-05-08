package com.luo.a10.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.InnerItem;

import java.util.List;

//listview中内部gridView的适配器
public class MyGridViewAdapter extends BaseAdapter {
    private Context context;
    private int flag;
    private String[] typenames;//分类名称
    private String[] pics; //每个分类对应的图片


    public MyGridViewAdapter(String[] typenames, String[] pics, Context context, int flag) {
        this.context = context;
        this.flag = flag;
        this.typenames = typenames;
        this.pics = pics;
    }


    @Override
    public int getCount() {
        return typenames == null ? 0 : typenames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (flag == 0) {
                convertView = View.inflate(context, R.layout.item_list_inner, null);
                holder.iv = convertView.findViewById(R.id.iv);
                holder.tv_city = convertView.findViewById(R.id.tv_city);
                convertView.setTag(holder);
            } else {
                convertView = View.inflate(context, R.layout.item_list_inner_1, null);
                holder.iv = convertView.findViewById(R.id.iv);
                holder.tv_type = convertView.findViewById(R.id.tv_type);
                convertView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (flag == 0) { //地点
            holder.tv_city.setText(typenames[position]);
            Glide.with(context).load(pics[position]).error(R.drawable.city1).into(holder.iv);
        } else {//其他
            Glide.with(context).load(pics[position]).error(R.drawable.city1).into(holder.iv);
            holder.tv_type.setText(typenames[position]);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_type;
        TextView tv_city;
        ImageView iv;
    }
}
