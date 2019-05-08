package com.luo.a10.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luo.a10.IPlayMusicService;
import com.luo.a10.R;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.camera.CameraActivity;
import com.luo.a10.eventBusObject.CancelEvent;
import com.luo.a10.eventBusObject.CancelMainMusicEvent;
import com.luo.a10.eventBusObject.CloseMusicServiceEvent;
import com.luo.a10.eventBusObject.DocsEvent;
import com.luo.a10.eventBusObject.MoreWindowEvent;
import com.luo.a10.eventBusObject.MusicPlayEvent;
import com.luo.a10.eventBusObject.MusicStateEvent;
import com.luo.a10.eventBusObject.OpenMenuEvent;
import com.luo.a10.eventBusObject.RefleshFolder;
import com.luo.a10.eventBusObject.SelectAllEvent;
import com.luo.a10.fileselect.LocalFileActivity;
import com.luo.a10.fileselect.bean.FileItem;
import com.luo.a10.fragment.CollectFragment;
import com.luo.a10.fragment.FolderFragment;
import com.luo.a10.fragment.HomeFragment;
import com.luo.a10.fragment.MeFragment;
import com.luo.a10.model.Model;
import com.luo.a10.service.UpLoadService;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.view.CommomDialog;
import com.luo.a10.view.MoreWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends FragmentActivity {
    @BindView(R.id.rl_boss)
    RelativeLayout rlBoss;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.iv_main_home)
    ImageView ivMainHome;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R.id.ll_main_home)
    LinearLayout llMainHome;
    @BindView(R.id.iv_main_search)
    ImageView ivMainSearch;
    @BindView(R.id.tv_main_search)
    TextView tvMainSearch;
    @BindView(R.id.ll_main_search)
    LinearLayout llMainSearch;
    @BindView(R.id.iv_main_update)
    ImageView ivMainUpdate;
    @BindView(R.id.tv_main_update)
    TextView tvMainUpdate;
    @BindView(R.id.ll_main_update)
    LinearLayout llMainUpdate;
    @BindView(R.id.iv_main_collect)
    ImageView ivMainCollect;
    @BindView(R.id.tv_main_collect)
    TextView tvMainCollect;
    @BindView(R.id.ll_main_collect)
    LinearLayout llMainCollect;
    @BindView(R.id.iv_main_me)
    ImageView ivMainMe;
    @BindView(R.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R.id.ll_main_me)
    LinearLayout llMainMe;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.ll_select_head)
    RelativeLayout llSelectHead;
    @BindView(R.id.id_num)
    TextView idNum;
    @BindView(R.id.rl_music)
    RelativeLayout rlMusic;
    @BindView(R.id.tv_play_state)
    TextView tvPlayState;
    @BindView(R.id.tv_songname)
    TextView tvSongname;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;

    private EasyPopup mCirclePop;
    private HomeFragment homeFragment;
    private FolderFragment folderFragment;
    private CollectFragment collectFragment;
    private MeFragment meFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        //默认选中首页
        setSelect(0);
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_search, R.id.ll_main_collect, R.id.ll_main_me})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home: //首页
                setSelect(0);
                break;
            case R.id.ll_main_search: //搜索
                setSelect(1);
