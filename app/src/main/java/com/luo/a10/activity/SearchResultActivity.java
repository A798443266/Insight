package com.luo.a10.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.luo.a10.R;
import com.luo.a10.adapter.fenlei.FenleiDocAdapter;
import com.luo.a10.adapter.fenlei.MusicAdapter;
import com.luo.a10.adapter.fenlei.PhotoAdapter;
import com.luo.a10.adapter.fenlei.PhotoLvAdapter;
import com.luo.a10.adapter.fenlei.QitaAdapter;
import com.luo.a10.adapter.fenlei.VideoAdapter;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.MyGridView;
import com.luo.a10.view.MyListView;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;
    @BindView(R.id.toorbar)
    Toolbar toorbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.piechart)
    PieChart piechart;
    @BindView(R.id.gv_photo)
    MyGridView gvPhoto;
    @BindView(R.id.gv_video)
    MyGridView gvVideo;
    @BindView(R.id.gv_doc)
    MyGridView gvDoc;
    @BindView(R.id.gv_music)
    MyGridView gvMusic;
    @BindView(R.id.gv_qita)
    MyGridView gvQita;
    @BindView(R.id.lv_photo)
    MyListView lvPhoto;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_pic_num)
    TextView tvPicNum;
    @BindView(R.id.ll_pic)
    LinearLayout llPic;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.tv_video_num)
    TextView tvVideoNum;
    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    @BindView(R.id.tv_doc_num)
    TextView tvDocNum;
    @BindView(R.id.ll_doc)
    LinearLayout llDoc;
    @BindView(R.id.tv_music_num)
    TextView tvMusicNum;
    @BindView(R.id.ll_music)
    LinearLayout llMusic;
    @BindView(R.id.tv_qita_num)
    TextView tvQitaNum;
    @BindView(R.id.ll_qita)
    LinearLayout llQita;
    @BindView(R.id.boss)
    CoordinatorLayout boss;

    private Typeface mTf;//声明字体库
    private int isSelectMode = -1;//操作模式  0：新建归档  1：合并照片
    private PhotoAdapter adapter1;
    private VideoAdapter adapter2;
    private FenleiDocAdapter adapter3;
    private MusicAdapter adapter4;
    private QitaAdapter adapter5;
    private PhotoLvAdapter adapter6;
    private EasyPopup mCirclePop;

    private List<FolderAndDoc> addSelects = new ArrayList<>();

    private int selectNum = 0;//记录文件选择的数量
    String result;
    List<FolderAndDoc> files;
    List<FolderAndDoc> imgs = new ArrayList<>();
    List<FolderAndDoc> videos = new ArrayList<>();
    List<FolderAndDoc> musics = new ArrayList<>();
    List<FolderAndDoc> docs = new ArrayList<>();
    List<FolderAndDoc> qitas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        //自定义沉浸式方法
        setStatusBarTransparent(this);
        changeToolBar();

        init();
        //弹出常用工具
        boss.post(new Runnable() {
            @Override
            public void run() {
                popTools(); //PopupWindow必须在某个事件中显示或者是开启一个新线程去调用，不能直接在onCreate方法中显示一个Popupwindow
            }
        });
    }


    private void init() {
        result = getIntent().getStringExtra("result");
        if (!TextUtils.isEmpty(result)) {
            files = JsonUtils.parseSearch(result);
        }

        if (files == null || files.size() == 0) {
            ll.setVisibility(View.GONE);
            llQita.setVisibility(View.GONE);
            llPic.setVisibility(View.GONE);
            llDoc.setVisibility(View.GONE);
            llMusic.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            tvNum.setText("没有找到对应的文件");
            return;
        }

        tvNum.setText("已找到" + files.size() + "个文件");
        for (int i = 0; i < files.size(); i++) {
            FolderAndDoc folderAndDoc = files.get(i);
            if (folderAndDoc.getCategory() == 2) {//图片
                imgs.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 4) {//视频
                videos.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 1) {//文档
                docs.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 3) {//音频
                musics.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 5) {
                qitas.add(folderAndDoc);
            }
        }

        initData();
        initChart();
    }


    private void initData() {

        if (imgs != null && imgs.size() > 0)
            Glide.with(this).load(imgs.get(0).getLink()).error(R.drawable.city1).into(iv);

        if (imgs.size() > 0) {
            llPic.setVisibility(View.VISIBLE);
            tvPicNum.setText(imgs.size() + "个");
        } else {
            llPic.setVisibility(View.GONE);
        }

        if (videos.size() > 0) {
            llVideo.setVisibility(View.VISIBLE);
            tvVideoNum.setText(videos.size() + "个");
        } else {
            llVideo.setVisibility(View.GONE);
        }
        if (docs.size() > 0) {
            llDoc.setVisibility(View.VISIBLE);
            tvDocNum.setText(docs.size() + "个");
        } else {
            llDoc.setVisibility(View.GONE);
        }

        if (musics.size() > 0) {
            llMusic.setVisibility(View.VISIBLE);
            tvMusicNum.setText(musics.size() + "个");
        } else {
            llMusic.setVisibility(View.GONE);
        }

        if (qitas.size() > 0) {
            llQita.setVisibility(View.VISIBLE);
            tvQitaNum.setText(qitas.size() + "个");
        } else {
            llQita.setVisibility(View.GONE);
        }

        adapter1 = new PhotoAdapter(this, imgs);
        adapter2 = new VideoAdapter(this, videos);
        adapter3 = new FenleiDocAdapter(this, docs);
        adapter4 = new MusicAdapter(this, musics);
        adapter5 = new QitaAdapter(this, qitas);
//        adapter6 = new PhotoLvAdapter(this, lv_pics);

        gvPhoto.setAdapter(adapter1);
        gvVideo.setAdapter(adapter2);
        gvDoc.setAdapter(adapter3);
        gvMusic.setAdapter(adapter4);
        gvQita.setAdapter(adapter5);
        lvPhoto.setAdapter(adapter6);

        setListener();

    }

    private void setListener() {
        gvPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (isSelectMode == -1) {
                    isSelectMode = 0;
                    tvCancel.setVisibility(View.VISIBLE);
                    tvOk.setVisibility(View.VISIBLE);
                    selectNum += adapter1.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                }
                return true;
            }
        });

        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode != -1) {//当前已经开启了选择文件的模式
                    selectNum += adapter1.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, BigPhotoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("path", imgs.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        gvVideo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (isSelectMode == -1) {
                    isSelectMode = 0;
                    tvCancel.setVisibility(View.VISIBLE);
                    tvOk.setVisibility(View.VISIBLE);
                    selectNum += adapter2.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                }
                return true;
            }
        });

        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode != -1) {//当前已经开启了选择文件的模式
                    selectNum += adapter2.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, PlayVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("video", videos.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        gvDoc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (isSelectMode == -1) {
                    isSelectMode = 0;
                    tvCancel.setVisibility(View.VISIBLE);
                    tvOk.setVisibility(View.VISIBLE);
                    selectNum += adapter3.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                }
                return true;
            }
        });

        gvDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode != -1) {//当前已经开启了选择文件的模式
                    selectNum += adapter3.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, DocSeeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("doc", docs.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        gvMusic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (isSelectMode == -1) {
                    isSelectMode = 0;
                    tvCancel.setVisibility(View.VISIBLE);
                    tvOk.setVisibility(View.VISIBLE);
                    selectNum += adapter4.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                }
                return true;
            }
        });

        gvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode != -1) {//当前已经开启了选择文件的模式
                    selectNum += adapter4.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, MusicActivity.class);
                    startActivity(intent);
                }
            }
        });

        gvQita.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (isSelectMode == -1) {
                    isSelectMode = 0;
                    tvCancel.setVisibility(View.VISIBLE);
                    tvOk.setVisibility(View.VISIBLE);
                    selectNum += adapter5.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                }
                return true;
            }
        });

        gvQita.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode != -1) {//当前已经开启了选择文件的模式
                    selectNum += adapter5.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {

                }
            }
        });
    }

    private void popTools() {
        View view = View.inflate(this, R.layout.pop_more1, null);
        LinearLayout ll5 = view.findViewById(R.id.ll5);
        LinearLayout ll3 = view.findViewById(R.id.ll3);
        ImageView iv_xiala = view.findViewById(R.id.iv_xiala);
        mCirclePop = new EasyPopup(this)
                .setContentView(view)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(UIUtils.dp2px(260))
                .setBackgroundDimEnable(false)
                .setAnimationStyle(R.style.popyuyin_animation)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(boss, YGravity.ALIGN_BOTTOM, XGravity.CENTER, 0, 0);
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f; //0.0-1.0
        getWindow().setAttributes(lp);

        //证件合并
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchResultActivity.this, "请选择相应的证件图片", Toast.LENGTH_SHORT).show();
                isSelectMode = 1;
                tvTitle.setText("证件合并");
                tvCancel.setVisibility(View.VISIBLE);
                tvOk.setVisibility(View.VISIBLE);
                mCirclePop.dismiss();

            }
        });

        //新建归档
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectMode = 0;
                tvTitle.setText("新建归档");
                tvCancel.setVisibility(View.VISIBLE);
                tvOk.setVisibility(View.VISIBLE);
                mCirclePop.dismiss();
                Toast.makeText(SearchResultActivity.this, "请选择要归档的文件", Toast.LENGTH_SHORT).show();
            }
        });

        iv_xiala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
            }
        });

        mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {//取消选择文件的模式
        cancelselectMode();
    }

    private void cancelselectMode() {
        tvCancel.setVisibility(View.GONE);
        tvOk.setVisibility(View.GONE);
        tvTitle.setText("搜索结果");
        isSelectMode = -1;
        adapter1.setCancelSelectMode();//取消选择文件的模式
        adapter2.setCancelSelectMode();
        adapter3.setCancelSelectMode();
        adapter4.setCancelSelectMode();
        adapter5.setCancelSelectMode();
        tvNum.setText("已找到" + files.size() + "个文件");
        addSelects.clear();
        selectNum = 0;
    }

    @OnClick({R.id.ll_back, R.id.tv_ok, R.id.iv_more, R.id.rl_shangla})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_ok:
                opration();//根据不同的状态操作不同
                break;

            case R.id.iv_more:
                break;

            case R.id.rl_shangla:
                popTools();
                break;
        }
    }

    private void opration() {
        if (adapter1.getSelectPosition().size() != 0)
            for (int i = 0; i < adapter1.getSelectPosition().size(); i++) {
                int position = adapter1.getSelectPosition().get(i);
                addSelects.add(imgs.get(position));
            }
        if (adapter2.getSelectPosition().size() != 0)
            for (int i = 0; i < adapter2.getSelectPosition().size(); i++) {
                int position = adapter2.getSelectPosition().get(i);
                addSelects.add(videos.get(position));
            }
        if (adapter3.getSelectPosition().size() != 0)
            for (int i = 0; i < adapter3.getSelectPosition().size(); i++) {
                int position = adapter3.getSelectPosition().get(i);
                addSelects.add(docs.get(position));
            }
        if (adapter4.getSelectPosition().size() != 0)
            for (int i = 0; i < adapter4.getSelectPosition().size(); i++) {
                int position = adapter2.getSelectPosition().get(i);
                addSelects.add(musics.get(position));
            }
        if (adapter5.getSelectPosition().size() != 0)
            for (int i = 0; i < adapter5.getSelectPosition().size(); i++) {
                int position = adapter2.getSelectPosition().get(i);
                addSelects.add(qitas.get(position));
            }

        if (addSelects.size() == 0) {
            Toast.makeText(this, "请先选择文件后才能新建分类", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (isSelectMode) {
            case 0://新建归档
                Intent intent = new Intent(this, AddFenleiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) addSelects);
                intent.putExtras(bundle);
                startActivity(intent);
                cancelselectMode();
                break;
            case 1://证件合并
                popZhengjian();
                cancelselectMode();
                break;
            default:
                break;
        }


    }

    private void popZhengjian() {
        if (addSelects == null || addSelects.size() == 0) {
            Toast.makeText(SearchResultActivity.this, "请先选择要合并的图片", Toast.LENGTH_SHORT).show();
            return;
        }

        for (FolderAndDoc doc : addSelects) {
            if (doc.getCategory() != 2) {
                Toast.makeText(SearchResultActivity.this, "只能选择图片文件", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        View view = View.inflate(SearchResultActivity.this, R.layout.card_pop, null);
        ImageView iv1 = view.findViewById(R.id.iv1);
        ImageView iv2 = view.findViewById(R.id.iv2);
        Button btn_save = view.findViewById(R.id.btn_save);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        if (docs.size() == 1) {//只选了一张图片
            iv1.setVisibility(View.VISIBLE);
            Glide.with(SearchResultActivity.this).load(addSelects.get(0).getLink()).error(R.drawable.city1).into(iv1);
        } else {//大于两张图片只合并两张
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);
            Glide.with(SearchResultActivity.this).load(addSelects.get(0).getLink()).error(R.drawable.city1).into(iv1);
            Glide.with(SearchResultActivity.this).load(addSelects.get(1).getLink()).error(R.drawable.city1).into(iv2);
        }

        mCirclePop = new EasyPopup(SearchResultActivity.this)
                .setContentView(view)
                .setWidth(UIUtils.dp2px(320))
                .setHeight(UIUtils.dp2px(520))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.popyuyin_animation)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(boss, YGravity.CENTER, XGravity.CENTER, 0, 0);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                Toast.makeText(SearchResultActivity.this, "保存成功，可在我的证件照中查看", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.iv_paixv1, R.id.iv_paixv2})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_paixv1:
               /* if (gvPhoto.getVisibility() == View.VISIBLE) {
                    gvPhoto.setVisibility(View.GONE);
                    lvPhoto.setVisibility(View.VISIBLE);
                } else {
                    gvPhoto.setVisibility(View.VISIBLE);
                    lvPhoto.setVisibility(View.GONE);
                }*/
                break;
            case R.id.iv_paixv2:
                break;
        }
    }

    private void initChart() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        piechart.setDescription("");
        piechart.setHoleRadius(30f);//最内层的圆的半径
        piechart.setTransparentCircleRadius(40f);//包裹内层圆的半径
        piechart.setCenterText("");
        piechart.setCenterTextTypeface(mTf);
        piechart.setCenterTextSize(14f);
        //是否使用百分比。true:各部分的百分比的和是100%。
        piechart.setUsePercentValues(false);
        piechart.setDrawSliceText(false);

        PieData pieData = generateDataPie();
        pieData.setDrawValues(true);
        pieData.setValueTypeface(mTf);
        pieData.setValueTextSize(8f);
        pieData.setValueTextColor(Color.BLACK);
        // set data
        piechart.setData(pieData);
        //获取右上角的描述结构的对象
        Legend l = piechart.getLegend();
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        l.setYEntrySpace(0f);//相邻的entry在y轴上的间距
        l.setYOffset(0f);//第一个entry距离最顶端的间距
        piechart.animateXY(1500, 1500);
    }

    private PieData generateDataPie() {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        if (docs.size() > 0)
            entries.add(new Entry(docs.size(), 0));
        if (imgs.size() > 0)
            entries.add(new Entry(imgs.size(), 1));
        if (videos.size() > 0)
            entries.add(new Entry(videos.size(), 2));
        if (musics.size() > 0)
            entries.add(new Entry(musics.size(), 3));
        if (qitas.size() > 0)
            entries.add(new Entry(qitas.size(), 4));
        PieDataSet d = new PieDataSet(entries, "");
        // 相邻模块的间距
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }

    private ArrayList<String> getQuarters() {
        ArrayList<String> q = new ArrayList<String>();
        if (docs.size() > 0)
            q.add("文档");
        if (imgs.size() > 0)
            q.add("图片");
        if (videos.size() > 0)
            q.add("视频");
        if (musics.size() > 0)
            q.add("音频");
        if (qitas.size() > 0)
            q.add("其他");
        return q;
    }

    //实现状态栏沉浸式
    public void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void changeToolBar() {
        //滑动改变搜索框的透明度和按钮的变化
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    rlBar.setBackgroundColor(Color.argb(0, 143, 193, 255));
                    if (isSelectMode == -1)
                        tvTitle.setVisibility(View.INVISIBLE);
                    else
                        tvTitle.setVisibility(View.VISIBLE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    rlBar.setBackgroundColor(Color.argb(255, 143, 193, 255));
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    int alpha = (int) (255 - verticalOffset / (float) appBarLayout.getTotalScrollRange() * 255);
                    rlBar.setBackgroundColor(Color.argb(alpha, 143, 193, 255));
                }
            }
        });
    }

}
