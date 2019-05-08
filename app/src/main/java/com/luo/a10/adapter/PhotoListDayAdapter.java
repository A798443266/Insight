package com.luo.a10.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.bean.DayPhotoItem;
import com.luo.a10.bean.PicInfo;
import com.luo.a10.bean.PicTimeAxis;
import com.luo.a10.utils.UIUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 按天查看照片的适配器
 */
public class PhotoListDayAdapter extends BaseAdapter {

    private PicTimeAxis datas;
    private Context context;

    public PhotoListDayAdapter(PicTimeAxis datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.getPics() == null ? 0 : datas.getPics().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_photo_day, null);
            holder.gv = convertView.findViewById(R.id.gv);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<PicInfo> pics = datas.getPics();
        holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(pics.get(0).getTime())));
//        MyGridViewAdapter1 adapter = new MyGridViewAdapter1(datas.get(position).getPics(),context);
//        holder.gv.setAdapter(adapter);
        holder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
//                Intent intent  = new Intent(context,BigPhotoActivity.class);
//                intent.putExtra("path",datas.get(position).getPics().get(index));
//                context.startActivity(intent);
            }
        });
        return convertView;
    }
}


class ViewHolder {
    TextView tv_time;
    GridView gv;

}
