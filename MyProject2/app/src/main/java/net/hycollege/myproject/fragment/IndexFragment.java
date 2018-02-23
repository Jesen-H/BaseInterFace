package net.hycollege.myproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hycollege.myproject.R;
import net.hycollege.myproject.base.BaseFragment;
import net.hycollege.myproject.tools.Adapter;
import net.hycollege.myproject.tools.Product;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */
public class IndexFragment extends BaseFragment {
    @Override
    protected View initView() {
        return null;
    }

    List<Product> data;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index,null);

        listView = (ListView) view.findViewById(R.id.listView);

        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    String path = "http://192.168.1.103:8080/Web_Mysql/productservlet";
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int res = conn.getResponseCode();
                    if (res == 200) {
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024];
                        int len = -1;
                        while ((len = is.read(b)) != -1) {
                            baos.write(b, 0, len);
                        }
                        final String result = baos.toString();

                        is.close();
                        baos.close();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run(){
                                //Gson框架解析json数据
                                String jsonString = result;
                                data = new Gson().fromJson(jsonString, new TypeToken<List<Product>>() {
                                }.getType());

                                //ListView绑定BaseAdapter
                                Adapter myAdapter = new Adapter(getActivity(), data);
                                listView.setAdapter(myAdapter);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    conn.disconnect();
                }
            }
        }.start();
        return view;
    }
}
