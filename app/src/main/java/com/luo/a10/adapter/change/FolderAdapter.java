package com.luo.a10.adapter.change;

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

public class FolderAdapter extends BaseAdapter {

    private List<FolderAndDoc> folderAndDocs;
    private Context context;
    Map<Integer, Boolean> cbState = new TreeMap<>();//记录每个cb的选择状态，防止convertView复用带来的混乱问题
    public static final int FOLDER = 0; //文件夹
    public static final int NOMOL = 1; //普通文件
    public static final int PICORVIDER = 2; //图片和视频


    public FolderAdapter(List<FolderAndDoc> folderAndDocs, Context context) {
        this.folderAndDocs = folderAndDocs;
        this.context = context;

        if (folderAndDocs != null && folderAndDocs.size() > 0) {
            for (int i = 0; i < folderAndDocs.size(); i++) {
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
        FolderAndDoc doc = folderAndDocs.get(position);
        if (doc.getCategory() == -1) {//文件夹
            return FOLDER;
        } else if (doc.getCategory() == 2 || doc.getCategory() == 4) {//图片或者视频
            return PICORVIDER;
        } else {//文件
            return NOMOL;
        }
    }


    @Override
    public int getCount() {
        return folderAndDocs == null ? 0 : folderAndDocs.size();
    }

    @Override
    public Object getItem(int position) {
        return folderAndDocs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int item_type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (item_type) {
                case FOLDER:
                    convertView = View.inflate(context, R.layout.item_folder, null);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_time = convertView.findViewById(R.id.tv_time);
                    holder.tv_content = convertView.findViewById(R.id.tv_content);
                    holder.cb = convertView.findViewById(R.id.cb);
                    holder.ll = convertView.findViewById(R.id.ll);
                    holder.tv_size = convertView.findViewById(R.id.tv_size);
                    holder.tv_month = convertView.findViewById(R.id.tv_month);
                    holder.iv_folder = convertView.findViewById(R.id.iv_folder);
                    break;

                case NOMOL:
                    convertView = View.inflate(context, R.layout.item_doc, null);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_time = convertView.findViewById(R.id.tv_time);
                    holder.tv_size = convertView.findViewById(R.id.tv_size);
                    holder.tv_content = convertView.findViewById(R.id.tv_content);
                    holder.cb = convertView.findViewById(R.id.cb);
                    holder.ll = convertView.findViewById(R.id.ll);
                    holder.tv_month = convertView.findViewById(R.id.tv_month);
                    holder.iv = convertView.findViewById(R.id.iv);
                    break;

                case PICORVIDER:
                    convertView = View.inflate(context, R.layout.item_doc1, null);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_time = convertView.findViewById(R.id.tv_time);
                    holder.tv_content = convertView.findViewById(R.id.tv_content);
                    holder.cb = convertView.findViewById(R.id.cb);
                    holder.ll = convertView.findViewById(R.id.ll);
                    holder.tv_size = convertView.findViewById(R.id.tv_size);
                    holder.tv_month = convertView.findViewById(R.id.tv_month);
                    holder.iv_cover = convertView.findViewById(R.id.iv_cover);
                    holder.iv_play = convertView.findViewById(R.id.iv_play);
                    break;
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FolderAndDoc folderAndDoc = folderAndDocs.get(position);

        //设置公共的数据
        holder.ll.setVisibility(View.GONE);
        holder.tv_name.setText(folderAndDoc.getName());
        holder.cb.setChecked(cbState.get(position));

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cb.isChecked()) {
                    cbState.put(position, true);
                } else {
                    cbState.put(position, false);
                }
                if (onSelectDotClick != null) {
                    onSelectDotClick.onDotClick(position, holder.cb);
                }

            }
        });

        switch (item_type) {
            case FOLDER:
                holder.ll.setVisibility(View.GONE);
                holder.tv_time.setText("2019年");
                break;
            case PICORVIDER:
                holder.tv_size.setText(Formatter.formatFileSize(context, folderAndDoc.getSize()));
                if (position == 0) {
                    holder.tv_month.setText(UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())).substring(0, 7));
                    holder.ll.setVisibility(View.VISIBLE);
                } else if(position != 0 && folderAndDocs.get(position-1).getCategory() != -1) {//文件夹没有设置时间
                    String preMonth = UIUtils.StringTime(Long.parseLong(folderAndDocs.get(position - 1).getTime())).substring(0, 7);
                    String currentMonth = UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())).substring(0, 7);
                    if (currentMonth.equals(preMonth)) {
                        holder.ll.setVisibility(View.GONE);
                    } else {
                        holder.ll.setVisibility(View.VISIBLE);
                        holder.tv_month.setText(currentMonth);
                    }
                }

                if (folderAndDoc.getCategory() == 2) {//图片
                    holder.iv_play.setVisibility(View.GONE);
                    holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())));
                    Glide.with(context).load(folderAndDoc.getLink()).error(R.drawable.city1).into(holder.iv_cover);
                } else {//视频
                    holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())));
                    holder.iv_play.setVisibility(View.VISIBLE);
                    Glide.with(context).load(folderAndDoc.getCover()).error(R.drawable.city1).into(holder.iv_cover);
                }
                break;
            case NOMOL:
                if (position == 0) {
                    holder.tv_month.setText(UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())).substring(0, 7));
                    holder.ll.setVisibility(View.VISIBLE);
                } else if(position != 0 && folderAndDocs.get(position-1).getCategory() != -1) {//文件夹没有设置时间
                    String preMonth = UIUtils.StringTime(Long.parseLong(folderAndDocs.get(position - 1).getTime())).substring(0, 7);
                    String currentMonth = UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())).substring(0, 7);
                    if (currentMonth.equals(preMonth)) {
                        holder.ll.setVisibility(View.GONE);
                    } else {
                        holder.ll.setVisibility(View.VISIBLE);
                        holder.tv_month.setText(currentMonth);
                    }
                }

                holder.tv_size.setText(Formatter.formatFileSize(context, folderAndDoc.getSize()));
                holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(folderAndDoc.getTime())));
                String leixing = folderAndDoc.getName().substring(folderAndDoc.getName().lastIndexOf("."), folderAndDoc.getName().length());
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
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        TextView tv_size;
        CheckBox cb;
        LinearLayout ll;
        TextView tv_month;
        ImageView iv;
        ImageView iv_cover;
        ImageView iv_play;
        ImageView iv_folder;
    }

    public void setCbState(int position, boolean b) {
        cbState.put(position, b);
    }

    public Map<Integer, Boolean> getCbState() {
        return cbState;
    }

    //取消所有的选择
    public void setCancelAll() {
        for (int i = 0; i < folderAndDocs.size(); i++) {
            cbState.put(i, false);
        }
        notifyDataSetChanged();
    }

    //全选
    public void setSelectAll() {
        for (int i = 0; i < folderAndDocs.size(); i++) {
            cbState.put(i, true);
        }
        notifyDataSetChanged();
    }

    private OnSelectDotClick onSelectDotClick;

    public interface OnSelectDotClick {
        void onDotClick(int position, CheckBox cb);
    }

    public void setOnSelectDotClick(OnSelectDotClick onSelectDotClick) {
        this.onSelectDotClick = onSelectDotClick;
    }


}
