package com.example.exercise_helper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText titleEditTV;
    private EditText delayEditTV;
    private EditText contentEditTv;
    private EditText timeEditTV;
    private EditText sensorEditTV;
    private Spinner categorySpinner;
    private Button cancelBtn;
    private Button saveBtn;

    private DBHelper dbHelper;

    private String category;
    private String mode;
    private Dictionary dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        // connect db
        dbHelper = new DBHelper(getApplicationContext());

        // 전달 받은 데이터 세팅
        mode = MyPointer.getMode();
        dictionary = MyPointer.getDictionary();

        initView();
        modifyView();
    }

    private void initView(){
        cancelBtn = findViewById(R.id.cancle_btn);
        saveBtn = findViewById(R.id.save_btn);
        titleEditTV = findViewById(R.id.title_editTV);
        delayEditTV = findViewById(R.id.delay_editTV);
        contentEditTv = findViewById(R.id.content_editTV);
        timeEditTV = findViewById(R.id.time_editTV);
        sensorEditTV = findViewById(R.id.sensor_editTV);
        categorySpinner = findViewById(R.id.category_spinner);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        categorySpinner.setOnItemSelectedListener(this);
    }

    private void modify_categorySpinner(){
        String[] musle = getResources().getStringArray(R.array.muscle);
        for (int i = 0; i<musle.length; i++){
            if (musle[i].equals(dictionary.getCategory())){
                categorySpinner.setSelection(i);
                break;
            }
        }
    }

    private void modify_delayEditTV(String delay){
        String message = "00 : 00 : 00";
        try{
            int myTimer = Integer.parseInt(delay);
            message = String.format("%02d : %02d : %02d", (int)(myTimer/3600), (int)((myTimer/60)%60), (int)(myTimer%60));
        } catch (Exception e){
        }
        delayEditTV.setText(message);
    }

    private void modify_timeEditTV_now(){
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String myTime = format1.format(new Date());
        timeEditTV.setText(myTime);
    }

    private void modifyView(){
        if (mode.equals(MyPointer.getCREATE_EXERCISE_MODE())){ // main -> exercise -> diary
            modify_delayEditTV(dictionary.getDelay()); // editText 설정
            modify_timeEditTV_now(); // 현재 시간 setting

            titleEditTV.setText(dictionary.getTitle());
            contentEditTv.setText(dictionary.getContent());
            sensorEditTV.setText(dictionary.getAverage());

        } else if(mode.equals(MyPointer.getCREATE_MODE())){ // main -> diary (click image button)
            modify_timeEditTV_now(); // 현재 시간 setting
            modify_delayEditTV("0"); // editText 설정

            sensorEditTV.setText("0");

        } else if(mode.equals(MyPointer.getUPDATE_MODE())){ // main -> diary (click recyclerview)
            modify_categorySpinner(); // 근육 선택창 설정
            modify_delayEditTV(dictionary.getDelay()); // editText 설정

            titleEditTV.setText(dictionary.getTitle());
            contentEditTv.setText(dictionary.getContent());
            timeEditTV.setText(dictionary.getTime());
            sensorEditTV.setText(dictionary.getAverage());

            // 버튼 설정
            saveBtn.setText("modify");
            cancelBtn.setText("delete");
        }
    }

    // Validity of input data
    private String delay;
    private Boolean getValidity(String myTime, String strDate, String average){
        if (!myTime.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d$")) {
            Toast.makeText(getApplicationContext(), "YYYY-MM-DD hh:mm:ss 형태로 입력해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (strDate.matches("^\\d\\d : \\d\\d : \\d\\d$")) {
            strDate = strDate.replaceAll("[^0-9]", "");
            Integer hour = Integer.parseInt(strDate.charAt(0)+""+strDate.charAt(1));
            Integer min = Integer.parseInt(strDate.charAt(2)+""+strDate.charAt(3));
            Integer sec = Integer.parseInt(strDate.charAt(4)+""+strDate.charAt(5));
            delay = hour*3600 + min*60 + sec + "";
        }
        else {
            Toast.makeText(getApplicationContext(), "HH : MM : SS 형태로 입력해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            if (Double.parseDouble(average)<0){
                Toast.makeText(getApplicationContext(), "sensor data에 0보다 큰 실수를 입력해주세요!", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "sensor data에 0보다 큰 실수를 입력해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_btn :
                String title = titleEditTV.getText().toString();
                String content = contentEditTv.getText().toString();
                String myTime = timeEditTV.getText().toString();
                String strDate = delayEditTV.getText().toString();
                String average = sensorEditTV.getText().toString();
                
                if (getValidity(myTime, strDate, average) == false){
                    // 올바르지 않은 값을 입력 받음
                    break;
                }
                else if (mode.equals(MyPointer.getCREATE_EXERCISE_MODE())){ // main -> exercise -> diary
                    //average = dictionary.getAverage();
                    dbHelper.insert(title, category, delay, content, average, myTime);
                } else if(mode.equals(MyPointer.getCREATE_MODE())){ // main -> diary (click image button)
                    dbHelper.insert(title, category, delay, content, average, myTime);
                } else if(mode.equals(MyPointer.getUPDATE_MODE())){ // main -> diary (click recyclerview)
                    dbHelper.update(dictionary.getId(), title, category, delay, content, myTime, average);
                }

                finish();
                break;

            case R.id.cancle_btn :
                if (mode.equals(MyPointer.getCREATE_EXERCISE_MODE())){ // main -> exercise -> diary
                } else if(mode.equals(MyPointer.getCREATE_MODE())){ // main -> diary (click image button)
                } else if(mode.equals(MyPointer.getUPDATE_MODE())){ // main -> diary (click recyclerview)
                    dbHelper.delete(dictionary.getId());
                }
                finish();
                break;
        }
    }

    // spinner event
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = ""+parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}