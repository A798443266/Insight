package com.study.fileselectlibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.study.fileselectlibrary.R;
import com.study.fileselectlibrary.bean.FileItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class GvAdapter extends BaseAdapter {
    private final Context context;
    private final List<FileItem> items;
    private Bitmap bitmap;

    public GvAdapter(Context context, List<FileItem> items) {

        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gv_item, null);
        }

        final FileItem fileItem = items.get(position);
        String name = fileItem.getName();
        String path = fileItem.getPath();
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv);

        try {
            bitmap = revitionImageSize(path, 144, 144);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null)
            iv.setImageBitmap(bitmap);
//        Picasso.
//                with(context).
//                load(R.drawable.icon_type_video).
//                into(iv);
        return convertView;
    }

    //    获取图片的缩略图
    public static Bitmap revitionImageSize(String path, int maxWidth, int maxHeight) throws IOException {
        Bitmap bitmap = null;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                    new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= maxWidth)
                        && (options.outHeight >> i <= maxHeight)) {
                    in = new BufferedInputStream(
                            new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
        } catch (Exception e) {
            return null;
        }
        return bitmap;
    }
}
