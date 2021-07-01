package com.example.customlayouttest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customlayouttest.R;

public class CustomTitleView extends FrameLayout implements View.OnClickListener {

    private View.OnClickListener mLeftOnClickListener;
    private Button mBackBtn;
    private TextView mTittleView;


    public CustomTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_title_view,this);
        mBackBtn = findViewById(R.id.btn_left);
        mBackBtn.setOnClickListener(this);
        mTittleView = findViewById(R.id.title_tv);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                if (mLeftOnClickListener !=null){
                    mLeftOnClickListener.onClick(v);
                }
        }
    }


    public void setLeftOnClickListener(View.OnClickListener leftOnClickListener) {
        mLeftOnClickListener = leftOnClickListener;
    }

    public void setTittle(String title){
        mTittleView.setText(title);
    }
}
