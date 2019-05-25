package com.luo.a10.fileselect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.luo.a10.fileselect.fragment.Base1Fragment;

import java.util.List;

public class VpAdapter extends FragmentPagerAdapter {
    private final FragmentManager fm;
    private final List<Base1Fragment> list;
    private String[] titles;

    public VpAdapter(FragmentManager fm, List<Base1Fragment> fragmentList, String[] titles) {
        super(fm);
        this.fm = fm;
        list = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
