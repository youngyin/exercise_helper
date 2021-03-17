package com.example.exercise_helper;

import android.content.Context;
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

    public static void delete(Context context, String _id){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        sqlDB.execSQL("delete from "+DBHelper.DATABASE_NAME+" where _ID="+_id);
        Toast.makeText(context,"삭제되었습니다.",Toast.LENGTH_SHORT).show();

    }

    public static void insert(Context context, String title, String category, String delay, String content){
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String _time = format1.format(new Date());

        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO "+DBHelper.DATABASE_NAME+" (title, category, delay, content, _time) VALUES ( '" + title + "', '"+ category +"', '"+ delay+"', '"+ content+"', '"+ _time+"');");
        sqlDB.close();
        Toast.makeText(context,"저장되었습니다.",Toast.LENGTH_SHORT).show();
    }

    public static void update(Context context, String _id, String title, String category, String delay, String content){
        SQLiteOpenHelper myDBHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        sqlDB.execSQL("update "+DBHelper.DATABASE_NAME
                +" set title='"+title+"',"
                +" category='"+category+"',"
                +" delay='"+delay+"',"
                +" content='"+content+"'"
                +" where _ID="+_id);
        sqlDB.close();
        Toast.makeText(context,"수정되었습니다.",Toast.LENGTH_SHORT).show();
    }
}
