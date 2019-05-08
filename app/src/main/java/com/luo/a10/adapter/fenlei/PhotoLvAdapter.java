package com.luo.a10.adapter.fenlei;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.view.MyRoundLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类详情照片的适配器
 */
public class PhotoLvAdapter extends BaseAdapter {

    private Context context;
    private List<String> pics;

    public PhotoLvAdapter(Context context, List<String> pics) {
        this.context = context;
        this.pics = pics;
    }

    @Override
    public int getCount() {
        return pics == null ? 0 : pics.size();
    }

    @Override
    public Object getItem(int position) {
        return pics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder holder;
        if (convertView == null) {
            holder = new Viewholder();
            convertView = View.inflate(context, R.layout.item_fenlei_lv_photo, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        if(position == 0){
            holder.tv_time.setText("2017年");
        }else if(position == 1){
            holder.tv_time.setText("2018年");
        }else if(position == 2){
            holder.tv_time.setText("2019年");
        }

        Glide.with(context).load(pics.get(position)).error(R.drawable.city1).into(holder.iv);
        return convertView;
    }


    static class Viewholder {
        ImageView iv;
        TextView tv_time;
        TextView tv_num;
    }
}
