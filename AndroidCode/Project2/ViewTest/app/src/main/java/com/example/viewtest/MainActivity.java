package com.example.viewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.viewtest.gridview.GridViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button grid_view = (Button)findViewById(R.id.gv_btn);
        grid_view.setOnClickListener(this);

        Button recycle_view = (Button)findViewById(R.id.rv_btn);
        recycle_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gv_btn:
                startActivity(new Intent(MainActivity.this, GridViewActivity.class));
                break;
            case R.id.rv_btn:
                startActivity(new Intent(MainActivity.this, GridViewActivity.class));
                break;
        }
    }
}