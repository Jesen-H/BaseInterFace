package net.hycollege.myproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.hycollege.myproject.R;
import net.hycollege.myproject.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/19.
 */
public class MeFragment extends BaseFragment {

    @Bind(R.id.main_content_frame_parent)
    RelativeLayout mainContentFrameParent;
    @Bind(R.id.main_left_drawer_layout)
    RelativeLayout Left;
    @Bind(R.id.main_right_drawer_layout)
    RelativeLayout Right;
    @Bind(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerbar;

    @Override
    protected View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);

        initLayout();
        initEvent();
        return view;
    }

    public void initLayout() {
        //设置菜单内容之外其他区域的背景色
        drawerLayout.setScrimColor(Color.TRANSPARENT);

    }

    //设置开关监听
    private void initEvent() {
        drawerbar = new ActionBarDrawerToggle(getActivity(), drawerLayout, null, R.string.open, R.string.close) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerbar);
    }

    //左边菜单开关事件
    @OnClick(R.id.main_left_drawer_layout)
    void Left(View v){
        if (drawerLayout.isDrawerOpen(Left)) {
            drawerLayout.closeDrawer(Left);
        } else {
            drawerLayout.openDrawer(Left);
        }
    }

    //右边菜单开关事件
    @OnClick(R.id.main_left_drawer_layout)
    void Right(View v){
        if (drawerLayout.isDrawerOpen(Right)) {
            drawerLayout.closeDrawer(Right);
        } else {
            drawerLayout.openDrawer(Right);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
