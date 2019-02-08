package com.msproject.myhome.mydays;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;


public class EventActivity extends AppCompatActivity {
    ListView eventListView;
    EventListAdapter eventListAdapter;
    GridView gridView;
    CategoryGridAdapter categoryGridAdapter;
    ConstraintLayout titleBar;
    String date;
    String content= "";
    int quarterNo;
    MydaysDBHelper myDaysDB = new MydaysDBHelper(this,"MyDays.db",null,1);
    CategoryDBHelper categoryDB = new CategoryDBHelper(this,"CATEGORY.db",null,1);
    ArrayList<Category> categories;
    Category selectedCategory;
    DragEventCallBackListener dragEventCallBackListener;

    private final int RESPONSE_SAVE_CODE = 1;
    private final int RESPONSE_UNSAVE_CODE = 0;

    private final int REQUEST_SETTING_CODE = 3;
    private final int RESPONSE_SETTING_CODE = 4;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent mainIntent = getIntent();
        date = mainIntent.getStringExtra("Date");
        categories = new ArrayList<>();
        eventListView = findViewById(R.id.event_listview);
        gridView  = findViewById(R.id.category_gridview);
        titleBar = findViewById(R.id.title_bar);
        quarterNo = Integer.parseInt(mainIntent.getStringExtra("Hour"));
        setResult(RESPONSE_UNSAVE_CODE);
        setTitleContents(date);
        setCategories();

        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Event> DBEvents = myDaysDB.getEvents(date,quarterNo);


        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            //상단 바 색상 변경
            getWindow().setStatusBarColor(getColor(R.color.colorTitleBar));
        }

        for(int i = 0; i < 6; i++){
            events.add(new Event(quarterNo+i,"",""));
        }

        for(int j=0; j< DBEvents.size();j++){
            events.set(DBEvents.get(j).getEventNo() - quarterNo,DBEvents.get(j));
        }

        eventListAdapter = new EventListAdapter(events, this);

        eventListView.setAdapter(eventListAdapter);
        dragEventCallBackListener = new DragEventCallBackListener() {
            boolean canDrag;
            @Override
            public void setCanDrag(boolean canDrag){
                this.canDrag = canDrag;
            }
            @Override
            public void onDragFinished(ArrayList<Event> events) {
                createDialog(date, events, selectedCategory);
            }

            @Override
            public boolean dragable() {
                return canDrag;
            }
        };
        eventListAdapter.setDragEventCallBackListener(dragEventCallBackListener);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) categoryGridAdapter.getItem(position);
                view.setBackgroundColor(Color.parseColor("#6EA2D5"));
//
                //밖으로 빼야댐

                eventListAdapter.setDragable(true);

            }
        });

        eventListView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SETTING_CODE){
            if(resultCode == RESPONSE_SETTING_CODE){
                setCategories();
                categoryGridAdapter.notifyDataSetChanged();
            }
        }
    }

    public void createDialog(final String date, final ArrayList<Event> events, final Category category){
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
                        for(int i = 0; i < events.size(); i++){
                            myDaysDB.insert(date, events.get(i).getEventNo(), category.getCategoryName(), content);
                            eventListAdapter.setItem(events.get(i).getEventNo() - quarterNo, new Event(events.get(i).getEventNo(), category.getCategoryName(), content));
                        }
                        ArrayList<Event> events = myDaysDB.getResult(date);
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

    public void setTitleContents(String date){

        ImageView backButton = titleBar.findViewById(R.id.back_bt);
        TextView dateView = titleBar.findViewById(R.id.bt_title);
        ImageView menuButton = titleBar.findViewById(R.id.menu_bt);
        date = date.substring(2,4)+"월 "+date.substring(4,6)+"일";
        dateView.setText(date);
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
                            startActivityForResult(intent, REQUEST_SETTING_CODE);

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
                setResult(RESPONSE_SAVE_CODE);
            }
        });
    }

    public void setCategories(){
        categories = categoryDB.getResult();
        categoryGridAdapter = new CategoryGridAdapter(categories,this);
        gridView.setAdapter(categoryGridAdapter);
    }


}
