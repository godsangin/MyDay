package com.msproject.myhome.mydays;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;


public class EventActivity extends AppCompatActivity {
    ListView eventListView;
    EventListAdapter eventListAdapter;
    GridView gridView;
    CategoryGridAdapter categoryGridAdapter;
    String date;
    String content= "";
    MydaysDBHelper myDaysDB = new MydaysDBHelper(this,"MyDays.db",null,1);
    CategoryDBHelper categoryDB = new CategoryDBHelper(this,"CATEGORY.db",null,1);
//    private SQLiteDatabase init_database(){
//        SQLiteDatabase DB = null;
//
//        File file = new File(getFilesDir(),"MyDays.db");
//
//        try {
//            DB = SQLiteDatabase.openOrCreateDatabase(file, null);
//        }catch(SQLiteException e){
//            e.printStackTrace();
//        }
//        if(DB!=null){
//            String createTableCategory = "create TABLE IF NOT EXISTS category("+
//                    "'categoryName'" + " TEXT,"+
//                    "'color'" + " TEXT," +
//                    "PRIMARY KEY('categoryName'));";//#은 자바에서 처리
//            String createTableMyDays = "create table IF NOT EXISTS MyDays(" +
//                    "'date'" + " TEXT NOT NULL," +
//                    "'eventNo'"+" INTEGER NOT NULL," +
//                    "'categoryName'"+ " TEXT,"+
//                    "'eventContent'"+ " TEXT," +
//                    "PRIMARY KEY('date','eventNo'));";
//
//            DB.execSQL(createTableCategory);
//            DB.execSQL(createTableMyDays);
//        }else {
//            System.out.println("데이터베이스가 존재하지 않습니다");
//        }
//        return DB;
//    }
//    //Cursor 해결
//    private void setTable(SQLiteDatabase DB,String date,int quarterNo){
//        TextView eventTime0 = findViewById(R.id.eventTime0);
//        TextView eventTime1 = findViewById(R.id.eventTime1);
//        TextView eventTime2 = findViewById(R.id.eventTime2);
//        TextView eventTime3 = findViewById(R.id.eventTime3);
//        TextView eventTime4 = findViewById(R.id.eventTime4);
//        TextView eventTime5 = findViewById(R.id.eventTime5);
//
//        int startEventNo = -1;
//        int EndEventNo = -1;
//
//        //4분할중 선택된 quarter 구분
//        switch (quarterNo){
//            case 1:
//                eventTime0.setText("00:00 ~ 01:00");
//                eventTime1.setText("01:00 ~ 02:00");
//                eventTime2.setText("02:00 ~ 03:00");
//                eventTime3.setText("03:00 ~ 04:00");
//                eventTime4.setText("04:00 ~ 05:00");
//                eventTime5.setText("05:00 ~ 06:00");
//                startEventNo = 0;
//                EndEventNo = 5;
//                break;
//            case 2:
//                eventTime0.setText("06:00 ~ 07:00");
//                eventTime1.setText("07:00 ~ 08:00");
//                eventTime2.setText("08:00 ~ 09:00");
//                eventTime3.setText("09:00 ~ 10:00");
//                eventTime4.setText("10:00 ~ 11:00");
//                eventTime5.setText("11:00 ~ 12:00");
//                startEventNo = 6;
//                EndEventNo = 11;
//                break;
//            case 3:
//                eventTime0.setText("12:00 ~ 13:00");
//                eventTime1.setText("13:00 ~ 14:00");
//                eventTime2.setText("14:00 ~ 15:00");
//                eventTime3.setText("15:00 ~ 16:00");
//                eventTime4.setText("16:00 ~ 17:00");
//                eventTime5.setText("17:00 ~ 18:00");
//                startEventNo = 12;
//                EndEventNo = 17;
//                break;
//            case 4:
//                eventTime0.setText("18:00 ~ 19:00");
//                eventTime1.setText("19:00 ~ 20:00");
//                eventTime2.setText("20:00 ~ 21:00");
//                eventTime3.setText("21:00 ~ 22:00");
//                eventTime4.setText("22:00 ~ 23:00");
//                eventTime5.setText("23:00 ~ 24:00");
//                startEventNo = 18;
//                EndEventNo = 23;
//                break;
//
//        }
//        String sqlSelect = "select * from MyDays where date="+'"'+date+'"'+" and eventNo between "+ startEventNo+ " and "+EndEventNo;
//        Cursor cursor = DB.rawQuery(sqlSelect,null);
//
//
//
//    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
//        SQLiteDatabase DB = init_database();

        Intent MainIntent = getIntent();
        date = MainIntent.getStringExtra("date");
        int quarterNo = MainIntent.getIntExtra("quarterNo",-1);
        eventListView = findViewById(R.id.event_listview);
        gridView  = findViewById(R.id.category_gridview);


        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Category> categories = categoryDB.getResult();
        events.add(new Event(1, "공부", "수학"));
        events.add(new Event(2, "공부", "수학"));
        events.add(new Event(3, "운동", "등운동"));
        events.add(new Event(4, "운동", "하체운동"));
        events.add(new Event(5, "휴식", "게임"));
        events.add(new Event(6, "휴식", "게임"));
//        events.add(new Event("07:00", "휴식", "잠"));
//        events.add(new Event("08:00", "휴식", "잠"));


        eventListAdapter = new EventListAdapter(events, this);

        eventListView.setAdapter(eventListAdapter);

        categoryGridAdapter = new CategoryGridAdapter(categories,this);
        gridView.setAdapter(categoryGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Category category = (Category) categoryGridAdapter.getItem(position);
                eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                             date="190121";
                             Event clickedEvent = (Event) eventListAdapter.getItem(position);
                             int eventNo = clickedEvent.eventNo;
                             dialog(date,eventNo,category);

                         }
                    }
                );
            }
        });
    }

    public void dialog(final String date, final int eventNo, final Category category){
        final EditText contentEdit = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Event 내용 입력");
        builder.setMessage("세부내용을 입력해주세요");
        builder.setView(contentEdit);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        content = contentEdit.getText().toString();
                        myDaysDB.insert(date,eventNo,category.getCategoryName(),content);
                        ArrayList<Event> events = myDaysDB.getResult(date);
                        for(int i=0; i < events.size();i++){
                            Log.d("Event==",events.get(i).getEventContent());
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }



}
