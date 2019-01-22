package com.msproject.myhome.mydays;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;


public class EventActivity extends AppCompatActivity {

    private SQLiteDatabase init_database(){
        SQLiteDatabase DB = null;

        File file = new File(getFilesDir(),"MyDays.db");

        try {
            DB = SQLiteDatabase.openOrCreateDatabase(file, null);
        }catch(SQLiteException e){
            e.printStackTrace();
        }
        if(DB!=null){
            String createTableCategory = "create TABLE IF NOT EXISTS category("+
                    "'categoryName'" + " TEXT,"+
                    "'color'" + " TEXT," +
                    "PRIMARY KEY('categoryName'));";//#은 자바에서 처리
            String createTableMyDays = "create table IF NOT EXISTS MyDays(" +
                    "'date'" + " TEXT NOT NULL," +
                    "'eventNo'"+" INTEGER NOT NULL," +
                    "'categoryName'"+ " TEXT,"+
                    "'eventContent'"+ " TEXT," +
                    "PRIMARY KEY('date','eventNo'));";

            DB.execSQL(createTableCategory);
            DB.execSQL(createTableMyDays);
        }else {
            System.out.println("데이터베이스가 존재하지 않습니다");
        }
        return DB;
    }
    //Cursor 해결
    private void setTable(SQLiteDatabase DB,String date,int quarterNo){
        TextView eventTime0 = findViewById(R.id.eventTime0);
        TextView eventTime1 = findViewById(R.id.eventTime1);
        TextView eventTime2 = findViewById(R.id.eventTime2);
        TextView eventTime3 = findViewById(R.id.eventTime3);
        TextView eventTime4 = findViewById(R.id.eventTime4);
        TextView eventTime5 = findViewById(R.id.eventTime5);

        int startEventNo = -1;
        int EndEventNo = -1;

        //4분할중 선택된 quarter 구분
        switch (quarterNo){
            case 1:
                eventTime0.setText("00:00 ~ 01:00");
                eventTime1.setText("01:00 ~ 02:00");
                eventTime2.setText("02:00 ~ 03:00");
                eventTime3.setText("03:00 ~ 04:00");
                eventTime4.setText("04:00 ~ 05:00");
                eventTime5.setText("05:00 ~ 06:00");
                startEventNo = 0;
                EndEventNo = 5;
                break;
            case 2:
                eventTime0.setText("06:00 ~ 07:00");
                eventTime1.setText("07:00 ~ 08:00");
                eventTime2.setText("08:00 ~ 09:00");
                eventTime3.setText("09:00 ~ 10:00");
                eventTime4.setText("10:00 ~ 11:00");
                eventTime5.setText("11:00 ~ 12:00");
                startEventNo = 6;
                EndEventNo = 11;
                break;
            case 3:
                eventTime0.setText("12:00 ~ 13:00");
                eventTime1.setText("13:00 ~ 14:00");
                eventTime2.setText("14:00 ~ 15:00");
                eventTime3.setText("15:00 ~ 16:00");
                eventTime4.setText("16:00 ~ 17:00");
                eventTime5.setText("17:00 ~ 18:00");
                startEventNo = 12;
                EndEventNo = 17;
                break;
            case 4:
                eventTime0.setText("18:00 ~ 19:00");
                eventTime1.setText("19:00 ~ 20:00");
                eventTime2.setText("20:00 ~ 21:00");
                eventTime3.setText("21:00 ~ 22:00");
                eventTime4.setText("22:00 ~ 23:00");
                eventTime5.setText("23:00 ~ 24:00");
                startEventNo = 18;
                EndEventNo = 23;
                break;

        }
        String sqlSelect = "select * from MyDays where date="+'"'+date+'"'+" and eventNo between "+ startEventNo+ " and "+EndEventNo;
        Cursor cursor = DB.rawQuery(sqlSelect,null);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        SQLiteDatabase DB = init_database();

        Intent MainIntent = getIntent();
        String date = MainIntent.getStringExtra("date");
        int quarterNo = MainIntent.getIntExtra("quarterNo",-1);

    }


}
