package com.msproject.myhome.mydays;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    ImageView menuButton;
    PieChart pieChart;
    ConstraintLayout titleBar;
    CalendarDialog calendarDialog;
    Context context;


    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
    ArrayList<Integer> colors = new ArrayList<>();
    String[] times = new String[24];

    LinearLayout time_1, time_2, time_3, time_4;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuButton = findViewById(R.id.menu_bt);
        titleBar = findViewById(R.id.title_bar);
        context = this;

        LinearLayout calendarButton = titleBar.findViewById(R.id.calendar_bt);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog = new CalendarDialog(context);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
//                calendarDialog.setDialogListener(myDialogListener);
                calendarDialog.show();
                calendarDialog.setCancelable(true);
                Window window = calendarDialog.getWindow();
                int x = (int)(size.x * 0.8f);
                int y = (int)(size.y * 0.8f);

                window.setLayout(x,y);
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            //상단 바 색상 변경
            getWindow().setStatusBarColor(getColor(R.color.colorTitleBar));
        }

        time_1 = findViewById(R.id.time_1);
        time_2 = findViewById(R.id.time_2);
        time_3 = findViewById(R.id.time_3);
        time_4 = findViewById(R.id.time_4);

        time_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(1);
            }
        });
        time_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(2);
            }
        });
        time_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(3);
            }
        });
        time_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(4);
            }
        });

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
                            Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.setting) {
                            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivity(intent);

                        } else if (item.getItemId() == R.id.remove_ad) {//광고제거

                        }
                        return false;
                    }
                });
                p.show(); // 메뉴를 띄우기
            }
        });


        pieChart = (PieChart)findViewById(R.id.piechart);

        //pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
//        pieChart.setTransparentCircleRadius(61f);


        for(int i = 0; i < 24; i++){
            times[i] = "default";
        }
        yValues.add(new PieEntry(24,""));
        ColorMakeHelper.setColor("default", Color.LTGRAY);
        colors.add(Color.LTGRAY);

        updateChar(false, 0,0,null, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0){
            if(data.getStringExtra("category") != null) {
                updateChar(true, data.getIntExtra("start", 0), data.getIntExtra("end", 0), data.getStringExtra("category"), data.getIntExtra("color", 0));
            }
        }
        else{

        }
    }

    public void addTime(int index){
//        Bundle b = new Bundle();
//        b.putInt("hour",index);

        Intent intent = new Intent(MainActivity.this, InputActivity.class);
        intent.putExtra("Hour", index);
        startActivityForResult(intent, 0);
    }

    public void makeChart(){
//        yValues.add(new PieEntry(1f,"기본"));
//        yValues.add(new PieEntry(1f,"기본_2"));


        Description description = new Description();
        description.setText("2019.01.17"); //라벨 : 오늘 날짜 적으면 좋을 듯
        description.setTextSize(15);
        pieChart.setDescription(description);


        PieDataSet dataSet = new PieDataSet(yValues,"");

//        dataSet.setSliceSpace(3f);
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
//        legend.setCustom(Color.YELLOW, new String[]{"씨발"});
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        HashSet<String> s = new HashSet<>();

        for(int i = 0; i < 24; i++){
            if(!s.contains(times[i])){
                s.add(times[i]);
                legendEntries.add(new LegendEntry(times[i],legend.getForm(),legend.getFormSize(),legend.getFormLineWidth(),legend.getFormLineDashEffect(),ColorMakeHelper.getColor(times[i])));
            }
        }
        legend.setCustom(legendEntries);
    }
    public void updateChar(Boolean add, int start, int end, String category, int color){
        if(add){
            int e_start = 0;
            int e_end = 0;
            int a_start = start;
            int a_end = end;
            ArrayList<PieEntry> new_entry = new ArrayList<>();
            ArrayList<Integer> new_color = new ArrayList<>();

            ColorMakeHelper.setColor(category, color);
            if(start < end) {
                for (int i = start; i < end; i++) {
                    times[i] = category;
                }
            }
            else{
                for (int i = start; i < 24; i++) {
                    times[i] = category;
                }
                for(int i = 0; i < end; i++){
                    times[i] = category;
                }
            }
            PieEntry e = new PieEntry(1, times[0]);
            for(int i = 1; i < 23; i++){
                if(e.getLabel().equals(times[i])){
                    e.setY(e.getValue() + 1);
                }
                else{
                    new_entry.add(e);
                    new_color.add(ColorMakeHelper.getColor(e.getLabel()));
                    e = new PieEntry(1, times[i]);
                }
            }

            if(e.getLabel().equals(times[23])){
                e.setY(e.getValue() + 1);
                new_entry.add(e);
                new_color.add(ColorMakeHelper.getColor(e.getLabel()));
            }
            else{
                new_entry.add(e);
                new_color.add(ColorMakeHelper.getColor(e.getLabel()));
                e = new PieEntry(1, times[23]);
                new_entry.add(e);
                new_color.add(ColorMakeHelper.getColor(e.getLabel()));
            }

//            PieEntry e = new_entry.get(0);
//            PieEntry temp;
//            for(int i = 1; i < new_entry.size(); i++){
//                temp = new_entry.get(i);
//                if(e.getLabel().equals(temp.getLabel())){
//                    new_entry.remove(i);
//                    colors.remove(i);
//                    e.setY(e.getValue() + temp.getValue());
//                    i--;
//                }
//                e = new_entry.get(i);
//            }
            yValues = new_entry;
            colors = new_color;
        }

        makeChart();
    }

}
