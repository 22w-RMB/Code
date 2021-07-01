package com.example.okhttptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = (Button)findViewById(R.id.send_request);
        textView = (TextView)findViewById(R.id.response_text);

        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_request:
                sendRequestWithOkHttp();
                break;
        }
    }

    private void sendRequestWithOkHttp() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                // GET
                String responseData = "";

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.baidu.com")
                        .build();

                // 调用 OkHttpClient 的 newCall() 方法来创建一个 Call 对象
                // 并调用他的 excute() 方法来发送请求并获取服务器返回的数据
                try {
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showResponse(responseData);

                // POST
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("username","admin")
//                        .add("password","123456")
//                        .build();
//
//                Request postReq = new Request.Builder()
//                        .url("http://www.baidu.com")
//                        .post(requestBody)
//                        .build();
            }
        }).start();

    }

    private void showResponse(String toString) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(toString);
            }
        });

    }
}