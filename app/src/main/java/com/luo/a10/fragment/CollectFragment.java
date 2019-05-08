package com.luo.a10.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.luo.a10.R;
import com.luo.a10.camera.CameraActivity;
import com.luo.a10.fragment.others.CollectFragment1;
import com.luo.a10.fragment.others.CollectFragment2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CollectFragment extends BaseFragment {
    @BindView(R.id.rl_tool)
    RelativeLayout rlTool;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;

    private String[] titles = new String[]{"收藏", "关注"};
    private ArrayList<Fragment> fragments;
    private CollectFragment1 fragment1;
    private CollectFragment2 fragment2;

    private boolean isOpen;//判断是否打开了收藏栏

    @Override
    protected void initView() {
        fragments = new ArrayList<>();
        fragment1 = new CollectFragment1();
        fragment2 = new CollectFragment2();
        fragments.add(fragment1);
        fragments.add(fragment2);

        vp.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tabLayout.setViewPager(vp);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect;
    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll7, R.id.ll8, R.id.ll9})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                break;
            case R.id.ll2:
                break;
            case R.id.ll3:
                break;
            case R.id.ll4://关注
                tabLayout.setCurrentTab(1);
                rlTool.setVisibility(View.GONE);
                llCollect.setVisibility(View.VISIBLE);
                isOpen = true;
                break;
            case R.id.ll5:
                break;
            case R.id.ll7://收藏
                tabLayout.setCurrentTab(0);
                rlTool.setVisibility(View.GONE);
                llCollect.setVisibility(View.VISIBLE);
                isOpen = true;
                break;
            case R.id.ll8:
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0x12);
                    return;
                }
                CameraActivity.openCertificateCamera(getActivity(), 0);
                break;
            case R.id.ll9:
                break;
        }
    }


    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        llCollect.setVisibility(View.GONE);
        rlTool.setVisibility(View.VISIBLE);
        isOpen = false;
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }



}
