package com.msproject.myhome.mydays;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {
    ArrayList<Event> events;
    Context context;
    CategoryDBHelper dbHelper;
    DragEventCallBackListener dragEventCallBackListener;
    int startPos;
    int endPos;

    public void setDragEventCallBackListener(DragEventCallBackListener dragEventCallBackListener){
        this.dragEventCallBackListener = dragEventCallBackListener;
    }

    public void setDragable(boolean b){
        this.dragEventCallBackListener.setCanDrag(b);
    }

    public EventListAdapter(ArrayList<Event> events, Context context){
        this.events = events;
        this.context = context;
        dbHelper = new CategoryDBHelper(context, "CATEGORY.db", null, 1);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setItem(int position, Event event){
        this.events.set(position, event);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        TextView timeTextView = view.findViewById(R.id.event_time);
        TextView categoryName = view.findViewById(R.id.category_name);
        TextView eventContent = view.findViewById(R.id.event_content);
        if(events.get(position).eventNo < 10){
            timeTextView.setText("0"+events.get(position).getEventNo()+":00");
        }
        else{
            timeTextView.setText(events.get(position).getEventNo() + ":00");
        }
        categoryName.setText(events.get(position).getCategoryName());
        eventContent.setText(events.get(position).getEventContent());
        if(events.get(position).getCategoryName() != ""){
            String color = dbHelper.getColor(events.get(position).getCategoryName());
            categoryName.setBackgroundColor(Color.parseColor(color));
            eventContent.setBackgroundColor(Color.parseColor(color));
            categoryName.setTextColor(Color.parseColor("#FFFFFF"));
            eventContent.setTextColor(Color.parseColor("#FFFFFF"));
        }
        MyListViewDragListener myListViewDragListener = new MyListViewDragListener();
        view.setId(position);
        view.setOnDragListener(myListViewDragListener);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
                startPos = position;
                v.startDrag(null, shadowBuilder, null, 0);
                return false;
            }
        });

        return view;
    }

    private class MyListViewDragListener implements View.OnDragListener{
        int index;
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if(!dragEventCallBackListener.dragable()){
                return false;
            }
            final int action = event.getAction();
            switch(action){
                case DragEvent.ACTION_DRAG_LOCATION:

                    return true;
                case DragEvent.ACTION_DRAG_STARTED:

                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    if(index == v.getId())
                    v.setBackgroundColor(Color.BLUE);
                    v.invalidate();
                    index = v.getId();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED://되면 여기서 이벤트 추가해서 eventActivity로 콜백 ㄱㄱ

                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    endPos = v.getId();
                    Log.d("Start==", startPos + "End==" + endPos);
                    ArrayList<Event> selectedEvent = new ArrayList<>(events.subList(startPos, endPos + 1));
                    dragEventCallBackListener.onDragFinished(selectedEvent);
                    dragEventCallBackListener.setCanDrag(false);
                    return true;
                default:
                    break;
            }
            return false;
        }
    }
}
