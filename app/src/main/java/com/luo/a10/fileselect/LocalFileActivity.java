package com.luo.a10.fileselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.luo.a10.R;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.fileselect.adapter.FolderAdapter;
import com.luo.a10.fileselect.adapter.VpAdapter;
import com.luo.a10.fileselect.bean.FileItem;
import com.luo.a10.fileselect.fragment.AudioFragment;
import com.luo.a10.fileselect.fragment.Base1Fragment;
import com.luo.a10.fileselect.fragment.DocumentFragment;
import com.luo.a10.fileselect.fragment.OtherFragment;
import com.luo.a10.fileselect.fragment.PictureFragment;
import com.luo.a10.fileselect.fragment.VideoFragment;
import com.luo.a10.model.Model;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.SpUtils;
import com.luo.a10.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class LocalFileActivity extends AppCompatActivity {
    private final static int RESULT_CODE = 200;
    //默认最大选择文件数量
    private final static int FILE_SELECT_MAX_COUNT = 10;
    @BindView(R.id.activity_all_file)
    RelativeLayout activityAllFile;
    private int defaultCount = FILE_SELECT_MAX_COUNT;
    private int tab_select = 0;
    private FolderAndDoc currentFolder;//接收传递过来的文件夹，表示上传到此文件夹，不用再选择了
    List<String> tags = new ArrayList<>();//自定义标签列表

    TextView tvBack;
    LinearLayout llBack;
    TextView tvTitle;
    TextView tvFoldername;
    SlidingTabLayout tl;
    TextView tvEmpty;
    FrameLayout flContainer;
    TextView tvSize;
    Button btSend;
    ViewPager vp;
    LinearLayout llSelectFolder;
    RelativeLayout rlMore;
    ListView lv;
    LinearLayout llDocs;

    List<Base1Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"文档", "音频", "图片", "视频", "其他"};
    private VpAdapter vpAdapter;
    private FolderAdapter adapter;
    private List<FolderAndDoc> datas = new ArrayList<>();//文件夹
    private boolean isSelect = false;//判断下是否选择文件夹列表已经打开
    private ArrayList<FileItem> selectedList;
    private HashSet<String> pathSet = new HashSet<>();
    private String titleName = "选择文件";
    private EasyPopup mCirclePop;

    private FolderAndDoc folder;//用来记录选择上传的文件夹
    private String json;//我的文件夹信息的json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_file);
        ButterKnife.bind(this);

        initView();
        initListener();
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tv_back);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvFoldername = (TextView) findViewById(R.id.tv_foldername);
        tl = (SlidingTabLayout) findViewById(R.id.tl);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        tvSize = (TextView) findViewById(R.id.tv_size);
        btSend = (Button) findViewById(R.id.bt_send);
        llSelectFolder = findViewById(R.id.ll_select_folder);
        rlMore = findViewById(R.id.rl_more);
        lv = findViewById(R.id.lv);
        vp = (ViewPager) findViewById(R.id.vp);
        llDocs = findViewById(R.id.ll_docs);

        Intent intent = getIntent();
        tab_select = intent.getIntExtra("tab_select", 0);
        selectedList = selectedList == null ? new ArrayList<FileItem>() : selectedList;

        folder = (FolderAndDoc) intent.getSerializableExtra("Folder");//获取传递过来的要上传的文件夹
        if (folder != null) {
            tvFoldername.setText(folder.getName());
        }

        if (selectedList.size() > 0) {
            tvTitle.setText("已选" + selectedList.size() + "个");
        } else {
            tvTitle.setText(titleName);
        }
        fragmentList.add(new DocumentFragment());
        fragmentList.add(new AudioFragment());
        fragmentList.add(new PictureFragment());
        fragmentList.add(new VideoFragment());
        fragmentList.add(new OtherFragment());

        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragmentList, titles);
        vp.setAdapter(vpAdapter);
        tl.setViewPager(vp);
        tl.setCurrentTab(tab_select);


        json = SpUtils.getString(this, Constant.save_myfolders);
        if (!TextUtils.isEmpty(json)) {
            List<FolderAndDoc> folders = JsonUtils.parseFolderAndDoc(json);
            if(folders != null && folders.size() > 0){
                for(int i = 0;i < folders.size();i++){
                    if(folders.get(i).getCategory() == -1){
                        datas.add(folders.get(i));
                    }
                }
            }

            showFolders();
        } else {
            //如果第一次上来没有缓存过文件夹就上传文件的话，要去服务请求文件夹信息
            OkHttpUtils.get().url(Constant.GETFOLDER_FILE)
                    .addParams("id",SpUtils.getInt(LocalFileActivity.this,Constant.USERID)+"")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("TAG", "失败");
                            showFolders();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            List<FolderAndDoc> folders = JsonUtils.parseFolderAndDoc(response);
                            if(folders != null && folders.size() > 0){
                                for(int i = 0;i < folders.size();i++){
                                    if(folders.get(i).getCategory() == -1){
                                        datas.add(folders.get(i));
                                    }
                                }
                            }

                            showFolders();
                        }
                    });
        }

    }

    //显示文件夹信息
    private void showFolders() {
        if (datas != null && datas.size() > 0) {
            adapter = new FolderAdapter(this, datas);
            lv.setAdapter(adapter);
        } else {
            //没有文件夹
            Toast.makeText(this, "您还没有创建任何文件夹", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        if (isSelect) {
            isSelect = false;
            tvTitle.setText("选择文件夹");
            llSelectFolder.setClickable(true);
            llDocs.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        } else {
            finish();
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }


    private void initListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelect) {
                    isSelect = false;
                    if (folder == null)
                        tvTitle.setText("选择文件夹");
                    else
                        tvTitle.setText(folder.getName());

                    llSelectFolder.setClickable(true);
                    llDocs.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                } else {
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedList != null && selectedList.size() > 0) {
                    Intent data = new Intent();
                    data.putParcelableArrayListExtra("file", selectedList);
                    if (folder == null) {
                        //如果没有选择文件夹就默认跟目录
                        data.putExtra("folderId", SpUtils.getInt(LocalFileActivity.this,Constant.USERID)+"");
                    } else {
                        data.putExtra("folderId", folder.getId() + "");
                    }
                    String tag = "";//标签

                    for(int i = 0;i < tags.size();i++){
                        if(!TextUtils.isEmpty(tags.get(i))){
                            tag += tags.get(i)+" ";
                        }
                    }

                    if(!TextUtils.isEmpty(tag))
                        data.putExtra("tag",tag);//传递标签
                    data.putExtra("max", defaultCount);

                    setResult(RESULT_CODE, data);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    Toast.makeText(LocalFileActivity.this, "请选择文件", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //选择文件夹
        llSelectFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = true;
                if (selectedList != null && selectedList.size() > 0) {
                    tvTitle.setText("已选" + selectedList.size() + "个");
                } else {
                    tvTitle.setText(titleName);
                }

                llDocs.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                llSelectFolder.setClickable(false);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null) {
                    adapter.setSelect(position);
                    folder = datas.get(position);
                    tvFoldername.setText(folder.getName());

                }
            }
        });

        rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(LocalFileActivity.this, R.layout.pop_selectfile, null);
                RelativeLayout rl2 = view.findViewById(R.id.rl2);
                mCirclePop = new EasyPopup(LocalFileActivity.this)
                        .setContentView(view)
                        .setWidth(UIUtils.dp2px(130))
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

                //添加标签
                rl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCirclePop.dismiss();
                        View view = View.inflate(LocalFileActivity.this, R.layout.pop_selectfile_biaoqian, null);
                        final LinearLayout lls[] = new LinearLayout[3];
                        final TextView tvs[] = new TextView[3];
                        ImageView ivs[] = new ImageView[3];
                        final EditText et = view.findViewById(R.id.et);
                        RelativeLayout rl_gou = view.findViewById(R.id.rl_gou);
                        final LinearLayout ll_all = view.findViewById(R.id.ll_all);
                        Button btn_ok = view.findViewById(R.id.btn_ok);
                        Button btn_cancel = view.findViewById(R.id.btn_cancel);

                        setBiaoQian(view, lls, tvs, ivs, et, rl_gou, ll_all, btn_ok, btn_cancel);

                        mCirclePop = new EasyPopup(LocalFileActivity.this)
                                .setContentView(view)
                                .setWidth(UIUtils.dp2px(320))
                                .setHeight(UIUtils.dp2px(230))
                                .setBackgroundDimEnable(true)
                                .setAnimationStyle(R.style.popfolder_animation)
                                .setDimValue(0.2f)
                                .setFocusAndOutsideEnable(true)
                                .apply();
                        mCirclePop.showAtAnchorView(activityAllFile, YGravity.CENTER, XGravity.CENTER, 0, 0);
                        mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                biaoqianNum = 0;
                            }
                        });
                    }
                });
            }
        });
    }

    private int biaoqianNum = 0;//当前显示的标签数量

    //设置标签
    private void setBiaoQian(View view, final LinearLayout[] lls, final TextView[] tvs, ImageView[] ivs, final EditText et,
                             RelativeLayout rl_gou, final LinearLayout ll_all, Button btn_ok, Button btn_cancel) {
        lls[0] = view.findViewById(R.id.ll1);
        lls[1] = view.findViewById(R.id.ll2);
        lls[2] = view.findViewById(R.id.ll3);

        tvs[0] = view.findViewById(R.id.tv1);
        tvs[1] = view.findViewById(R.id.tv2);
        tvs[2] = view.findViewById(R.id.tv3);

        ivs[0] = view.findViewById(R.id.iv_close1);
        ivs[1] = view.findViewById(R.id.iv_close2);
        ivs[2] = view.findViewById(R.id.iv_close3);

        rl_gou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                et.setText("");
                switch (biaoqianNum) {
                    case 0:
                        ll_all.setVisibility(View.VISIBLE);
                        tvs[0].setText(content);
                        lls[0].setVisibility(View.VISIBLE);
                        tags.add(content);
                        biaoqianNum++;
                        break;
                    case 1:
                        tvs[1].setText(content);
                        lls[1].setVisibility(View.VISIBLE);
                        tags.add(content);
                        biaoqianNum++;
                        break;
                    case 2:
                        tvs[2].setText(content);
                        lls[2].setVisibility(View.VISIBLE);
                        tags.add(content);
                        biaoqianNum++;
                        break;
                    case 3:
                        Toast.makeText(LocalFileActivity.this, "标签数量以达上限", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                tags = new ArrayList<>();
            }
        });
    }

    public ArrayList<FileItem> getSelectedList() {
        return selectedList;
    }

    public HashSet<String> getPathSet() {
        return pathSet;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public String getTitleName() {
        return titleName;
    }

    public TextView getTvSize() {
        return tvSize;
    }

    public Button getBtSend() {
        return btSend;
    }

    public int getDefaultCount() {
        return defaultCount;
    }

}
