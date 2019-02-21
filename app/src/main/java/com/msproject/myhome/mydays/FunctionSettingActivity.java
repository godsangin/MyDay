package com.msproject.myhome.mydays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FunctionSettingActivity extends AppCompatActivity {
    ListView functionSettingListView;
    FSListViewAdapter fsListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_setting);
        functionSettingListView = findViewById(R.id.function_setting_listview);
        ArrayList<SettingItem> items = new ArrayList<>();
        items.add(new SettingItem("푸시알림 허용", "체크 시 푸시 알림 기능을 활성화하고 이벤트를 수신합니다."));
        items.add(new SettingItem("백그라운드 작업 허용", "체크 시 수면시간측정, 일정 등록 알림 등의 작업을 어플이 꺼져있을 경우에도 진행합니다."));
        fsListViewAdapter = new FSListViewAdapter(items);
        functionSettingListView.setAdapter(fsListViewAdapter);

    }

    public class FSListViewAdapter extends BaseAdapter{
        ArrayList<SettingItem> items;

        public FSListViewAdapter(ArrayList<SettingItem> items){
            this.items = items;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item,parent,false);
            TextView settingTitle = view.findViewById(R.id.item_name);
            TextView settingContent = view.findViewById(R.id.item_content);
            settingTitle.setText(items.get(position).getTitle());
            settingContent.setText(items.get(position).getContent());
            final CheckBox cb = view.findViewById(R.id.checkbox);
            cb.setVisibility(View.VISIBLE);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:
                            if(cb.isChecked()){
                                //푸시 되게

                                Toast.makeText(parent.getContext(), "푸시 기능이 활성화됩니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                //안되게

                                Toast.makeText(parent.getContext(), "푸시 기능이 해제됩니다.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            if(cb.isChecked()){
                                //백그라운드 되게

                                Toast.makeText(parent.getContext(), "백그라운드 작업이 활성화됩니다.", Toast.LENGTH_SHORT).show();
                            }else{


                                Toast.makeText(parent.getContext(), "백그라운드 작업이 해제됩니다.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
            return view;
        }
    }
}
