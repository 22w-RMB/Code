package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.networktest.listener.HttpCallbackListener;
import com.example.networktest.util.HttpUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                String address = "http://www.baidu.com";
                HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        // 这里根据返回内容执行具体过程
                        showResponse(response.toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        // 这里对异常情况进行处理
                    }
                });
//                sendRequestWithHttpURLConnection();
                break;
        }
    }

    private void sendRequestWithHttpURLConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    // GET
                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());


                    // POST 的写法
//                    connection = (HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("POST");
//                    DataOutputStream  out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection!=null)
                        connection.disconnect();
                }

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