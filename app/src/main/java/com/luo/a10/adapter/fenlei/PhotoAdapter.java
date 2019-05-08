package com.luo.a10.adapter.fenlei;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.activity.SearchResultActivity;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.Constant;
import com.luo.a10.view.MyRoundLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类详情照片的适配器
 */
public class PhotoAdapter extends BaseAdapter {

    private Context context;
    private List<FolderAndDoc> pics;
    private boolean[] select = new boolean[]{};;

    public PhotoAdapter(Context context, List<FolderAndDoc> pics) {
        this.context = context;
        this.pics = pics;

        if (pics != null && pics.size() > 0) {
            select = new boolean[pics.size()];
            for (int i = 0; i < select.length; i++) {
                select[i] = false;
            }
        }

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
            convertView = View.inflate(context, R.layout.item_fenlei_photo, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.ml_bg = convertView.findViewById(R.id.ml_bg);
            holder.iv_select = convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        Glide.with(context).load(pics.get(position).getLink()).error(R.drawable.city1).into(holder.iv);
        if (select[position]) {
            holder.ml_bg.setVisibility(View.VISIBLE);
            holder.iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.ml_bg.setVisibility(View.GONE);
            holder.iv_select.setVisibility(View.GONE);
        }

        return convertView;
    }

    //选择文件
    public int setSelect(int position) {
        select[position] = !select[position];
        notifyDataSetChanged();
        if(select[position]){
            return 1;
        }
        return -1;
    }

    public void setCancelSelectMode() {
        for (int i = 0; i < select.length; i++) {
            select[i] = false;
        }
        notifyDataSetChanged();
    }

    //获取所有选中的位置
    public List<Integer> getSelectPosition() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < select.length; i++) {
            if(select[i]){
                positions.add(i);
            }
        }
        return positions;
    }

    static class Viewholder {
        ImageView iv;
        MyRoundLayout ml_bg;
        ImageView iv_select;
    }
}
