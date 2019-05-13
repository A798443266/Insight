package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.HomeGuidang;

import java.util.List;

public class HomeFragmentAdapter extends BaseAdapter {

    private List<HomeGuidang> datas;
    private Context context;

    public HomeFragmentAdapter(List<HomeGuidang> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_home_fragment,null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        HomeGuidang guidang = datas.get(position);
        holder.tv_name.setText(guidang.getName());
        holder.tv_num.setText(guidang.getNum()+ "  个");
        Glide.with(context).load(guidang.getPic()).error(R.drawable.city1).into(holder.iv);

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        TextView tv_num;
        ImageView iv;
    }
}
