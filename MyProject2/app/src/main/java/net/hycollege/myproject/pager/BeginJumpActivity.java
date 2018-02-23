package net.hycollege.myproject.pager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import net.hycollege.myproject.R;


public class BeginJumpActivity extends AppCompatActivity{


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                Intent i = new Intent(BeginJumpActivity.this,ViewpagerActivity.class);
                startActivity(i);
                finish();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        handler.sendEmptyMessageDelayed(1,2000);
    }
}
