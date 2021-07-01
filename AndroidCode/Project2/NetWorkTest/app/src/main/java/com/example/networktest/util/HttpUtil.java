 package com.example.networktest.util;

import com.example.networktest.listener.HttpCallbackListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {


    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
        // 开启线程执行耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader br = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    // GET 的写法
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    InputStream is = connection.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=br.readLine())!=null){
                        response.append(line);
                    }


                    // POST 的写法
//                    connection = (HttpURLConnection)url.openConnection();
//                    connection.setRequestMethod("POST");
//                    DataOutputStream  out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");

                    if (listener!=null){
                        // 回调 onFinish() 方法
                        listener.onFinish(response.toString());
                    }
                } catch (IOException e) {
                    if (listener!=null){
                        // 回调 onError() 方法
                        listener.onError(e);
                    }
                } finally {
                    if (br!=null){
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection !=null)
                        connection.disconnect();
                }
            }
        }).start();

    }

    public static void main(String[] args) {
        String address = "http://www.baidu.com";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                // 这里根据返回内容执行具体过程
                System.out.println(response);
            }

            @Override
            public void onError(Exception e) {
                // 这里对异常情况进行处理
            }
        });

    }



}
