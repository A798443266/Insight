package com.luo.a10.adapter.fenlei;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luo.a10.R;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.view.MyRoundLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类详情照片的适配器
 */
public class QitaAdapter extends BaseAdapter {

    private Context context;
    private List<FolderAndDoc> datas;
    private boolean[] select = new boolean[]{};;

    public QitaAdapter(Context context, List<FolderAndDoc> datas) {
        this.context = context;
        this.datas = datas;

        if (datas != null && datas.size() > 0) {
            select = new boolean[datas.size()];
            for (int i = 0; i < select.length; i++) {
                select[i] = false;
            }
        }
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
        Viewholder holder;
        if (convertView == null) {
            holder = new Viewholder();
            convertView = View.inflate(context, R.layout.item_fenlei_qita, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.ml_bg = convertView.findViewById(R.id.ml_bg);
            holder.iv_select = convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        FolderAndDoc doc = datas.get(position);
        holder.tv_name.setText(doc.getName());
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
        if (select[position]) {
            return 1;
        }
        return -1;
    }

    //取消选择
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
            if (select[i]) {
                positions.add(i);
            }
        }
        return positions;
    }

    static class Viewholder {
        ImageView iv;
        TextView tv_name;
        MyRoundLayout ml_bg;
        ImageView iv_select;
    }
}
