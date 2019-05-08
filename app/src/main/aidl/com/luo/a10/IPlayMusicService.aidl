package com.luo.a10;
interface IPlayMusicService {
        //播放音乐
        void start();
        //暂停音乐
        void pause();
        //停止音乐
        void stop();
        //得到当前的播放进度
        int getCurrentPosition();
        //得到音乐总时长
        int getDuration();
        //判断当前是否有音乐在播放
        boolean isPlaying();
        //拖动进度条播放
        void seekTo(int position);
        //设置播放路径
        void setPath(String path);
}
