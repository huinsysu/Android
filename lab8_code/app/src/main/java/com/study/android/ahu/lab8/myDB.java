package com.study.android.ahu.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ahu on 16-11-18.
 */
public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "my_database";
    private static final String TABLE_NAME = "birthday_table";
    private static final int DB_VERSION = 1;

    public myDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,"
                + " birthday TEXT, gift TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i, int i1) {}

    private BirthdayItem[] ConverToBirthdayItem(Cursor cursor) {
        int counts = cursor.getCount();
        if (counts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        BirthdayItem[] items = new BirthdayItem[counts];
        for (int i = 0; i < counts; i++) {
            items[i] = new BirthdayItem();
            items[i].name = cursor.getString(cursor.getColumnIndex("name"));
            items[i].birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            items[i].gift = cursor.getString(cursor.getColumnIndex("gift"));
            cursor.moveToNext();
        }
        return items;
    }

    public void insert2DB(BirthdayItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("name", item.name);
        newValues.put("birthday", item.birthday);
        newValues.put("gift", item.gift);
        db.insert(TABLE_NAME, null, newValues);
        db.close();
    }

    public void updateOneDate(BirthdayItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues updateValuse = new ContentValues();
        updateValuse.put("birthday", item.birthday);
        updateValuse.put("gift", item.gift);
        db.update(TABLE_NAME, updateValuse,"name=" + '"' + item.name + '"', null);
    }

    public void deleteOneData(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "name=" + '"' + name + '"', null);
    }

    public BirthdayItem[] getALLItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {"name", "birthday", "gift"},
                null, null, null, null, null);
        return ConverToBirthdayItem(cursor);
    }

    public BirthdayItem[] getItemByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {"name", "birthday", "gift"},
                "name=" + '"' + name + '"', null, null, null, null);
        return ConverToBirthdayItem(cursor);
    }

}
