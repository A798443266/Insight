package com.luo.a10.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luo.a10.R;
import com.luo.a10.adapter.change.FolderAdapter;
import com.luo.a10.bean.FolderRecord;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.fileselect.LocalFileActivity;
import com.luo.a10.fileselect.bean.FileItem;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.CommomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 打开文件夹
 */
public class OpenFolderActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;
    @BindView(R.id.ll_boss)
    RelativeLayout llBoss;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.ll_select_head)
    RelativeLayout llSelectHead;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private List<FolderAndDoc> datas;
    private FolderAndDoc currentFolder;//用来记录当前打开的是哪个文件夹
    private FolderAdapter adapter;
    private EasyPopup mCirclePop;
    private List<FolderRecord> folderRecords = new ArrayList<>();//用来记录当前打开到文件的哪一级
    private boolean isSelect = false;//判断下面的文件操作列表是否已经打开了
    private List<FolderAndDoc> addSelects = new ArrayList<>();//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_folder);
        ButterKnife.bind(this);

        currentFolder = (FolderAndDoc) getIntent().getSerializableExtra("folder");
        tvTitle.setText(currentFolder.getName());
        //联网请求里面的文件
        getFilesFromNet(currentFolder,true);
    }


    private void getFilesFromNet(FolderAndDoc folderAndDoc, final boolean isNew) {
        llLoad.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(Constant.GETFOLDER_FILE)
                .addParams("id", folderAndDoc.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        llLoad.setVisibility(View.GONE);
                        datas = null;
                        show(isNew);
                        Toast.makeText(OpenFolderActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        llLoad.setVisibility(View.GONE);
                        datas = JsonUtils.parseFolderAndDoc(response);
                        show(isNew);
                    }
                });
    }

    private void show(boolean isNew) {
        FolderRecord folderRecord = new FolderRecord(currentFolder, datas == null ? new ArrayList<FolderAndDoc>() : datas);
        if (isNew)
            folderRecords.add(folderRecord);//添加到记录中
        //设置监听
        setListener();
    }

    private void setListener() {
        if (datas != null && datas.size() > 0) {
            tvNo.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adapter = new FolderAdapter(datas, this);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (isSelect) {//如果文件操作菜单打开，点击item就相当于点击cb
                        CheckBox cb = view.findViewById(R.id.cb);
                        cb.setChecked(!cb.isChecked());
                        adapter.setCbState(position, cb.isChecked());//保存状态
                        Map<Integer, Boolean> cbState = adapter.getCbState();
                        addSelects = new ArrayList<>();
                        for (int i = 0; i < cbState.size(); i++) {
                            if (cbState.get(i)) {
                                addSelects.add(datas.get(i));
                            }

                        }
                        Log.e("TAG", addSelects.size() + "");

                    } else {
                        FolderAndDoc folderAndDoc = datas.get(position);
                        if (folderAndDoc.getCategory() == -1) {//文件夹
                            currentFolder = datas.get(position);
                            tvTitle.setText(currentFolder.getName());
                            getFilesFromNet(currentFolder,true);

                        } else if (folderAndDoc.getCategory() == 2) {//图片
                            Intent intent = new Intent(OpenFolderActivity.this, BigPhotoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("path", folderAndDoc);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (folderAndDoc.getCategory() == 3) {//音频
                            Intent intent = new Intent(OpenFolderActivity.this, MusicActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("music", folderAndDoc);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (folderAndDoc.getCategory() == 4) {//视频
                            Intent intent = new Intent(OpenFolderActivity.this, PlayVideoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("video", folderAndDoc);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {//文档
                            Intent intent = new Intent(OpenFolderActivity.this, DocSeeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("doc", folderAndDoc);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                }
            });

            adapter.setOnSelectDotClick(new FolderAdapter.OnSelectDotClick() {
                @Override
                public void onDotClick(int position, CheckBox cb) {
                    if (isSelect) {
                        Map<Integer, Boolean> cbState = adapter.getCbState();
                        addSelects = new ArrayList<>();
                        for (int i = 0; i < cbState.size(); i++) {
                            if (cbState.get(i))
                                addSelects.add(datas.get(i));
                        }
                    } else {
                        startMenu();
                        addSelects.add(datas.get(position));
                    }
                }
            });

        } else {
            lv.setVisibility(View.GONE);
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.ll_back, R.id.iv_search, R.id.iv_chuanshu, R.id.iv_paixv, R.id.ll_search})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                back();
                break;
            case R.id.iv_search:
                break;
            case R.id.iv_chuanshu:
                break;
            case R.id.iv_paixv:
                showPop();
                break;
            case R.id.ll_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
    }

    private void showPop() {
        View view1 = View.inflate(this, R.layout.pop_folder_paixv1, null);
        RelativeLayout rl1 = view1.findViewById(R.id.rl1);
        RelativeLayout rl2 = view1.findViewById(R.id.rl2);
        RelativeLayout rl3 = view1.findViewById(R.id.rl3);
        RelativeLayout rl4 = view1.findViewById(R.id.rl4);
        RelativeLayout rl5 = view1.findViewById(R.id.rl5);
        mCirclePop = new EasyPopup(this)
                .setContentView(view1)
                .setWidth(UIUtils.dp2px(130))
                .setHeight(UIUtils.dp2px(225))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.popfolder_animation)
                .setDimValue(0.2f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(rlBar, YGravity.BELOW, XGravity.RIGHT, 0, UIUtils.dp2px(3));
        mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mCirclePop = null;
            }
        });

        //新建文件夹
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                View view = View.inflate(OpenFolderActivity.this, R.layout.pop_new_folder, null);
                final EditText et_name = view.findViewById(R.id.et_name);
                Button btn_cancel = view.findViewById(R.id.btn_cancel);
                Button btn_ok = view.findViewById(R.id.btn_ok);
                mCirclePop = new EasyPopup(OpenFolderActivity.this)
                        .setContentView(view)
                        .setWidth(UIUtils.dp2px(300))
                        .setHeight(UIUtils.dp2px(300))
                        .setBackgroundDimEnable(true)
                        .setAnimationStyle(R.style.popphoto_animation)
                        .setDimValue(0.4f)
                        .setFocusAndOutsideEnable(true)
                        .apply();
                mCirclePop.showAtAnchorView(llBoss, YGravity.CENTER, XGravity.CENTER, 0, 0);
                mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mCirclePop = null;
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCirclePop.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString().trim();
                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(OpenFolderActivity.this, "文件夹名称不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        OkHttpUtils.post().url(Constant.NewFolder)
                                .addParams("pid", currentFolder.getId() + "")
                                .addParams("name", et_name.getText().toString().trim())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(OpenFolderActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Toast.makeText(OpenFolderActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                                        //刷新
                                        if (mCirclePop.isShowing())
                                            mCirclePop.dismiss();
                                        getFilesFromNet(currentFolder,false);
                                    }
                                });
                    }
                });
            }
        });

        //选择上传文件
        rl5.setOnClickListener(new View.OnClickListener() { //上传文件到当前打开的文件夹
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                Intent intent = new Intent(OpenFolderActivity.this, LocalFileActivity.class);
                intent.putExtra("Folder", currentFolder);//表示已经有要上传的文件夹了
                startActivityForResult(intent, 1);
            }
        });

        //选择文件
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                startMenu();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 200) {
                ArrayList<FileItem> resultFileList = data.getParcelableArrayListExtra("file");
                if (resultFileList == null && resultFileList.size() == 0) {
                    Log.e("TAG", "没有选择文件");
                    return;
                }


                //得到要上传的文件夹
                String folderId = data.getStringExtra("folderId");
                String tag = data.getStringExtra("tag");

                Log.e("TAG", "M:" + tag);
                Log.e("TAG", "M:" + folderId);

                Intent intent = new Intent(this, TransmissionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", resultFileList);
                if (!TextUtils.isEmpty(folderId))
                    intent.putExtra("folderId", folderId);
                if (!TextUtils.isEmpty(tag))
                    intent.putExtra("tag", tag);//传递标签
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFilesFromNet(currentFolder,false);
    }

    //返回上一级
    private void back() {
        if (folderRecords == null || folderRecords.size() <= 1) {
            finish();
        } else {
            folderRecords.remove(folderRecords.size() - 1);//删除最外层的文件夹
            tvTitle.setText(folderRecords.get(folderRecords.size() - 1).getFolderAndDoc().getName());
            currentFolder = folderRecords.get(folderRecords.size() - 1).getFolderAndDoc();//设置当前文件夹为上一个
            datas = folderRecords.get(folderRecords.size() - 1).getDocs();
            setListener();//重新加载并且设置监听
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelect) {
            cancelSelect();
        } else {
            if (folderRecords == null || folderRecords.size() <= 1) {
                super.onBackPressed();
            } else {
                folderRecords.remove(folderRecords.size() - 1);
                tvTitle.setText(folderRecords.get(folderRecords.size() - 1).getFolderAndDoc().getName());
                currentFolder = folderRecords.get(folderRecords.size() - 1).getFolderAndDoc();//设置当前文件夹为上一个
                datas = folderRecords.get(folderRecords.size() - 1).getDocs();
                setListener();//重新加载并且设置监听
            }
        }

    }


    @OnClick({R.id.ll_select4, R.id.tv_cancel, R.id.tv_select_all})
    public void onViewClic(View view) {
        switch (view.getId()) {
            case R.id.ll_select4://删除文件
                if (addSelects.size() == 0 || addSelects == null) {
                    Toast.makeText(this, "请选择文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                new CommomDialog(this, R.style.dialog, "确定要删除这些文件吗？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            Toast.makeText(OpenFolderActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            cancelSelect();
                            addSelects = null;
                        }
                    }
                }).setTitle("提示").show();
                break;

            case R.id.tv_cancel: //取消
                cancelSelect();
                break;
            case R.id.tv_select_all://全选
                if (tvSelectAll.getText().toString().equals("全选")) {
                    adapter.setSelectAll();
                    Map<Integer, Boolean> cbState = adapter.getCbState();
                    for (int i = 0; i < cbState.size(); i++) {
                        if (cbState.get(i))
                            addSelects.add(datas.get(i));
                    }
                    tvSelectAll.setText("全不选");
                } else {
                    adapter.setCancelAll();
                    addSelects.clear();
                    tvSelectAll.setText("全选");
                }
                break;
        }
    }

    //开启菜单选项
    private void startMenu() {
        addSelects = new ArrayList<>();
        llSelect.setVisibility(View.VISIBLE);
        llSelectHead.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(OpenFolderActivity.this, R.anim.translate_select_in);
        Animation animation1 = AnimationUtils.loadAnimation(OpenFolderActivity.this, R.anim.translate_select_head_in);
        llSelect.startAnimation(animation);
        llSelectHead.startAnimation(animation1);
        isSelect = true;
    }

    //取消菜单选项
    private void cancelSelect() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_select_out);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.translate_select_head_out);
        llSelect.startAnimation(animation);
        llSelectHead.startAnimation(animation1);
        llSelect.setVisibility(View.GONE);
        llSelectHead.setVisibility(View.GONE);
        isSelect = false;
        adapter.setCancelAll();//取消所有选择
        tvSelectAll.setText("全选");
    }

    @OnClick({R.id.ll_select1, R.id.ll_select2, R.id.ll_select3, R.id.ll_select4, R.id.ll_select5, R.id.ll_select6, R.id.ll_select7, R.id.ll_select8, R.id.ll_select9, R.id.ll_select10})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select1:
                break;
            case R.id.ll_select2:
                break;
            case R.id.ll_select3:
                break;
            case R.id.ll_select4:
                break;
            case R.id.ll_select5:
                break;
            case R.id.ll_select6:
                break;
            case R.id.ll_select7:
                break;
            case R.id.ll_select8:
                break;
            case R.id.ll_select9:
                break;
            case R.id.ll_select10:
                cancelSelect();
                showMore();
                break;
        }
    }

    public void showMore() {//收到打开从下打开更多pop的请求
        View view = View.inflate(this, R.layout.pop_more, null);
        LinearLayout ll5 = view.findViewById(R.id.ll5);
        mCirclePop = new EasyPopup(this)
                .setContentView(view)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(UIUtils.dp2px(280))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.popyuyin_animation)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(llBoss, YGravity.ALIGN_BOTTOM, XGravity.CENTER, 0, 0);

        //证件合并
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                if (addSelects == null || addSelects.size() == 0) {
                    Toast.makeText(OpenFolderActivity.this, "请先选择要合并的图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (FolderAndDoc doc : addSelects) {
                    if (doc.getCategory() != 2) {
                        Toast.makeText(OpenFolderActivity.this, "只能选择图片文件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                View view = View.inflate(OpenFolderActivity.this, R.layout.card_pop, null);
                ImageView iv1 = view.findViewById(R.id.iv1);
                ImageView iv2 = view.findViewById(R.id.iv2);
                Button btn_save = view.findViewById(R.id.btn_save);
                Button btn_cancel = view.findViewById(R.id.btn_cancel);

                if (addSelects.size() == 1) {//只选了一张图片
                    iv1.setVisibility(View.VISIBLE);
                    Glide.with(OpenFolderActivity.this).load(addSelects.get(0).getLink()).error(R.drawable.city1).into(iv1);
                } else {//大于两张图片只合并两张
                    Log.e("TAG", addSelects.get(0).getLink());
                    Log.e("TAG", addSelects.get(1).getLink());
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    Glide.with(OpenFolderActivity.this).load(addSelects.get(0).getLink()).error(R.drawable.city1).into(iv1);
                    Glide.with(OpenFolderActivity.this).load(addSelects.get(1).getLink()).error(R.drawable.city1).into(iv2);
                }

                mCirclePop = new EasyPopup(OpenFolderActivity.this)
                        .setContentView(view)
                        .setWidth(UIUtils.dp2px(320))
                        .setHeight(UIUtils.dp2px(520))
                        .setBackgroundDimEnable(true)
                        .setAnimationStyle(R.style.popyuyin_animation)
                        .setDimValue(0.4f)
                        .setFocusAndOutsideEnable(true)
                        .apply();
                mCirclePop.showAtAnchorView(llBoss, YGravity.CENTER, XGravity.CENTER, 0, 0);

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
                        Toast.makeText(OpenFolderActivity.this, "保存成功，可在我的证件照中查看", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
