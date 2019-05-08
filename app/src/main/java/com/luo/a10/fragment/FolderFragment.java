package com.luo.a10.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.activity.DocSeeActivity;
import com.luo.a10.activity.MainActivity;
import com.luo.a10.activity.MusicActivity;
import com.luo.a10.activity.OpenFolderActivity;
import com.luo.a10.activity.PlayVideoActivity;
import com.luo.a10.activity.SearchActivity;
import com.luo.a10.activity.TransmissionActivity;
import com.luo.a10.adapter.DocAdapter;
import com.luo.a10.adapter.change.FolderAdapter;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.eventBusObject.CancelEvent;
import com.luo.a10.eventBusObject.DocsEvent;
import com.luo.a10.eventBusObject.OpenMenuEvent;
import com.luo.a10.eventBusObject.RefleshFolder;
import com.luo.a10.eventBusObject.SelectAllEvent;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class FolderFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;
    @BindView(R.id.pop_fenlei)
    LinearLayout popFenlei;
    @BindView(R.id.iv_jiantou)
    ImageView ivJiantou;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private boolean b = false;//判断分类选项是否展开
    private FolderAdapter adapter;
    private List<FolderAndDoc> docs;
    private boolean isSelect = false;//判断下面的文件操作列表是否已经打开了
    private EasyPopup mCirclePop;

    private boolean a = false;
    private String json;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
//        ((MainActivity) this.getActivity()).registerMyOnTouchListener(mTouchListener);
    }


    @Override
    public void initData() {
        json = SpUtils.getString(mContext, Constant.save_myfolders);
        if (!TextUtils.isEmpty(json)) {
            docs = JsonUtils.parseFolderAndDoc(json);
        }

        llLoad.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(Constant.GETFOLDER_FILE)
                .addParams("id", SpUtils.getInt(mContext, Constant.USERID) + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "失败");
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.save_myfolders, response);//存入最新数据
                        docs = JsonUtils.parseFolderAndDoc(response);
                        show();
                        Log.e("TAG", docs.size() + "");
                    }
                });
    }

    private void show() {
        llLoad.setVisibility(View.GONE);
        if (docs != null && docs.size() > 0) {
            adapter = new FolderAdapter(docs, mContext);
            lv.setAdapter(adapter);
            setListener();
        } else {
            Toast.makeText(mContext, "您还没有过文件上传或者创建过文件夹", Toast.LENGTH_SHORT).show();
        }

    }

    private void setListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelect) {
                    CheckBox cb = view.findViewById(R.id.cb);
                    cb.setChecked(!cb.isChecked());
                    adapter.setCbState(position, cb.isChecked());//保存状态
                    Map<Integer, Boolean> cbState = adapter.getCbState();
                    List<FolderAndDoc> docs1 = new ArrayList<>();
                    for (int i = 0; i < cbState.size(); i++) {
                        if (cbState.get(i))
                            docs1.add(docs.get(i));
                    }
//                    EventBus.getDefault().post(new DocsEvent(docs1));
                } else {
                    FolderAndDoc folderAndDoc = docs.get(position);
                    if (folderAndDoc.getCategory() == -1) {//文件夹
                        Intent intent = new Intent(mContext, OpenFolderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("folder", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else if (folderAndDoc.getCategory() == 2) {//图片
                        Intent intent = new Intent(mContext, BigPhotoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("path", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (folderAndDoc.getCategory() == 3) {//音频
                        Intent intent = new Intent(mContext, MusicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("music", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (folderAndDoc.getCategory() == 4) {//视频
                        Intent intent = new Intent(mContext, PlayVideoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("video", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {//文档
                        Intent intent = new Intent(mContext, DocSeeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("doc", docs.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });


        if (adapter != null) {
            adapter.setOnSelectDotClick(new FolderAdapter.OnSelectDotClick() {
                @Override
                public void onDotClick(int position, CheckBox cb) {
                    if (!isSelect) {
                        EventBus.getDefault().post(new OpenMenuEvent(docs.get(position)));
                        isSelect = true;
                    } else {
                        cb.setChecked(cb.isChecked());
                        adapter.setCbState(position, cb.isChecked());//保存状态
                        Map<Integer, Boolean> cbState = adapter.getCbState();
                        List<FolderAndDoc> docs1 = new ArrayList<>();
                        for (int i = 0; i < cbState.size(); i++) {
                            if (cbState.get(i))
                                docs1.add(docs.get(i));
                        }
                        EventBus.getDefault().post(new DocsEvent(docs1));

                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(CancelEvent s) {
        isSelect = false;
        adapter.setCancelAll();//取消所有选择
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(SelectAllEvent s) { //收到全选通知
        if (s.isSelectAll())
            adapter.setSelectAll();
        else
            adapter.setCancelAll();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent2(RefleshFolder r) { //文件夹刷新的消息
       initData();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_folder;
    }


    @OnClick({R.id.ll_search, R.id.iv_paixv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(mContext, SearchActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.iv_paixv:
                View view1 = View.inflate(mContext, R.layout.pop_folder_paixv, null);
                RelativeLayout rl1 = view1.findViewById(R.id.rl1);
                RelativeLayout rl2 = view1.findViewById(R.id.rl2);
                RelativeLayout rl3 = view1.findViewById(R.id.rl3);
                RelativeLayout rl4 = view1.findViewById(R.id.rl4);
                mCirclePop = new EasyPopup(mContext)
                        .setContentView(view1)
                        .setWidth(UIUtils.dp2px(130))
                        .setHeight(UIUtils.dp2px(180))
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
                break;
        }

    }


    @OnClick(R.id.ll_fenlei)
    public void onViewClicked() {
        if (!b) {
            Animation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            Animation animation1 = AnimationUtils.loadAnimation(mContext, R.anim.pop_fenlei_in);
            animation.setDuration(400);
            animation.setFillAfter(true);
            ivJiantou.startAnimation(animation);
            popFenlei.setVisibility(View.VISIBLE);
            popFenlei.startAnimation(animation1);
            b = true;
        } else {
            Animation animation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            Animation animation1 = AnimationUtils.loadAnimation(mContext, R.anim.pop_fenlei_out);
            animation.setDuration(400);
            animation.setFillAfter(true);
            ivJiantou.startAnimation(animation);
            popFenlei.startAnimation(animation1);
            popFenlei.setVisibility(View.GONE);
            b = false;
        }
    }


    private MainActivity.MyOnTouchListener mTouchListener = new MainActivity.MyOnTouchListener() {
        @Override
        public boolean onTouch(MotionEvent ev) {
            int Y = (int) ev.getY();
            if (b && Y > UIUtils.dp2px(238)) {
                //如果分类打开了且 点击的位置不在弹出来的分类中
                Animation animation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                Animation animation1 = AnimationUtils.loadAnimation(mContext, R.anim.pop_fenlei_out);
                animation.setDuration(400);
                animation.setFillAfter(true);
                ivJiantou.startAnimation(animation);
                popFenlei.startAnimation(animation1);
                popFenlei.setVisibility(View.GONE);
                b = false;
            }
            return true;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) this.getActivity()).unregisterMyOnTouchListener(mTouchListener);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_chuanshu)
    public void onViewClicked1() {
        startActivity(new Intent(mContext, TransmissionActivity.class));
    }
}
