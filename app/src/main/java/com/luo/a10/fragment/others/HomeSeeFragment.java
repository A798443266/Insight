package com.luo.a10.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.luo.a10.R;
import com.luo.a10.activity.BigPhotoActivity;
import com.luo.a10.activity.DocSeeActivity;
import com.luo.a10.activity.MusicActivity;
import com.luo.a10.activity.OpenFolderActivity;
import com.luo.a10.activity.PlayVideoActivity;
import com.luo.a10.adapter.DocAdapter;
import com.luo.a10.adapter.HomeSeeFragmentAdapter;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.eventBusObject.CancelEvent;
import com.luo.a10.eventBusObject.OpenMenuEvent;
import com.luo.a10.eventBusObject.SelectAllEvent;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

//首页查看的fragment
public class HomeSeeFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private HomeSeeFragmentAdapter adapter;
    private List<FolderAndDoc> datas;
    private boolean isSelect = false;//判断下面的文件操作列表是否已经打开了

    @Override
    protected void initView() {
        datas = Constant.getHomeSeeFragmentDocs();
        adapter = new HomeSeeFragmentAdapter(mContext, datas);
        lv.setAdapter(adapter);

        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        super.initData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelect) { //如果文件操作菜单打开，点击item就相当于点击cb
                    CheckBox cb = view.findViewById(R.id.cb);
                    cb.setChecked(!cb.isChecked());
                    adapter.setCbState(position, cb.isChecked());//保存状态
                } else {
                    FolderAndDoc folderAndDoc = datas.get(position);

                    if (folderAndDoc.getCategory() == -1) {//是文件夹

                    } else if (folderAndDoc.getCategory() == 2) {
                        Intent intent = new Intent(mContext, BigPhotoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("path", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else if (folderAndDoc.getCategory() == 4) {
                        Intent intent = new Intent(mContext, PlayVideoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("video", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else if (folderAndDoc.getCategory() == 1) {
                        Intent intent = new Intent(mContext, DocSeeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("doc", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (folderAndDoc.getCategory() == 3) {
                        Intent intent = new Intent(mContext, MusicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("music", folderAndDoc);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });

        adapter.setOnSelectDotClick(new DocAdapter.OnSelectDotClick() {
            @Override
            public void onDotClick(int position, CheckBox cb) {
                if (!isSelect) {
                    EventBus.getDefault().post(new OpenMenuEvent());
                    isSelect = true;
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(CancelEvent s) { //收到取消的通知
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_see;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
