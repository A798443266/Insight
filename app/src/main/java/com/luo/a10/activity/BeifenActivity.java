package com.luo.a10.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.luo.a10.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeifenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beifen);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
