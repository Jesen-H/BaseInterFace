package net.hycollege.myproject.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.hycollege.myproject.MainActivity;
import net.hycollege.myproject.R;
import net.hycollege.myproject.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button registerbtn;
    EditText register_user,register_psw;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn = (Button) findViewById(R.id.register);

        register_user = (EditText) findViewById(R.id.user_register);
        register_psw = (EditText) findViewById(R.id.password_register);

        registerbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                getDataFromPost();
                break;
        }
    }

    /**
     * 使用post请求网络数据
     */
    private void getDataFromPost(){
        new Thread(){
            @Override
            public void run() {
                final String name = register_user.getText().toString().trim();
                final String password = register_psw.getText().toString().trim();
                try{
                    final String result = post("http://192.168.1.103:8080/Web_Mysql/registerServlet?name=" + name + "&password=" + password,"");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.equals("1")){
                                Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }finally{

                }
            }
        }.start();
    }

    /**
     * okhttp3的post请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private String post(String url,String json) throws IOException{
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
