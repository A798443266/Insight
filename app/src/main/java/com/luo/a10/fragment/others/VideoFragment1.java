package com.luo.a10.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.activity.DocSeeActivity;
import com.luo.a10.activity.MusicActivity;
import com.luo.a10.activity.OpenFolderActivity;
import com.luo.a10.activity.PlayVideoActivity;
import com.luo.a10.adapter.change.FolderAdapter;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.eventBusObject.PopEvent;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import okhttp3.Call;

public class VideoFragment1 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private List<FolderAndDoc> datas;
    private MyAdapter adapter;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }


    @Override
    public void initData() {
        llLoad.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(Constant.Videoinfo)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
                Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                datas = JsonUtils.parseCollection(response);
                show();
            }
        });
    }

    private void show() {
        llLoad.setVisibility(View.GONE);
        if (datas != null && datas.size() > 0) {
            tvNo.setVisibility(View.GONE);
            adapter = new MyAdapter();
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, PlayVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("video", datas.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(PopEvent p) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    class MyAdapter extends BaseAdapter {

        Map<Integer, Boolean> cbState = new TreeMap<>();//记录每个cb的选择状态，防止convertView复用带来的混乱问题

        public Map<Integer, Boolean> getCbState() {
            return cbState;
        }

        public void setCbState(int position, boolean b) {
            cbState.put(position, b);
        }

        public MyAdapter() {
            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    cbState.put(i, false);//初始化每个cb的状态
                }
            }
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
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_video1, null);
                holder.ll = convertView.findViewById(R.id.ll);
                holder.tv_time = convertView.findViewById(R.id.tv_time);
                holder.tv_month = convertView.findViewById(R.id.tv_month);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_size = convertView.findViewById(R.id.tv_size);
                holder.iv_cover = convertView.findViewById(R.id.iv_cover);
                holder.cb = convertView.findViewById(R.id.cb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            FolderAndDoc video = datas.get(position);

            if (position == 0) {
                holder.tv_month.setText(UIUtils.StringTime(Long.parseLong(video.getTime())).substring(0, 7));
                holder.ll.setVisibility(View.VISIBLE);
            } else {
                String preMonth = UIUtils.StringTime(Long.parseLong(datas.get(position - 1).getTime())).substring(0, 7);
                String currentMonth = UIUtils.StringTime(Long.parseLong(video.getTime())).substring(0, 7);
                if (currentMonth.equals(preMonth)) {
                    holder.ll.setVisibility(View.GONE);
                } else {
                    holder.ll.setVisibility(View.VISIBLE);
                    holder.tv_month.setText(currentMonth);
                }
            }


            Glide.with(mContext).load(video.getCover()).error(R.drawable.city1).into(holder.iv_cover);
            holder.tv_name.setText(video.getName());
            holder.tv_size.setText(Formatter.formatFileSize(mContext, video.getSize()));
            holder.tv_time.setText(UIUtils.StringTime(Long.parseLong(video.getTime())));


            holder.cb.setChecked(cbState.get(position));
            /*holder.cb.setOnClickListener(new View.OnClickListener() {
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
            });*/
            return convertView;
        }
    }

    static class ViewHolder {
        LinearLayout ll;
        TextView tv_time;
        TextView tv_month;
        TextView tv_name;
        TextView tv_size;
        ImageView iv_cover;
        CheckBox cb;
    }

}
