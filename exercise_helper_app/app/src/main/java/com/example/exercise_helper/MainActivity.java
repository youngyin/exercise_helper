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
import android.widget.TextView;
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

    private DBHelper dbHelper;
    private MyPointer myPointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext()); // connect db
        MyPointer.resetAll();

        initView();
        //custom_RecyclerView_Style();
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

    private void custom_RecyclerView_Style(){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void initRecyclerView(){
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new CustomAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        //mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void selectAllDB() {
        Cursor cursor = dbHelper.select_all();

        while (cursor.moveToNext()){
            Dictionary data = new Dictionary(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6));

            mArrayList.add(data); // RecyclerView??? ????????? ?????? ??????
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
                myPointer.setMode(myPointer.getCREATE_MODE());

                intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showInitializationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("delete diary");
        builder.setMessage("?????? ????????? ?????????????????????????");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initializationDB(); // DB ?????????
                initRecyclerView(); // ?????????????????? ?????????
                selectAllDB(); // ?????????????????? ?????? ??????
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

    // ?????????????????? ?????? ?????????
    @Override
    public void onItemClick(View v, int position) {
        //Toast.makeText(this, item.getId()+" ??? ??????!!", Toast.LENGTH_LONG).show();
        Intent intent;
        intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    // ???????????? ??????: https://atomic0x90.github.io/android-studio/2020/02/28/android-studio-back-button.html
    private long backKeyPressedTime = 0; // ??????????????? ?????? ?????? ????????? ????????? ??????
    private Toast toast;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "?????? ?????? ????????? ??? ??? ??? ????????? ???????????????.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
        }
    }

    // ???????????? ??????
    @Override
    public void onResume(){
        super.onResume();
        initRecyclerView(); // ?????????????????? ?????????
        selectAllDB(); // ?????????????????? ?????? ??????
    }
}
