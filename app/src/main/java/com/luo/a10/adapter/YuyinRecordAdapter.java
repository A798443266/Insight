package com.luo.a10.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.luo.a10.R;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.MyRoundLayout;
import com.luo.a10.yuyinrecord.RecordingItem;
import com.white.progressview.HorizontalProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类详情照片的适配器
 */
public class YuyinRecordAdapter extends BaseAdapter {

    private Context context;
    private List<RecordingItem> datas;
    private int temp = -1;
    private int playPosition = -1;
    private MediaPlayer mediaPlayer;

    public YuyinRecordAdapter(Context context, List<RecordingItem> datas) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Viewholder holder;
        if (convertView == null) {
            holder = new Viewholder();
            convertView = View.inflate(context, R.layout.item_yuyin_record, null);
            holder.iv_play = convertView.findViewById(R.id.iv_play);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_riqi = convertView.findViewById(R.id.tv_riqi);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_current = convertView.findViewById(R.id.tv_current);
            holder.tv_total = convertView.findViewById(R.id.tv_total);
            holder.seekbar = convertView.findViewById(R.id.seekbar);
            holder.rl2 = convertView.findViewById(R.id.rl2);

            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        if (temp != position) {
            holder.rl2.setVisibility(View.GONE);
            holder.iv_play.setBackgroundResource(R.drawable.icon_play_black);
        } else
            holder.rl2.setVisibility(View.VISIBLE);

        //更新进度条
        if(position == playPosition){
            if(mediaPlayer != null) {
                Log.e("TAG", "更新");
                holder.seekbar.setProgress(mediaPlayer.getCurrentPosition());
                holder.tv_current.setText(UIUtils.StringTime(mediaPlayer.getCurrentPosition()));
            }
        }

        RecordingItem item = datas.get(position);
        holder.tv_time.setText(UIUtils.stringForTime((int) item.getTime()));
        holder.tv_riqi.setText(UIUtils.StringTime(item.getRiqi()));
        holder.tv_name.setText(item.getName());

        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = position;
                if (onPlayListener != null) {
                    onPlayListener.onPlay(position, holder.rl2, holder.iv_play, holder.seekbar, holder.tv_current, holder.tv_total);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    //更新进度条
    public void update(MediaPlayer mediaPlayer,int position) {
        this.mediaPlayer = mediaPlayer;
        this.playPosition = position;
        notifyDataSetChanged();
    }

    //停止播放
    public void stop(MediaPlayer mediaPlayer, int position) {
        this.mediaPlayer = mediaPlayer;
        this.playPosition = position;
        notifyDataSetChanged();
    }


    static class Viewholder {
        ImageView iv_play;
        TextView tv_name;
        TextView tv_riqi;
        TextView tv_time;
        TextView tv_current;
        TextView tv_total;
        SeekBar seekbar;
        RelativeLayout rl2;
    }


    private OnPlayListener onPlayListener;

    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    public interface OnPlayListener {
        void onPlay(int position, RelativeLayout rl2, ImageView iv_play, SeekBar seekbar, TextView tv_current, TextView tv_total);
    }
}
