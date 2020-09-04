package com.msproject.myhome.mydays;

import android.content.Context;
import android.os.Build;;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    ConstraintLayout titleBar;
    RecyclerView recyclerView;
    Context context;
    private final int CATEGORY_RESULT_CODE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.colorTitleBar));
        }


        titleBar = (ConstraintLayout) findViewById(R.id.title_bar);
        recyclerView = (RecyclerView) findViewById(R.id.setting_recycler_view);
        context = this;
        setResult(CATEGORY_RESULT_CODE);//수정확인위함

        backButtonEnable();

        ArrayList<SettingItem> mItems = setItems();

        SettingRecyclerAdapter settingRecyclerAdapter = new SettingRecyclerAdapter(mItems, context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(settingRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void backButtonEnable(){
        ImageView backButton = titleBar.findViewById(R.id.back_bt);
        ImageView menuButton = titleBar.findViewById(R.id.menu_bt);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menuButton.setVisibility(View.GONE);
    }

    public ArrayList<SettingItem> setItems(){//설정에 필요한게 더 생길 경우 추가
        ArrayList<SettingItem> mItems = new ArrayList<>();

        mItems.add(new SettingItem("테마 설정", "어플리케이션 배경의 색상, 패턴, 디자인 등을 변경할 수 있습니다."));
        mItems.add(new SettingItem("기능 설정", "백그라운드 작업 또는 푸시알림 등의 기능을 변경할 수 있습니다."));

        return mItems;
    }


}
