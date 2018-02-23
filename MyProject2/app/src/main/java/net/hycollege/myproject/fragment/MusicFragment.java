package net.hycollege.myproject.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hycollege.myproject.AudioPlayerActivity;
import net.hycollege.myproject.R;
import net.hycollege.myproject.adapter.VideoPagerAdapter;
import net.hycollege.myproject.base.BaseFragment;
import net.hycollege.myproject.domain.MediaItem;
import net.hycollege.myproject.utils.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/19.
 */
@SuppressLint("ValidFragment")
public class MusicFragment extends BaseFragment {

    private VideoPagerAdapter videoPagerAdapter;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;


    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.tv_nomedia)
    TextView tvNomedia;
    @Bind(R.id.pb_loading)
    ProgressBar pbLoading;
    Context mContext;



    @SuppressLint("ValidFragment")
    public MusicFragment(Context mContext) {
        this.mContext = mContext;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() >0){
                //有数据
                //设置适配器
                videoPagerAdapter = new VideoPagerAdapter(mContext,mediaItems,false);
                listview.setAdapter(videoPagerAdapter);
                //把文本隐藏
                tvNomedia.setVisibility(View.GONE);
            }else{
                //没有数据
                //文本显示
                tvNomedia.setVisibility(View.VISIBLE);
                tvNomedia.setText("没有发现音频....");
            }


            //ProgressBar隐藏
            pbLoading.setVisibility(View.GONE);
        }
    };


    @Override
    protected View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        ButterKnife.bind(this, view);
        mContext = getActivity();

        listview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    public void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                super.run();

//                isGrantExternalRW((Activity) context);
//                SystemClock.sleep(2000);
                mediaItems = new ArrayList<>();
                ContentResolver resolver = mContext.getContentResolver();
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
                
                //Handler发消息
                handler.sendEmptyMessage(10);
            }
        }.start();
    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //3.传递列表数据-对象-序列化
            Intent intent = new Intent(mContext,AudioPlayerActivity.class);
            intent.putExtra("position",position);
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtil.e("本地视频的数据被初始化了...");
        getDataFromLocal();
    }
    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
