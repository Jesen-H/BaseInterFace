package net.hycollege.myproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.hycollege.myproject.base.BaseFragment;
import net.hycollege.myproject.fragment.IndexFragment;
import net.hycollege.myproject.fragment.MapFragment;
import net.hycollege.myproject.fragment.MeFragment;
import net.hycollege.myproject.fragment.MusicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {

    @Bind(R.id.rb_common_frame)
    RadioButton rbCommonFrame;
    @Bind(R.id.rb_thirdparty)
    RadioButton rbThirdparty;
    @Bind(R.id.rb_custom)
    RadioButton rbCustom;
    @Bind(R.id.rb_other)
    RadioButton rbOther;
    private RadioGroup mRg_main;
    private List<BaseFragment> mBaseFragment;
    Context context;

    /**
     * 选中的Fragment的对应的位置
     */
    private int position;

    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;

//    FrameLayout frameLayout;
//
//    Fragment oneFragment;
//    Fragment twoFragment;
//    Fragment threeFragment;
//    Fragment fourFragment;
//
//    FragmentManager fragmentManager;
//
//    RelativeLayout oneRelativeLayout;
//    RelativeLayout twoRelativeLayout;
//    RelativeLayout threeRelativeLayout;
//    RelativeLayout fourRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();

        //drawableTop设置
        initDrawable();

    }

    private void initDrawable() {
    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_common_frame);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_common_frame://首页
                    position = 0;
                    break;
                case R.id.rb_thirdparty://百度地图
                    position = 1;
                    break;
                case R.id.rb_custom://音乐
                    position = 2;
                    break;
                case R.id.rb_other://我
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换
            switchFrament(mContent, to);

        }
    }


    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFrament(Fragment from, Fragment to) {
        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                if (to != null) {
                    ft.add(R.id.frameLayout, to).commit();
                }
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                if (to != null) {
                    ft.show(to).commit();
                }
            }
        }

    }

//    private void switchFrament(BaseFragment fragment) {
//        //1.得到FragmentManger
//        FragmentManager fm = getSupportFragmentManager();
//        //2.开启事务
//        FragmentTransaction transaction = fm.beginTransaction();
//        //3.替换
//        transaction.replace(R.id.fl_content, fragment);
//        //4.提交事务
//        transaction.commit();
//    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new IndexFragment());//首页Fragment
        mBaseFragment.add(new MapFragment());//地图Fragment
        mBaseFragment.add(new MusicFragment(context));//音乐Fragment
        mBaseFragment.add(new MeFragment());//我Fragment
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);

    }

    private boolean exit = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                exit = false;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!exit) {
                exit = true;
                Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show();
                //2s内再点击一次退出
                handler.sendEmptyMessageDelayed(1, 2000);
                return true;//不退出
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}


