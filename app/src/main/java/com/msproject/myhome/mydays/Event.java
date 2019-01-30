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

    public boolean equals(Event event){
        if(event == null){
            return false;
        }
        if(event.getEventNo() == this.eventNo && event.getCategoryName().equals(this.categoryName) && event.getEventContent().equals(this.eventContent)){
            return true;
        }
        return false;
    }
}
