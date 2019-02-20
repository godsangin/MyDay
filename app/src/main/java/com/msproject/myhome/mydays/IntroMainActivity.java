package com.msproject.myhome.mydays;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroMainActivity extends AppCompatActivity {
    int count;
    ImageView imageFirst;
    TextView textFirst;
    ImageView imageSecond;
    TextView textSecond;
    ImageView imageThird;
    TextView textThird;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_main);

        imageFirst = findViewById(R.id.mouse_img);
        textFirst = findViewById(R.id.intro_text);
        imageSecond = findViewById(R.id.mouse_img2);
        textSecond = findViewById(R.id.intro_text2);
        imageThird = findViewById(R.id.mouse_img3);
        textThird = findViewById(R.id.intro_text3);
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.mouse_focus);
        imageFirst.startAnimation(animation);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            //상단 바 색상 변경
            getWindow().setStatusBarColor(getColor(R.color.colorTitleBar));
        }

        imageFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFirst.clearAnimation();
                imageFirst.setVisibility(View.GONE);
                textFirst.setVisibility(View.GONE);
                imageSecond.setVisibility(View.VISIBLE);
                textSecond.setVisibility(View.VISIBLE);
                imageSecond.startAnimation(animation);

            }
        });

        imageSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSecond.clearAnimation();
                imageSecond.setVisibility(View.GONE);
                textSecond.setVisibility(View.GONE);
                imageThird.setVisibility(View.VISIBLE);
                textThird.setVisibility(View.VISIBLE);
                imageThird.startAnimation(animation);
            }
        });

        imageThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
