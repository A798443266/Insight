package com.luo.a10.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.gyf.barlibrary.ImmersionBar;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.luo.a10.R;
import com.luo.a10.adapter.SearchRecordAdapter;
import com.luo.a10.bean.RecordInfo;
import com.luo.a10.model.Model;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.JsonParser;
import com.luo.a10.utils.JsonUtils;
import com.luo.a10.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.menu1)
    DropDownMenu menu1;
    @BindView(R.id.menu2)
    DropDownMenu menu2;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.rl)
    RelativeLayout ll;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private SpeechRecognizer speechRecognizer;
    private String[] types = new String[]{"word", "pdf", "图片", "视频", "音频", "zip", "其他"};
    private String[] titles = new String[]{"路径搜索", "内容搜索", "关键词搜索", "标签搜索", "名称搜索", "不限"};
    private List<String[]> list1 = new ArrayList<>();
    private List<String[]> list2 = new ArrayList<>();
    private SearchRecordAdapter adapter;
    private List<RecordInfo> records;
    private int way = 6;//搜索关键
    VoiceLineView voicLine;
    EasyPopup mCirclePop;
    TextView tv;
    TextView tv1;
    ImageView yuyin;

    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initData();
    }

    @SuppressLint({"NewApi"})
    private void initData() {
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.black)
                .statusBarDarkFont(true)
                .statusBarAlpha(0.0f)
                .fitsSystemWindows(true)
                .autoStatusBarDarkModeEnable(true, 0)
                .init();
        list1.add(types);
        list2.add(titles);
        initMenu();
        //让软键盘出现搜索
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

        getRecord();
    }

    private void getRecord() {
        //从数据库中获取用户的搜索记录
        records = Model.getInstance().getDbDao().queryById("1");
        if (records != null && records.size() > 0) {
            adapter = new SearchRecordAdapter(this, records);
            lv.setAdapter(adapter);
            tvClear.setVisibility(View.VISIBLE);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    etInput.setText(records.get(position).getContent());
                    etInput.setSelection(etInput.length());//指针移动到文字末尾
                }
            });
        } else {
            adapter = new SearchRecordAdapter(this, null);
            lv.setAdapter(adapter);
            tvClear.setVisibility(View.GONE);
        }
    }


    private void initMenu() {
        menu1.setDefaultMenuTitle(new String[]{"文件类型"});
        menu2.setDefaultMenuTitle(new String[]{"搜索关键"});
        menu1.setmMenuCount(1);
        menu2.setmMenuCount(1);

        menu1.setShowCheck(true);
        menu2.setShowCheck(true);

        menu1.setmMenuTitleTextSize(12);//Menu的文字大小
        menu2.setmMenuTitleTextSize(12);//Menu的文字大小

        menu1.setmMenuBackColor(Color.parseColor("#f1f6fe"));//Menu的背景颜色
        menu2.setmMenuBackColor(Color.parseColor("#f1f6fe"));//Menu的背景颜色

        menu1.setmMenuPressedBackColor(UIUtils.getColor(R.color.system_blue));
        menu2.setmMenuPressedBackColor(UIUtils.getColor(R.color.system_blue));

        menu1.setmMenuTitleTextColor(Color.parseColor("#4891f6"));//Menu的文字颜色
        menu2.setmMenuTitleTextColor(Color.parseColor("#4891f6"));//Menu的文字颜色

        menu1.setmMenuListTextSize(12);//Menu展开list的文字大小
        menu2.setmMenuListTextSize(12);//Menu展开list的文字大小

        menu1.setmDownArrow(R.drawable.icon_down);
        menu2.setmDownArrow(R.drawable.icon_down);

        menu1.setmMenuListTextColor(UIUtils.getColor(R.color.system_black));//Menu展开list的文字颜色
        menu2.setmMenuListTextColor(UIUtils.getColor(R.color.system_black));//Menu展开list的文字颜色

        menu1.setmMenuItems(list1);
        menu2.setmMenuItems(list2);

        menu1.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {

            }
        });
        menu2.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                way = RowIndex+1;
                if(RowIndex == 3)
                    way = 3;
            }
        });
    }

    @OnClick({R.id.iv_yuyin, R.id.icon_search, R.id.tv_clear,R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_yuyin:
                showDialog();
                break;
            case R.id.icon_search:
                search();
                break;
            case R.id.tv_clear:
                Model.getInstance().getDbDao().deleteAll("1");
                getRecord();
                break;
            case R.id.rl_back:
                finish();
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;
        }
    }

    private void search() {
        content = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        llLoad.setVisibility(View.VISIBLE);

        OkHttpUtils.post().url(Constant.SEARCH)
                .addParams("way",way+"")
                .addParams("text",content)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        llLoad.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this,"搜索失败",Toast.LENGTH_SHORT).show();
                        //搜索记录存入本地数据库
                        Model.getInstance().getDbDao().addRecord("1", content);
                        //重新刷新搜索记录
                        getRecord();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        show(response);
                    }
                });



    }

    private void show(String response) {
        if(TextUtils.isEmpty(response)){
            return;
        }
        llLoad.setVisibility(View.GONE);

        Intent intent = new Intent(this,SearchResultActivity.class);
        intent.putExtra("result",response);
        startActivity(intent);
        //搜索记录存入本地数据库
        Model.getInstance().getDbDao().addRecord("1", content);
        //重新刷新搜索记录
        getRecord();
        finish();
    }



    private RecognizerDialog mDialog;

    //科大讯飞语音输入
    private void showDialog() {
        showPop();
    }

    private void showPop() {
        View view = View.inflate(this, R.layout.pop_yuyin, null);
        voicLine = view.findViewById(R.id.voicLine);
        tv = view.findViewById(R.id.tv);
        tv1 = view.findViewById(R.id.tv1);
        yuyin = view.findViewById(R.id.iv_yuyin);
        mCirclePop = new EasyPopup(this)
                .setContentView(view)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(UIUtils.dp2px(300))
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.popyuyin_animation)
                .setDimValue(0.4f)
                .setFocusAndOutsideEnable(true)
                .apply();
        mCirclePop.showAtAnchorView(ll, YGravity.ALIGN_BOTTOM, XGravity.CENTER, 0, 0);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speechRecognizer.isListening())
                    speechRecognizer.stopListening();
                if (mCirclePop != null && mCirclePop.isShowing())
                    mCirclePop.dismiss();
            }
        });
        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);
        mIatResults.clear();
        setSpeechParam();
        int ret = speechRecognizer.startListening(mRecoListener);
        if (ret != ErrorCode.SUCCESS) {
            //此时听写失败
            Toast.makeText(SearchActivity.this, "听写失败", Toast.LENGTH_SHORT).show();
            mCirclePop.dismiss();
        }
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
            etInput.setText(s1);
            etInput.setSelection(etInput.length());
            if (speechRecognizer.isListening())
                speechRecognizer.stopListening();
            mCirclePop.dismiss();
        }

        @Override
        public void onError(final SpeechError speechError) {
            if (speechRecognizer.isListening())
                speechRecognizer.stopListening();
            yuyin.setVisibility(View.VISIBLE);
            tv.setText("我没有听清哦");
            tv1.setVisibility(View.GONE);
            voicLine.setVisibility(View.GONE);
            yuyin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setText("正在倾听");
                    tv1.setVisibility(View.VISIBLE);
                    voicLine.setVisibility(View.VISIBLE);
                    yuyin.setVisibility(View.GONE);
                    speechRecognizer.startListening(mRecoListener);
                }
            });
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


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
    }
}
