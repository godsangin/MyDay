package com.msproject.myhome.mydays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    ImageView menuButton;
    PieChart pieChart;
    ConstraintLayout titleBar;
    CalendarDialog calendarDialog;
    Context context;
    MyDialogListener myDialogListener;
    TextView calendarDate;
    int year; // 년도는 view에 존재하지 않기 때문에 변수로 담고 있어야함.
    MydaysDBHelper mydaysDBHelper;
    CategoryDBHelper categoryDBHelper;

    String Default = "";
    String DefaultLabel = "쉬는 시간";

    int i = 0;

    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
    ArrayList<Integer> colors = new ArrayList<>();
    String[] times = new String[24];

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuButton = findViewById(R.id.menu_bt);
        titleBar = findViewById(R.id.title_bar);
        context = this;
        calendarDate = titleBar.findViewById(R.id.calendar_date);
        ImageView lastdayButton = titleBar.findViewById(R.id.bt_lastday);
        ImageView nextdayButton = titleBar.findViewById(R.id.bt_nextday);
        mydaysDBHelper = new MydaysDBHelper(this,"MyDays.db",null,1);
        categoryDBHelper = new CategoryDBHelper(this,"CATEGORY.db",null,1);
        year = 2018;//임시
        setMoveDay(lastdayButton, nextdayButton);
        setCalendarView();
        setTitleContents();

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            //상단 바 색상 변경
            getWindow().setStatusBarColor(getColor(R.color.colorTitleBar));
        }

        pieChart = (PieChart)findViewById(R.id.piechart);

        pieChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                RectF rectF = pieChart.getCircleBox();

                float centerX = rectF.centerX();
                float centerY =rectF.centerY();

                float r = (rectF.right - rectF.left)/2;

                float touchX = motionEvent.getX();
                float touchY = motionEvent.getY();
                if( Math.pow((touchX - centerX),2) + Math.pow((touchY - centerY), 2) <= Math.pow(r,2)){
                    if(centerX > touchX && centerY > touchY){
                        //왼쪽 위
                        addTime(18);
                    }
                    else if(centerX > touchX && centerY < touchY){
                        //왼쪽 아래
                        addTime(12);
                    }
                    else if(centerX < touchX && centerY > touchY) {
                        //오른쪽 위
                        addTime(0);
                    }
                    else{
                        //오른쪽 아래
                        addTime(6);
                    }
                }

                return false;
            }
        });

        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.setHoleColor(Color.WHITE);

        updateChart(false, 0, 0, "", ColorMakeHelper.getColor(null));
        loadEventData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//추가된 이벤트가 있으면 chart를 새로그림 !
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            loadEventData();
        }
        else{

        }
    }

    public void addTime(int index){
        Intent intent = new Intent(MainActivity.this, EventActivity.class);
        intent.putExtra("Hour", index + "");
        LocalDate ld = this.parsingLocalDate(calendarDate.getText().toString());
        Log.d("date==",ld.toString());
        intent.putExtra("Date", ld.toString().replace("-","").substring(2,8));
        Log.d("date==",ld.toString().replace("-",""));
        startActivityForResult(intent, 0);
    }

    public void makeChart(){
//        Description description = new Description();
//        LocalDate localDate = parsingLocalDate(calendarDate.getText().toString());
//        description.setText(year + "." + localDate.getMonthOfYear() + "." + localDate.getDayOfMonth()); //라벨 : 오늘 날짜 적으면 좋을 듯
//        description.setTextSize(15);
//        pieChart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(yValues,"");

        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        Legend legend = pieChart.getLegend();
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        HashSet<String> s = new HashSet<>();

        for(int i = 0; i < 24; i++){
            if(!s.contains(times[i])){
                s.add(times[i]);
                if(times[i].length() == 0){
                    legendEntries.add(new LegendEntry(DefaultLabel,legend.getForm(),legend.getFormSize(),legend.getFormLineWidth(),legend.getFormLineDashEffect(),ColorMakeHelper.getColor(times[i])));
                }
                else{
                    legendEntries.add(new LegendEntry(times[i],legend.getForm(),legend.getFormSize(),legend.getFormLineWidth(),legend.getFormLineDashEffect(),ColorMakeHelper.getColor(times[i])));
                }
            }
        }
        legend.setCustom(legendEntries);
    }
    public void updateChart(Boolean add, int start, int end, String category, int color){
        if(add){
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

            yValues = new_entry;
            colors = new_color;
        }

        else{
            yValues = new ArrayList<>();
            yValues.add(new PieEntry(24,""));
            for(int i = 0; i < 24; i++){
                times[i] = "";
            }
            colors = new ArrayList<>();
            colors.add(color);
        }

        makeChart();
    }

    public void setCalendarView(){//현재 TextView에 존재하는 날짜를 기준으로한 달의 CalendarDialog를 호출

        calendarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog = new CalendarDialog(context, parsingLocalDate(calendarDate.getText().toString()));
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                setCalendarDialogLisetener();//캘린더의 콜백을 받기 위한 리스너
                calendarDialog.show();
                calendarDialog.setCancelable(true);
                calendarDialog.setCanceledOnTouchOutside(true);
                Window window = calendarDialog.getWindow();
                int x = (int)(size.x * 0.8f);
                int y = (int)(size.y * 0.8f);

                window.setLayout(x,y);
            }
        });
    }

    public void setCalendarDialogLisetener(){//CalendarDialog를 호출하고 해당 날짜를 클릭했을때 콜백을 받아 날짜 TextView와 전역변수 year를 수정함.
        myDialogListener = new MyDialogListener() {
            @Override
            public void onPostClicked(Category category) {

            }

            @Override
            public void onModifyClicked(Category category, int index) {

            }

            @Override
            public void onNegativeClicked() {

            }

            @Override
            public void onCalendatItemClicked(LocalDate localDate) {
                calendarDate.setText(localDate.getMonthOfYear() + "월 " + localDate.getDayOfMonth() + "일");
                year = localDate.getYear();
                calendarDialog.dismiss();
            }
        };
        calendarDialog.setDialogListener(myDialogListener);
    }

    public LocalDate parsingLocalDate(String str){//Titlebar의 날짜 텍스트를 localdate로 변환 -> year는 전역변수로 불러옴(year가 변경되는 경우 수정 필요!)
        int month;
        int day;
        String[] parsingMonth = str.split("월 ");
        month = Integer.parseInt(parsingMonth[0]);
        String[] parsingDay = parsingMonth[1].split("일");
        day = Integer.parseInt(parsingDay[0]);
        LocalDate localDate = new LocalDate(year, month, day);
        return localDate;
    }

    public void setMoveDay(ImageView lastdayButton, ImageView nextdayButton){//day movement function
        lastdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate localDate = parsingLocalDate(calendarDate.getText().toString());
                LocalDate lastDate = localDate.minusDays(1);
                if(localDate.getYear() != lastDate.getYear()) year--;
                calendarDate.setText(lastDate.getMonthOfYear() + "월 " + lastDate.getDayOfMonth() + "일");

                updateChart(false, 0, 24, "", ColorMakeHelper.getColor(null));
                loadEventData();

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

                Toast.makeText(getApplicationContext(), "왼쪽", Toast.LENGTH_SHORT).show();
            }
        });

        nextdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate localDate = parsingLocalDate(calendarDate.getText().toString());
                LocalDate nextDate = localDate.plusDays(1);
                if(localDate.getYear() != nextDate.getYear()) year++;
                calendarDate.setText(nextDate.getMonthOfYear() + "월 " + nextDate.getDayOfMonth() + "일");

                updateChart(false, 0, 24, "", ColorMakeHelper.getColor(null));
                loadEventData();

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

                Toast.makeText(getApplicationContext(), "오른쪽", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTitleContents(){//Title(today, menu) Button function
        TextView textView = titleBar.findViewById(R.id.today);

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

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate ld = new LocalDate();
                calendarDate.setText(ld.getMonthOfYear() + "월 " + ld.getDayOfMonth() + "일");
                year = ld.getYear();
                Intent intent = new Intent(MainActivity.this, IntroMainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loadEventData(){//
        //이벤트 추가
        LocalDate ld = this.parsingLocalDate(calendarDate.getText().toString());
        String todayString = ld.toString().replace("-","").substring(2,8);
        Log.d("today==",todayString);
        ArrayList<Event> events = mydaysDBHelper.getResult(todayString);
        if(events.isEmpty()){
            updateChart(true, 0, 24, Default, ColorMakeHelper.getColor(Default));
            return;
        }
        ArrayList<UpdateListItem> updateListItems = new ArrayList<>();
        Event temp = null;
        for(int i = 0; i < events.size(); i++){
            Event myEvent = events.get(i);
            if(myEvent.equals(temp)){
                updateListItems.get(updateListItems.size()-1).end++;
            }
            else{
                String colorString = categoryDBHelper.getColor(myEvent.getCategoryName());
                updateListItems.add(new UpdateListItem(myEvent.getEventNo(), myEvent.getEventNo()+1, myEvent.getCategoryName(), colorString));
            }
            temp = myEvent;
        }

        for(int i = 0; i < updateListItems.size(); i++){
            UpdateListItem updateItem = updateListItems.get(i);
            updateChart(true, updateItem.getStart(), updateItem.getEnd(), updateItem.getCategoryName(), Color.parseColor(updateItem.getCategoryColor()));
        }
    }

    public class UpdateListItem{
        int start;
        int end;
        String categoryName;
        String categoryColor;

        public UpdateListItem(int start, int end, String categoryName, String categoryColor) {
            this.start = start;
            this.end = end;
            this.categoryName = categoryName;
            this.categoryColor = categoryColor;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryColor() {
            return categoryColor;
        }

        public void setCategoryColor(String categoryColor) {
            this.categoryColor = categoryColor;
        }
    }

}
