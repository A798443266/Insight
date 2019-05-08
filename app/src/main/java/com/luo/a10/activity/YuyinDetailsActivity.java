package com.luo.a10.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.luo.a10.R;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.yuyinrecord.RecordingItem;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YuyinDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.voicLine)
    VoiceLineView voicLine;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    private RecordingItem recordingItem;
    private MediaPlayer mediaPlayer;
    private boolean isPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuyin_details);
        ButterKnife.bind(this);

        recordingItem = (RecordingItem) getIntent().getSerializableExtra("data");
        initData();
    }

    private void initData() {
        tvTime.setText(UIUtils.stringForTime((int) recordingItem.getTime()));
        tvTitle.setText(recordingItem.getName());
        tvCurrent.setText("00:00");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    removeMessages(1);
                    voicLine.setVolume(getDB());
                    handler.sendEmptyMessageDelayed(1, 30);
                    break;
                case 2:
                    removeMessages(2);
                    tvCurrent.setText(UIUtils.stringForTime(mediaPlayer.getCurrentPosition()));
                    handler.sendEmptyMessageDelayed(2, 1000);
                    break;
                case 3:
                    loading.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    tvText.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    public int getDB() {
        int db = (int) (Math.random() * 60);
        return db;
    }

    @OnClick({R.id.rl_more, R.id.iv_play, R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_more:
                break;
            case R.id.iv_play:
                play();

                break;

            case R.id.rl_back:
                finish();
                break;
        }
    }

    private void play() {
        if (!isPlay && mediaPlayer == null) {//第一次播放
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(recordingItem.getFilePath());
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        isPlay = true;
                        handler.sendEmptyMessage(1);
                        handler.sendEmptyMessageDelayed(2,1000);
                        voicLine.run();
                    }
                });
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                ivPlay.setBackgroundResource(R.drawable.icon_pause_white1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!isPlay && mediaPlayer != null) {//暂停到播放
            mediaPlayer.start();
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessageDelayed(2,1000);
            isPlay = true;
            ivPlay.setBackgroundResource(R.drawable.icon_pause_white1);
        } else if (isPlay && mediaPlayer != null) {//播放到暂停
            mediaPlayer.pause();
            handler.removeCallbacksAndMessages(null);
            isPlay = false;
            ivPlay.setBackgroundResource(R.drawable.icon_play_white1);
        }

    }

    @OnClick(R.id.ll_change)
    public void onViewClicked() {
        loading.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(3, 1500);
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mediaPlayer.release();
            isPlay = false;
            mediaPlayer = null;
            handler.removeCallbacksAndMessages(null);
            ivPlay.setBackgroundResource(R.drawable.icon_play_white1);
            tvCurrent.setText("00:00");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
