package com.luo.a10.fragment.others;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luo.a10.R;

import java.util.List;

class DownloadAdapter extends BaseAdapter {

    private List<String> datas;
    private Context context;

    public DownloadAdapter(List<String> datas, Context context) {
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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_download,null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_current = convertView.findViewById(R.id.tv_current);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.iv = convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_current;
        TextView tv_size;
        ImageView iv;
    }
}
