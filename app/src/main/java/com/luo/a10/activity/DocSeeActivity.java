package com.luo.a10.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.luo.a10.adapter.PiZhuAdapter;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.JsonParser;
import com.luo.a10.utils.UIUtils;
import com.tencent.smtt.sdk.TbsReaderView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 查看文件的界面
 */
public class DocSeeActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback {

    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.iv_beizhu)
    ImageView ivBeizhu;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    private TbsReaderView mTbsReaderView;
    private EasyPopup mCirclePop;
    private List<String> data = new ArrayList<>();
    private String content = "";
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private SpeechRecognizer speechRecognizer;
    private VoiceLineView voicLine;
    private EditText et_input;
    private String base = Environment.getExternalStorageDirectory().getAbsolutePath();
    private FolderAndDoc doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_see);
        ButterKnife.bind(this);

        doc = (FolderAndDoc) getIntent().getSerializableExtra("doc");
        if (doc == null) {
            return;
        }
        Log.e("TAG", doc.getTime());
        tvTitle.setText(doc.getName());
        mTbsReaderView = new TbsReaderView(this, this);
        rlContent.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        openFile();
    }

    private void openFile() {
        if (fileIsExists(base + "/downloadFile/" + doc.getName())) {//如果已经下载过了本地已经缓存了
            open(base + "/downloadFile/" + doc.getName());

        } else {
            downloadFile();
        }
    }


    private void open(String path) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", path);
        //临时文件的路径，必须设置，否则会报错
        bundle.putString("tempPath", base + "/腾讯文件TBS");
        File file = new File(base + "/腾讯文件TBS");
        if (!file.exists()) {
            file.mkdir();
        }
        //准备
        boolean result = mTbsReaderView.preOpen(getFileType(path), false);
        if (result) {
            //预览文件
            mTbsReaderView.openFile(bundle);
        }
    }

    //下载文件
    private void downloadFile() {
        llLoad.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(doc.getLink())
                .build()
                .execute(new FileCallBack(base + "/downloadFile", doc.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        llLoad.setVisibility(View.GONE);
                        Toast.makeText(DocSeeActivity.this, "预览失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        llLoad.setVisibility(View.GONE);
                        //下载成功从本地打开
                        open(base + "/downloadFile/" + doc.getName());
                    }
                });

    }


    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null");
            return str;
        }
        Log.d("print", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("print", "i <= -1");
            return str;
        }
        str = paramString.substring(i + 1);
        Log.d("print", "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //需要将预览服务停止，一定不要忘了
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    @OnClick({R.id.rl_back, R.id.iv_info, R.id.iv_beizhu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_info:
                showInfo();
                break;
            case R.id.iv_beizhu:
                showBeizhu();
                break;
        }
    }


    //备注信息
    private void showBeizhu() {
        View view = View.inflate(this, R.layout.pop_doc_beizhu, null);
        final ListView lv = view.findViewById(R.id.lv);
        final LinearLayout ll1 = view.findViewById(R.id.ll1);
        final LinearLayout ll2 = view.findViewById(R.id.ll2);
        final TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        final TextView tv_ok = view.findViewById(R.id.tv_ok);
        final TextView tv_speak = view.findViewById(R.id.tv_speak);
        et_input = view.findViewById(R.id.et_input);
        voicLine = view.findViewById(R.id.voicLine);
        LinearLayout ll_pizhu = view.findViewById(R.id.ll_pizhu);
        data.add("远期利率协议是一种远期合约。");
        data.add("本票是指由出票人签发的，承诺自己在见票时无条件支付确定的金额给收款人或者持票人的票据。");

        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);
        final PiZhuAdapter adapter = new PiZhuAdapter(this, data);
        lv.setAdapter(adapter);
        mCirclePop = new EasyPopup(this)
                .setContentView(view)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(UIUtils.dp2px(350))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.popyuyin_animation)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(rl, YGravity.ALIGN_BOTTOM, XGravity.CENTER, 0, 0);
        ivBeizhu.setBackgroundResource(R.drawable.icon_seedoc4_1);
        mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivBeizhu.setBackgroundResource(R.drawable.icon_seedoc4);
                mCirclePop = null;
                data.clear();//清空数据
            }
        });

        ll_pizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                et_input.setText("");
            }
        });

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    tv_ok.setTextColor(UIUtils.getColor(R.color.system_gray));
                    tv_ok.setClickable(false);
                } else {
                    tv_ok.setTextColor(UIUtils.getColor(R.color.system_blue));
                    tv_ok.setClickable(true);
                }
            }
        });


        tv_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tv_speak.setBackgroundResource(R.drawable.pizhu_btn_bg1);
                        mIatResults.clear();
                        setSpeechParam();
                        voicLine.setVisibility(View.VISIBLE);
                        int ret = speechRecognizer.startListening(mRecoListener);
                        if (ret != ErrorCode.SUCCESS) {
                            //此时听写失败
                            Toast.makeText(DocSeeActivity.this, "听写失败", Toast.LENGTH_SHORT).show();
                            voicLine.setVisibility(View.GONE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        tv_speak.setBackgroundResource(R.drawable.pizhu_btn_bg);
                        voicLine.setVisibility(View.GONE);
                        if (speechRecognizer.isListening())
                            speechRecognizer.stopListening();
                        break;
                }
                return true;
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.add(0, content);
                ll1.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                lv.setSelection(0);
                ll2.setVisibility(View.GONE);
            }
        });
    }

    private void setSpeechParam() {
        speechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");
    }


    //显示文档信息
    private void showInfo() {
        View view = View.inflate(this, R.layout.pop_doc_info, null);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_type = view.findViewById(R.id.tv_type);
        TextView tv_size = view.findViewById(R.id.tv_size);
        TextView tv_time = view.findViewById(R.id.tv_time);
        TextView tv_time1 = view.findViewById(R.id.tv_time1);
        if (doc != null) {
            tv_name.setText(doc.getName());
            tv_type.setText(doc.getName().substring(doc.getName().lastIndexOf(".") + 1) + "文档");
            tv_size.setText(Formatter.formatFileSize(this, doc.getSize()));
            tv_time.setText(UIUtils.StringTime(Long.parseLong(doc.getTime())));
            tv_time1.setText(UIUtils.StringTime(Long.parseLong(doc.getTime())));
        }
        mCirclePop = new EasyPopup(this)
                .setContentView(view)
                .setWidth(UIUtils.dp2px(300))
                .setHeight(UIUtils.dp2px(370))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.pop_docinfo_animation)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(rl, YGravity.CENTER, XGravity.CENTER, 0, 0);
        ivInfo.setBackgroundResource(R.drawable.icon_seedoc3_1);
        mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivInfo.setBackgroundResource(R.drawable.icon_seedoc3);
                mCirclePop = null;
            }
        });

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
            String s1 = s.substring(0, s.length() - 1);
            et_input.setText(s1);
            et_input.setSelection(et_input.length());
            voicLine.setVisibility(View.GONE);
        }

        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(DocSeeActivity.this, "我没有听清哦", Toast.LENGTH_SHORT).show();
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

}
