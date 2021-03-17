package com.example.exercise_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener {
    EditText titleEditTV;
    EditText categoryEditTV;
    EditText delayEditTV;
    EditText contentEditTv;
    EditText idEditTV;
    TextView timeTV;

    Dictionary item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Button cancelBtn = findViewById(R.id.cancle_btn);
        Button saveBtn = findViewById(R.id.save_btn);
        idEditTV = findViewById(R.id.id_editTV);
        titleEditTV = findViewById(R.id.title_editTV);
        categoryEditTV = findViewById(R.id.category_editTV);
        delayEditTV = findViewById(R.id.delay_editTV);
        contentEditTv = findViewById(R.id.content_editTV);
        timeTV = findViewById(R.id.time_textView2);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        ImageView ivMenu=findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);

        item = MainActivity.item;
        if (item!=null){
            titleEditTV.setText(item.getTitle());
            categoryEditTV.setText(item.getCategory());
            contentEditTv.setText(item.getContent());
            delayEditTV.setText(item.getDelay());
            idEditTV.setText(item.getId());
            timeTV.setText(item.getTime());
            saveBtn.setText("modify");
            cancelBtn.setText("delete");
        }
        else {
            timeTV.setText("");
            timeTV.setHeight(0);
        }
        MainActivity.item = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        SQLiteOpenHelper myDBHelper = new DBHelper(this);
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();

        switch (v.getId()){
            case R.id.save_btn :
                String title = titleEditTV.getText().toString();
                String category = categoryEditTV.getText().toString();
                String delay = delayEditTV.getText().toString();
                String content = contentEditTv.getText().toString();
                SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                String time = format1.format(new Date());

                if (item!=null){
                    sqlDB.execSQL("update "+DBHelper.DATABASE_NAME
                            +" set title='"+title+"',"
                            +" category='"+category+"',"
                            +" delay='"+delay+"',"
                            +" content='"+content+"'"
                            +" where _ID="+item.getId());
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(),"수정되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    sqlDB.execSQL("INSERT INTO "+DBHelper.DATABASE_NAME+" (title, category, delay, content, _time) VALUES ( '" + title + "', '"+ category +"', '"+ delay+"', '"+ content+"', '"+ time+"');");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
                }

                intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;

            case R.id.cancle_btn :
                if (item!=null){
                    sqlDB.execSQL("delete from "+DBHelper.DATABASE_NAME+" where _ID="+item.getId());
                    Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                }

                intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;

            case R.id.iv_menu :
                intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() { // 뒤로가기 버튼 비활성화
    }
}