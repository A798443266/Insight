package com.luo.a10.fragment.others;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.adapter.PhotoFragment3Adapter;
import com.luo.a10.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 最近上传图片的fragment
 */
public class DownloadFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private List<String> datas;
    private DownloadAdapter adapter;


    @Override
    protected void initView() {
        datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add(i + "");
        }

        adapter = new DownloadAdapter(datas,mContext);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_download;
    }


}
