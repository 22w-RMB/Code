package com.example.okhttptest.listener;

/**
 *
 *  回调的接口
 *
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
