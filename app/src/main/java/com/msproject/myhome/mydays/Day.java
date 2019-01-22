package com.msproject.myhome.mydays;

import java.util.Date;

public class Day {
    Event[] events;
    Date date;

    public Day(Event[] events, Date date){
        this.events = events;
        this.date = date;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
