package com.luo.a10.fileselect.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luo.a10.R;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;

import java.util.List;

public class FolderAdapter extends BaseAdapter {
    private Context context;
    private List<FolderAndDoc> floders;
    private int selectPosition = -1;

    public FolderAdapter(Context context, List<FolderAndDoc> floders) {
        this.context = context;
        this.floders = floders;
    }


    @Override
    public int getCount() {
        return floders != null ? floders.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_selectfile_folder, null);
            holder.iv_check = convertView.findViewById(R.id.iv_check);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FolderAndDoc folder = floders.get(position);

        holder.tv_name.setText(folder.getName());
        if (position == selectPosition) {
            holder.iv_check.setBackgroundResource(R.drawable.icon_xuan1);
        } else {
            holder.iv_check.setBackgroundResource(R.drawable.icon_xuan);
        }


        return convertView;
    }

    public void setSelect(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        ImageView iv_check;
        TextView tv_name;
    }

}
