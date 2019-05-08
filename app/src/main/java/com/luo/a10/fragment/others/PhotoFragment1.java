package com.luo.a10.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.activity.PlayVideoActivity;
import com.luo.a10.bean.DayPhotoItem;
import com.luo.a10.bean.PicInfo;
import com.luo.a10.bean.PicTimeAxis;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.eventBusObject.PopEvent;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.view.MyGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;
import butterknife.BindView;
import okhttp3.Call;

public class PhotoFragment1 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.gv)
    MyGridView gv;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private List<PicTimeAxis> datas;
    private MyAdapter adapter1;
    private GvAdapter adapter2;


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(PopEvent p) {
        switch (p.getType()) {
            case 0:
            case 1:
            case 2:
                if (adapter1 != null) {
                    lv.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
                break;
            case 3:
                if (adapter2 != null) {
                    lv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {
        OkHttpUtils.get().url(Constant.Sortimage).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                datas = JsonUtils.parseShiGZ(response);
                show();
            }
        });
    }

    private void show() {
        if(datas != null && datas.size() > 0){
            adapter1 = new MyAdapter();
            lv.setAdapter(adapter1);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    lv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    tvTime.setText(datas.get(position).getMonth());

                    adapter2 = new GvAdapter(datas.get(position).getPics());
                    gv.setAdapter(adapter2);

                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                            FolderAndDoc folderAndDoc = new FolderAndDoc();
                            PicInfo picInfo = datas.get(position).getPics().get(position1);

                            folderAndDoc.setLink(picInfo.getUrl());
                            folderAndDoc.setName(picInfo.getName());
                            folderAndDoc.setTime(picInfo.getTime());
                            folderAndDoc.setTag(picInfo.getTag());
                            folderAndDoc.setId(picInfo.getId());
                            folderAndDoc.setSize(Long.parseLong(picInfo.getSize()));

                            Intent intent = new Intent(mContext, BigPhotoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("path",folderAndDoc);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                }
            });

        }else{

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    //按年或者月份查看的适配器
    class MyAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_photo1, null);
                holder.iv = convertView.findViewById(R.id.iv);
                holder.tv_num = convertView.findViewById(R.id.tv_num);
                holder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PicTimeAxis picTimeAxis = datas.get(position);
            holder.tv_num.setText(picTimeAxis.getPics().size()+"张");
            holder.tv_time.setText(picTimeAxis.getMonth());
            Glide.with(mContext).load( picTimeAxis.getPics().get(0).getUrl())
                    .error(R.drawable.city1).into(holder.iv);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_time;
        TextView tv_num;
        ImageView iv;
    }



    class GvAdapter extends BaseAdapter {

        private List<PicInfo> pics;

        public GvAdapter(List<PicInfo> pics) {
            this.pics = pics;
        }

        @Override
        public int getCount() {
            return pics == null ? 0 : pics.size();
        }

        @Override
        public Object getItem(int position) {
            return pics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 holder;
            if (convertView == null) {
                holder = new ViewHolder1();
                convertView = View.inflate(mContext, R.layout.item_list_inner1, null);
                holder.iv = convertView.findViewById(R.id.iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }
            PicInfo pic = pics.get(position);
            Glide.with(mContext).load(pic.getUrl())
                    .error(R.drawable.city1).into(holder.iv);
            return convertView;
        }
    }

    static class ViewHolder1 {
        ImageView iv;
    }


}
