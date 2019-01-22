package com.msproject.myhome.mydays;

public class Event {
    String categoryName;
    String eventName;

    public Event(String categoryName, String eventName){
        this.categoryName = categoryName;
        this.eventName = eventName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
