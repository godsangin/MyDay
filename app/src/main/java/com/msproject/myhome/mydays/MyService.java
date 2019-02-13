package com.msproject.myhome.mydays;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

    private boolean isStop;
    int count;
    int startTime;
    public boolean callback;

    IMySleepCountService.Stub binder = new IMySleepCountService.Stub() {
        @Override
        public int getCount() throws RemoteException{
            return count;
        }

        @Override
        public int getStart() {
            return startTime;
        }

        @Override
        public boolean getCallback() throws RemoteException {
            return callback;
        }
    };


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("service==", "bind");
        return binder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        isStop = true;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service==", "create");

        Thread counter = new Thread(new Counter());
        counter.start();
    }

    @Override
    public void onDestroy() {
        Log.d("service==", "destroy");
        isStop = true;
        super.onDestroy();
    }

    public void sleepingEnd(){//notification발생 && 쓰레드멈춤..?
        callback = true;
    }

    private class Counter implements Runnable{
        boolean duplicate;

        @Override
        public void run() {
            while(!isStop){
                Log.d("count==", count + "");
                PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                Log.d("ScreenOn==", isScreenOn + "");
                if(isScreenOn){
                    if(duplicate && count < 3){//3시간=1080
                        count = 0;
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                        String formatDate = sdfNow.format(date);
                        String[] split = formatDate.split(":");
                        startTime = Integer.parseInt(split[0]);
                        if(Integer.parseInt(split[1]) > 30){
                            startTime++;
                        }
                        callback = true;
                    }
                    else if(duplicate && count >= 3){
                        sleepingEnd();
                        break;
                    }
                    duplicate = true;
                }
                else{
                    duplicate = false;
                    callback = false;
                }
                try {
                    Thread.sleep(10000);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
