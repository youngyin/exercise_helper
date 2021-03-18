package com.example.exercise_helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        initView();
    }

    private void initView(){
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

        switch (v.getId()){
            case R.id.save_btn :
                String title = titleEditTV.getText().toString();
                String category = categoryEditTV.getText().toString();
                String delay = delayEditTV.getText().toString();
                String content = contentEditTv.getText().toString();

                if (item!=null){
                    DBHelper.update(getApplicationContext(), item.getId(), title, category, delay, content);
                }
                else{
                    DBHelper.insert(getApplicationContext(), title, category, delay, content);
                }

                // main activity refresh
                ((MainActivity)MainActivity.CONTEXT).onResume();
                finish();
                break;

            case R.id.cancle_btn :
                if (item!=null){
                    DBHelper.delete(getApplicationContext(), item.getId());
                }
                else{
                }

                finish();
                break;
        }
    }
}