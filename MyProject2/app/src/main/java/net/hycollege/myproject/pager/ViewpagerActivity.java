package net.hycollege.myproject.pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.hycollege.myproject.R;
import net.hycollege.myproject.login.AppActivity;
import net.hycollege.myproject.tools.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerActivity extends Activity {
    private View view1, view2, view3;
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //为ViewPager对象添加动画页面资源
//        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());

        LayoutInflater inflater=getLayoutInflater();
        view1 = inflater.inflate(R.layout.view_one_pager, null);
        view2 = inflater.inflate(R.layout.view_two_pager,null);
        view3 = inflater.inflate(R.layout.view_three_pager, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);


        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
    public void Beginbtn(View v){
        Intent i = new Intent(ViewpagerActivity.this,AppActivity.class);
        startActivity(i);
        finish();
    }
}
