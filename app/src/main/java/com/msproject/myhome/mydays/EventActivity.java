package com.msproject.myhome.mydays;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;


public class EventActivity extends AppCompatActivity {
    ListView eventListView;
    EventListAdapter eventListAdapter;
    GridView gridView;
    CategoryGridAdapter categoryGridAdapter;
    ConstraintLayout titleBar;
    String time;
    String content= "";
    int quarterNo;
    MydaysDBHelper myDaysDB = new MydaysDBHelper(this,"MyDays.db",null,1);
    CategoryDBHelper categoryDB = new CategoryDBHelper(this,"CATEGORY.db",null,1);

    private final int RESPONSE_SAVE_CODE = 1;
    private final int RESPONSE_UNSAVE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent mainIntent = getIntent();
        time = mainIntent.getStringExtra("Date");
        eventListView = findViewById(R.id.event_listview);
        gridView  = findViewById(R.id.category_gridview);
        titleBar = findViewById(R.id.title_bar);
        quarterNo = Integer.parseInt(mainIntent.getStringExtra("Hour"));
        setResult(RESPONSE_UNSAVE_CODE);

        setTitleContents();


        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Event> DBEvents = myDaysDB.getEvents(time,quarterNo);
        ArrayList<Category> categories = categoryDB.getResult();

        for(int i = 0; i < 6; i++){
            events.add(new Event(quarterNo+i,"",""));
        }

        for(int j=0; j< DBEvents.size();j++){
            events.set(DBEvents.get(j).getEventNo() - quarterNo,DBEvents.get(j));
        }



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
                             Event clickedEvent = (Event) eventListAdapter.getItem(position);
                             int eventNo = clickedEvent.eventNo;
                             dialog(time,eventNo,category);

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
                            Log.d("Event==",events.get(i).toString());
                        }
                        eventListAdapter.setItem(eventNo-quarterNo, new Event(eventNo,category.getCategoryName(),content));
                        eventListAdapter.notifyDataSetChanged();
                        setResult(RESPONSE_SAVE_CODE);
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

    public void setTitleContents(){
        ImageView backButton = titleBar.findViewById(R.id.back_bt);
        ImageView menuButton = titleBar.findViewById(R.id.menu_bt);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭시 팝업 메뉴가 나오게 하기
                // PopupMenu 는 API 11 레벨부터 제공한다
                PopupMenu p = new PopupMenu(
                        getApplicationContext(), // 현재 화면의 제어권자
                        v); // anchor : 팝업을 띄울 기준될 위젯
                getMenuInflater().inflate(R.menu.menu_main, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.statistic_graph) {//StatisicActivity로 intent
                            Intent intent = new Intent(EventActivity.this, StatisticActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.setting) {
                            Intent intent = new Intent(EventActivity.this, SettingActivity.class);
                            startActivity(intent);

                        } else if (item.getItemId() == R.id.remove_ad) {//광고제거

                        }
                        return false;
                    }
                });
                p.show(); // 메뉴를 띄우기
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
