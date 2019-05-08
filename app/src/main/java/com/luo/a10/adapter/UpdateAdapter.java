package com.luo.a10.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.fileselect.bean.FileItem;
import com.white.progressview.CircleProgressView;

import java.util.List;

public class UpdateAdapter extends BaseAdapter {

    private List<FileItem> files;
    private Context context;

    private int progress[];
    private int flag = -1;

    public UpdateAdapter(List<FileItem> files, Context context) {
        this.files = files;
        this.context = context;

        if (files != null && files.size() > 0)
            progress = new int[files.size()];
    }

    @Override
    public int getCount() {
        return files == null ? 0 : files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
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
            convertView = View.inflate(context, R.layout.item_update, null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_current = convertView.findViewById(R.id.tv_current);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv_ok = convertView.findViewById(R.id.tv_ok);
            holder.iv_pic = convertView.findViewById(R.id.iv_pic);
            holder.progress = convertView.findViewById(R.id.progress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FileItem file = files.get(position);

        String type = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
        if (type.equals(".jpg") || type.equals(".png") || type.equals(".jpeg") || type.equals(".JPG")
                || type.equals(".PNG") || type.equals(".JPEG")) {
            holder.iv.setVisibility(View.GONE);
            holder.iv_pic.setVisibility(View.VISIBLE);
            Glide.with(context).load(file.getPath()).error(R.drawable.city1).into(holder.iv_pic);
        } else if (type.equals(".mp3") || type.equals(".MP3") || type.equals(".wav") || type.equals(".WAV")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_mp3).into(holder.iv);
        } else if (type.equals(".mp4") || type.equals(".MP4")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_video).into(holder.iv);
        } else if (type.equals(".doc") || type.equals(".docx") || type.equals(".DOC") || type.equals(".DOCX")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_doc).into(holder.iv);
        } else if (type.equals(".pdf") || type.equals(".PDF")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_pdf).into(holder.iv);
        } else if (type.equals(".ppt") || type.equals(".pptx") || type.equals(".PPT") || type.equals(".PPTX")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_ppt).into(holder.iv);
        } else if (type.equals(".xls") || type.equals(".xlsx") || type.equals(".XLS") || type.equals(".XLSX")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_xls).into(holder.iv);
        } else if (type.equals(".zip") || type.equals(".ZIP") || type.equals(".rar") || type.equals(".RAR")) {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_type_zip).into(holder.iv);
        } else {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv_pic.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.type_txt).into(holder.iv);
        }

        if(flag == 1){
            holder.tv_ok.setText("已完成");
            holder.progress.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.VISIBLE);
        }else if(flag == 2){
            holder.tv_ok.setText("上传失败");
            holder.progress.setVisibility(View.VISIBLE);
            holder.tv_ok.setVisibility(View.GONE);
        }
        holder.tv_size.setText(Formatter.formatFileSize(context, file.getFileSize()));
        holder.tv_current.setText(Formatter.formatFileSize(context, (long) (progress[position]*0.01*file.getFileSize()))+"/");
        holder.progress.setProgress(progress[position]);

        return convertView;
    }

    public void setFlag(int flag) {
        this.flag = flag;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_current;
        TextView tv_size;
        TextView tv_ok;
        ImageView iv;
        ImageView iv_pic;

        CircleProgressView progress;
    }

    public void setProgress(int position, int progress) {
        this.progress[position] = progress;
        notifyDataSetChanged();
    }


}
