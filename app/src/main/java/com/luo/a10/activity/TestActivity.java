package com.luo.a10.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.luo.a10.IPlayMusicService;
import com.luo.a10.R;
import com.luo.a10.eventBusObject.MusicPlayEvent;
import com.luo.a10.service.PlayMusicService;
import com.luo.a10.utils.JsonParser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.voicLine)
    VoiceLineView voicLine;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll)
    LinearLayout ll;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SpeechRecognizer speechRecognizer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);//注册EventBus

        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);

        btnStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIatResults.clear();
                        setSpeechParam();
                        int ret = speechRecognizer.startListening(mRecoListener);
                        if (ret != ErrorCode.SUCCESS) {
                            //此时听写失败
                            Toast.makeText(TestActivity.this, "听写失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }
                return true;
            }
        });
    }

    private void setSpeechParam() {
        speechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");
    }


    private MediaRecorder mediaRecorder = new MediaRecorder();
    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            voicLine.setVolume(i);
        }

        @Override
        public void onBeginOfSpeech() {
            mediaRecorder.start();
            voicLine.setVolume(getDB());
            voicLine.run();
        }

        @Override
        public void onEndOfSpeech() {
            mediaRecorder.stop();
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String results = recognizerResult.getResultString();//语音解析的结果（json数据）
            String text = JsonParser.parseIatResult(results);//解析json数据
            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results);
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mIatResults.put(sn, text);
            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            String s = resultBuffer.toString();
            if (TextUtils.isEmpty(s))
                return;
            String s1 = s.substring(0, s.length() - 1);
            tvContent.setText(s1);
        }

        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(TestActivity.this, "出错了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };


    public int getDB() {
        double ratio = (double) mediaRecorder.getMaxAmplitude();
        double db = 0;
        if (ratio > 1) db = 20 * Math.log10(ratio);
        return (int) db;
    }


    //利用服务播放音乐
    public void click2(View view) {
        Intent intent = new Intent(this, PlayMusicService.class);
        intent.setAction("MUSIC");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);//绑定服务
        startService(intent);//不至于实例化多个服务
    }

    public void click3(View view) {
        try {
            if (Iservice.isPlaying()) {
                Iservice.pause();//暂停
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IPlayMusicService Iservice;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Iservice = IPlayMusicService.Stub.asInterface(iBinder);
            if (Iservice != null) {
                try {
                    Iservice.setPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/kgmusic/download/水木年华 - 一生有你.mp3");//设置播放路径开始播放
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (Iservice != null) {
                    Iservice.stop();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    //订阅事件，如果订阅事件成功，就表明歌曲播放成功，此方法就用来显示歌曲信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showViewDataAndChek(MusicPlayEvent m) {
        if (m.getType() == 0){
            Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();
//            showViewData();//显示界面进度
        }

        else if(m.getType() == 1){
            Toast.makeText(this, "播放错误", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (conn != null) {
            unbindService(conn);//解绑
            conn = null;
        }

    }
}
