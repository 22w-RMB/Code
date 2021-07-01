package com.example.serverpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.serverpractice.service.DownloadService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DownloadService.DownloadBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadService.DownloadBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPermission();

        Button start = (Button)findViewById(R.id.start_download);
        Button pause = (Button)findViewById(R.id.pause_download);
        Button cancel = (Button)findViewById(R.id.cancel_download);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);   //启动服务
        bindService(intent,connection,BIND_AUTO_CREATE);    // 绑定服务


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_download:
                String url = "http://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                binder.startDownload(url);

                break;
            case R.id.pause_download:
                binder.pauseDownload();
                break;
            case R.id.cancel_download:
                binder.cancelDownload();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }


    public void checkPermission(){
        int targetSDKVersion = 0;
        String[] permissionString = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        try {
            //获取编译该应用使用的版本
            final PackageInfo info = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(),0);
            //获取应用的target版本
            targetSDKVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("err","检查权限");
        }

        //当前的手机系统版本大于6.0
        //Build.VERSION.SDK_INT 是获取当前手机版本
        //Build.VERSION_CODES.M  是安卓6.0系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            //编译该程序的版本大于6.0
            if (targetSDKVersion >= Build.VERSION_CODES.M){
                //第 1 步：   检查是否有相应的权限
                boolean isAllGranted = checkPermissionAllGrangted(permissionString);
                if (isAllGranted){
                    Log.e("err","所有权限已经授权");
                    return;
                }
                //调用申请权限的函数
                //一次请求多个权限，如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(this, permissionString, 1);
            }
        }

    }


    /**
     * 检查是否拥有指定的权限
     */
    private boolean checkPermissionAllGrangted(String[] permissions){
        for (String permission:permissions){
            //判断是否有权限没有申请
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED ){
                //只要有一个权限没被授予，则直接返回false
                Log.e("err","权限 " + permission + " 没有授权");
                return false;
            }
        }
        return true;
    }


    /**
     * 申请权限返回结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==1 ){
            boolean isAllGranted = true;
            //判断是否所有权限都已经授予
            for (int grant:grantResults){
                if (grant != PackageManager.PERMISSION_GRANTED){
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted){
                //所有权限都授予了
                Log.e("err","权限都授予了");
            }else {

                //弹出对话框告诉用户需要权限的原因，并引导用户去应用权限管理中手动打开权限按钮
                //容易判断错，所以注释掉
                //MyDialog("提示","某些权限未打开，请手动开启", 1);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("某些权限需要手动开启");
                builder.setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package",getPackageName(),null));
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        }
    }
}