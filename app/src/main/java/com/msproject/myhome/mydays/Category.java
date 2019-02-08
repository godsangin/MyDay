package com.msproject.myhome.mydays;

public class Category {
    String categoryName;
    String color;

    public Category(String categoryName, String color) {
        this.categoryName = categoryName;
        this.color = color;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean equals(Category category){
        if(this.categoryName.equals(category.getCategoryName())  && this.color.equals(category.getColor())){
            return true;
        }
        return false;
    }

}
