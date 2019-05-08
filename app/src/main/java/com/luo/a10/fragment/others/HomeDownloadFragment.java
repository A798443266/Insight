package com.luo.a10.fragment.others;

import android.widget.ListView;

import com.luo.a10.R;
import com.luo.a10.adapter.DocAdapter;
import com.luo.a10.bean.DocAndFloder;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;

import java.util.List;

import butterknife.BindView;

//首页下载的fragment
public class HomeDownloadFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private DocAdapter adapter;
    private List<DocAndFloder> datas;

    @Override
    protected void initView() {
        datas = Constant.getDocs();
        adapter = new DocAdapter(datas, mContext);
        lv.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_see;
    }
}
