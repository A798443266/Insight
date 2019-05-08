package com.luo.a10.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.UIUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeSeeFragmentAdapter extends BaseAdapter {

    private static final int DOCUMENT = 0;
    private static final int PIC = 1;
    private static final int VIDEO = 2;
    private Context context;
    private List<FolderAndDoc> docs;
    private String type;

    Map<Integer, Boolean> cbState = new TreeMap<>();//记录每个cb的选择状态，防止convertView复用带来的混乱问题

    public Map<Integer, Boolean> getCbState() {
        return cbState;
    }

    public void setCbState(int position, boolean b) {
        cbState.put(position, b);
    }

    public HomeSeeFragmentAdapter(Context context, List<FolderAndDoc> docs) {
        this.context = context;
        this.docs = docs;

        if (docs != null && docs.size() > 0) {
            for (int i = 0; i < docs.size(); i++) {
                cbState.put(i, false);//初始化每个cb的状态
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        type = docs.get(position).getName().substring(docs.get(position).getName().lastIndexOf("."), docs.get(position).getName().length());
        if (docs.get(position).getCategory() == 4) {
            return VIDEO;
        } else if (docs.get(position).getCategory() == 2) {
            return PIC;
        } else
            return DOCUMENT;
    }


    @Override
    public int getCount() {
        return docs == null ? 0 : docs.size();
    }

    @Override
    public Object getItem(int position) {
        return docs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FolderAndDoc doc = docs.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (getItemViewType(position)) {
                case DOCUMENT:
                    convertView = View.inflate(context, R.layout.item_doc, null);
                    holder.iv = convertView.findViewById(R.id.iv);
                    holder.tv_month = convertView.findViewById(R.id.tv_month);
                    holder.tv_time = convertView.findViewById(R.id.tv_time);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_size = convertView.findViewById(R.id.tv_size);
                    holder.ll = convertView.findViewById(R.id.ll);
                    holder.cb = convertView.findViewById(R.id.cb);
                    convertView.setTag(holder);
                    break;
                case PIC:
                    convertView = View.inflate(context, R.layout.item_pic, null);
                    holder.iv_cover = convertView.findViewById(R.id.iv_cover);
                    holder.tv_month = convertView.findViewById(R.id.tv_month);
                    holder.tv_time = convertView.findViewById(R.id.tv_time);
                    holder.tv_size = convertView.findViewById(R.id.tv_size);
                    holder.ll = convertView.findViewById(R.id.ll);
                    holder.cb = convertView.findViewById(R.id.cb);
                    convertView.setTag(holder);
                    break;
                case VIDEO:
                    convertView = View.inflate(context, R.layout.item_video1, null);
                    holder.iv_cover = convertView.findViewById(R.id.iv_cover);
                    holder.tv_month = convertView.findViewById(R.id.tv_month);
                    holder.tv_time = convertView.findViewById(R.id.tv_time);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_size = convertView.findViewById(R.id.tv_size);
                    holder.ll = convertView.findViewById(R.id.ll);
                    holder.cb = convertView.findViewById(R.id.cb);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.ll.setVisibility(View.VISIBLE);
        } else {
            //上一个文件的时间
            String preTime = UIUtils.StringTime(Long.parseLong(docs.get(position - 1).getTime())).substring(0, 10);
            //本文件的时间
            String currentTime = UIUtils.StringTime(Long.parseLong(docs.get(position).getTime())).substring(0, 10);
            if(currentTime.equals(preTime)){
                holder.ll.setVisibility(View.GONE);
            }else {
                holder.ll.setVisibility(View.VISIBLE);
            }
        }

        holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(doc.getTime())));
        holder.tv_month.setText(UIUtils.StringTime(Long.parseLong(doc.getTime())).substring(0, 10));
        holder.tv_size.setText(Formatter.formatFileSize(context, doc.getSize()));

        holder.cb.setChecked(cbState.get(position));
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cb.setChecked(holder.cb.isChecked());
                if (onSelectDotClick != null) {
                    onSelectDotClick.onDotClick(position, holder.cb);
                }
                if (holder.cb.isChecked()) {
                    cbState.put(position, true);
                } else {
                    cbState.put(position, false);
                }

            }
        });
        switch (getItemViewType(position)) {
            case DOCUMENT:
                holder.tv_name.setText(doc.getName());
                setIcon(holder.iv);
                break;
            case PIC:
                Glide.with(context).load(doc.getLink()).error(R.drawable.city1).into(holder.iv_cover);
                break;
            case VIDEO:
                holder.tv_name.setText(doc.getName());
                Glide.with(context).load(doc.getCover()).error(R.drawable.city1).into(holder.iv_cover);
                break;

        }

        return convertView;
    }

    private void setIcon(ImageView iv) {
        if (type.equals(".doc") || type.equals(".docx") || type.equals(".DOC") || type.equals(".DOCX")) {
            Glide.with(context).load(R.drawable.icon_type_doc).into(iv);
        } else if (type.equals(".pdf") || type.equals(".PDF")) {
            Glide.with(context).load(R.drawable.icon_type_pdf).into(iv);
        } else if (type.equals(".ppt") || type.equals(".pptx")|| type.equals(".PPT")|| type.equals(".PPTX")) {
            Glide.with(context).load(R.drawable.icon_type_ppt).into(iv);
        } else if (type.equals(".xls") || type.equals(".xlsx")) {
            Glide.with(context).load(R.drawable.icon_type_xls).into(iv);
        } else if (type.equals(".mp3") || type.equals(".MP3") || type.equals(".wav") || type.equals(".WAV")) {
            Glide.with(context).load(R.drawable.icon_type_mp3).into(iv);
        }
    }

    //取消所有的选择
    public void setCancelAll() {
        for (int i = 0; i < docs.size(); i++) {
            cbState.put(i, false);
        }
        notifyDataSetChanged();
    }

    //全选
    public void setSelectAll() {
        for (int i = 0; i < docs.size(); i++) {
            cbState.put(i, true);
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
        ImageView iv_cover;  //图片和视频的缩略图
        ImageView iv;//普通文件类型图标
        TextView tv_month;
        TextView tv_time;
        TextView tv_name;
        TextView tv_size;
        LinearLayout ll;
        CheckBox cb;

    }

    private DocAdapter.OnSelectDotClick onSelectDotClick;

    public interface OnSelectDotClick {
        void onDotClick(int position, CheckBox cb);
    }

    public void setOnSelectDotClick(DocAdapter.OnSelectDotClick onSelectDotClick) {
        this.onSelectDotClick = onSelectDotClick;
    }


}
