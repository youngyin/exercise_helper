package com.example.exercise_helper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
            // case 1. exercise에서 넘어옴

            // case 2. 리사이클러뷰 선택하여 수정
            if (!item.getId().equals("")){
                saveBtn.setText("modify");
                cancelBtn.setText("delete");
            }
        }else {
            // 새로운 값 저장
            timeTV.setText("");
            timeTV.setHeight(0);
        }
        MainActivity.item = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_btn :
                String title = titleEditTV.getText().toString();
                String category = categoryEditTV.getText().toString();
                String delay = delayEditTV.getText().toString();
                String content = contentEditTv.getText().toString();

                if (item!=null){
                    if (item.getId()==""){ // exercise에서 넘어옴
                        DBHelper.insert(getApplicationContext(), title, category, delay, content);
                    }
                    else{ // 리사이클러뷰 선택하여 수정
                        DBHelper.update(getApplicationContext(), item.getId(), title, category, delay, content);
                    }
                }
                else{ // 새로운 값 저장
                    DBHelper.insert(getApplicationContext(), title, category, delay, content);
                }

                // main activity refresh
                ((MainActivity)MainActivity.CONTEXT).onResume();
                finish();
                break;

            case R.id.cancle_btn :

                if (item!=null){
                    if (item.getId()==""){ // exercise에서 넘어옴
                    }
                    else{ // 리사이클러뷰 선택하여 삭제
                        DBHelper.delete(getApplicationContext(), item.getId());
                    }
                }
                else{ // 새로운 값 저장
                }

                finish();
                break;
        }
    }
}