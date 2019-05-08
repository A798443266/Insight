package com.study.fileselectlibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.study.fileselectlibrary.R;
import com.study.fileselectlibrary.bean.FileItem;
import com.study.fileselectlibrary.emnu.Type;

import java.io.File;
import java.util.List;

public class LvAdapter extends BaseAdapter {
    private final Context context;
    private final List<FileItem> list;
    private Type adapterType = Type.DEFAULT;

    public LvAdapter(Context context, List<FileItem> fileItemList) {
        this.context = context;
        list = fileItemList;
    }

    public void setAdapterType(Type type) {
        this.adapterType = type;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public FileItem getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        FileItem fileItem = list.get(position);
        return fileItem.isFile() ? 1 : 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {

        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    convertView = View.inflate(context, R.layout.list_directory_item, null);
                    break;
                case 1:
                    convertView = View.inflate(context, R.layout.list_file_item, null);
                    break;
            }
        }

        FileItem fileItem = list.get(position);
        String name = fileItem.getName();
        switch (type) {
            case 0:
                break;
            case 1:
                ImageView iv = convertView.findViewById(R.id.iv);

                switch (adapterType) {
                    case AUDIO:  //音频
                        iv.setBackgroundResource(R.drawable.icon_type_mp3);
                        break;
                    case VIDEO:
                        iv.setBackgroundResource(R.drawable.icon_type_video);
                        break;
                    case DOCUMENT: //文档
                        setImageIconByName(name, iv);
                        break;
                    case OTHER:  //其他
                        if (name.endsWith(".txt")) {
                            iv.setBackgroundResource(R.drawable.type_txt);
                        } else if (name.endsWith(".zip") || name.endsWith(".rar")) {
                            iv.setBackgroundResource(R.drawable.icon_type_zip);
                        }
                        break;
                    case DEFAULT: //默认
                        iv.setBackgroundResource(R.drawable.type_other);
                        break;
                    default:
                        iv.setBackgroundResource(R.drawable.type_other);
                        break;
                }


                TextView tvSize = (TextView) convertView.findViewById(R.id.tv_size);
                tvSize.setText(Formatter.formatFileSize(context, fileItem.getFileSize()));
                TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", fileItem.getLastModifyTime()));
                CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
                if (fileItem.isChecked()) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }
                break;
        }


        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(name);
        return convertView;
    }

    /**
     * 根据文件名获取对应文档的图片Uri
     *
     * @param filename
     * @return
     */
    private void setImageIconByName(String filename, ImageView iv) {

        String name = filename.substring(filename.lastIndexOf("."), filename.length());
        switch (name) {
            case ".docx":
            case ".doc":
                iv.setBackgroundResource(R.drawable.icon_type_doc);
                break;
            case ".ppt":
            case ".pptx":
                iv.setBackgroundResource(R.drawable.icon_type_ppt);
            case ".pdf":
                iv.setBackgroundResource(R.drawable.icon_type_pdf);
                break;
            case ".xls":
            case ".xlsx":
                iv.setBackgroundResource(R.drawable.icon_type_xls);
                break;
            default:
                break;
        }
    }


}
