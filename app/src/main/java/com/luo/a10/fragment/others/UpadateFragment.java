package com.luo.a10.fragment.others;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.luo.a10.R;
import com.luo.a10.adapter.UpdateAdapter;
import com.luo.a10.eventBusObject.UpLoadEvent;
import com.luo.a10.fileselect.bean.FileItem;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.service.UpLoadService;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 最近上传图片的fragment
 */
@SuppressLint("ValidFragment")
public class UpadateFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_pause)
    TextView tvPause;

    private List<FileItem> files;
    private UpdateAdapter adapter;
    String folderId;
    String tag;

    @SuppressLint("ValidFragment")
    public UpadateFragment(List<FileItem> files, String folderId, String tag) {
        if (files == null) {
            this.files = new ArrayList<>();
        } else {
            this.files = files;
        }
        if (TextUtils.isEmpty(tag)) {
            this.tag = "";
        } else {
            this.tag = tag;
        }
        if (TextUtils.isEmpty(folderId)) {
            folderId = "0";
        } else {
            this.folderId = folderId;
        }
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }


    @Override
    public void initData() {
        adapter = new UpdateAdapter(files, mContext);

        lv.setAdapter(adapter);

        Log.e("TAG", "F:" + tag);

        Intent intent = new Intent(mContext, UpLoadService.class);
        Bundle bundle = new Bundle();
        if (files.size() > 0)
            bundle.putSerializable("files", (Serializable) files);
        if (!TextUtils.isEmpty(folderId))
            intent.putExtra("folderId", folderId);
        if (!TextUtils.isEmpty(tag))
            intent.putExtra("tag", tag);//传递标签
        intent.putExtras(bundle);
        mContext.startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getmessage(UpLoadEvent u) {
        if (u.getType().equals("上传")) {
            adapter.setProgress(0, u.getProgress());
        } else if (u.getType().equals("成功")) {
            adapter.setFlag(1);
//            tvNum.setText(0);
//            tvPause.setVisibility(View.GONE);
        } else {
            adapter.setFlag(2);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update;
    }

}
