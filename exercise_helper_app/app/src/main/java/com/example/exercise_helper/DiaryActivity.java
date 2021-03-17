package com.example.exercise_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Button cancelBtn = findViewById(R.id.cancle_btn);
        Button saveBtn = findViewById(R.id.save_btn);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.save_btn :
                break;

            case R.id.cancle_btn :
                intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
        }
    }
}