package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.view.MyGridView;

import java.util.List;

public class PhotoFragment3Adapter extends BaseAdapter {

    private List<String> datas;
    private Context context;

    public PhotoFragment3Adapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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
            convertView = View.inflate(context, R.layout.item_photo_fragment3, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.ll = convertView.findViewById(R.id.ll);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_month = convertView.findViewById(R.id.tv_month);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.ll.setVisibility(View.VISIBLE);
        } else {
            holder.ll.setVisibility(View.GONE);
        }

        Glide.with(context).load(datas.get(position)).error(R.drawable.city1).into(holder.iv);
        return convertView;
    }


    static class ViewHolder {
        ImageView iv;
        TextView tv_name;
        TextView tv_size;
        TextView tv_time;
        TextView tv_month;
        LinearLayout ll;
    }
}
