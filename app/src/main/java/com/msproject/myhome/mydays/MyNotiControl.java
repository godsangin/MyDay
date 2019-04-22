package com.msproject.myhome.mydays;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class MyNotiControl {
    Context context;
    RemoteViews rView;
    NotificationCompat.Builder nBuilder;
    Notification notification;

    public MyNotiControl (Context parent)
    {
        this.context= parent;

        nBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("GUNMAN SERVICE")
                .setSmallIcon(R.drawable.logo3)
                .setPriority(Notification.PRIORITY_MIN) //요 부분이 핵심입니다. MAX가 아닌 MIN을 줘야 합니다.
                .setOngoing(true);

        rView = new RemoteViews(parent.getPackageName(), R.layout.activity_my_noti_control); //노티바를 내렸을때 보여지는 화면입니다.

        //set the listener
        setListener(rView);
        nBuilder.setContent(rView);

        notification = nBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyNotiControl(Context parent, String NOTIFICATION_CHANNEL_ID, String channelName){//Oreo버전 이상의 서비스를 위해 notification channel 추가
        this.context= parent;
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        MyNotiControl c1 = new MyNotiControl(parent);
        nBuilder = new NotificationCompat.Builder(parent, NOTIFICATION_CHANNEL_ID);
        notification = nBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.logo3)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setProgress(0,0,true)
                .build();
        rView = new RemoteViews(parent.getPackageName(), R.layout.activity_my_noti_control); //노티바를 내렸을때 보여지는 화면입니다.

        //set the listener
        setListener(rView);
        nBuilder.setContent(rView);
        notification = nBuilder.build();
    }

    public Notification getNoti()
    {
        return notification;
    }

    public void setListener(RemoteViews view){
        Intent i = new Intent(context, MyNotiControlActivity.class);
        WiseSaying wiseSaying = WiseSaying.getInstance();
        view.setTextViewText(R.id.wise_title, wiseSaying.getWiseSay().getSay());
        view.setTextViewText(R.id.wise_who, wiseSaying.getWiseSay().getSource());
        PendingIntent button = PendingIntent.getActivity(context, 0, i, 0);
        view.setOnClickPendingIntent(R.id.wise_title, button);
    }
}
