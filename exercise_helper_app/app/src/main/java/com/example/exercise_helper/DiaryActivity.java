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
    private EditText idEditTV;
    private TextView timeTV;
    private TextView categoryTV;
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
    }

    private void modifyView(){
        if (mode.equals(MyPointer.getCREATE_EXERCISE_MODE())){ // main -> exercise -> diary
            // 근육 선택창 설정
            String[] musle = getResources().getStringArray(R.array.muscle);
            for (int i = 0; i<musle.length; i++){
                if (musle[i].equals(dictionary.getCategory())){
                    categorySpinner.setSelection(i);
                    break;
                }
            }

            // editText 설정
            String message = "00 : 00 : 00";
            try{
                int myTimer = Integer.parseInt(dictionary.getDelay());
                message = String.format("%02d : %02d : %02d",
                        (int)(myTimer/3600), (int)((myTimer/60)%60), (int)(myTimer%60));
            } catch (Exception e){

            }

            titleEditTV.setText(dictionary.getTitle());
            categoryTV.setText("운동할 근육: "+dictionary.getCategory() + "");
            contentEditTv.setText(dictionary.getContent());
            delayEditTV.setText(message);
            idEditTV.setText(dictionary.getId());
            timeTV.setText(dictionary.getTime());

        } else if(mode.equals(MyPointer.getCREATE_MODE())){ // main -> diary (click image button)
            timeTV.setText("");
            timeTV.setHeight(0);

            int myTimer = 0;
            String message = String.format("%02d : %02d : %02d", (int)(myTimer/3600), (int)((myTimer/60)%60), (int)(myTimer%60));
            delayEditTV.setText(message);
        } else if(mode.equals(MyPointer.getUPDATE_MODE())){ // main -> diary (click recyclerview)
            // 근육 선택창 설정
            String[] musle = getResources().getStringArray(R.array.muscle);
            for (int i = 0; i<musle.length; i++){
                if (musle[i].equals(dictionary.getCategory())){
                    categorySpinner.setSelection(i);
                    break;
                }
            }

            // editText 설정
            String message = "00 : 00 : 00";
            try{
                int myTimer = Integer.parseInt(dictionary.getDelay());
                message = String.format("%02d : %02d : %02d", (int)(myTimer/3600), (int)((myTimer/60)%60), (int)(myTimer%60));
            } catch (Exception e){

            }

            titleEditTV.setText(dictionary.getTitle());
            categoryTV.setText("운동할 근육: "+dictionary.getCategory() + "");
            contentEditTv.setText(dictionary.getContent());
            delayEditTV.setText(message);
            idEditTV.setText(dictionary.getId());
            timeTV.setText(dictionary.getTime());

            // 버튼 설정
            if (!dictionary.getId().equals("")){
                saveBtn.setText("modify");
                cancelBtn.setText("delete");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_btn :
                String title = titleEditTV.getText().toString();
                String delay = "0";
                String content = contentEditTv.getText().toString();

                String strDate = delayEditTV.getText().toString();
                Boolean validation = strDate.matches("^\\d\\d : \\d\\d : \\d\\d$");
                if (validation) {
                    strDate = strDate.replaceAll("[^0-9]", "");
                    Integer hour = Integer.parseInt(strDate.charAt(0)+""+strDate.charAt(1));
                    Integer min = Integer.parseInt(strDate.charAt(2)+""+strDate.charAt(3));
                    Integer sec = Integer.parseInt(strDate.charAt(4)+""+strDate.charAt(5));
                    delay = hour*3600 + min*60 + sec + "";
                }
                else {
                    Toast.makeText(getApplicationContext(), "HH : MM : SS 형태로 입력해주세요!", Toast.LENGTH_LONG).show();
                    break;
                }

                if (mode.equals(MyPointer.getCREATE_EXERCISE_MODE())){ // main -> exercise -> diary
                    String average = dictionary.getAverage();
                    dbHelper.insert(title, category, delay, content, average);
                } else if(mode.equals(MyPointer.getCREATE_MODE())){ // main -> diary (click image button)
                    String average = "0.0000";
                    dbHelper.insert(title, category, delay, content, average);
                } else if(mode.equals(MyPointer.getUPDATE_MODE())){ // main -> diary (click recyclerview)
                    dbHelper.update(dictionary.getId(), title, category, delay, content);
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
        categoryTV.setText("운동할 근육: "+category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        categoryTV.setText("운동할 근육");
    }
}