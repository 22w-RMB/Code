package com.example.customlayouttest.ativity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.customlayouttest.R;
import com.example.customlayouttest.view.CustomTitleView;

public class CustomTitleActivity extends AppCompatActivity {

    private CustomTitleView customTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_title);

        customTitleView = (CustomTitleView)findViewById(R.id.custom_view_title);
        customTitleView.setTittle("This is Title");
        customTitleView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}