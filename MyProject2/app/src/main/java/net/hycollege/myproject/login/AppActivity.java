package net.hycollege.myproject.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import net.hycollege.myproject.R;
import net.hycollege.myproject.register.RegisterActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppActivity extends AppCompatActivity{

    @Bind(R.id.applogin)
    Button app_login;
    @Bind(R.id.appregister)
    Button app_register;
    @Bind(R.id.activity_login)
    LinearLayout activityLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);


        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, Color.BLACK); // 边框粗细及颜色

        app_register.setBackgroundDrawable(drawable);
    }

    @OnClick(R.id.applogin)
    void app_login(View v){
        Intent i = new Intent(AppActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.appregister)
    void app_register(View v){
        Intent i = new Intent(AppActivity.this, RegisterActivity.class);
        startActivity(i);
    }
}
