package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.flyco.tablayout.SlidingTabLayout;
import com.luo.a10.R;
import com.luo.a10.fileselect.bean.FileItem;
import com.luo.a10.fragment.others.DownloadFragment;
import com.luo.a10.fragment.others.PhotoFragment2;
import com.luo.a10.fragment.others.UpadateFragment;
import com.luo.a10.fragment.others.VideoFragment1;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 传输列表
 */
public class TransmissionActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;

    private List<FileItem> files;//接收传递过来的额要上传的文件列表
    String folderId="";
    String tag = "";

    private String[] titles = new String[]{"上传列表", "下载列表"};
    private ArrayList<Fragment> fragments;
    private DownloadFragment fragment1;
    private UpadateFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);
        ButterKnife.bind(this);

        files = (List<FileItem>) getIntent().getSerializableExtra("files");
        folderId = getIntent().getStringExtra("folderId");//要上传的文件夹id
        tag = getIntent().getStringExtra("tag");
        Log.e("TAG", "T:"+tag);

        fragments = new ArrayList<>();
        fragment1 = new DownloadFragment();
        fragment2 = new UpadateFragment(files,folderId,tag);
        fragments.add(fragment2);
        fragments.add(fragment1);
        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, this, fragments);

    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
