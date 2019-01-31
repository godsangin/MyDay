package com.msproject.myhome.mydays;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class CategoryDBHelper extends SQLiteOpenHelper {
    public CategoryDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT, color TEXT, flag INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String categoryName, String color){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM CATEGORY where categoryName="+'"'+categoryName+'"', null);
        cursor.moveToNext();
        int exist = cursor.getInt(0);
        if(exist != 0){
            Log.d("exist==", exist + "");
            update(categoryName, color);
            db.close();
            return;
        }
        db.execSQL("insert into category values(null,"+'"'+categoryName+'"'+","+'"'+color+'"'+",1);");

        db.close();
    }

    public void update(String categoryName, String color){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE CATEGORY SET color='"+ color + "', flag = 1 WHERE categoryName='" + categoryName + "';");
        db.close();
    }

    public void delete(String categoryName, String color){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE CATEGORY SET color='"+ color + "', flag = 0 WHERE categoryName='" + categoryName + "';");
        db.close();
    }


    public ArrayList<Category> getResult(){
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY", null);

        while(cursor.moveToNext()){
            Category category = new Category(cursor.getString(1),cursor.getString(2));
            if(cursor.getInt(3) == 0){
                continue;
            }
            categories.add(category);
        }
        db.close();
        return categories;
    }

    public String getColor(String categoryName){
        SQLiteDatabase db = getReadableDatabase();
        Log.d("color==", categoryName);
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY where categoryName= "+'"'+categoryName+'"',null);
        cursor.moveToNext();

        Log.d("color==", cursor.getString(2));
        db.close();
        return cursor.getString(2);
    }

    public void clearDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CATEGORY");
    }
}
