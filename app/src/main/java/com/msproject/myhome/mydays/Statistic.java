package com.msproject.myhome.mydays;

public class Statistic {
    Category[] categories;
    Day[] days;

    public Statistic(Category[] categories, Day[] days) {
        this.categories = categories;
        this.days = days;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }
}
