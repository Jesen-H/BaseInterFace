package net.hycollege.myproject.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import net.hycollege.myproject.AudioPlayerActivity;
import net.hycollege.myproject.IMusicPlayerService;
import net.hycollege.myproject.R;
import net.hycollege.myproject.domain.MediaItem;
import net.hycollege.myproject.utils.CacheUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;

/**
 * Created by Administrator on 2018/1/28.
 */

public class MusicPlayerService extends Service {


    public static final String OPENAUDIO = "com.hycollege.mobileplayer_OPENAUDIO";
    private ArrayList<MediaItem> mediaItems;
    private int position;

    //顺序播放
    public static final int REPEAT_NORMAL = 1;
    //单曲循环
    public static final int REPEAT_SINGLE = 2;
    //循环播放
    public static final int REPEAT_ALL = 3;

    private int playmode = REPEAT_NORMAL;

    //得到当前播放的音频文件对象
    private MediaItem mediaItem;

    //用于播放音乐
    private MediaPlayer mediaplayer;

    @Override
    public void onCreate() {
        super.onCreate();

        playmode = CacheUtil.getPlaymode(this,"playmode");

        //加载音乐列表
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                super.run();

//                isGrantExternalRW((Activity) context);
//                SystemClock.sleep(2000);
                mediaItems = new ArrayList<>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视频总时长
                        MediaStore.Audio.Media.SIZE,//视频的文件大小
                        MediaStore.Audio.Media.DATA,//视频的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者

                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor != null){
                    while (cursor.moveToNext()){

                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);//写在上面

                        String name = cursor.getString(0);//视频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);

                    }
                    cursor.close();
                }
            }
        }.start();
    }

    private MusicPlayerService service = MusicPlayerService.this;
    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public String getSinger() throws RemoteException {
            return service.getSinger();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
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
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {
            service.setPlayMode(playmode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }


        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    //根据位置打开音乐
    private void openAudio(int position){
        
        this.position = position;

        if (mediaItems != null && mediaItems.size() > 0){
            mediaItem = mediaItems.get(position);


            if (mediaplayer != null){
//                mediaplayer.release();
                mediaplayer.reset();
            }

            try {
                mediaplayer = new MediaPlayer();
                //设置监听：播放完成，播放失败，准备好
                mediaplayer.setOnPreparedListener(new MyOnPreparedListener());//准备好
                mediaplayer.setOnCompletionListener(new MyOnCompletionListener());//播放完成
                mediaplayer.setOnErrorListener(new MyOnErrorListener());//播放失败
                mediaplayer.setDataSource(mediaItem.getData());
                mediaplayer.prepareAsync();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(MusicPlayerService.this,"还没加载数据",Toast.LENGTH_SHORT).show();
        }

    }
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
//            //注册广播传数据
//            notifySend(OPENAUDIO);
            EventBus.getDefault().post(mediaItem);

            start();
        }
    }

    private void notifySend(String action) {
        //根据动作发广播
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            next();
        }
    }
    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            next();
            return true;
        }
    }

    private NotificationManager manager;

    //播放音乐
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void start(){
        mediaplayer.start();


        //当播放歌曲的时候，在状态栏显示正在播放，点击的时候，可以进入音乐播放界面
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("Notification",true);//标识点击进入的状态栏
        PendingIntent pendingintent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("网易云音乐")
                .setContentText("正在播放..." + getName())
                .setContentIntent(pendingintent)
                .build();
        manager.notify(1,notification);

    }



    //暂停音乐
    private void pause(){
        mediaplayer.pause();
        manager.cancel(1);
    }

    //关闭音乐
    private void stop(){
        mediaplayer.stop();
    }

    //下一首
    private void next(){
        //设置歌曲当前位置
        setNextPosition();

        //根据当前播放模式 播放歌曲
        openNextAudio();

    }

    private void openNextAudio() {
        int playmode = getPlayMode();
        if (playmode == MusicPlayerService.REPEAT_NORMAL){
            if (position < mediaItems.size()){
                //正常范围
                openAudio(position);
            }else{
                position = mediaItems.size()-1;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if (position < mediaItems.size()){
                //正常范围
                openAudio(position);
            }else{
                position = mediaItems.size()-1;
            }
        }
    }

    private void setNextPosition() {
        int playmode = getPlayMode();
        if (playmode == MusicPlayerService.REPEAT_NORMAL){
            position++;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position++;
            if (position >= mediaItems.size()){
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            position++;
            if (position >= mediaItems.size()){
                position = 0;
            }
        }else{
            position++;
        }
    }

    //上一首
    private void pre(){

        setPrePosition();
        openPreAudio();

    }

    private void openPreAudio() {
        int playmode = getPlayMode();
        if (playmode == MusicPlayerService.REPEAT_NORMAL){
            if (position >= 0){
                //正常范围
                openAudio(position);
            }else{
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if (position >= 0){
            //正常范围
            openAudio(position);
            }else{
                position = 0;
            }
        }
    }

    private void setPrePosition() {
        int playmode = getPlayMode();
        if (playmode == MusicPlayerService.REPEAT_NORMAL){
            position--;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position--;
            if (position < 0){
                position = mediaItems.size()-1;
            }
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            position-- ;
            if (position < 0){
                position = mediaItems.size()-1;
            }
        }else{
            position--;
        }
    }

    //获取歌曲的演唱者
    private String getSinger(){
        return mediaItem.getArtist();
    }

    //获取歌曲的名称
    private String getName(){
        return mediaItem.getName();
    }

    //获取歌曲的进度
    private int getCurrentPosition(){
        return mediaplayer.getCurrentPosition();
    }

    //获取歌曲的总时长
    private int getDuration(){
        return mediaplayer.getDuration();
    }

    //得到歌曲播放的路径
    private String getAudioPath(){
        return "";
    }

    //设置播放模式
    private void setPlayMode(int playmode){
        this.playmode = playmode;
        CacheUtil.putPlaymode(this,"playmode",playmode);

        if (playmode == MusicPlayerService.REPEAT_SINGLE){
            mediaplayer.setLooping(true);
        }else{
            mediaplayer.setLooping(false);
        }
    }

    //获取播放模式
    private int getPlayMode(){
        return playmode;
    }

    //正在播放
    private boolean isPlaying(){
      return mediaplayer.isPlaying();
    }

    private void seekTo(int position){
        mediaplayer.seekTo(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
