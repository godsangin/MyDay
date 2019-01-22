package com.msproject.myhome.mydays;

import android.graphics.Color;

import java.util.HashMap;

public class ColorMakeHelper {
    private static HashMap<String, Integer> colors = new HashMap();

    public static Integer getColor(String category){
        return colors.get(category);
    }

    public static void setColor(String category, Integer color){
        colors.put(category, color);
    }
}
