package com.example.exercise_helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        String mySQL = String.format(
                "create table if not exists %s (" +
                "%s integer primary key autoincrement, " +
                "%s text, %s text, %s text, %s text, %s text);",
                DATABASE_NAME, COL_1, COL_2, COL_3, COL_4, COL_5, COL_6);
        db.execSQL(mySQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String mySQL = String.format("drop table %s;", DATABASE_NAME);
        db.execSQL(mySQL);
        onCreate(db);
    }

    public static void delete(Context context, String _id){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String mySQL = String.format("delete from %s where _ID = %s;", DATABASE_NAME, _id);
        sqlDB.execSQL(mySQL);
        Toast.makeText(context,"삭제되었습니다.",Toast.LENGTH_SHORT).show();

    }

    public static void insert(Context context, String title, String category, String delay, String content){
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String _time = format1.format(new Date());

        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String mySQL = String.format(
                "insert into %s (%s, %s, %s, %s, %s) values ('%s', '%s', '%s', '%s', '%s');",
                DATABASE_NAME, COL_2, COL_3, COL_4, COL_5, COL_6, title, category, delay, content, _time);
        sqlDB.execSQL(mySQL);
        sqlDB.close();
        Toast.makeText(context,"저장되었습니다.",Toast.LENGTH_SHORT).show();
    }

    public static void update(Context context, String _id, String title, String category, String delay, String content){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String mySQL = String.format(
                "update %s set %s = '%s', %s = '%s', %s = '%s', %s = '%s' where _ID = %s;"
                , DATABASE_NAME, COL_2, title, COL_3, category, COL_4, delay, COL_5, content, _id);
        sqlDB.execSQL(mySQL);
        sqlDB.close();
        Toast.makeText(context,"수정되었습니다.",Toast.LENGTH_SHORT).show();
    }

    public static Cursor selectAll(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        String mySQL = String.format(
                "select %s, %s, %s, %s, %s, %s from %s order by _id DESC;"
                , COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, DATABASE_NAME);
        Cursor cursor = sqlDB.rawQuery(mySQL, null);
        return cursor;
    }

    public static void select(Context context){

    }
}
