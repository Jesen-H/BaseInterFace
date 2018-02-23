// IMusicPlayerService.aidl
package net.hycollege.myproject;

// Declare any non-default types here with import statements

interface IMusicPlayerService {

        //根据位置打开音乐
        void openAudio(int position);

        //播放音乐
        void start();

        //暂停音乐
        void pause();

        //关闭音乐
        void stop();

        //下一首
        void next();

        //上一首
        void pre();

        //获取歌曲的演唱者
        String getSinger();

        //获取歌曲的名称
        String getName();

        //获取歌曲的进度
        int getCurrentPosition();

        //获取歌曲的总时长
        int getDuration();

        //得到歌曲播放的路径
        String getAudioPath();

        //设置播放模式
        void setPlayMode(int playmode);

        //获取播放模式
        int getPlayMode();

        //正在播放
        boolean isPlaying();

        //拖动进度条
        void seekTo(int position);
}
