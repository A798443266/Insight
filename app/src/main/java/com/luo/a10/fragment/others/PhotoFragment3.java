package com.luo.a10.fragment.others;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.adapter.PhotoFragment3Adapter;
import com.luo.a10.adapter.PhotoTypeAdapter;
import com.luo.a10.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 最近上传图片的fragment
 */
public class PhotoFragment3 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private List<String> datas;
    private PhotoFragment3Adapter adapter;
    public String BASE = "http://192.168.191.1:8080/A10/fenlei_img/";


    @Override
    protected void initView() {
        datas = new ArrayList<>();
        datas.add(BASE + "ziran_pics/ziran4.jpg");
        datas.add(BASE + "ziran_pics/ziran5.jpg");
        datas.add(BASE + "ziran_pics/ziran6.jpg");
        datas.add(BASE + "ziran_pics/ziran7.jpg");
        datas.add(BASE + "ziran_pics/ziran8.jpg");

        adapter = new PhotoFragment3Adapter(datas,mContext);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext,BigPhotoActivity.class));
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo3;
    }


}
