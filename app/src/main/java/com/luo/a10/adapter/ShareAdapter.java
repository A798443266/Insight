package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.ShareInfo;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.UIUtils;

import java.util.List;

public class ShareAdapter extends BaseAdapter {

    private Context context;
    private List<ShareInfo> datas;

    public ShareAdapter(Context context, List<ShareInfo> datas) {
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_share, null);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.iv_cover = convertView.findViewById(R.id.iv_cover);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_look = convertView.findViewById(R.id.tv_look);
            holder.tv_download = convertView.findViewById(R.id.tv_download);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShareInfo shareInfo = datas.get(position);
        holder.tv_name.setText(shareInfo.getFolderAndDoc().getName());
        holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(shareInfo.getFolderAndDoc().getTime())));
        holder.tv_download.setText(shareInfo.getDownload()+"次下载");
        holder.tv_look.setText(shareInfo.getLook()+"人浏览");

        if (shareInfo.getFolderAndDoc().getCategory() == 2) {
            holder.iv_cover.setVisibility(View.VISIBLE);
            holder.iv.setVisibility(View.GONE);
            Glide.with(context).load(shareInfo.getFolderAndDoc().getLink()).error(R.drawable.city1).into(holder.iv_cover);
        } else {
            holder.iv_cover.setVisibility(View.GONE);
            holder.iv.setVisibility(View.VISIBLE);
            FolderAndDoc folderAndDoc = shareInfo.getFolderAndDoc();
            String leixing = folderAndDoc.getName().substring(folderAndDoc.getName().lastIndexOf("."), folderAndDoc.getName().length());
            switch (shareInfo.getFolderAndDoc().getCategory()) {
                case 1:
                    if (leixing.equals(".doc") || leixing.equals(".docx") || leixing.equals(".DOC") || leixing.equals(".DOCX")) {
                        Glide.with(context).load(R.drawable.icon_type_doc).into(holder.iv);
                    } else if (leixing.equals(".pdf") || leixing.equals(".PDF")) {
                        Glide.with(context).load(R.drawable.icon_type_pdf).into(holder.iv);
                    } else if (leixing.equals(".ppt") || leixing.equals(".pptx") || leixing.equals(".PPT") || leixing.equals(".PPTX")) {
                        Glide.with(context).load(R.drawable.icon_type_ppt).into(holder.iv);
                    } else if (leixing.equals(".xls") || leixing.equals(".xlsx") || leixing.equals(".XLS") || leixing.equals(".XLSX")) {
                        Glide.with(context).load(R.drawable.icon_type_xls).into(holder.iv);
                    } else if (leixing.equals(".mp3") || leixing.equals(".MP3") || leixing.equals(".wav") || leixing.equals(".WAV")) {
                        Glide.with(context).load(R.drawable.icon_type_mp3).into(holder.iv);
                    } else if (leixing.equals(".zip") || leixing.equals(".ZIP") || leixing.equals(".rar") || leixing.equals(".RAR")) {
                        Glide.with(context).load(R.drawable.icon_type_zip).into(holder.iv);
                    }
                    break;
                default:
                    break;
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_look;
        TextView tv_download;
        ImageView iv;
        ImageView iv_cover;
    }
}
