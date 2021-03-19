package com.example.exercise_helper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText titleEditTV;
    private EditText delayEditTV;
    private EditText contentEditTv;
    private EditText idEditTV;
    private TextView timeTV;
    private TextView categoryTV;
    private Spinner categorySpinner;

    private Dictionary item;

    private String category;

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
        delayEditTV = findViewById(R.id.delay_editTV);
        contentEditTv = findViewById(R.id.content_editTV);
        timeTV = findViewById(R.id.time_textView2);
        categorySpinner = findViewById(R.id.category_spinner);
        categoryTV = findViewById(R.id.category_textview2);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        categorySpinner.setOnItemSelectedListener(this);

        item = MainActivity.item;
        if (item!=null){
            String[] musle = getResources().getStringArray(R.array.muscle);
            for (int i = 0; i<musle.length; i++){
                if (musle[i].equals(item.getCategory())){
                    categorySpinner.setSelection(i);
                    break;
                }
            }

            titleEditTV.setText(item.getTitle());
            categoryTV.setText("운동할 근육: "+item.getCategory() + "");
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

    // spinner event
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = ""+parent.getItemAtPosition(position);
        categoryTV.setText("운동할 근육: "+category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        categoryTV.setText("운동할 근육");
    }
}