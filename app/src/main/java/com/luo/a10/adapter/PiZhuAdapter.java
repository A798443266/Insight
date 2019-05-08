package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.luo.a10.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PiZhuAdapter extends BaseAdapter {

    private Context context;
    private List<String> datas;

    public PiZhuAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
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
            convertView = View.inflate(context,R.layout.item_pizhu,null);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tv_content.setText(datas.get(position));
        holder.tv_time.setText(sdf.format(new Date()));

        return convertView;
    }

    static class ViewHolder{
        TextView tv_content;
        TextView tv_time;
        CircleImageView cv_user_pic;
    }
}
