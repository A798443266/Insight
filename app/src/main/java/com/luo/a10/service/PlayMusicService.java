package com.luo.a10.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import com.luo.a10.IPlayMusicService;
import com.luo.a10.eventBusObject.MusicPlayEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * 后台播放音乐的service
 */
public class PlayMusicService extends Service {

    private String mPath; //播放音乐的地址
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public PlayMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IPlayMusicService.Stub stub = new IPlayMusicService.Stub() {
        //服务类
        PlayMusicService service = PlayMusicService.this;

        @Override
        public void setPath(String path) throws RemoteException {
            service.setPath(path);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seeTo(position);
        }


    };


    //设置播放路径
    public void setPath(String path) {
        mPath = path;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
//        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(this, Uri.parse(mPath));
        //设置监听
        try {
            mediaPlayer.prepareAsync();//准备播放
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
        mediaPlayer.setOnErrorListener(new MyOnErrorListener());
        mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
//            mediaPlayer.setDataSource(mPath);
        //此时转到onprepare()播放
    }

    public void start() {
        mediaPlayer.start();//开启音乐
    }

    public void stop() {
        mediaPlayer.stop();//关闭音乐
    }

    public void pause() {
        mediaPlayer.pause();//暂停音乐
    }

    //得到当前的播放进度
    private int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();//获取文件的总长度
    }

    public void seeTo(int position) {
        mediaPlayer.seekTo(position);//重新设定播放进度
    }

    //是否正在播放
    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
//            发布事件，activity通过订阅事件来获取播放成功的消息，好让activity显示歌曲信息
            EventBus.getDefault().post(new MusicPlayEvent(0));
            start();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        //播放错误
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            EventBus.getDefault().post(new MusicPlayEvent(1));
            return true;
        }
    }

    //播放完成
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            EventBus.getDefault().post(new MusicPlayEvent(2));
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }
}
