package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.Guidang;

import java.util.List;

public class FenleiFragmentAdapter extends BaseAdapter {

    private Context context;
    private List<Guidang> datas;

    public FenleiFragmentAdapter(Context context, List<Guidang> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
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
            convertView = View.inflate(context, R.layout.item_fragment_fenlei, null);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_type.setText(datas.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_type;
    }
}
