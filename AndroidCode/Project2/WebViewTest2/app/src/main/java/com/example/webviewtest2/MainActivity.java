package com.example.webviewtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.web_view);
        // getSettings()：可以去设置一些浏览器属性
        // setJavaScriptEnabled(true)：设置webview 支持JavaScript 脚本
        webView.getSettings().setJavaScriptEnabled(true);
        // setWebViewClient(new WebViewClient()) ：
        // 当需要一个网页跳转到另外一个网页时，我们希望的目标网页仍在当前WebView中显示，而不是打开系统浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.baidu.com");

    }
}