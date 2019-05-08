package com.luo.a10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.view.MyCheckBox;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 文件夹和各式文件的适配器
 */
public class DocAdapter extends BaseAdapter {

    private List<DocAndFloder> datas;
    private Context context;
    private String leixing = "";

    public static final int FOLDER = 0; //文件夹
    public static final int NOMOL = 1; //普通文件
    public static final int PICORVIDER = 2; //图片和视频

    Map<Integer, Boolean> cbState = new TreeMap<>();//记录每个cb的选择状态，防止convertView复用带来的混乱问题

    public Map<Integer, Boolean> getCbState() {
        return cbState;
    }

    public void setCbState(int position, boolean b) {
        cbState.put(position, b);
    }

    public DocAdapter(List<DocAndFloder> datas, Context context) {
        this.datas = datas;
        this.context = context;

        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                cbState.put(i, false);//初始化每个cb的状态
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        DocAndFloder docAndFloder = datas.get(position);
        if (docAndFloder.getIsFolder() == 0) {
            return FOLDER;
        } else {
            String docname = docAndFloder.getDocName();
            leixing = docname.substring(docname.lastIndexOf("."), docname.length());
            if (leixing.equals(".MP4") || leixing.equals(".JPG") || leixing.equals(".JPEG") || leixing.equals(".PNG")
                    || leixing.equals(".mp4") || leixing.equals(".png") || leixing.equals(".jpg") || leixing.equals(".jpeg")) {
                return PICORVIDER;
            } else {
                return NOMOL;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        int item_type = getItemViewType(position);
        DocAndFloder docAndFloder = datas.get(position);
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        if (convertView == null) {
            if (getItemViewType(position) == FOLDER) { //文件夹
                holder1 = new ViewHolder1();
                convertView = View.inflate(context, R.layout.item_folder, null);
                holder1.iv_folder = convertView.findViewById(R.id.iv_folder);
                holder1.tv_month = convertView.findViewById(R.id.tv_month);
                holder1.tv_time = convertView.findViewById(R.id.tv_time);
                holder1.tv_name = convertView.findViewById(R.id.tv_name);
                holder1.tv_content = convertView.findViewById(R.id.tv_content);
                holder1.ll = convertView.findViewById(R.id.ll);
                holder1.cb = convertView.findViewById(R.id.cb);
                convertView.setTag(holder1);
            } else if (getItemViewType(position) == PICORVIDER) { //图片或视频
                holder2 = new ViewHolder2();
                convertView = View.inflate(context, R.layout.item_doc1, null);
                holder2.iv_cover = convertView.findViewById(R.id.iv_cover);
                holder2.tv_month = convertView.findViewById(R.id.tv_month);
                holder2.tv_time = convertView.findViewById(R.id.tv_time);
                holder2.tv_name = convertView.findViewById(R.id.tv_name);
                holder2.tv_size = convertView.findViewById(R.id.tv_size);
                holder2.ll = convertView.findViewById(R.id.ll);
                holder2.cb = convertView.findViewById(R.id.cb);
                convertView.setTag(holder2);
            } else { //doc等普通文件
                holder3 = new ViewHolder3();
                convertView = View.inflate(context, R.layout.item_doc, null);
                holder3.iv = convertView.findViewById(R.id.iv);
                holder3.tv_month = convertView.findViewById(R.id.tv_month);
                holder3.tv_time = convertView.findViewById(R.id.tv_time);
                holder3.tv_name = convertView.findViewById(R.id.tv_name);
                holder3.tv_size = convertView.findViewById(R.id.tv_size);
                holder3.ll = convertView.findViewById(R.id.ll);
                holder3.cb = convertView.findViewById(R.id.cb);
                convertView.setTag(holder3);
            }
        } else {
            if (getItemViewType(position) == FOLDER)
                holder1 = (ViewHolder1) convertView.getTag();
            else if (getItemViewType(position) == PICORVIDER)
                holder2 = (ViewHolder2) convertView.getTag();
            else
                holder3 = (ViewHolder3) convertView.getTag();
        }


//设置数据
        switch (item_type) {
            case FOLDER:
                if (position == 0) {
                    holder1.ll.setVisibility(View.VISIBLE);
                } else {
                    //上一条记录的月份
                    String preMonth = datas.get(position - 1).getTime().substring(0, 7);
                    //当前记录的月份
                    String currentMonth = datas.get(position).getTime().substring(0, 7);
                    if (currentMonth.equals(preMonth)) {//如果和上一条的月份相同的化就不显示
                        holder1.ll.setVisibility(View.GONE);
                    } else {
                        holder1.ll.setVisibility(View.VISIBLE);
                    }
                }

                holder1.tv_name.setText(docAndFloder.getDocName());
                holder1.tv_time.setText(docAndFloder.getTime());
                holder1.tv_month.setText(docAndFloder.getTime().substring(0,4) + "年" + docAndFloder.getTime().substring(5,7) + "月");
                holder1.tv_content.setText(docAndFloder.getContent() + "项"); //设置文件夹里面有多少项内容
                //cb的点击事件
                holder1.cb.setChecked(cbState.get(position));
                final ViewHolder1 finalHolder = holder1;
                holder1.cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalHolder.cb.setChecked(finalHolder.cb.isChecked());
                        if (onSelectDotClick != null) {
                            onSelectDotClick.onDotClick(position, finalHolder.cb);
                        }
                        if (finalHolder.cb.isChecked()) {
                            cbState.put(position, true);
                        } else {
                            cbState.put(position, false);
                        }

                    }
                });
                break;

            case PICORVIDER:
                if (position == 0) {
                    holder2.ll.setVisibility(View.VISIBLE);
                } else {
                    //上一条记录的月份
                    String preMonth = datas.get(position - 1).getTime().substring(0, 7);
                    //当前记录的月份
                    String currentMonth = datas.get(position).getTime().substring(0, 7);
                    if (currentMonth.equals(preMonth)) {//如果和上一条的月份相同的化就不显示
                        holder2.ll.setVisibility(View.GONE);
                    } else {
                        holder2.ll.setVisibility(View.VISIBLE);
                    }
                }
                holder2.tv_name.setText(docAndFloder.getDocName());
                holder2.tv_month.setText(docAndFloder.getTime().substring(0,4) + "年" + docAndFloder.getTime().substring(5,7) + "月");
                Glide.with(context).load(docAndFloder.getLink()).error(R.drawable.city1).into(holder2.iv_cover);
                holder2.tv_size.setText(docAndFloder.getSize());
                holder2.tv_time.setText(docAndFloder.getTime());
                holder2.cb.setChecked(cbState.get(position));
                final ViewHolder2 finalHolder1 = holder2;
                holder2.cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalHolder1.cb.setChecked(finalHolder1.cb.isChecked());
                        if (onSelectDotClick != null) {
                            onSelectDotClick.onDotClick(position, finalHolder1.cb);
                        }
                        if (finalHolder1.cb.isChecked()) {
                            cbState.put(position, true);
                        } else {
                            cbState.put(position, false);
                        }

                    }
                });
                break;

            case NOMOL:
                if (position == 0) {
                    holder3.ll.setVisibility(View.VISIBLE);
                } else {
                    //上一条记录的月份
                    String preMonth = datas.get(position - 1).getTime().substring(0, 7);
                    //当前记录的月份
                    String currentMonth = datas.get(position).getTime().substring(0, 7);
                    if (currentMonth.equals(preMonth)) {//如果和上一条的月份相同的化就不显示
                        holder3.ll.setVisibility(View.GONE);
                    } else {
                        holder3.ll.setVisibility(View.VISIBLE);
                    }
                }
                holder3.tv_name.setText(docAndFloder.getDocName());
                holder3.tv_month.setText(docAndFloder.getTime().substring(0,4) + "年" + docAndFloder.getTime().substring(5,7) + "月");
                holder3.tv_time.setText(docAndFloder.getTime());
                holder3.tv_size.setText(docAndFloder.getSize());
                if (leixing.equals(".doc") || leixing.equals(".docx")) {
                    Glide.with(context).load(R.drawable.icon_type_doc).into(holder3.iv);
                } else if (leixing.equals(".pdf")) {
                    Glide.with(context).load(R.drawable.icon_type_pdf).into(holder3.iv);
                } else if (leixing.equals(".ppt") || leixing.equals(".pptx")) {
                    Glide.with(context).load(R.drawable.icon_type_ppt).into(holder3.iv);
                } else if (leixing.equals(".xls") || leixing.equals(".xlsx")) {
                    Glide.with(context).load(R.drawable.icon_type_xls).into(holder3.iv);
                } else if (leixing.equals(".mp3") || leixing.equals(".MP3") || leixing.equals(".wav") || leixing.equals(".WAV")) {
                    Glide.with(context).load(R.drawable.icon_type_mp3).into(holder3.iv);
                }
                holder3.cb.setChecked(cbState.get(position));
                final ViewHolder3 finalHolder2 = holder3;
                holder3.cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalHolder2.cb.setChecked(finalHolder2.cb.isChecked());
                        if (onSelectDotClick != null) {
                            onSelectDotClick.onDotClick(position, finalHolder2.cb);
                        }
                        if (finalHolder2.cb.isChecked()) {
                            cbState.put(position, true);
                        } else {
                            cbState.put(position, false);
                        }

                    }
                });
                break;
            default:
                break;
        }

        return convertView;
    }


    //取消所有的选择
    public void setCancelAll() {
        for (int i = 0; i < datas.size(); i++) {
            cbState.put(i, false);
        }
        notifyDataSetChanged();
    }

    //全选
    public void setSelectAll() {
        for (int i = 0; i < datas.size(); i++) {
            cbState.put(i, true);
        }
        notifyDataSetChanged();
    }


    static class ViewHolder1 {
        ImageView iv_folder;  //文件夹图标
        TextView tv_month;
        TextView tv_time;
        TextView tv_name;
        TextView tv_content;
        LinearLayout ll;
        CheckBox cb;

    }

    static class ViewHolder2 {
        ImageView iv_cover;  //图片和视频的缩略图
        TextView tv_month;
        TextView tv_time;
        TextView tv_name;
        TextView tv_size;
        LinearLayout ll;
        CheckBox cb;

    }

    static class ViewHolder3 {
        ImageView iv; //普通文件的图标  doc ppt等
        TextView tv_month;
        TextView tv_time;
        TextView tv_name;
        TextView tv_size;
        LinearLayout ll;
        CheckBox cb;

    }

    private OnSelectDotClick onSelectDotClick;

    public interface OnSelectDotClick {
        void onDotClick(int position, CheckBox cb);
    }

    public void setOnSelectDotClick(OnSelectDotClick onSelectDotClick) {
        this.onSelectDotClick = onSelectDotClick;
    }
}
