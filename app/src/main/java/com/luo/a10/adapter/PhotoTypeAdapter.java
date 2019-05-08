package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.bean.ClassificationItem;
import com.luo.a10.view.MyGridView;

import java.util.List;

public class PhotoTypeAdapter extends BaseAdapter {

    private List<ClassificationItem> datas;
    private Context context;

    public PhotoTypeAdapter(List<ClassificationItem> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            if (getItemViewType(position) == 0) { //地点的适配器，只显示3列item‘
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_photo2, null);
                holder.gv = convertView.findViewById(R.id.gv);
                holder.tv = convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            } else { //其他显示4列item
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_photo2_1, null);
                holder.gv1 = convertView.findViewById(R.id.gv1);
                holder.tv = convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassificationItem data = datas.get(position);
        MyGridViewAdapter adapter;
        switch (getItemViewType(position)) {
            case 0: //地点
                holder.tv.setText(data.getItemName());
                adapter = new MyGridViewAdapter(data.getTypenames(),data.getPics(), context,0);
                holder.gv.setAdapter(adapter);
                holder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int gvPosition, long id) {
                        if(onGvItemClickListener!= null){
                            onGvItemClickListener.onClick(position,gvPosition);
                        }
                    }
                });
                break;
            case 1:
                holder.tv.setText(data.getItemName());
                adapter = new MyGridViewAdapter(data.getTypenames(),data.getPics(), context,1);
                holder.gv1.setAdapter(adapter);
                holder.gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int gvPosition, long id) {
                        if(onGvItemClickListener!= null){
                            onGvItemClickListener.onClick(position,gvPosition);
                        }
                    }
                });
                break;
        }

        return convertView;
    }


    static class ViewHolder {
        TextView tv;
        MyGridView gv;
        MyGridView gv1;
    }

    private OnGvItemClickListener onGvItemClickListener;
    public interface OnGvItemClickListener{
        void onClick(int position, int gvPosition);
    }
    public void setOnGvItemClickListener(OnGvItemClickListener onGvItemClickListener){
        this.onGvItemClickListener = onGvItemClickListener;
    }
}
