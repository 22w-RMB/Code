package com.example.lockscreen.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.lockscreen.R;
import com.example.lockscreen.activity.DetailActivity;
import com.example.lockscreen.receiver.LockScreenReceiver;

public class LockScreenService extends Service {

    private LockScreenReceiver receiver;
    private IntentFilter filter = new IntentFilter();
    private boolean isNotiShow = false;


    public LockScreenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_TIME_TICK);

        filter.setPriority(Integer.MAX_VALUE);

        if (receiver == null){
            receiver = new LockScreenReceiver();
            filter.setPriority(Integer.MAX_VALUE);
            registerReceiver(receiver,filter);
            buildNotification();
            Toast.makeText(getApplicationContext(),"开启成功",Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }

    private void buildNotification() {
        if (!isNotiShow){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            String channelId = "c_0001";
            String name = "通知";

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(channelId,name,NotificationManager.IMPORTANCE_LOW);
                manager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(this, DetailActivity.class);

            PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(getApplicationContext(),channelId)
                    .setTicker("app正在运行")
                    .setAutoCancel(false)
                    .setContentTitle("app正在运行")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pi)
                    .build();
            manager.notify(1,notification);
            startForeground(1,notification);
            isNotiShow = true;

        }

    }
}