package com.msproject.myhome.mydays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {//WorkManager사용?..

    private boolean isStop;
    int count;
    int startTime;
    public boolean callback;
    Thread counter;
    final int APPLICATION_ID = 12982;
    SharedPreferences sharedPreferences;
    boolean threadRunning;
    WiseSaying wiseSaying;
    Saying todaySaying;

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
        Log.d("service==", "unbind");
        return super.onUnbind(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!threadRunning){
            counter = new Thread(new Counter());
            counter.start();
            threadRunning = true;
        }

        if (intent != null) {
            int startTime = intent.getIntExtra("startTime", -1);
            int count = intent.getIntExtra("count", -1);
            boolean isMain = intent.getBooleanExtra("main", false);
            if (startTime != -1 && count != -1) {
                this.startTime = startTime;
                this.count = count;
            }
            startForeground(1, new Notification());
            Log.d("intent==", "main");
            /**
             * startForeground 를 사용하면 notification 을 보여주어야 하는데 없애기 위한 코드
             */
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification;
            Log.d("count==", todaySaying.getSay());
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(todaySaying.getSay())
                    .setContentText(" - " + todaySaying.getSource())
                    .setSmallIcon(R.drawable.logo3)
                    .build();


            nm.notify(startId, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service==", "create");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
        String formatDate = sdfNow.format(date);
        String[] split = formatDate.split(":");
        startTime = Integer.parseInt(split[0]);
        wiseSaying = WiseSaying.getInstance();
        todaySaying = wiseSaying.getWiseSay();
        unregisterRestartAlarm();

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        boolean background = sharedPreferences.getBoolean("background", false);
        boolean push = sharedPreferences.getBoolean("push", false);
        if(!background){
            stopSelf();
        }
        if(push){
            new AlarmHATT(getApplicationContext()).alarm();
        }
        counter = new Thread(new Counter());
        counter.start();
        threadRunning = true;
    }

    @Override
    public void onDestroy() {
        Log.d("service==", "destroy");
        counter.interrupt();
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        boolean background = sharedPreferences.getBoolean("background", false);
        Log.d("background==", background + "");
        if(background && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            registerRestartAlarm();
        }
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void registerRestartAlarm(){
        Log.d("restartAl==", "true");
        Intent intent = new Intent(MyService.this, MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_RESTART_PERSISTENTSERVICE);
        intent.putExtra("startTime", startTime);
        intent.putExtra("count", count + (10 * 3));
        PendingIntent sender = PendingIntent.getBroadcast(MyService.this, APPLICATION_ID, intent, 0);
        Calendar restart = Calendar.getInstance();
        restart.setTimeInMillis(System.currentTimeMillis());
        restart.add(Calendar.MINUTE, 10);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10 * 60 * 1000,  sender);//doze모드에서도 정상작동하기위함 10분 뒤 서비스 재시작
        Log.d("startcount==", startTime + " " + count);
    }

    public void unregisterRestartAlarm(){
        Log.d("restartAl==", "false");
        Intent intent = new Intent(MyService.this, MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(MyService.this, APPLICATION_ID,intent,0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sleepingEnd(){//notification발생 && 쓰레드멈춤..?
        int endTime = startTime + (count / 180);
        Log.d("savestartTime==", startTime + "");
        if(endTime > 24){
            endTime -= 24;
        }
        if(count % 180 >= 90){
            count += 180;
        }
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("startTime", startTime);
        resultIntent.putExtra("endTime", endTime);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent mPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
        //notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.logo3)
                .setContentTitle("수면시간을 등록해보세요.")
                .setContentText(startTime + "시부터 " + (startTime + count/180) + "시까지 잠을 잤나요?")//360
                .setLargeIcon(mLargeIconForNoti)
                .setAutoCancel(true)
                .setContentIntent(mPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }

    private class Counter implements Runnable{
        boolean duplicate;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            while(!isStop){
                Log.d("count==", count + "");
                Log.d("startTime==", startTime + "");
                PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                Log.d("ScreenOn==", isScreenOn + "");
                if(isScreenOn){
                    if(duplicate && count < 540){//3시간=1080
                        count = 0;
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                        String formatDate = sdfNow.format(date);
                        String[] split = formatDate.split(":");
                        startTime = Integer.parseInt(split[0]);
                        if(Integer.parseInt(split[1]) > 30){//30분이상이면 다음시간으로 취급(hour)
                            startTime++;
                        }
                        callback = true;
                    }
                    else if(duplicate && count >= 540){
                        sleepingEnd();
                        count= 0;
                    }
                    duplicate = true;
                }
                else{
                    duplicate = false;
                    callback = false;
                }
                try {
                    Thread.sleep(20000);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class AlarmHATT{
        private Context context;
        public AlarmHATT(Context context){
            this.context = context;
        }

        public void alarm(){
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MyService.this, BroadcastD.class);
            PendingIntent sender = PendingIntent.getBroadcast(MyService.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 0, 0);

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            todaySaying = WiseSaying.getInstance().getWiseSay();
        }
    }
}
