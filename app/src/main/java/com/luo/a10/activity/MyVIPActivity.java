package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.luo.a10.R;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyVIPActivity extends AppCompatActivity {

    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_username)
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip);
        ButterKnife.bind(this);

        tvUsername.setText(SpUtils.getString(this,Constant.USERNAME));
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
