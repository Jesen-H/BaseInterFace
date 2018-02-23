package net.hycollege.myproject;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.hycollege.myproject.domain.MediaItem;
import net.hycollege.myproject.service.MusicPlayerService;
import net.hycollege.myproject.utils.Utils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioPlayerActivity extends AppCompatActivity {

    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_artist)
    TextView tvArtist;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.seekbar_audio)
    SeekBar seekbarAudio;
    @Bind(R.id.btn_audio_playmode)
    Button btnAudioPlaymode;
    @Bind(R.id.btn_audio_pre)
    Button btnAudioPre;
    @Bind(R.id.btn_audio_start_pause)
    Button btnAudioStartPause;
    @Bind(R.id.btn_audio_next)
    Button btnAudioNext;
    @Bind(R.id.btn_lyrc)
    Button btnLyrc;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;




    private boolean notifiy;
    private static final int PROGRESS = 1;//进度更新
    private int position;
    private MyReceiver receiver;
    private Utils util;
    private IMusicPlayerService service;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS :
                    try {
                        //1.得到当前的进度
                        int currentPosition = service.getCurrentPosition();

                        //2.设置SeekBar的进度
                        seekbarAudio.setProgress(currentPosition);

                        //3.时间进度更新
                        tvTime.setText(util.stringForTime(currentPosition)+"/"+util.stringForTime(service.getDuration()));

                        //4.每秒更新一次
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS,1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    private ServiceConnection conn = new ServiceConnection() {

        //当服务链接成功的时候回调这个方法
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);
            if(service != null){
                try {
                    if(!notifiy) {
                        //从列表
                        service.openAudio(position);
                    }else{
                        //从状态栏
                        showViewData();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        //当断开链接的时候回调这个方法
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                if (service != null){
                    service.stop();
                    service = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        ButterKnife.bind(this);
        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable rocket = (AnimationDrawable) ivIcon.getBackground();
        rocket.start();

        getData();
        bindAudioService();
        initData();

        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (b){
                //拖动进度
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    private void initData() {
        util = new Utils();

//        //注册广播
//        receiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(MusicPlayerService.OPENAUDIO);
//        registerReceiver(receiver,intentFilter);

        EventBus.getDefault().register(this);

    }

    //类继承广播
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            showData(null);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)
    public void showData(MediaItem mediaItem){
        showViewData();
        checkPlayMode();
    }

    private void showViewData(){
        try {
            tvArtist.setText(service.getSinger());
            tvName.setText(service.getName());

            //设置进度条
            seekbarAudio.setMax(service.getDuration());

            //发消息
            handler.sendEmptyMessage(PROGRESS);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindAudioService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.hycollege.mobileplayer_OPENAUDIO");
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
        startService(intent);//不至于实例化多个服务
    }



    private void getData() {

        notifiy = getIntent().getBooleanExtra("Notification",false);

        if (!notifiy) {
            position = getIntent().getIntExtra("position", 0);
        }
    }

    @OnClick(R.id.btn_audio_start_pause)
    void btnAudioStartPause(View v){
        if(service != null){
            try {
                if (service.isPlaying()){
                    //暂停
                    service.pause();
                    //播放按钮
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                }else{
                    //播放
                    service.start();
                    //暂停按钮
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_audio_playmode)
    void btnAudioPlaymode(View v){
        setPlaymode();
    }

    @OnClick(R.id.btn_audio_next)
    void btnAudioNext(View v){
        try {
            service.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_audio_pre)
    void btnAudioPre(View v){
        try {
            service.pre();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setPlaymode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL){
                playmode = MusicPlayerService.REPEAT_SINGLE;
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                playmode = MusicPlayerService.REPEAT_ALL;
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }else{
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }

            service.setPlayMode(playmode);

            setDrawable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setDrawable() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(AudioPlayerActivity.this,"单曲循环",Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(AudioPlayerActivity.this,"循环播放",Toast.LENGTH_SHORT).show();
            }else{
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void checkPlayMode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayerService.REPEAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            }else{
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(PROGRESS);

//        if (receiver != null){
//            unregisterReceiver(receiver);
//            receiver = null;
//        }

        EventBus.getDefault().unregister(this);

        if (conn != null){
            unbindService(conn);
            conn = null;
        }
        super.onDestroy();

    }
}
