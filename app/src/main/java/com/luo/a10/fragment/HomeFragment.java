package com.luo.a10.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.luo.a10.R;
import com.luo.a10.activity.AllPhotoActivity;
import com.luo.a10.activity.AllVideoActivity;
import com.luo.a10.activity.MyAudioActivity;
import com.luo.a10.activity.MyDocsActivity;
import com.luo.a10.activity.SearchActivity;
import com.luo.a10.activity.SearchResultActivity;
import com.luo.a10.activity.TransmissionActivity;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.MyRoundLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {

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
    @BindView(R.id.piechart)
    PieChart piechart;
    @BindView(R.id.radarChart)
    RadarChart radarChart;

    private Typeface mTf;//声明字体库
    private float[] data = {16.4f, 43.7f, 32.5f, 4.6f, 2.8f};


    @Override
    protected void initView() {
        tvName.setText(SpUtils.getString(mContext, Constant.USERNAME));
    }

    @Override
    public void initData() {
        initPieChart();//初始化饼状图
    }

    private void initPieChart() {
        mTf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        piechart.setDescription("");
        piechart.setHoleRadius(50f);//最内层的圆的半径
        piechart.setTransparentCircleRadius(55f);//包裹内层圆的半径
        piechart.setCenterText("6.24GB");
        piechart.setCenterTextTypeface(mTf);
        piechart.setCenterTextSize(10f);
        //是否使用百分比。true:各部分的百分比的和是100%。
        piechart.setUsePercentValues(false);
        piechart.setDrawSliceText(false);
        piechart.setCenterTextColor(UIUtils.getColor(R.color.system_blue1));

        PieData pieData = getDataPie();
        pieData.setDrawValues(false);
        pieData.setValueTypeface(mTf);
        pieData.setValueTextSize(7f);
        pieData.setValueTextColor(UIUtils.getColor(R.color.system_gray));
        // set data
        piechart.setData(pieData);
        //获取右上角的描述结构的对象
        Legend l = piechart.getLegend();
        l.setTextSize(9f);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setYEntrySpace(2f);//相邻的entry在y轴上的间距
        l.setYOffset(0f);//第一个entry距离最顶端的间距
        l.setXEntrySpace(10f);
        l.setTextColor(UIUtils.getColor(R.color.system_gray));
        piechart.animateXY(1500, 1500);
    }

    private PieData getDataPie() {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(16.4f, 0));
        entries.add(new Entry(43.7f, 1));
        entries.add(new Entry(32.5f, 2));
        entries.add(new Entry(4.6f, 3));
        entries.add(new Entry(2.8f, 4));
        PieDataSet d = new PieDataSet(entries, "");
        d.setSliceSpace(1f);//两个饼图之间的间隔
        //设置颜色
        int[] colors = {
                Color.rgb(130, 212, 253), Color.rgb(253, 244, 108), Color.rgb(212, 151, 255),
                Color.rgb(97, 215, 123), Color.rgb(248, 181, 81)
        };
        d.setColors(colors);
        ArrayList<String> q = new ArrayList<String>();
        q.add("文档  " + data[0] + "%  " + "1.02GB");
        q.add("图片  " + data[1] + "%  " + "2.73GB");
        q.add("视频  " + data[2] + "%  " + "2.03GB");
        q.add("音频  " + data[3] + "%  " + "0.27GB");
        q.add("其他  " + data[4] + "%  " + "0.17GB");
        PieData cd = new PieData(q, d);
        return cd;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @OnClick({R.id.iv_car_next, R.id.ll_car1})
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
//            case R.id.fab:
//                startActivity(new Intent(mContext, FenleiActivity.class));
//                break;
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
//                startActivity(new Intent(mContext, MyAudioActivity.class));
                startActivity(new Intent(mContext, SearchResultActivity.class));
                break;
            case R.id.ll4:
                startActivity(new Intent(mContext, AllVideoActivity.class));
                break;
        }
    }

}
