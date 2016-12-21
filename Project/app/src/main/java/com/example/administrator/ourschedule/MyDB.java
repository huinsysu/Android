package com.example.administrator.ourschedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class MyDB extends SQLiteOpenHelper {
    private static final String TABLE = "Course";
    private String[] Ssat0 = {"na", "ty", "cr", "tyo", "ca", "tea", "si", "wn"};
    private String[] Ssat1 = {"we", "st", "en"};

    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CT = "create table if not exists Course ("
                + "_id integer primary key, na text, ty text, cr text, tyo text," +
                "ca text, tea text, we integer, st integer, en integer, " +
                "si text, wn text)";
        db.execSQL(CT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int _insert(Mytool.Course Ct) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < 8; i++)
            cv.put(Ssat0[i], Ct._dataS.get(i));
        for (int i = 0; i < 3; i++)
            cv.put(Ssat1[i], Ct._datat.get(i));
        long i = db.insert(TABLE, null, cv);
        db.close();
        return (int) i;
    }

    public void _update(Mytool.Course Ct, int idt) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < 8; i++)
            cv.put(Ssat0[i], Ct._dataS.get(i));
        for (int i = 0; i < 3; i++)
            cv.put(Ssat1[i], Ct._datat.get(i));
        String whe = "_id=" + idt;
        db.update(TABLE, cv, whe, null);
        db.close();
    }

    public void _deleteall() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, null, null);
        db.close();
    }

    public void _delete(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String whe = "_id=" + id;
        db.delete(TABLE, whe, null);
        db.close();
    }

    public ArrayList<Mytool.Course> query_all() {
        ArrayList<Mytool.Course> data = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cu = db.query(TABLE, null, null, null, null, null, null);
        int[] ClIs = new int[8];
        int[] ClIi = new int[3];
        for (int i = 0; i < 8; i++)
            ClIs[i] = cu.getColumnIndex(Ssat0[i]);
        for (int i = 0; i < 3; i++)
            ClIi[i] = cu.getColumnIndex(Ssat1[i]);
        int idi = cu.getColumnIndex("_id");

        for (cu.moveToFirst(); !(cu.isAfterLast()); cu.moveToNext()) {
            ArrayList<String> tempS = new ArrayList<>();
            ArrayList<Integer> tempI = new ArrayList<>();
            for (int i = 0; i < 8; i++)
                tempS.add(cu.getString(ClIs[i]));
            for (int i = 0; i < 3; i++)
                tempI.add(cu.getInt(ClIi[i]));
            tempI.add(cu.getInt(idi));
            Mytool.Course Ct = new Mytool.Course(tempS, tempI);
            data.add(Ct);
        }
        cu.close();
        db.close();

        return data;
    }
}
