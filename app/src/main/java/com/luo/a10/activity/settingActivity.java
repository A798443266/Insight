package com.luo.a10.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.luo.a10.R;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class settingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_out)
    public void onViewClicked() {
        SpUtils.putBoolean(this, Constant.ISLOGIN, false);
        startActivity(new Intent(this,LoginActivity.class));
    }
}