//                StatusBarUtils.setColor(this,UIUtils.getColor(R.color.system_blue),100);
                break;
            case R.id.ll_main_collect://工具
                setSelect(2);
                break;
            case R.id.ll_main_me: //我的
                setSelect(3);
                break;
        }
    }

    public void setSelect(int select) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏fragment
        hideFragment();
        //重置颜色
        reSetResourse();
        switch (select) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_main, homeFragment);
                }
                ivMainHome.setBackgroundResource(R.drawable.nav_home1);
                tvMainHome.setTextColor(UIUtils.getColor(R.color.system_blue));
                transaction.show(homeFragment);
                break;
            case 1:
                if (folderFragment == null) {
                    folderFragment = new FolderFragment();
                    transaction.add(R.id.fl_main, folderFragment);
                }
                ivMainSearch.setBackgroundResource(R.drawable.nav_search1);
                tvMainSearch.setTextColor(UIUtils.getColor(R.color.system_blue));
                transaction.show(folderFragment);
                break;
            case 2:
                if (collectFragment == null) {
                    collectFragment = new CollectFragment();
                    transaction.add(R.id.fl_main, collectFragment);
                }
                ivMainCollect.setBackgroundResource(R.drawable.nav_tool1);
                tvMainCollect.setTextColor(UIUtils.getColor(R.color.system_blue));
                transaction.show(collectFragment);
                break;
            case 3:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                ivMainMe.setBackgroundResource(R.drawable.nav_me1);
                tvMainMe.setTextColor(UIUtils.getColor(R.color.system_blue));
                transaction.show(meFragment);
                break;
        }

        transaction.commit();
    }

    //重置颜色图标
    private void reSetResourse() {
        ivMainHome.setBackgroundResource(R.drawable.nav_home);
        ivMainSearch.setBackgroundResource(R.drawable.nav_search);
        ivMainCollect.setBackgroundResource(R.drawable.nav_tool);
        ivMainMe.setBackgroundResource(R.drawable.nav_me);

        tvMainHome.setTextColor(UIUtils.getColor(R.color.Nag_text_color));
        tvMainSearch.setTextColor(UIUtils.getColor(R.color.Nag_text_color));
        tvMainCollect.setTextColor(UIUtils.getColor(R.color.Nag_text_color));
        tvMainMe.setTextColor(UIUtils.getColor(R.color.Nag_text_color));
    }

    //隐藏fragment
    private void hideFragment() {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (folderFragment != null) {
            transaction.hide(folderFragment);
        }
        if (collectFragment != null) {
            transaction.hide(collectFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
    }

    @OnClick({R.id.ll_main_update, R.id.iv_main_update})
    public void onViewClicked() {
        MoreWindow mMoreWindow = new MoreWindow(this);
        mMoreWindow.init();
        mMoreWindow.showMoreWindow(llMainUpdate, 100);
    }


    private List<FolderAndDoc> docs;//接收传递过来的文件，可对其操作

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(OpenMenuEvent openMenuEvent) {
        docs = new ArrayList<>();
        docs.add(openMenuEvent.getDoc());
        //显示消息
        llSelect.setVisibility(View.VISIBLE);
        llSelectHead.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_select_in);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.translate_select_head_in);
        llSelect.startAnimation(animation);
        llSelectHead.startAnimation(animation1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDocsEvent(DocsEvent docsEvent) {
        docs = new ArrayList<>();
        docs = docsEvent.getDocs();
    }


    //收到来自MoreWindow发来的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoreWindow(MoreWindowEvent m) {
        switch (m.getType()) {
            case 1:
               showMore();
                break;
            case 2:
                View view = View.inflate(this, R.layout.pop_new_folder, null);
                final EditText et_name = view.findViewById(R.id.et_name);
                Button btn_cancel = view.findViewById(R.id.btn_cancel);
                Button btn_ok = view.findViewById(R.id.btn_ok);
                mCirclePop = new EasyPopup(this)
                        .setContentView(view)
                        .setWidth(UIUtils.dp2px(300))
                        .setHeight(UIUtils.dp2px(300))
                        .setBackgroundDimEnable(true)
                        .setAnimationStyle(R.style.popphoto_animation)
                        .setDimValue(0.4f)
                        .setFocusAndOutsideEnable(true)
                        .apply();
                mCirclePop.showAtAnchorView(rlBoss, YGravity.CENTER, XGravity.CENTER, 0, 0);
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
                            Toast.makeText(MainActivity.this, "文件夹名称不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        OkHttpUtils.post().url(Constant.NewFolder)
                                .addParams("pid", SpUtils.getInt(MainActivity.this, Constant.USERID) + "")
                                .addParams("name", et_name.getText().toString().trim())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast.makeText(MainActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        EventBus.getDefault().post(new RefleshFolder());//发送消息到folderFragment中刷新
                                        Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                                        setSelect(1);//转到文件
                                        mCirclePop.dismiss();
                                    }
                                });
                    }
                });
                break;

            case 3:
                Intent intent = new Intent(this, LocalFileActivity.class);
                intent.putExtra("tab_select", 2);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 4:
                Intent intent1 = new Intent(this, LocalFileActivity.class);
                intent1.putExtra("tab_select", 3);
                startActivityForResult(intent1, 1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 5:
                Intent intent2 = new Intent(this, LocalFileActivity.class);
                intent2.putExtra("tab_select", 0);
                startActivityForResult(intent2, 1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 6:
                Intent intent3 = new Intent(this, LocalFileActivity.class);
                intent3.putExtra("tab_select", 1);
                startActivityForResult(intent3, 1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 7:
                Intent intent4 = new Intent(this, LocalFileActivity.class);
                intent4.putExtra("tab_select", 4);
                startActivityForResult(intent4, 1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 8:
                startActivity(new Intent(this, YuYinRecordActivity.class));
                break;
            default:
                break;
        }
    }

//Activity回显结果=======================================================================================================
    //收到选择的文件列表，去上传
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == 200) {
                ArrayList<FileItem> resultFileList = data.getParcelableArrayListExtra("file");//文件列表
                if (resultFileList == null && resultFileList.size() == 0) {
                    Log.e("TAG", "没有选择文件");
                    return;
                }
                //得到要上传的文件夹
                String folderId = data.getStringExtra("folderId");
                String tag = data.getStringExtra("tag");

                Log.e("TAG", "M:" + tag);
                Log.e("TAG", "M:" + folderId);

//                Intent intent = new Intent(this, UpLoadService.class);
                Intent intent = new Intent(this, TransmissionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", resultFileList);
                if (!TextUtils.isEmpty(folderId))
                    intent.putExtra("folderId", folderId);
                if (!TextUtils.isEmpty(tag))
                    intent.putExtra("tag", tag);//传递标签
                intent.putExtras(bundle);
                startActivity(intent);
//                startService(intent);

            }
        } else if (requestCode == CameraActivity.REQUEST_CODE && resultCode == CameraActivity.RESULT_CODE) {
            //获取文件路径，显示图片
            String path = "";
            String path1 = "";
            int type;
            if (data != null) {
                type = data.getIntExtra("type", 0);
                if (type == 0) {
                    path = data.getStringExtra("path");//正面
                    path1 = data.getStringExtra("path1");//反面
                }else{
                    path = data.getStringExtra("path");//正面
                }
            }

            View view = View.inflate(this, R.layout.card_pop, null);
            ImageView iv1 = view.findViewById(R.id.iv1);
            ImageView iv2 = view.findViewById(R.id.iv2);
            Button btn_save = view.findViewById(R.id.btn_save);
            Button btn_cancel = view.findViewById(R.id.btn_cancel);

            if(!TextUtils.isEmpty(path)){
                FileInputStream fis = null;
                try {
                    iv1.setVisibility(View.VISIBLE);
                    fis = new FileInputStream(path);
                    Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                    bitmap = UIUtils.rotateImage(bitmap,-90);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes=baos.toByteArray();
                    Glide.with(this).load(bytes).error(R.drawable.city1).into(iv1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(!TextUtils.isEmpty(path1)){
                FileInputStream fis = null;
                try {
                    iv2.setVisibility(View.VISIBLE);
                    fis = new FileInputStream(path1);
                    Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                    bitmap = UIUtils.rotateImage(bitmap,-90);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes=baos.toByteArray();
                    Glide.with(this).load(bytes).error(R.drawable.city1).into(iv2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            mCirclePop = new EasyPopup(this)
                    .setContentView(view)
                    .setWidth(UIUtils.dp2px(320))
                    .setHeight(UIUtils.dp2px(520))
                    .setBackgroundDimEnable(true)
                    .setAnimationStyle(R.style.popyuyin_animation)
                    .setDimValue(0.4f)
                    .setFocusAndOutsideEnable(true)
                    .apply();
            mCirclePop.showAtAnchorView(rlBoss, YGravity.CENTER, XGravity.CENTER, 0, 0);

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
                    Toast.makeText(MainActivity.this,"保存成功，可在我的证件照中查看",Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

//=====================================================================================

    public void showMore() {//收到打开从下打开更多pop的请求
        View view = View.inflate(this, R.layout.pop_more, null);
        LinearLayout ll5 = view.findViewById(R.id.ll5);
        mCirclePop = new EasyPopup(this)
                .setContentView(view)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(UIUtils.dp2px(260))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.popyuyin_animation)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(rlBoss, YGravity.ALIGN_BOTTOM, XGravity.CENTER, 0, 0);
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCirclePop.dismiss();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0x12);
                    return;
                }
                CameraActivity.openCertificateCamera(MainActivity.this, 0);
            }
        });

    }


    //----播放音乐--------------------------------------------------------------------------------------------------------------
    //接收播放音乐服务传来的消息

    private IPlayMusicService Iservice;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showViewDataAndChek(MusicPlayEvent m) {
        if (m.getType() == 0) {
            rlMusic.setVisibility(View.VISIBLE);
        } else if (m.getType() == 1) {
            rlMusic.setVisibility(View.GONE);
            tvPlayState.setText("正在播放：");
        } else {
            Toast.makeText(this, "播放结束", Toast.LENGTH_SHORT).show();
            tvPlayState.setText("正在播放：");
            rlMusic.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getIservice(IPlayMusicService Iservice) {
        this.Iservice = Iservice;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getIservice(CancelMainMusicEvent c) {
        tvPlayState.setText("正在播放：");
        rlMusic.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void show(MusicStateEvent m) {
        if (m.getType() == 0) {
            tvPlayState.setText("正在播放：");
        } else {
            tvPlayState.setText("已暂停：");
        }
    }

    @OnClick({R.id.iv_close, R.id.rl_music})
    public void onViewCl(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                EventBus.getDefault().post(new CloseMusicServiceEvent());
                if (Iservice != null) {
                    try {
                        Iservice.stop();//停止播放
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                rlMusic.setVisibility(View.GONE);
                tvPlayState.setText("正在播放：");
                break;

            case R.id.rl_music:
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("isNew", false);
                //正在播放传过去1，已暂停传过去0
                intent.putExtra("playState", tvPlayState.getText().toString().equals("正在播放：") ? 1 : 0);
                startActivity(intent);

        }
    }

//--------------------------------------------------------------------------------------------------------------------------------

    @OnClick({R.id.ll_select1, R.id.ll_select2, R.id.ll_select3, R.id.ll_select4, R.id.ll_select5, R.id.ll_select6, R.id.ll_select7, R.id.ll_select8, R.id.ll_select9, R.id.ll_select10, R.id.tv_cancel, R.id.tv_select_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select1:
                break;
            case R.id.ll_select2:
                break;
            case R.id.ll_select3:
                break;
            case R.id.ll_select4: //删除传递过来的文件
                if (docs.size() == 0) {
                    Toast.makeText(this, "请选择文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                new CommomDialog(this, R.style.dialog, "确定要删除这些文件吗？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
//                            Log.e("TAG", docs.get(0).getId() + "");
//                            Model.getInstance().getDbDao().deleteDocs(docs);
                            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            cancelSelect();
//                            EventBus.getDefault().post(new RefleshFolder());
                            docs = null;
                        }
                    }
                }).setTitle("提示").show();

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
                break;
            case R.id.tv_cancel:
                cancelSelect();
                break;
            case R.id.tv_select_all:
                selectAll();
                break;
        }
    }

    //全选
    private void selectAll() {
        if (tvSelectAll.getText().toString().equals("全选")) {
            EventBus.getDefault().post(new SelectAllEvent(true));//发送全选通知
            tvSelectAll.setText("全不选");
        } else {
            EventBus.getDefault().post(new SelectAllEvent(false));//发送全不选通知
            tvSelectAll.setText("全选");
        }

    }

    private void cancelSelect() {
        EventBus.getDefault().post(new CancelEvent());//向homeseeFragment中发事件，重置boolean值
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_select_out);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.translate_select_head_out);
        llSelect.startAnimation(animation);
        llSelectHead.startAnimation(animation1);
        llSelect.setVisibility(View.GONE);
        llSelectHead.setVisibility(View.GONE);
        tvSelectAll.setText("全选");
        docs = null;
    }


    //以下是Fragment中onTouchEvent的实现（fragment没有ontouch方法）
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }


    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //连按两次退出
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_BACK) {
                flag = true;
            }
        }
    };

    private boolean flag = true;
    private static final int WHAT_BACK = 0;

    //连续点击两次返回键退出
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            flag = false;
            handler.sendEmptyMessageDelayed(WHAT_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
