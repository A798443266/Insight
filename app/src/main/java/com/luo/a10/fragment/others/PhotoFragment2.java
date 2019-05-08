package com.luo.a10.fragment.others;

import android.widget.ListView;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.adapter.PhotoTypeAdapter;
import com.luo.a10.bean.ClassificationItem;
import com.luo.a10.bean.InnerItem;
import com.luo.a10.fragment.BaseFragment;
import com.luo.a10.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 图片智能分类的fragment
 */
public class PhotoFragment2 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private List<ClassificationItem> datas;
    private PhotoTypeAdapter adapter;


    @Override
    protected void initView() {
        datas = new ArrayList<>();
        String typeNames1[] = new String[]{"杭州市", "上海市", "宁波市"};
        String typeNames2[] = new String[]{"夜景", "河流/湖泊", "动物", "食物", "建筑", "车", "花/叶"};
        String typeNames3[] = new String[]{"男人", "女人", "孩童"};

        String pics1[] = new String[]
                {Constant.BASE + "img40.jpg",
                        Constant.BASE + "img41.jpg",
                        Constant.BASE + "img20.jpg"};

        String pics2[] = new String[]
                {Constant.BASE + "img38.jpg",
                        Constant.BASE + "img13.jpg",
                        Constant.BASE + "img9.jpg",
                        Constant.BASE + "img33.jpg",
                        Constant.BASE + "img22.jpg",
                        Constant.BASE + "img10.jpg",
                        Constant.BASE + "img4.jpg",
                };

        String pics3[] = new String[]
                {Constant.BASE + "img8.jpg",
                        Constant.BASE + "img5.jpg",
                        Constant.BASE + "img14.jpg"};

        for (int i = 0; i < 3; i++) {
            ClassificationItem item = new ClassificationItem();
            if (i == 0) {
                item.setItemName("地点");
                item.setTypenames(typeNames1);
                item.setPics(pics1);
            } else if (i == 1) {
                item.setItemName("事物");
                item.setTypenames(typeNames2);
                item.setPics(pics2);
            } else {
                item.setItemName("人物");
                item.setTypenames(typeNames3);
                item.setPics(pics3);
            }

            datas.add(item);
        }

        adapter = new PhotoTypeAdapter(datas, mContext);
        lv.setAdapter(adapter);

    }

    @Override
    public void initData() {
        adapter.setOnGvItemClickListener(new PhotoTypeAdapter.OnGvItemClickListener() {
            @Override
            public void onClick(int position, int gvPosition) {
                String name = datas.get(position).getTypenames()[gvPosition];
                Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo1;
    }


}
