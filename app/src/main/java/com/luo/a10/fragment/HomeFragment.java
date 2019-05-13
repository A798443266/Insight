package com.luo.a10.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.luo.a10.R;
import com.luo.a10.activity.AllPhotoActivity;
import com.luo.a10.activity.AllVideoActivity;
import com.luo.a10.activity.FenleiActivity;
import com.luo.a10.activity.MyAudioActivity;
import com.luo.a10.activity.MyDocsActivity;
import com.luo.a10.activity.SearchActivity;
import com.luo.a10.activity.SearchResultActivity;
import com.luo.a10.activity.TransmissionActivity;
import com.luo.a10.adapter.HomeFragmentAdapter;
import com.luo.a10.bean.HomeGuidang;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.MyGridView;
import com.luo.a10.view.MyRoundLayout;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.gv)
    MyGridView gv;

    private Typeface mTf;//声明字体库
    private float[] data = {16.4f, 43.7f, 32.5f, 4.6f, 2.8f};
    private HomeFragmentAdapter adapter;
    private List<HomeGuidang> datas = new ArrayList<>();

    @Override
    protected void initView() {
        tvName.setText(SpUtils.getString(mContext, Constant.USERNAME));
    }

    @Override
    public void initData() {

        HomeGuidang guidang1 = new HomeGuidang();
        guidang1.setName("旅游景点");
        guidang1.setNum(6);
        guidang1.setPic(Constant.BASEURL + "testpreview/xiaozhu/IMAGE/leifeng.jpg");
        datas.add(guidang1);

        HomeGuidang guidang2 = new HomeGuidang();
        guidang2.setName("宝宝");
        guidang2.setNum(3);
        guidang2.setPic(Constant.BASEURL + "testpreview/xiaozhu/IMAGE/59797992720793_n.jpg");
        datas.add(guidang2);

        HomeGuidang guidang3 = new HomeGuidang();
        guidang3.setName("美食");
        guidang3.setNum(4);
        guidang3.setPic(Constant.BASEURL + "testpreview/xiaozhu/IMAGE/pictureunlock_hknew_52679.pictureunlock.jpg");
        datas.add(guidang3);

        HomeGuidang guidang4 = new HomeGuidang();
        guidang4.setName("夜景");
        guidang4.setNum(5);
        guidang4.setPic(Constant.BASEURL + "testpreview/xiaozhu/IMAGE/img_20171004_184408.jpg");
        datas.add(guidang4);

        HomeGuidang guidang5 = new HomeGuidang();
        guidang5.setName("自然");
        guidang5.setNum(2);
        guidang5.setPic(Constant.BASEURL + "testpreview/xiaozhu/IMAGE/5450ae2fdef8a.jpg");
        datas.add(guidang5);

        adapter = new HomeFragmentAdapter(datas, mContext);
        gv.setAdapter(adapter);
        initPieChart();//初始化饼状图
        initRadarChart();//初始化雷达图
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

    @OnClick({R.id.ll5, R.id.ll6, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll_changyong1})
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

            case R.id.ll_changyong1:
                startActivity(new Intent(mContext, FenleiActivity.class));
                break;
        }
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

    private void initRadarChart() {
        mTf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");
        radarChart.setDescription("");
        radarChart.setWebLineWidth(1f);//设置基线的宽度 默认是1.5
        radarChart.setWebLineWidthInner(1f);//设置环线的宽度 默认是1.5
        radarChart.setDrawWeb(true);//是否画网格线
        radarChart.setSkipWebLineCount(0);//间隔几条画一次线默认为0全部画出来
        radarChart.setWebAlpha(120);//设置网格线的透明度 默认为150
        radarChart.setWebColor(UIUtils.getColor(R.color.system_blue));//设置基线的颜色
        radarChart.setWebColorInner(UIUtils.getColor(R.color.system_blue));//设置环线的颜色

        Legend l = radarChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setXEntrySpace(3f);
        l.setYEntrySpace(3f);
        l.setXOffset(15);
        l.setTextColor(UIUtils.getColor(R.color.system_black));
        l.setTextSize(10);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(8);
        l.setTypeface(mTf);

        //X坐标
        XAxis xAxis = radarChart.getXAxis();
        List<String> xTitles = new ArrayList<>();
        xTitles.add("查看");
        xTitles.add("下载");
        xTitles.add("分享");
        xTitles.add("编辑");
        xTitles.add("上传");
        xAxis.setValues(xTitles);
        xAxis.setTextSize(10);
        xAxis.setTextColor(UIUtils.getColor(R.color.system_blue1));
        xAxis.setXOffset(10);
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setYOffset(10);

        //Y坐标
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setDrawLabels(false);

        //每个数据的Y轴坐标
        List<Entry> radarEntries = new ArrayList<>();
        radarEntries.add(new Entry(90, 0));
        radarEntries.add(new Entry(62, 1));
        radarEntries.add(new Entry(88, 2));
        radarEntries.add(new Entry(42, 3));
        radarEntries.add(new Entry(80, 4));

        List<Entry> radarEntries2 = new ArrayList<>();
        radarEntries2.add(new Entry(80, 0));
        radarEntries2.add(new Entry(25, 1));
        radarEntries2.add(new Entry(30, 2));
        radarEntries2.add(new Entry(65, 3));
        radarEntries2.add(new Entry(55, 4));

        List<Entry> radarEntries3 = new ArrayList<>();
        radarEntries3.add(new Entry(40, 0));
        radarEntries3.add(new Entry(80, 1));
        radarEntries3.add(new Entry(20, 2));
        radarEntries3.add(new Entry(0, 3));
        radarEntries3.add(new Entry(0, 4));

        List<Entry> radarEntries4 = new ArrayList<>();
        radarEntries4.add(new Entry(38, 0));
        radarEntries4.add(new Entry(60, 1));
        radarEntries4.add(new Entry(23, 2));
        radarEntries4.add(new Entry(0, 3));
        radarEntries4.add(new Entry(70, 4));

        List<Entry> radarEntries5 = new ArrayList<>();
        radarEntries5.add(new Entry(30, 0));
        radarEntries5.add(new Entry(18, 1));
        radarEntries5.add(new Entry(88, 2));
        radarEntries5.add(new Entry(0, 3));
        radarEntries5.add(new Entry(48, 4));

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "文档");
        radarDataSet.setLineWidth(1);
        radarDataSet.setValueTextSize(12);
        radarDataSet.setColor(Color.parseColor("#46aaff"));
        radarDataSet.setDrawFilled(true);
        radarDataSet.setFillColor(Color.parseColor("#46aaff"));

        RadarDataSet radarDataSet2 = new RadarDataSet(radarEntries2, "图片");
        radarDataSet2.setLineWidth(1);
        radarDataSet2.setValueTextSize(12);
        radarDataSet2.setColor(Color.parseColor("#fff45c"));
        radarDataSet2.setDrawFilled(true);
        radarDataSet2.setFillColor(Color.parseColor("#fff45c"));

        RadarDataSet radarDataSet3 = new RadarDataSet(radarEntries3, "视频");
        radarDataSet3.setLineWidth(1);
        radarDataSet3.setValueTextSize(12);
        radarDataSet3.setColor(Color.parseColor("#d497ff"));
        radarDataSet3.setDrawFilled(true);
        radarDataSet3.setFillColor(Color.parseColor("#d497ff"));

        RadarDataSet radarDataSet4 = new RadarDataSet(radarEntries4, "音频");
        radarDataSet4.setLineWidth(1);
        radarDataSet4.setValueTextSize(12);
        radarDataSet4.setColor(Color.parseColor("#61d77b"));
        radarDataSet4.setDrawFilled(true);
        radarDataSet4.setFillColor(Color.parseColor("#61d77b"));

        RadarDataSet radarDataSet5 = new RadarDataSet(radarEntries5, "其他");
        radarDataSet5.setLineWidth(1);
        radarDataSet5.setValueTextSize(12);
        radarDataSet5.setColor(Color.parseColor("#efb63f"));
        radarDataSet5.setDrawFilled(true);
        radarDataSet5.setFillColor(Color.parseColor("#efb63f"));

        List<RadarDataSet> dataSets = new ArrayList<>();
        dataSets.add(radarDataSet);
        dataSets.add(radarDataSet2);
        dataSets.add(radarDataSet3);
        dataSets.add(radarDataSet4);
        dataSets.add(radarDataSet5);
        RadarData radarData = new RadarData(xTitles, dataSets);
        radarData.setDrawValues(false);
        radarChart.setData(radarData);
        radarChart.animateY(2000, Easing.EasingOption.Linear);
    }

}
