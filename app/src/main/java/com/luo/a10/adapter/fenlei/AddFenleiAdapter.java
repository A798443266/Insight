package com.luo.a10.adapter.fenlei;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.AddFenleiInfo;
import com.luo.a10.bean.FolderRecord;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.view.MyRoundLayout;

import java.util.List;

/**
 * 分类详情照片的适配器
 */
public class AddFenleiAdapter extends BaseAdapter {

    private Context context;
    private List<FolderAndDoc> datas;

    public AddFenleiAdapter(Context context, List<FolderAndDoc> datas) {
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
        Viewholder holder;
        if (convertView == null) {
            holder = new Viewholder();
            convertView = View.inflate(context, R.layout.item_add_fenlei, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.iv_play = convertView.findViewById(R.id.iv_play);
            holder.iv_close = convertView.findViewById(R.id.iv_close);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        FolderAndDoc doc = datas.get(position);

        switch (doc.getCategory()) {
            case 1://文档
                holder.iv_play.setVisibility(View.GONE);
                switch (doc.getName().substring(doc.getName().lastIndexOf(".")+1)) {
                    case "doc":
                    case "docx":
                    case "DOCX":
                    case "DOC":
                        Glide.with(context).load(R.drawable.icon_type_doc).error(R.drawable.city1).into(holder.iv);
                        break;

                    case "pdf":
                    case "PDF":
                        Glide.with(context).load(R.drawable.icon_type_pdf).error(R.drawable.city1).into(holder.iv);
                        break;

                    case "xls":
                    case "xlsx":
                        Glide.with(context).load(R.drawable.icon_type_xls).error(R.drawable.city1).into(holder.iv);
                        break;

                    case "ppt":
                    case "pptx":
                    case "PPT":
                    case "PPTX":
                        Glide.with(context).load(R.drawable.icon_type_ppt).error(R.drawable.city1).into(holder.iv);
                        break;
                }
                break;
            case 2://图片
                Glide.with(context).load(doc.getLink()).error(R.drawable.city1).into(holder.iv);
                holder.iv_play.setVisibility(View.GONE);
                break;
            case 3://音频
                holder.iv_play.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.icon_type_mp3).error(R.drawable.city1).into(holder.iv);
                break;
            case 4://视频
                Glide.with(context).load(doc.getCover()).error(R.drawable.city1).into(holder.iv);
                holder.iv_play.setVisibility(View.VISIBLE);
                break;
            case 5://其他
                Glide.with(context).load(R.drawable.icon_type_zip).error(R.drawable.city1).into(holder.iv);
                holder.iv_play.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    static class Viewholder {
        ImageView iv;
        ImageView iv_play;
        ImageView iv_close;
    }
}
