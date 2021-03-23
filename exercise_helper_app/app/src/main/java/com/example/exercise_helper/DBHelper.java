package com.example.exercise_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myDB";
    public static final String TABLE_NAME_DIARY = "diary"; 

    public static final String COL_NAME_ID = "_id"; // integer
    public static final String COL_NAME_DELAY = "delay"; // integer
    public static final String COL_NAME_AVERAGE = "_average"; // real
    public static final String COL_NAME_TITLE = "title";
    public static final String COL_NAME_CATEGORY = "category";
    public static final String COL_NAME_CONTENT = "content";
    public static final String COL_NAME_TIME = "_time";

    private static Context context = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String mySQL = String.format(
                "create table if not exists %s (" +
                "%s integer primary key autoincrement, %s integer default 0,  %s real default 0, " +
                "%s text, %s text, %s text, %s text);",
                TABLE_NAME_DIARY,
                COL_NAME_ID, COL_NAME_DELAY, COL_NAME_AVERAGE,
                COL_NAME_TITLE, COL_NAME_CATEGORY, COL_NAME_CONTENT, COL_NAME_TIME);
        db.execSQL(mySQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String mySQL = String.format("drop table if exists %s;", TABLE_NAME_DIARY);
        db.execSQL(mySQL);
        onCreate(db);
    }

    public void delete(String _id){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String mySQL = String.format("delete from %s where _ID = %s;", TABLE_NAME_DIARY, _id);
        sqlDB.execSQL(mySQL);
        Toast.makeText(context,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
    }

    public void insert(String title, String category, String delay, String content, String average){
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String _time = format1.format(new Date());

        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME_TITLE, title);
        cv.put(COL_NAME_CATEGORY, category);
        cv.put(COL_NAME_DELAY, delay);
        cv.put(COL_NAME_CONTENT, content);
        cv.put(COL_NAME_AVERAGE, average);
        cv.put(COL_NAME_TIME, _time);

        long result = sqlDB.insert(TABLE_NAME_DIARY, null, cv);
        if (result != -1){
            Toast.makeText(context,"저장되었습니다.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"저장에 실패했습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    public void update(String _id, String title, String category, String delay, String content){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME_TITLE, title);
        cv.put(COL_NAME_CATEGORY, category);
        cv.put(COL_NAME_DELAY, delay);
        cv.put(COL_NAME_CONTENT, content);

        long result = sqlDB.update(TABLE_NAME_DIARY, cv, COL_NAME_ID+"=?", new String[]{_id});
        if (result != -1){
            Toast.makeText(context,"수정되었습니다.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"수정에 실패했습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor select_all(){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        String mySQL = String.format(
                "select %s, %s, %s, %s, %s, %s, %s from %s order by _id DESC;",
                COL_NAME_ID, COL_NAME_TITLE, COL_NAME_CATEGORY, COL_NAME_DELAY, COL_NAME_CONTENT, COL_NAME_TIME, COL_NAME_AVERAGE,
                TABLE_NAME_DIARY);
        Cursor cursor = sqlDB.rawQuery(mySQL, null);
        return cursor;
    }

    public Cursor select_count_id_from_diary_group_by_category(){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        String mySQL = "select count(_id), category from diary group by category;";
        Cursor cursor = sqlDB.rawQuery(mySQL, null);
        return cursor;
    }

    public Cursor select_count_id_from_diary_group_by_date(){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        String mySQL = "select count(_TIME), substr(_TIME , 0, 11) from DIARY group by substr(_TIME , 0, 11);";
        Cursor cursor = sqlDB.rawQuery(mySQL, null);
        return cursor;
    }

    public Cursor select_average_from_diary_group_by_category(){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        String mySQL = "select avg(_AVERAGE ), CATEGORY from DIARY group by CATEGORY;";
        Cursor cursor = sqlDB.rawQuery(mySQL, null);
        return cursor;
    }

    public Cursor select_sum_delay_from_diary_group_by_category(){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        String mySQL = "select sum(delay), CATEGORY from DIARY group by CATEGORY;";
        Cursor cursor = sqlDB.rawQuery(mySQL, null);
        return cursor;
    }
}
