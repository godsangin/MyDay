package com.msproject.myhome.mydays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class SetCategoryActivity extends AppCompatActivity implements ColorPickerDialogListener {
    ConstraintLayout titleBar;
    GridView gridView;
    FloatingActionButton fab;
    Context context;
    private final int DIALOG_ID = 0;
    UpdateCategoryDialog Udialog;
    CategoryDBHelper dbHelper;
    CategoryGridAdapter gridAdapter;
    ArrayList<Category> categories;
    final CharSequence[] items = {"색상 변경", "삭제", "취소"};
    MyDialogListener myDialogListener;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_category);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(getColor(R.color.colorTitleBar));
        }
        context = this;
        dbHelper = new CategoryDBHelper(context, "CATEGORY.db", null, 1);
        titleBar = findViewById(R.id.title_bar);
        gridView = findViewById(R.id.category_gridview);
        fab = findViewById(R.id.fab);

        backButtonEnable();

        categories = initCategories();
        gridAdapter = new CategoryGridAdapter(categories, this.context);
        gridView.setAdapter(gridAdapter);

        setMyDialogListener();
        setGridViewLongClickListener();
        setFabOnClickListener();

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


    public ArrayList<Category> initCategories(){
        ArrayList<Category> categories = new ArrayList<>();

        categories.add(new Category("공부", "#455678"));
        categories.add(new Category("운동", "#fda967"));
        categories.add(new Category("휴식", "#5f5f5f"));
        if(dbHelper.getResult() != null){
            categories = dbHelper.getResult();
        }
        return categories;
    }

    public void setMyDialogListener(){//카테고리를 설정하는 dialog에서 콜백을 받기 위한 Listener(customListener)
        myDialogListener = new MyDialogListener() {
            @Override
            public void onPostClicked(Category category) {
                dbHelper.insert(category.getCategoryName(), category.getColor());
                gridAdapter.add(category);
                gridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onModifyClicked(Category category, int index) {
                dbHelper.update(category.getCategoryName(), category.getColor());
                gridAdapter.modify(category, index);
                gridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNegativeClicked() {

            }

            @Override
            public void onCalendatItemClicked(LocalDate localDate) {

            }
        };
    }

    public void setGridViewLongClickListener(){//GridView의 onItemLongLickListenr.
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("수행할 작업을 선택하세요")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0://수정
                                        Udialog = new UpdateCategoryDialog(context, (Category) gridAdapter.getItem(position), position);
                                        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
                                        Point size = new Point();
                                        display.getSize(size);
                                        Udialog.setDialogListener(myDialogListener);
                                        dialog.dismiss();
                                        Udialog.show();
                                        Udialog.setCancelable(true);
                                        Window window = Udialog.getWindow();
                                        int x = (int)(size.x * 0.8f);
                                        int y = (int)(size.y * 0.8f);
                                        window.setLayout(x,y);
                                        break;
                                    case 1://삭제
                                        dbHelper.delete(((Category)(gridAdapter.getItem(position))).getCategoryName(), ((Category)(gridAdapter.getItem(position))).getColor());
                                        gridAdapter.delete(position);
                                        gridAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        dialog.dismiss();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }

    public void setFabOnClickListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Udialog = new UpdateCategoryDialog(context);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                Udialog.setDialogListener(myDialogListener);
                Udialog.show();
                Udialog.setCancelable(true);
                Window window = Udialog.getWindow();
                int x = (int)(size.x * 0.8f);
                int y = (int)(size.y * 0.8f);

                window.setLayout(x,y);
            }
        });
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId){
            case DIALOG_ID:
                final int invertColor = ~color;
                final String hexColor = String.format("%X", color);
                final String hexInvertColor = String.format("%X", invertColor);
                if (BuildConfig.DEBUG) {
                    Log.d("color==", "id " + dialogId + " c: " + hexColor + " i:" + hexInvertColor);
                }
                Udialog.setPickedColor("#" + hexColor);
                break;
        }

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

}
