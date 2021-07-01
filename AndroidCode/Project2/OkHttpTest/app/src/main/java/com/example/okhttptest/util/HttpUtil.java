package com.example.okhttptest.util;


import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {


    /**
     *  okhttp3.Callback 是 OkHttp 库中自带的一个回调接口，
     *  类似于自己编写的 HttpCallbackListener 接口
     *
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void main(String[] args) {
        HttpUtil.sendOkHttpRequest("http://www.baidu.com", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // 在这里对异常进行处理
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // 得到服务器返回的具体内容
                String requestBody = response.body().string();
            }
        });
    }

}
