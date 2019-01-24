package com.msproject.myhome.mydays;

public class Event {
    int eventNo;
    String categoryName;
    String eventContent;

    public Event(int eventNo, String categoryName, String eventContent) {
        this.eventNo = eventNo;
        this.categoryName = categoryName;
        this.eventContent = eventContent;
    }

    public  Event (int eventNo){
        this.eventNo = eventNo;
    }


    public int getEventNo() {
        return eventNo;
    }

    public void setEventNo(int eventNo) {
        this.eventNo = eventNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }
}
