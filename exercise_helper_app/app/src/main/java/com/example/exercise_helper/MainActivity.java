package com.example.exercise_helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        // 초기설정
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select _id, title, category, delay, content, _time from "+ DBHelper.DATABASE_NAME+" order by _time", null);

        while (cursor.moveToNext()){
            Dictionary data = new Dictionary(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
            mAdapter.notifyDataSetChanged();
        }

        ImageView ivMenu=findViewById(R.id.iv_menu);
        ivMenu.setImageResource(R.drawable.ic_launcher_foreground);
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
                finish();
                startActivity(intent);
                break;

            case R.id.diary_btn :
                intent = new Intent(this, DiaryActivity.class);
                finish();
                startActivity(intent);
                break;

            case R.id.iv_menu :
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("delete diary");
                builder.setMessage("모든 기록을 삭제하시겠습니까?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper myDBHelper = new DBHelper(getApplicationContext());
                        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
                        myDBHelper.onUpgrade(sqlDB, 0, 0); // 전체 삭제

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("no",null);
                builder.create().show();
                break;
        }
    }

    public static Dictionary item = null;
    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, item.getId()+" 를 클릭!!", Toast.LENGTH_LONG).show();
        Intent intent;
        intent = new Intent(this, DiaryActivity.class);
        finish();
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