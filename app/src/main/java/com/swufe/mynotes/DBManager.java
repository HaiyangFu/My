package com.swufe.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(CourseItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Course", item.getCourse());
        values.put("Content", item.getContent());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public void addAll(List<CourseItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (CourseItem item : list) {
            ContentValues values = new ContentValues();
            values.put("Course", item.getCourse());
            values.put("Content", item.getContent());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void update(CourseItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CourseItem", item.getCourse());
        values.put("Content", item.getContent());
        db.update(TBNAME, values, "ID=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public List<CourseItem> listAll(){
        List<CourseItem> list = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            list = new ArrayList<CourseItem>();
            while(cursor.moveToNext()){
                CourseItem item = new CourseItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setCourse(cursor.getString(cursor.getColumnIndex("CURCOURSE")));
                item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

                list.add(item);
            }
            cursor.close();
        }
        db.close();
        return list;

    }

    public CourseItem findById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null);
        CourseItem item = null;
        if(cursor!=null && cursor.moveToFirst()){
            item = new CourseItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setCourse(cursor.getString(cursor.getColumnIndex("COURSE")));
            item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            cursor.close();
        }
        db.close();
        return item;
    }
}
