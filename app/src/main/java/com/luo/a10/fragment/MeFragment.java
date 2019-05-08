package com.luo.a10.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luo.a10.R;
import com.luo.a10.activity.BeifenActivity;
import com.luo.a10.activity.HuiSZActivity;
import com.luo.a10.activity.MyVIPActivity;
import com.luo.a10.activity.ShareJiluActivity;
import com.luo.a10.activity.settingActivity;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import com.luo.a10.view.PerfectArcView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeFragment extends BaseFragment {
    @BindView(R.id.perfectarcview)
    PerfectArcView perfectarcview;
    @BindView(R.id.tv_username)
    TextView tvUsername;

    @Override
    protected void initView() {
        perfectarcview.setDrawableResource(R.drawable.me_head_bg);
        tvUsername.setText(SpUtils.getString(mContext, Constant.USERNAME));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }


    @OnClick(R.id.rl4)
    public void onViewClicked() {
        startActivity(new Intent(mContext,settingActivity.class));
    }


    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                startActivity(new Intent(mContext,HuiSZActivity.class));
                break;
            case R.id.ll2:
                startActivity(new Intent(mContext,MyVIPActivity.class));
                break;
            case R.id.ll3:
                startActivity(new Intent(mContext,ShareJiluActivity.class));
                break;
            case R.id.ll4:
                startActivity(new Intent(mContext,BeifenActivity.class));
                break;

        }
    }
}
