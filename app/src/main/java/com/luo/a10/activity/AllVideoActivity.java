package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.PopupWindow;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.luo.a10.R;
import com.luo.a10.fragment.others.PhotoFragment2;
import com.luo.a10.fragment.others.PhotoFragment3;
import com.luo.a10.fragment.others.VideoFragment1;
import com.luo.a10.utils.UIUtils;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllVideoActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;

    private EasyPopup mCirclePop;
    private String[] titles = new String[]{"列表 ﹀ ", "智能分类", "最近上传"};
    private ArrayList<Fragment> fragments;
    private VideoFragment1 fragment1;
    private PhotoFragment2 fragment2;
    private PhotoFragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_video);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragment1 = new VideoFragment1();
        fragment2 = new PhotoFragment2();
        fragment3 = new PhotoFragment3();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, this, fragments);

        setPop();
    }

    private void setPop() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    View view = View.inflate(AllVideoActivity.this, R.layout.pop_video_paixv, null);
                    mCirclePop = new EasyPopup(AllVideoActivity.this)
                            .setContentView(view)
                            .setWidth(UIUtils.dp2px(100))
                            .setHeight(UIUtils.dp2px(120))
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
                }
            }
        });
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
