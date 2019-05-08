package com.luo.a10.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.luo.a10.R;
import com.luo.a10.adapter.YuyinRecordAdapter;
import com.luo.a10.model.Model;
import com.luo.a10.utils.UIUtils;
import com.luo.a10.yuyinrecord.RecordAudioDialogFragment;
import com.luo.a10.yuyinrecord.RecordingItem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YuYinRecordActivity extends AppCompatActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.iv_start)
    ImageView ivStart;

    private YuyinRecordAdapter adapter;
    private List<RecordingItem> datas;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;//判断当前是否正在播放
    private int curentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu_yin_record);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        datas = Model.getInstance().getDbDao().queryLuyins();
        adapter = new YuyinRecordAdapter(this, datas);

        adapter.setOnPlayListener(new YuyinRecordAdapter.OnPlayListener() {
            @Override
            public void onPlay(final int position, RelativeLayout rl2, final ImageView iv_play, final SeekBar seekbar, final TextView tv_current, TextView tv_total) {

                if (mediaPlayer != null && position == curentPosition && isPlaying) {//正在播放且在此点击这条语音要暂停
                    mediaPlayer.pause();
                    iv_play.setBackgroundResource(R.drawable.icon_play_black);
                    tv_total.setText(UIUtils.stringForTime(mediaPlayer.getDuration()));
                    isPlaying = false;
                    handler.sendEmptyMessage(3);//暂停
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlaying();
                            iv_play.setBackgroundResource(R.drawable.icon_play_black);
                            seekbar.setProgress(0);
                            tv_current.setText("00:00");
                            handler.sendEmptyMessage(2);
                        }
                    });
                    Log.e("TAG", "正在播放且在此点击这条语音要暂停");

                } else if (mediaPlayer != null && position == curentPosition && !isPlaying) {//从暂停开始播放
                    mediaPlayer.start();
                    handler.sendEmptyMessageDelayed(1,1000);
                    Log.e("TAG", "从暂停开始播放");
                    iv_play.setBackgroundResource(R.drawable.icon__pause_black);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlaying();
                            iv_play.setBackgroundResource(R.drawable.icon_play_black);
                            seekbar.setProgress(0);
                            tv_current.setText("00:00");
                            handler.sendEmptyMessage(2);
                        }
                    });
                    isPlaying = true;

                } else if (mediaPlayer != null && position != curentPosition) {//已经有一个在播放了，再点另一个
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                    Log.e("TAG", "已经有一个在播放了，再点另一个");

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(datas.get(position).getFilePath());
                        mediaPlayer.prepare();
                        seekbar.setMax(mediaPlayer.getDuration());
                        tv_total.setText(UIUtils.stringForTime(mediaPlayer.getDuration()));
                        iv_play.setBackgroundResource(R.drawable.icon__pause_black);
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                                isPlaying = true;
                                curentPosition = position;
                                handler.sendEmptyMessageDelayed(1,1000);

                            }
                        });
                        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlaying();
                            iv_play.setBackgroundResource(R.drawable.icon_play_black);
                            seekbar.setProgress(0);
                            tv_current.setText("00:00");
                            handler.sendEmptyMessage(2);
                        }
                    });

                } else {//还没有播放过
                    mediaPlayer = new MediaPlayer();
                    try {
                        Log.e("TAG", "还没有播放过");
                        mediaPlayer.setDataSource(datas.get(position).getFilePath());
                        mediaPlayer.prepare();
                        iv_play.setBackgroundResource(R.drawable.icon__pause_black);
                        seekbar.setMax(mediaPlayer.getDuration());
                        tv_total.setText(UIUtils.stringForTime(mediaPlayer.getDuration()));
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                                isPlaying = true;
                                curentPosition = position;
                                handler.sendEmptyMessageDelayed(1,1000);
                            }
                        });
                        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlaying();
                            iv_play.setBackgroundResource(R.drawable.icon_play_black);
                            seekbar.setProgress(0);
                            tv_current.setText("00:00");
                            handler.sendEmptyMessage(2);
                        }
                    });
                }
            }
        });

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(YuYinRecordActivity.this,YuyinDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",datas.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    removeMessages(1);
                    adapter.update(mediaPlayer, curentPosition);
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                case 2:
                    //播放完成停止更新进度条
                    removeMessages(2);
                    adapter.stop(null, -1);
                    break;
                case 3:
                    removeMessages(1);//暂停就是清空更新进度的消息
                    break;
            }
        }
    };

    private void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
        curentPosition = -1;
        handler.removeCallbacksAndMessages(null);
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);

                }

            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeCallbacksAndMessages(null);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessage(1);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoreWindow(RecordAudioDialogFragment m) {//录音完之后收到消息刷新列表
        initData();
    }

    @OnClick({R.id.rl_back, R.id.iv_start,R.id.rl_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.iv_start:
                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        fragment.dismiss();
                    }
                });
                break;

            case R.id.rl_more:
                Model.getInstance().getDbDao().delAllYuyin();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        if (mediaPlayer != null)
            stopPlaying();
    }
}
