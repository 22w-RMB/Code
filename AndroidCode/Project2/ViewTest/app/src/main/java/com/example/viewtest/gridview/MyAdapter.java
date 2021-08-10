package com.example.viewtest.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.viewtest.R;


public class MyAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    public MyAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView ==null){
            convertView = inflater.inflate(R.layout.layout_grid_item,null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.iv);
            holder.textView = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder)convertView.getTag();

        holder.textView.setText("花");
        Glide.with(context).load("https://img1.baidu.com/it/u=3156924425,2418415068&fm=26&fmt=auto&gp=0.jpg").into(holder.imageView);

        return convertView;
    }
}
