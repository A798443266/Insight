package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import com.luo.a10.R;
import com.luo.a10.bean.change.FolderAndDoc;
import com.luo.a10.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private FolderAndDoc pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        ButterKnife.bind(this);

        pic = (FolderAndDoc) getIntent().getSerializableExtra("pic");
        if (pic != null) {
            tvName.setText(pic.getName());
            tvSize.setText(Formatter.formatFileSize(this,pic.getSize()));
            tvTime.setText(UIUtils.StringTime(Long.parseLong(pic.getTime())));
        }
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
