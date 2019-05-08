package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.luo.a10.R;
import com.luo.a10.eventBusObject.PopEvent;
import com.luo.a10.fragment.others.PhotoFragment1;
import com.luo.a10.fragment.others.PhotoFragment2;
import com.luo.a10.fragment.others.PhotoFragment3;
import com.luo.a10.fragment.others.VideoFragment1;
import com.luo.a10.utils.UIUtils;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;

    private String[] titles = new String[]{"时光轴 ﹀ ", "智能分类", "最近上传"};
    private ArrayList<Fragment> fragments;
    private PhotoFragment1 fragment1;
    private PhotoFragment2 fragment2;
    private PhotoFragment3 fragment3;
    private EasyPopup mCirclePop;

    private LinearLayout[] lls = new LinearLayout[4];
    private TextView[] tvs = new TextView[4];
    private int tvColorFlag = 0;//用来记录pop选择了哪项，好让下次弹出的pop的textview变颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_photo);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragment1 = new PhotoFragment1();
        fragment2 = new PhotoFragment2();
        fragment3 = new PhotoFragment3();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, this, fragments);

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    View view = View.inflate(AllPhotoActivity.this, R.layout.pop_photo_paixv, null);
                    lls[0] = view.findViewById(R.id.ll0);
                    lls[1] = view.findViewById(R.id.ll1);
                    lls[2] = view.findViewById(R.id.ll2);
                    lls[3] = view.findViewById(R.id.ll3);
                    tvs[0] = view.findViewById(R.id.tv0);
                    tvs[1] = view.findViewById(R.id.tv1);
                    tvs[2] = view.findViewById(R.id.tv2);
                    tvs[3] = view.findViewById(R.id.tv3);
                    setPopListener();
                    mCirclePop = new EasyPopup(AllPhotoActivity.this)
                            .setContentView(view)
                            .setWidth(UIUtils.dp2px(100))
                            .setHeight(UIUtils.dp2px(150))
                            .setBackgroundDimEnable(true)
                            .setAnimationStyle(R.style.popphoto_animation)
                            .setDimValue(0.2f)
                            .setFocusAndOutsideEnable(true)
                            .apply();
                    mCirclePop.showAtAnchorView(tabLayout, YGravity.BELOW, XGravity.ALIGN_LEFT, UIUtils.dp2px(20), UIUtils.dp2px(3));
                    mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mCirclePop = null;
                        }
                    });
                    for(int i = 0;i < 4;i++){
                        if(tvColorFlag == i)
                            tvs[i].setTextColor(UIUtils.getColor(R.color.system_blue));
                        else
                            tvs[i].setTextColor(UIUtils.getColor(R.color.system_gray));
                    }
                }
            }
        });
    }

    private void setPopListener() {
        for(int i = 0;i < 4;i++){
            lls[i].setOnClickListener(this);
        }
    }


    @OnClick({R.id.ll_back, R.id.iv_search, R.id.iv_chuanshu, R.id.iv_paixv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_search:
                break;
            case R.id.iv_chuanshu:
                break;
            case R.id.iv_paixv:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll0:
                EventBus.getDefault().post(new PopEvent(0));
                tvColorFlag = 0;
                dismissPop();
                break;

            case R.id.ll1:
                EventBus.getDefault().post(new PopEvent(1));
                tvColorFlag = 1;
                dismissPop();
                break;

            case R.id.ll2:
                EventBus.getDefault().post(new PopEvent(2));
                tvColorFlag = 2;
                dismissPop();
                break;
            case R.id.ll3:
                EventBus.getDefault().post(new PopEvent(3));
                tvColorFlag = 3;
                dismissPop();
                break;
            default:
                break;
        }
    }

    private void dismissPop(){
        if(mCirclePop != null){
            mCirclePop.dismiss();
        }
    }
}
