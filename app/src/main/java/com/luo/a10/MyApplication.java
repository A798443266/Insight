package com.luo.a10;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.luo.a10.model.Model;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    private void init() {

        Model.getInstance().init(context);

        //初始化科大讯飞
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5ca5b466");

        QbSdk.setDownloadWithoutWifi(true);//非wifi条件下允许下载X5内核
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

        //okhttpUtils配置
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar)
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
     */
    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished(boolean arg0) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            Log.e("TAG", "加载x5内核是否成功==" + arg0);
        }

        @Override
        public void onCoreInitFinished() {
            Log.e("APPAplication", " onCoreInitFinished");
        }
    };
}
