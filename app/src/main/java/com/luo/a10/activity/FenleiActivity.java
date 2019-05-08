package com.luo.a10.activity;

/**
 * 自定义分类页面
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.luo.a10.R;
import com.luo.a10.fragment.others.FenleiFragment1;
import com.luo.a10.fragment.others.FenleiFragment2;
import com.luo.a10.fragment.others.PhotoFragment1;
import com.luo.a10.fragment.others.PhotoFragment2;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FenleiActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;

    private String[] titles = new String[]{"智能归档", "自定义归档"};
    private ArrayList<Fragment> fragments;
    private FenleiFragment1 fragment1;
    private FenleiFragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragment1 = new FenleiFragment1();
        fragment2 = new FenleiFragment2();
        fragments.add(fragment1);
        fragments.add(fragment2);

        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, this, fragments);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
