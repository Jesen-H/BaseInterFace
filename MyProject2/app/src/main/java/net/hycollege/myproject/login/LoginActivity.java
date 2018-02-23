package net.hycollege.myproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.hycollege.myproject.MainActivity;
import net.hycollege.myproject.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

        public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

        private Button login_btn;
        private EditText user,psw;
        private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = (Button) findViewById(R.id.login);

        user = (EditText) findViewById(R.id.user_login);
        psw = (EditText) findViewById(R.id.password_login);

        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
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
                final String name = user.getText().toString().trim();
                final String password = psw.getText().toString().trim();
                try{
                    final String result = post("http://192.168.1.103:8080/Web_Mysql/loginServlet?name=" + name + "&password=" + password,"");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.equals("1")){
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
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
            //分线程执行
//            new Thread() {
//                @Override
//                public void run() {

//                    HttpURLConnection conn = null;
//                    try {
                        //获取Web连接
//                        String path = "http://172.19.69.118:8080/Web_Mysql/loginServlet?name=" + name + "&password=" + password;
//                        URL url = new URL(path);
//                        //网络请求
//                        conn = (HttpURLConnection) url.openConnection();
//
//                        conn.connect();
//                        int respose = conn.getResponseCode();
//                        //如果等于200，获取流数据
//                        if (respose == 200) {
//                            InputStream is = conn.getInputStream();
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            byte[] b = new byte[1024];
//                            int len = -1;
//                            while ((len = is.read(b)) != -1) {
//                                baos.write(b, 0, len);
//                            }
//                            final String result = baos.toString();
//
//                            //关闭IO流
//                            is.close();
//                            baos.close();

