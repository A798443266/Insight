package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.luo.a10.R;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

public class PlayVideoActivity extends AppCompatActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_duration)
    TextView tvDuration;

    private FolderAndDoc video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.black)
                .statusBarAlpha(1.0f)
                .init();

        video = (FolderAndDoc) getIntent().getSerializableExtra("video");
        if (video == null) {
            video = new FolderAndDoc();
        }

        tvSize.setText(Formatter.formatFileSize(this, video.getSize()));
        tvTime.setText(UIUtils.StringTime(Long.parseLong(video.getTime())));
        tvDuration.setText(UIUtils.stringForTime((int) video.getSize()));

        videoplayer.setUp(video.getLink(), JZVideoPlayerStandard.SCROLL_INDICATOR_TOP, "    " + video.getName());
        Glide.with(this).load(video.getCover()).error(R.drawable.city1).into(videoplayer.thumbImageView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoplayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (videoplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
    }
}
