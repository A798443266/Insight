package com.luo.a10.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.activity.DocSeeActivity;
import com.luo.a10.activity.MusicActivity;
import com.luo.a10.activity.OpenFolderActivity;
import com.luo.a10.activity.PlayVideoActivity;
import com.luo.a10.adapter.DocAdapter;
import com.luo.a10.adapter.change.FolderAdapter;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class CollectFragment2 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private FolderAdapter adapter;
    private List<FolderAndDoc> datas;

    @Override
    protected void initView() {
    }

    @Override
    public void initData() {
        llLoad.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(Constant.Guanzhu)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
                Toast.makeText(mContext,"网络出错了",Toast.LENGTH_SHORT).show();
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
        if(datas != null && datas.size() > 0){
            tvNo.setVisibility(View.GONE);
            adapter = new FolderAdapter(datas,mContext);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FolderAndDoc folderAndDoc = datas.get(position);
                    if(folderAndDoc.getCategory() == -1){//文件夹
                        Intent intent  = new Intent(mContext,OpenFolderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("folder",folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }else if(folderAndDoc.getCategory() == 2){//图片
                        Intent intent  = new Intent(mContext,BigPhotoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("path", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if(folderAndDoc.getCategory() == 3){//音频
                        Intent intent = new Intent(mContext, MusicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("music", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if(folderAndDoc.getCategory() == 4){//视频
                        Intent intent = new Intent(mContext, PlayVideoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("video", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{//文档
                        Intent intent = new Intent(mContext, DocSeeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("doc", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_see;
    }
}
