package com.example.customlayouttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.customlayouttest.ativity.CustomTitleActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button custom_title_btn = (Button)findViewById(R.id.custom_title_btn);
        custom_title_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_title_btn:
                startActivity(new Intent(MainActivity.this, CustomTitleActivity.class));
                break;
        }
    }
}