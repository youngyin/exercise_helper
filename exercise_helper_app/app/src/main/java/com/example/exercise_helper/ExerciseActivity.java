package com.example.exercise_helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class ExerciseActivity extends AppCompatActivity implements BluetoothSPP.OnDataReceivedListener, BluetoothSPP.BluetoothConnectionListener, View.OnClickListener {

    private BluetoothSPP bt;
    private Button btnConnect;
    private ProgressBar progressBar;
    private TextView timerTextview;
    private TextView categoryTV;

    public static Integer mytime;
    private static String timerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        progressBar = findViewById(R.id.progress);
        btnConnect = findViewById(R.id.btnConnect);
        timerTextview = findViewById(R.id.myTimerTV);
        categoryTV = findViewById(R.id.category_editTV2);

        btnConnect.setOnClickListener(this);
        findViewById(R.id.playBtn).setOnClickListener(this);
        findViewById(R.id.stopBtn).setOnClickListener(this);
        findViewById(R.id.resetBtn).setOnClickListener(this);
        findViewById(R.id.createDiaryBtn).setOnClickListener(this);

        bt = new BluetoothSPP(this); //Initializing
        bt.setOnDataReceivedListener(this); // 데이터 수신
        bt.setBluetoothConnectionListener(this); // 블루투스 연결상태 감지

        startTimer(); // timer setting
    }

    // timer setting: https://velog.io/@hojw1019/Android-Timer-update-TextView 참고
    private void startTimer(){
        mytime = 0;
        timerState = "stop";
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ExerciseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (timerState.equals("stop")){
                            //pass
                        }
                        else if (timerState.equals("reset")){
                            mytime = 0;
                        }
                        else {
                            mytime++;
                        }
                        String message = String.format("%02d : %02d : %02d",
                                (int)(mytime/3600), (int)((mytime/60)%60), (int)(mytime%60));
                        timerTextview.setText("" + message);
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    /*
    블루투스 설정
    https://blog.codejun.space/13
    https://chaniii.tistory.com/7 참고
     */
    private void setResponse(String message_toast, String message_textView, String message_button){
        Toast.makeText(getApplicationContext(), message_toast , Toast.LENGTH_SHORT).show();

        TextView textViewReceive = findViewById(R.id.textViewReceive);
        textViewReceive.setText(message_textView);

        btnConnect.setText(message_button);
    }
    
    public void activateBluetooth(){
        try {
            if (!bt.isBluetoothAvailable()) { // 블루투스 사용 불가
                Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if (!bt.isBluetoothEnabled()) { // 앱의 상태를 보고 블루투스 사용 가능하면
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
            } else {
                if (!bt.isServiceAvailable()) { // 블루투스 사용 불가
                    bt.setupService();
                    bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기끼리
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 메세지창 응답 처리
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
            if (resultCode == Activity.RESULT_OK) // 연결됨
                bt.connect(data);

        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
            if (resultCode == Activity.RESULT_OK) { // 연결됨
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);

            } else { // 사용불가
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDataReceived(byte[] data, String message) { // 데이터를 받았을 때 처리
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); // 토스트로 데이터 띄움
        try{
            int power = (int) (Double.parseDouble(message)*100);// Double <-> String
            progressBar.setProgress(power);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 블루투스 연결상태 리스너
    @Override
    public void onDeviceConnected(String name, String address) {
        setResponse("Connected to " + name + "\n" + address, "connected...", "disconnect"); // 연결성공
    }

    @Override
    public void onDeviceDisconnected() {
        progressBar.setProgress(0);
        setResponse("연결이 해제되었습니다.", "disconnected...", "connect"); // 연결해제
    }

    @Override
    public void onDeviceConnectionFailed() { //연결실패
        progressBar.setProgress(0);
        setResponse("연결에 실패하였습니다.", "disconnected...", "connect"); // 연결실패
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                // 디바이스 연결 시도: 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
                break;

            case R.id.playBtn:
                timerState = "play";
                break;

            case R.id.stopBtn :
                timerState = "stop";
                break;

            case R.id.resetBtn :
                timerState = "reset";
                break;

            case R.id.createDiaryBtn :
                Intent intent = new Intent(this, DiaryActivity.class);
                MainActivity.item = new Dictionary("", "", categoryTV.getText().toString(), timerTextview.getText().toString(), "", "");
                finish();
                startActivity(intent);
                break;
        }
    }

    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    // 액티비티 시작시
    public void onStart() {
        super.onStart();
        activateBluetooth(); // 블루투스 활성화
    }
}