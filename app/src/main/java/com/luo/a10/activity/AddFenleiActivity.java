package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import com.luo.a10.R;
import com.luo.a10.adapter.fenlei.AddFenleiAdapter;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 添加分类的界面
 */
public class AddFenleiActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.gv)
    GridView gv;

    private AddFenleiAdapter adapter;
    private List<FolderAndDoc> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fenlei);
        ButterKnife.bind(this);

        datas = (List<FolderAndDoc>) getIntent().getSerializableExtra("data");
        if (datas != null) {
            adapter = new AddFenleiAdapter(this, datas);
            gv.setAdapter(adapter);
        }

    }

    @OnClick({R.id.rl_back, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_save:

                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    Toast.makeText(this, "请输入分类名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String,String> map = new IdentityHashMap<>();
                map.put("name", etName.getText().toString().trim());
                for (int i = 0; i < datas.size(); i++) {
                    map.put("ids",datas.get(i).getId()+"");
                }

                for (int i = 0; i < datas.size(); i++) {
                    map.put("category",datas.get(i).getId()+"");
                }


                OkHttpUtils.post().url(Constant.Createdefined)
                        .addParams("name", etName.getText().toString().trim())
                        .addParams("ids", datas.get(0).getId()+"")
                        .addParams("ids", datas.get(1).getId()+"")
                        .addParams("category", datas.get(0).getCategory()+"")
                        .addParams("category", datas.get(1).getCategory()+"")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(AddFenleiActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Toast.makeText(AddFenleiActivity.this, "归档成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                break;
        }

    }

}
