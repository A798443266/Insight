package com.luo.a10.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;

import java.util.List;

//PhotoFragment1listview中内部gridView的适配器
public class MyGridViewAdapter1 extends BaseAdapter {
    private List<String> pics;
    private Context context;


    public MyGridViewAdapter1(List<String> pics, Context context) {
        this.context = context;
        this.pics = pics;
    }

    @Override
    public int getCount() {
        return pics == null ? 0 : pics.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
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
            convertView = View.inflate(context, R.layout.item_list_inner1, null);
            holder.iv = convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String pic = pics.get(position);
        Glide.with(context).load(pic).error(R.drawable.city1).into(holder.iv);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
    }
}
