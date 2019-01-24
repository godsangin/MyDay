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
    int quarterNo;
    MydaysDBHelper myDaysDB = new MydaysDBHelper(this,"MyDays.db",null,1);
    CategoryDBHelper categoryDB = new CategoryDBHelper(this,"CATEGORY.db",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent MainIntent = getIntent();
        date = MainIntent.getStringExtra("date");
//        quarterNo = MainIntent.getIntExtra("quarterNo",-1);
        quarterNo=0;
        eventListView = findViewById(R.id.event_listview);
        gridView  = findViewById(R.id.category_gridview);


        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Event> DBEvents = myDaysDB.getEvents(date,quarterNo);
        ArrayList<Category> categories = categoryDB.getResult();

        for(int i = 0; i < 6; i++){
            events.add(new Event(quarterNo+i,"",""));
        }

        for(int j=0; j< DBEvents.size();j++){
            events.set(DBEvents.get(j).getEventNo(),DBEvents.get(j));
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
                            Log.d("Event==",events.get(i).toString());
                        }
                        eventListAdapter.setItem(eventNo-quarterNo, new Event(eventNo,category.getCategoryName(),content));
                        eventListAdapter.notifyDataSetChanged();
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
