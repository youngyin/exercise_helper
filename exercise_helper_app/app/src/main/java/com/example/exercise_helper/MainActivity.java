package com.example.exercise_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapter.OnItemClickListener {
    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private Toolbar toolbar;

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

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new CustomAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // 테스트용
        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dictionary data = new Dictionary("Apple", "사과");
                mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
                mAdapter.notifyDataSetChanged();
            }
        });

        ImageView ivMenu=findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);
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

            case R.id.iv_menu :
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent;
        intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    /*
    뒤로가기 참고: https://atomic0x90.github.io/android-studio/2020/02/28/android-studio-back-button.html
     */
    private long backKeyPressedTime = 0; // 마지막으로 뒤로 가기 버튼을 눌렀던 시간
    private Toast toast;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르면 종료합니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
        }
    }
}