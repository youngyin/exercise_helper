package com.example.exercise_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton exerciseBtn = findViewById(R.id.exercise_btn);
        ImageButton diaryBtn = findViewById(R.id.diary_btn);
        ImageButton bluetoothBtn = findViewById(R.id.bluetooth_btn);
        ImageButton dashboardBtn = findViewById(R.id.dashboard_btn);

        exerciseBtn.setOnClickListener(this);
        diaryBtn.setOnClickListener(this);
        bluetoothBtn.setOnClickListener(this);
        dashboardBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.bluetooth_btn :
                break;

            case R.id.dashboard_btn :
                break;

            case R.id.exercise_btn :
                intent = new Intent(this, ExerciseActivity.class);
                startActivity(intent);
                break;

            case R.id.diary_btn :
                intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
                break;
        }
    }
}