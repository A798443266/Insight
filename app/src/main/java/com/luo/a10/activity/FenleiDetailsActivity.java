package com.luo.a10.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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
import com.luo.a10.adapter.fenlei.QitaAdapter;
import com.luo.a10.adapter.fenlei.VideoAdapter;
import com.luo.a10.bean.Guidang;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.CommomDialog;
import com.luo.a10.view.MyGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FenleiDetailsActivity extends AppCompatActivity {

    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;
    @BindView(R.id.toorbar)
    Toolbar toorbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    @BindView(R.id.piechart)
    PieChart piechart;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_fenlei)
    TextView tvFenlei;
    @BindView(R.id.rl_delete)
    RelativeLayout rlDelete;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.tv_pic_num)
    TextView tvPicNum;
    @BindView(R.id.ll_pic)
    LinearLayout llPic;
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

    private Typeface mTf;//声明字体库
    private PhotoAdapter adapter1;
    private VideoAdapter adapter2;
    private FenleiDocAdapter adapter3;
    private MusicAdapter adapter4;
    private QitaAdapter adapter5;

    private String result;
    private Guidang guidang;
    List<FolderAndDoc> files;
    List<FolderAndDoc> imgs = new ArrayList<>();
    List<FolderAndDoc> videos = new ArrayList<>();
    List<FolderAndDoc> musics = new ArrayList<>();
    List<FolderAndDoc> docs = new ArrayList<>();
    List<FolderAndDoc> qitas = new ArrayList<>();

    private boolean isSelectMode;//是否是选择文件的模式
    private int selectNum = 0;//记录文件选择的数量
    private EasyPopup mCirclePop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei_details);
        ButterKnife.bind(this);

        //自定义沉浸式方法
        setStatusBarTransparent(this);
        changeToolBar();

        result = getIntent().getStringExtra("result");
        guidang = (Guidang) getIntent().getSerializableExtra("guidang");
        tvFenlei.setText(guidang.getName());
        tvTitle.setText(guidang.getName());
        init();

    }

    private void init() {
        files = JsonUtils.parseSearch(result);
        if (files == null || files.size() == 0) {
            return;
        }

        tvCount.setText("一共" + files.size() + "个文件");

        for (int i = 0; i < files.size(); i++) {
            FolderAndDoc folderAndDoc = files.get(i);
            if (folderAndDoc.getCategory() == 2) {//图片
                imgs.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 4) {//视频
                videos.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 1) {//文档
                docs.add(folderAndDoc);
            } else if (folderAndDoc.getCategory() == 3) {//音频

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

        gvPhoto.setAdapter(adapter1);
        gvVideo.setAdapter(adapter2);
        gvDoc.setAdapter(adapter3);
        gvMusic.setAdapter(adapter4);
        gvQita.setAdapter(adapter5);

        setListener();
    }

    private void setListener() {
        gvPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (!isSelectMode) {
                    isSelectMode = true;
                    tvCancel.setVisibility(View.VISIBLE);
                    selectNum += adapter1.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                    starDeleteView();
                }
                return true;
            }
        });

        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode) {//当前已经开启了选择文件的模式
                    selectNum += adapter1.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(FenleiDetailsActivity.this, BigPhotoActivity.class);
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
                if (!isSelectMode) {
                    isSelectMode = true;
                    tvCancel.setVisibility(View.VISIBLE);
                    selectNum += adapter2.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                    starDeleteView();
                }
                return true;
            }
        });

        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode) {//当前已经开启了选择文件的模式
                    selectNum += adapter2.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(FenleiDetailsActivity.this, PlayVideoActivity.class);
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
                if (!isSelectMode) {
                    isSelectMode = true;
                    tvCancel.setVisibility(View.VISIBLE);
                    selectNum += adapter3.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                    starDeleteView();
                }
                return true;
            }
        });

        gvDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode) {//当前已经开启了选择文件的模式
                    selectNum += adapter3.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {
                    Intent intent = new Intent(FenleiDetailsActivity.this, DocSeeActivity.class);
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
                if (!isSelectMode) {
                    isSelectMode = true;
                    tvCancel.setVisibility(View.VISIBLE);
                    selectNum += adapter4.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                    starDeleteView();
                }
                return true;
            }
        });

        gvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode) {//当前已经开启了选择文件的模式
                    selectNum += adapter4.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                    starDeleteView();
                } else {
                    Intent intent = new Intent(FenleiDetailsActivity.this, MusicActivity.class);
                    startActivity(intent);
                }
            }
        });

        gvQita.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启选择文件的模式
                if (!isSelectMode) {
                    isSelectMode = true;
                    tvCancel.setVisibility(View.VISIBLE);
                    selectNum += adapter5.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                }
                return true;
            }
        });

        gvQita.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelectMode) {//当前已经开启了选择文件的模式
                    selectNum += adapter5.setSelect(position);
                    tvNum.setText("已选择" + selectNum + "项");
                } else {

                }
            }
        });
    }

    @OnClick({R.id.tv_cancel, R.id.rl_delete, R.id.rl_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                cancelSelectModel();
                break;
            case R.id.rl_delete:
                new CommomDialog(this, R.style.dialog, "确定要从该分类中删除这些文件吗？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            cancelSelectModel();
                            Toast.makeText(FenleiDetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                        } else {
                            cancelSelectModel();
                        }
                    }
                }).setTitle("提示").show();
                break;

            case R.id.rl_more:
                //更多
                View view1 = View.inflate(this, R.layout.pop_fenlei_details, null);
                mCirclePop = new EasyPopup(this)
                        .setContentView(view1)
                        .setWidth(UIUtils.dp2px(100))
                        .setHeight(UIUtils.dp2px(135))
                        .setBackgroundDimEnable(true)
                        .setAnimationStyle(R.style.popfolder_animation)
                        .setDimValue(0.2f)
                        .setFocusAndOutsideEnable(true)
                        .apply();
                mCirclePop.showAtAnchorView(rlMore, YGravity.BELOW, XGravity.RIGHT, 0, UIUtils.dp2px(3));
                mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mCirclePop = null;
                    }
                });
                break;
        }
    }

    private void cancelSelectModel() {
        tvCancel.setVisibility(View.GONE);
        isSelectMode = false;
        adapter1.setCancelSelectMode();//取消选择文件的模式
        adapter2.setCancelSelectMode();
        adapter3.setCancelSelectMode();
        adapter4.setCancelSelectMode();
        adapter5.setCancelSelectMode();
        selectNum = 0;
        tvCount.setText("一共" + files.size() + "个文件");
        Animation animation = AnimationUtils.loadAnimation(FenleiDetailsActivity.this, R.anim.translate_select_out);
        rlDelete.startAnimation(animation);
        rlDelete.setVisibility(View.GONE);
    }

    //开启删除菜单
    private void starDeleteView() {
        rlDelete.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(FenleiDetailsActivity.this, R.anim.translate_select_in);
        rlDelete.startAnimation(animation);
    }


    //初始化饼状图
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


    private void changeToolBar() {
        //滑动改变搜索框的透明度和按钮的变化
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    rlBar.setBackgroundColor(Color.argb(0, 143, 193, 255));
                    tvTitle.setVisibility(View.INVISIBLE);
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

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }


}
