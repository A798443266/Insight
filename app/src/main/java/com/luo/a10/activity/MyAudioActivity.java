package com.luo.a10.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.luo.a10.R;
import com.luo.a10.adapter.change.FolderAdapter;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MyAudioActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private FolderAdapter adapter;
    private List<FolderAndDoc> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_audio);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        llLoad.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(Constant.Audioinfo)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
                Toast.makeText(MyAudioActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
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
            adapter = new FolderAdapter(datas, this);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FolderAndDoc folderAndDoc = datas.get(position);
                    Intent intent = new Intent(MyAudioActivity.this, MusicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("music", folderAndDoc);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            });
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
