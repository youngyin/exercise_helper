package com.example.exercise_helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapter.OnItemClickListener {
    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    public static Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT = this;

        initView();
        initRecyclerView();
        selectAllDB();
    }

    private void initView(){
        ImageButton exerciseBtn = findViewById(R.id.exercise_btn);
        ImageButton diaryBtn = findViewById(R.id.diary_btn);
        ImageButton initializationBtn = findViewById(R.id.initialization_btn);
        ImageButton dashboardBtn = findViewById(R.id.dashboard_btn);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);

        exerciseBtn.setOnClickListener(this);
        diaryBtn.setOnClickListener(this);
        initializationBtn.setOnClickListener(this);
        dashboardBtn.setOnClickListener(this);
    }

    private void initRecyclerView(){
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new CustomAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void selectAllDB() {
        Cursor cursor = DBHelper.selectAll(getApplicationContext());

        while (cursor.moveToNext()){
            Dictionary data = new Dictionary(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
            mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.initialization_btn :
                showInitializationDialog();
                break;

            case R.id.dashboard_btn :
                intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
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

    private void showInitializationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("delete diary");
        builder.setMessage("모든 기록을 삭제하시겠습니까?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initializationDB(); // DB 초기화
                initRecyclerView(); // 리사이클러뷰 초기화
                selectAllDB(); // 리사이클러뷰 다시 로딩
            }
        });
        builder.setNegativeButton("no",null);
        builder.create().show();
    }
    
    private void initializationDB(){
        SQLiteOpenHelper myDBHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        myDBHelper.onUpgrade(sqlDB, 0, 0);
    }

    // 리사이클러뷰 클릭 이벤트
    public static Dictionary item = null;
    @Override
    public void onItemClick(View v, int position) {
        //Toast.makeText(this, item.getId()+" 를 클릭!!", Toast.LENGTH_LONG).show();
        Intent intent;
        intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    // 뒤로가기 참고: https://atomic0x90.github.io/android-studio/2020/02/28/android-studio-back-button.html
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

    // 새로고침 함수
    @Override
    public void onResume(){
        super.onResume();
        initRecyclerView(); // 리사이클러뷰 초기화
        selectAllDB(); // 리사이클러뷰 다시 로딩
    }
}