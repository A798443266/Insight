package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.luo.a10.R;
import com.luo.a10.adapter.ShareAdapter;
import com.luo.a10.bean.ShareInfo;
import com.luo.a10.bean.change.FolderAndDoc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareJiluActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;

    private ShareAdapter adapter;
    private List<ShareInfo> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_jilu);
        ButterKnife.bind(this);

        initData();
        adapter = new ShareAdapter(this,datas);
        lv.setAdapter(adapter);
    }

    private void initData() {
        FolderAndDoc doc4 = new FolderAndDoc();
        ShareInfo shareInfo4 = new ShareInfo();
        doc4.setName("20190410_623845.PNG");
        doc4.setCategory(2);
        doc4.setSize(1024 * 1024);
        doc4.setId(1);
        doc4.setLink("http://192.168.191.1:8080/A10/img2/img20.jpg");
        doc4.setTime("1555336472353");
        doc4.setpId(1);
        shareInfo4.setFolderAndDoc(doc4);
        shareInfo4.setDownload(2);
        shareInfo4.setLook(3);
        datas.add(shareInfo4);

        FolderAndDoc doc1 = new FolderAndDoc();
        ShareInfo shareInfo1 = new ShareInfo();
        doc1.setName("浅谈电子商务的服务与信誉.doc");
        doc1.setCategory(1);
        doc1.setSize(1024 * 1024);
        doc1.setTime("1555336479342");
        doc1.setId(1);
        doc1.setLink("");
        doc1.setpId(1);
        shareInfo1.setFolderAndDoc(doc1);
        shareInfo1.setDownload(1);
        shareInfo1.setLook(1);
        datas.add(shareInfo1);

        FolderAndDoc doc2 = new FolderAndDoc();
        ShareInfo shareInfo2 = new ShareInfo();
        doc2.setName("时间敏感数据流上的频繁项集挖掘算法.pdf");
        doc2.setCategory(1);
        doc2.setSize(1024 * 1024);
        doc2.setTime("1555336478558");
        doc2.setId(1);
        doc2.setLink("");
        doc2.setpId(1);
        shareInfo2.setFolderAndDoc(doc2);
        shareInfo2.setDownload(0);
        shareInfo2.setLook(1);
        datas.add(shareInfo2);

        FolderAndDoc doc3 = new FolderAndDoc();
        ShareInfo shareInfo3 = new ShareInfo();
        doc3.setName("资料课程费用.xlsx");
        doc3.setCategory(1);
        doc3.setSize(1024 * 1024);
        doc3.setTime("1555336478558");
        doc3.setId(1);
        doc3.setLink("");
        doc3.setpId(1);
        shareInfo3.setFolderAndDoc(doc3);
        shareInfo3.setDownload(6);
        shareInfo3.setLook(8);
        datas.add(shareInfo3);


        FolderAndDoc doc5 = new FolderAndDoc();
        ShareInfo shareInfo5 = new ShareInfo();
        doc5.setName("20190403_623345.JPG");
        doc5.setCategory(2);
        doc5.setSize(1024 * 1024);
        doc5.setTime("1555336478558");
        doc5.setId(1);
        doc5.setLink("http://192.168.191.1:8080/A10/img2/img11.jpg");
        doc5.setpId(1);
        shareInfo5.setFolderAndDoc(doc5);
        shareInfo5.setDownload(1);
        shareInfo5.setLook(5);
        datas.add(shareInfo5);


    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
