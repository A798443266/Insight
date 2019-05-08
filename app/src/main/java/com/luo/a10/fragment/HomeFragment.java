package com.luo.a10.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.luo.a10.R;
import com.luo.a10.activity.AllPhotoActivity;
import com.luo.a10.activity.AllVideoActivity;
import com.luo.a10.activity.FenleiActivity;
import com.luo.a10.activity.MyAudioActivity;
import com.luo.a10.activity.MyDocsActivity;
import com.luo.a10.activity.SearchActivity;
import com.luo.a10.activity.TestActivity;
import com.luo.a10.activity.TransmissionActivity;
import com.luo.a10.eventBusObject.CancelEvent;
import com.luo.a10.eventBusObject.OpenMenuEvent;
import com.luo.a10.fragment.others.HomeDownloadFragment;
import com.luo.a10.fragment.others.HomeSeeFragment;
import com.luo.a10.fragment.others.HomeUpFragment;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.MyRoundLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.my_round)
    MyRoundLayout myRound;
    @BindView(R.id.ll_car)
    LinearLayout llCar;
    @BindView(R.id.ll_car1)
    LinearLayout llCar1;
    @BindView(R.id.iv_car1_back)
    ImageView ivCar1Back;
    Unbinder unbinder;

    private String[] titles = new String[]{"查看", "上传", "下载"};
    private ArrayList<Fragment> fragments;
    private HomeSeeFragment fragment1;
    private HomeUpFragment fragment2;
    private HomeDownloadFragment fragment3;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        fragments = new ArrayList<>();
        fragment1 = new HomeSeeFragment();
        fragment2 = new HomeUpFragment();
        fragment3 = new HomeDownloadFragment();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, (FragmentActivity) mContext, fragments);

        tvName.setText(SpUtils.getString(mContext,Constant.USERNAME));
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(OpenMenuEvent flag) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_select_head_out);
        ll.startAnimation(animation);
        myRound.startAnimation(animation);
        ll.setVisibility(View.GONE);
        myRound.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = vp.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;    //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
        //设置margin
        marginParams.setMargins(0, UIUtils.dp2px(40), 0, UIUtils.dp2px(71));//120 - 49  49是底部导航栏的高度
        vp.setLayoutParams(marginParams);
    }

    //接收消息，点击取消的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(CancelEvent s) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_select_head_in);
        ll.setVisibility(View.VISIBLE);
        myRound.setVisibility(View.VISIBLE);
        ll.startAnimation(animation);
        myRound.startAnimation(animation);

        ViewGroup.LayoutParams params = vp.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;    //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
        //设置margin
        marginParams.setMargins(0, 0, 0, 0);
        vp.setLayoutParams(marginParams);

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }


    @OnClick({R.id.iv_car_next, R.id.ll_car1, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_car_next:
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_card_out);
                llCar.startAnimation(animation);
                llCar.setVisibility(View.GONE);
                llCar1.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_car1:
                Animation animation1 = AnimationUtils.loadAnimation(mContext, R.anim.translate_card_in);
                llCar.setVisibility(View.VISIBLE);
                llCar.startAnimation(animation1);
                llCar1.setVisibility(View.GONE);
                break;
            case R.id.fab:
                startActivity(new Intent(mContext, FenleiActivity.class));
                break;
        }
    }


    @OnClick({R.id.iv_history, R.id.iv_search, R.id.iv_notify})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.iv_history:
                startActivity(new Intent(mContext, TransmissionActivity.class));
                break;
            case R.id.iv_search:
                startActivity(new Intent(mContext, SearchActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.iv_notify:
                break;

        }
    }


    @OnClick({R.id.ll5, R.id.ll6, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4})
    public void onViewClicd(View view) {
        switch (view.getId()) {
            case R.id.ll5:
                break;
            case R.id.ll6:
                break;
            case R.id.ll1:
                startActivity(new Intent(mContext, MyDocsActivity.class));
                break;
            case R.id.ll2:
                startActivity(new Intent(mContext, AllPhotoActivity.class));
                break;
            case R.id.ll3:
                startActivity(new Intent(mContext, MyAudioActivity.class));
                break;
            case R.id.ll4:
                startActivity(new Intent(mContext, AllVideoActivity.class));
                break;
        }
    }
}
