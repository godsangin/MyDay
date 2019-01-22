package com.msproject.myhome.mydays;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CategoryDBHelper extends SQLiteOpenHelper {
    public CategoryDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT, color TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String categoryName, String color){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO CATEGORY VALUES(null, '"+ categoryName + "', '" + color + "');");
        db.close();
    }

    public void update(String categoryName, String color){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE CATEGORY SET color='"+ color + "' WHERE categoryName='" + categoryName + "';");
        db.close();
    }

    public void delete(String categoryName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CATEGORY WHERE categoryName='" + categoryName + "';");
        db.close();
    }


    public ArrayList<Category> getResult(){
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY", null);

        while(cursor.moveToNext()){
            categories.add(new Category(cursor.getString(1),cursor.getString(2)));
        }
        return categories;
    }

    public void clearDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CATEGORY");
    }
}
