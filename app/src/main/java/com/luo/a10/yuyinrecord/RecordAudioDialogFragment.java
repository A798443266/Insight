package com.luo.a10.yuyinrecord;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.model.Model;
import com.luo.a10.utils.UIUtils;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Date;

/**
 * 开始录音的 DialogFragment
 */

public class RecordAudioDialogFragment extends DialogFragment {

    private int mRecordPromptCount = 0;

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;

    long timeWhenPaused = 0;

    private ImageView mFabRecord;
    private Chronometer mChronometerTime;
    private ImageView mIvClose;
    private CardView cardview;

    private OnAudioCancelListener mListener;

    public static RecordAudioDialogFragment newInstance() {
        RecordAudioDialogFragment dialogFragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_record_audio, null);
        initView(view);

        EventBus.getDefault().register(this);

        mFabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;

            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                EventBus.getDefault().unregister(this);
            }
        });

        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void initView(View view) {
        mChronometerTime = (Chronometer) view.findViewById(R.id.record_audio_chronometer_time);
        mFabRecord = view.findViewById(R.id.record_audio_fab_record);
        mIvClose = (ImageView) view.findViewById(R.id.record_audio_iv_close);
        cardview = view.findViewById(R.id.cardview);
    }

    private void onRecord(boolean start) {

        Intent intent = new Intent(getActivity(), RecordingService.class);

        if (start) {
            Toast.makeText(getActivity(), "开始录音...", Toast.LENGTH_SHORT).show();
            mFabRecord.setBackgroundResource(R.drawable.icon_stop);
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                folder.mkdir();
            }

            mChronometerTime.setBase(SystemClock.elapsedRealtime());
            mChronometerTime.start();

            getActivity().startService(intent);

        } else {
            mChronometerTime.stop();
            timeWhenPaused = 0;
            Toast.makeText(getActivity(), "录音结束...", Toast.LENGTH_SHORT).show();

            getActivity().stopService(intent);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            EventBus.getDefault().post(new RecordingItem());//发消息更新
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoreWindow(final RecordingItem m) {//录音完之后收到service发来的消息提示用户输入名称，并插入数据库
        Log.e("TAG", "收到消息");
        View view = View.inflate(getActivity(), R.layout.pop_yuyin_inputname, null);
        final EditText et_name = view.findViewById(R.id.et_name);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final EasyPopup mCirclePop = new EasyPopup(getActivity())
                .setContentView(view)
                .setWidth(UIUtils.dp2px(270))
                .setHeight(UIUtils.dp2px(220))
                .setBackgroundDimEnable(false)
                .setDimValue(0f)
                .setFocusAndOutsideEnable(false)
                .apply();
        mCirclePop.showAtAnchorView(cardview, YGravity.ALIGN_BOTTOM, XGravity.CENTER, 0, 0);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Date date = new Date();
                Model.getInstance().getDbDao().insertLuyin(name, m.getFilePath(), "", m.getTime(), date.getTime());
                mCirclePop.dismiss();
                EventBus.getDefault().post(new RecordAudioDialogFragment());//发消息到activity页面更新语音数量
                dismiss();//关闭当前fragment
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Model.getInstance().getDbDao().insertLuyin(m.getName(), m.getFilePath(), "", m.getTime(), date.getTime());
                mCirclePop.dismiss();
                EventBus.getDefault().post(new RecordAudioDialogFragment());//发消息到activity页面更新语音数量
                dismiss();
                //关闭当前fragment
            }
        });


    }


    public void setOnCancelListener(OnAudioCancelListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    onRecord(mStartRecording);
                }
                break;
        }
    }

    public interface OnAudioCancelListener {
        void onCancel();
    }
}
