package com.luo.a10.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.barlibrary.ImmersionBar;
import com.luo.a10.R;
import com.luo.a10.bean.change.FolderAndDoc;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BigPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photoview)
    PhotoView photoview;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private FolderAndDoc pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo);
        ButterKnife.bind(this);

        pic = (FolderAndDoc) getIntent().getSerializableExtra("path");
        tvTitle.setText(pic.getName());


        Glide.with(this).load(pic.getLink()).error(R.drawable.city1).into(photoview);
    }

    @OnClick({R.id.rl_back,R.id.rl_more})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_more :
                startActivity(new Intent(this,PhotoDetailsActivity.class));
                break;
        }

    }

}
