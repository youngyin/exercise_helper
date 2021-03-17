package com.example.exercise_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db";

    public static final String COL_1 = "_id";
    public static final String COL_2 = "title";
    public static final String COL_3 = "category";
    public static final String COL_4 = "delay";
    public static final String COL_5 = "content";
    public static final String COL_6 = "_time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String mySQL = "create table if not exists " + DATABASE_NAME + "("+
                COL_1 +" integer primary key autoincrement, "+
                COL_2 +" text, "+
                COL_3 +" text, "+
                COL_4 +" text, "+
                COL_5 +" text, "+
                COL_6 + " text)";
        db.execSQL(mySQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String mySQL = "drop table "+DATABASE_NAME;
        db.execSQL(mySQL);
        onCreate(db);
    }
}
