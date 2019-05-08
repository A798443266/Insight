package com.luo.a10.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gyf.barlibrary.ImmersionBar;
import com.luo.a10.R;
import com.luo.a10.utils.Constant;
import com.luo.a10.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        final String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
            return;
        }
        llLoad.setVisibility(View.VISIBLE);

        OkHttpUtils.post().url(Constant.LOGIN)
                .addHeader("agent","Android")
                .addParams("username",name)
                .addParams("password",password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        llLoad.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        llLoad.setVisibility(View.GONE);
                        JSONObject jsonObject = JSON.parseObject(response);
                        boolean type = jsonObject.getBoolean("success");
                        if(type){
                            int userid = jsonObject.getInteger("root");
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            SpUtils.putBoolean(LoginActivity.this,Constant.ISLOGIN,true);
                            SpUtils.putString(LoginActivity.this,Constant.USERNAME,name);
                            SpUtils.putInt(LoginActivity.this,Constant.USERID,userid);
                            llLoad.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"账号密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        handler.sendEmptyMessageDelayed(1,1500);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            SpUtils.putBoolean(LoginActivity.this,Constant.ISLOGIN,true);
            llLoad.setVisibility(View.GONE);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
