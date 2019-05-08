package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.luo.a10.R;
import com.luo.a10.adapter.HuiszAdapter;
import com.luo.a10.bean.change.FolderAndDoc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 回收站
 */
public class HuiSZActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;

    private HuiszAdapter adapter;
    private List<FolderAndDoc> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hui_sz);
        ButterKnife.bind(this);

        initData();
        adapter = new HuiszAdapter(this,datas);
        lv.setAdapter(adapter);
    }

    private void initData() {
        FolderAndDoc doc4 = new FolderAndDoc();
        doc4.setName("20190410_623845.PNG");
        doc4.setCategory(2);
        doc4.setSize(1024 * 1024);
        doc4.setId(1);
        doc4.setLink("http://192.168.191.1:8080/A10/img2/img22.jpg");
        doc4.setTime("1555336479342");
        doc4.setpId(1);
        datas.add(doc4);

        FolderAndDoc doc1 = new FolderAndDoc();
        doc1.setName("营销方案第二版.pdf.doc");
        doc1.setCategory(1);
        doc1.setSize(1024 * 1024+200);
        doc1.setTime("1555336479342");
        doc1.setId(1);
        doc1.setLink("");
        doc1.setpId(1);
        datas.add(doc1);

        FolderAndDoc doc2 = new FolderAndDoc();
        doc2.setName("MP3_20190310_2220825.mp3");
        doc2.setCategory(3);
        doc2.setSize(1024 * 1024+160);
        doc2.setTime("1555336479342");
        doc2.setId(1);
        doc2.setLink("");
        doc2.setpId(1);
        datas.add(doc2);

        FolderAndDoc doc3 = new FolderAndDoc();
        doc3.setName("20190410_623845.jpg");
        doc3.setCategory(2);
        doc3.setSize(1024 * 1024*3);
        doc3.setTime("1555336479342");
        doc3.setId(1);
        doc3.setLink("http://192.168.191.1:8080/A10/img2/img28.jpg");
        doc3.setpId(1);
        datas.add(doc3);


        FolderAndDoc doc5 = new FolderAndDoc();
        doc5.setName("购买记录.docx");
        doc5.setCategory(1);
        doc5.setSize(1024 * 1024);
        doc5.setTime("1555336479342");
        doc5.setId(1);
        doc5.setLink("http://192.168.191.1:8080/A10/img2/img23.jpg");
        doc5.setpId(1);
        datas.add(doc5);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
