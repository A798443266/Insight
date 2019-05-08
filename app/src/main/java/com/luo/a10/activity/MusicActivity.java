package com.luo.a10.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.a10.IPlayMusicService;
import com.luo.a10.R;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.eventBusObject.CancelMainMusicEvent;
import com.luo.a10.eventBusObject.MusicPlayEvent;
import com.luo.a10.eventBusObject.MusicStateEvent;
import com.luo.a10.service.PlayMusicService;
import com.luo.a10.utils.UIUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_songname)
    TextView tvSongname;
    @BindView(R.id.tv_artname)
    TextView tvArtname;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.iv_start)
    ImageView ivStart;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_folder_path)
    TextView tvFolderPath;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_tag1)
    TextView tvTag1;
    @BindView(R.id.tv_tag2)
    TextView tvTag2;

    private static final int PROGRESS = 1;
    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;
    private IPlayMusicService Iservice;
    private String path;
    private boolean isNew;//判断是从哪里进来的
    private FolderAndDoc music;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    //得到当前的播放进度
                    try {
                        int currentPosition = Iservice.getCurrentPosition();
                        //设置播放进度
                        seekbar.setProgress(currentPosition);
                        //设置时间
                        tvCurrentTime.setText(UIUtils.stringForTime((currentPosition)));
                        //每秒更新一次
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

        music = (FolderAndDoc) getIntent().getSerializableExtra("music");
        if (music == null) {
            music = new FolderAndDoc();
            music.setName("一生有你.mp3");
            music.setLink(Environment.getExternalStorageDirectory().getAbsolutePath() + "/kgmusic/download/水木年华 - 一生有你.mp3");
        }

//        path = getIntent().getStringExtra("music_path");
        isNew = getIntent().getBooleanExtra("isNew", true);
        //显示暂停按钮的状态
        if (!isNew) {
            int state = getIntent().getIntExtra("playState", 0);
            if (state == 1) {//正在播放
                ivStart.setBackgroundResource(R.drawable.icon_pause);
            } else {
                ivStart.setBackgroundResource(R.drawable.icon_start);
            }
        }
        EventBus.getDefault().register(this);
        initView();
        Intent intent = new Intent(this, PlayMusicService.class);
        intent.setAction("MUSIC");
        intent.putExtra("name",music.getName());
        bindService(intent, conn, Context.BIND_AUTO_CREATE);//绑定服务
        startService(intent);//不至于实例化多个服务
    }

    private void initView() {
        tvName.setText(music.getName());
        tvSongname.setText(music.getName());
        tvSize.setText(Formatter.formatFileSize(this, music.getSize()));
        tvTitle.setText(music.getName());
        tvTime.setText(UIUtils.StringTime(Long.parseLong(music.getTime())));
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Iservice = IPlayMusicService.Stub.asInterface(iBinder);
            if (Iservice != null) {
                try {
                    if (isNew) {//
//                        Iservice.setPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/kgmusic/download/水木年华 - 一生有你.mp3");//设置播放路径开始播放
//                        Iservice.setPath("http://vfx.mtime.cn/Video/2019/05/07/mp4/190507082828616353.mp4");//设置播放路径开始播放
                        Iservice.setPath(music.getLink());//设置播放路径开始播放
                    } else {//从MainActivity中进来的
                        seekbar.setMax(Iservice.getDuration());//设置进度条的最大值
                        handler.sendEmptyMessage(PROGRESS);//发消息更新进度和时间
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (Iservice != null) {
                Iservice = null;
            }
            try {
                if (Iservice != null) {
                    Iservice.stop();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showViewDataAndChek(MusicPlayEvent m) {
        if (m.getType() == 0) {
            try {
                seekbar.setMax(Iservice.getDuration());//设置进度条的最大值
                tvTotalTime.setText(UIUtils.stringForTime(Iservice.getDuration()));//设置音频总时长
                seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
                EventBus.getDefault().post(Iservice); //传递给MainActivity,若果从MainActivity点击关闭，直接利用这个Iservice.stop关闭
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessageDelayed(PROGRESS, 1000);//发消息更新进度和时间
        } else if (m.getType() == 1) {
            Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "出错了", Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    Iservice.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    @OnClick({R.id.rl_back, R.id.iv_pre, R.id.iv_start, R.id.iv_next, R.id.rl_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_pre:
                break;
            case R.id.iv_start:
                if (Iservice != null) {
                    try {
                        if (Iservice.isPlaying()) {
                            Iservice.pause();
                            EventBus.getDefault().post(new MusicStateEvent(1));
                            ivStart.setBackgroundResource(R.drawable.icon_start);
                        } else {
                            EventBus.getDefault().post(new MusicStateEvent(0));
                            ivStart.setBackgroundResource(R.drawable.icon_pause);
                            Iservice.start();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.iv_next:
                break;

            case R.id.rl_close:
                EventBus.getDefault().post(new CancelMainMusicEvent());//让mainActivity显示消失
                try {
                    Iservice.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (conn != null) {
                    unbindService(conn);//解绑
                    conn = null;
                }
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacksAndMessages(null);
        if (conn != null) {
            unbindService(conn);//解绑服务
            conn = null;
        }
    }
}
