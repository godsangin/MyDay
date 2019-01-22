package com.msproject.myhome.mydays;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity {

    PieChart pieChart;

    Button btn_daily, btn_weekly, btn_monthly, btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        btn_daily = (Button) findViewById(R.id.btn_daily);
        btn_weekly = (Button) findViewById(R.id.btn_weekly);
        btn_monthly = (Button) findViewById(R.id.btn_monthly);

        btn_exit = findViewById(R.id.exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pieChart = (PieChart)findViewById(R.id.piechart);


        btn_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_chart(0);
            }
        });
        btn_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_chart(1);
            }
        });
        btn_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_chart(2);
            }
        });

        btn_daily.callOnClick();
    }

    public void show_chart(int i){
        switch (i){
            case 0://daily
                make_chart(new ArrayList<PieEntry>(), "일간");
                break;
            case 1://weekly
                make_chart(new ArrayList<PieEntry>(), "주간");
                break;
            case 2://monthly
                make_chart(new ArrayList<PieEntry>(), "월간");
                break;
        }
    }

    public void make_chart(ArrayList<PieEntry> pie_entry, String data_set){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setTouchEnabled(false);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);

        // 데이터 넣을 부분
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(1f,"운동"));
        yValues.add(new PieEntry(1f,"휴식"));
        yValues.add(new PieEntry(1f,"공부"));
        //

        int[] myColor = ColorTemplate.JOYFUL_COLORS;

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
        PieDataSet dataSet = new PieDataSet(yValues,data_set);
        dataSet.setSliceSpace(0.1f); // 간격
        dataSet.setSelectionShift(5f);

        dataSet.setColors(myColor);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
    }
}
