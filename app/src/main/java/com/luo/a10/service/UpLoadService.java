package com.luo.a10.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.luo.a10.eventBusObject.UpLoadEvent;
import com.luo.a10.fileselect.bean.FileItem;
import com.luo.a10.utils.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 上传文件的服务
 */
public class UpLoadService extends Service {
    String folderId;
    private List<FileItem> files;
    String tag;

    public UpLoadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        files = (ArrayList<FileItem>) intent.getSerializableExtra("files");//上传的文件列表
        folderId = intent.getStringExtra("folderId");//要上传的文件夹id
        tag = intent.getStringExtra("tag");

        Log.e("TAG", "标签：" + tag);
        if(TextUtils.isEmpty(folderId)){
            Log.e("TAG", "文件id没有");
        }

        if (files != null && files.size() > 0 && !TextUtils.isEmpty(folderId))
            upLoad();
        else {
            Log.e("TAG", "空");
        }

        return startId;
    }

    private void upLoad() {

        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        headers.put("Content-Type", "mutipart/form-data");
        File file = new File(files.get(0).getPath());
        params.put("filelocation", folderId);
        if (!TextUtils.isEmpty(tag))
            params.put("tag", tag);
        else
            params.put("tag", "");

        OkHttpUtils.post().url(Constant.UPLOAD)
                .headers(headers)
                .params(params)
                .addFile("uploadfile", files.get(0).getName(), file)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }


                    @Override
                    public void onError(Call call, Exception e, int id) {
                        EventBus.getDefault().post(new UpLoadEvent("失败",0));
                        Log.e("TAG", "失败");
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int p = (int) (progress * 100.0);
                        EventBus.getDefault().post(new UpLoadEvent("上传",p));

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        EventBus.getDefault().post(new UpLoadEvent("成功",0));
                        Log.e("TAG", "成功");
                    }
                });

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
