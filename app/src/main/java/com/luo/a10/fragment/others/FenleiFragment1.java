package com.luo.a10.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.activity.FenleiDetailsActivity;
import com.luo.a10.adapter.FenleiFragmentAdapter;
import com.luo.a10.bean.Guidang;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class FenleiFragment1 extends BaseFragment {

    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private FenleiFragmentAdapter adapter;
    private List<Guidang> datas;
    private String data;


    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

        data = SpUtils.getString(mContext, Constant.save_guidang1);
        if (!TextUtils.isEmpty(data)) {
            datas = JsonUtils.parseFenlei(data);
        }

        llLoad.setVisibility(View.VISIBLE);

        OkHttpUtils.get().url(Constant.get_insight).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
            }

            @Override
            public void onResponse(String response, int id) {
                SpUtils.putString(mContext, Constant.save_guidang1, response);
                datas = JsonUtils.parseFenlei(response);
                show();
            }
        });
    }

    private void show() {
        if (llLoad != null)
            llLoad.setVisibility(View.GONE);
        if (datas != null && datas.size() > 0) {
            adapter = new FenleiFragmentAdapter(mContext, datas);
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getDataFromNet(datas.get(position).getId(), position);
                }
            });
        } else {

        }
    }

    private void getDataFromNet(int id, final int position) {
        OkHttpUtils.get().url(Constant.Fenelei + id).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Intent intent = new Intent(mContext, FenleiDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("guidang", datas.get(position));
                intent.putExtras(bundle);
                intent.putExtra("result", response);
                startActivity(intent);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_fenlei1;
    }

}
