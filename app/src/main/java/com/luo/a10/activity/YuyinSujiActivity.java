package com.luo.a10.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
import com.luo.a10.R;
import com.luo.a10.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 语音速记
 */
public class YuyinSujiActivity extends AppCompatActivity {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_speak)
    TextView tvSpeak;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.voicLine)
    VoiceLineView voicLine;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuyin_suji);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener() {
        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ll.setBackgroundResource(R.drawable.pizhu_btn_bg1);
                        mIatResults.clear();
                        setSpeechParam();
                        voicLine.setVisibility(View.VISIBLE);
                        int ret = speechRecognizer.startListening(mRecoListener);
                        if (ret != ErrorCode.SUCCESS) {
                            //此时听写失败
                            Toast.makeText(YuyinSujiActivity.this, "听写失败", Toast.LENGTH_SHORT).show();
                            voicLine.setVisibility(View.GONE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ll.setBackgroundResource(R.drawable.pizhu_btn_bg);
                        voicLine.setVisibility(View.GONE);
                        if (speechRecognizer.isListening())
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
            voicLine.setVisibility(View.GONE);
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
            String s1 = s.substring(0, s.length() - 1);//去除后面的句号
            Log.e("TAG", "听到 : " + s1);
            String content = etInput.getText().toString();
            Log.e("TAG", "原来 ： " + content);
            etInput.setText(content + s1);
            etInput.setSelection(etInput.length());
            voicLine.setVisibility(View.GONE);
        }

        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(YuyinSujiActivity.this, "我没有听清哦", Toast.LENGTH_SHORT).show();
            voicLine.setVisibility(View.GONE);
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

    @OnClick({R.id.ll_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_save:
//                try {
//                    FileWriter fw = new FileWriter("/sdcard/aaa" + "/cmd.txt");//SD卡中的路径
//                    fw.flush();
//                    fw.write(etInput.getText().toString());
//                    fw.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
        }

    }
}
