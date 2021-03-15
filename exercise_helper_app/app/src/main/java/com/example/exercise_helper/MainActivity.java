package com.example.exercise_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    private Button btnConnect;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        블루투스 설정
        https://blog.codejun.space/13
        https://chaniii.tistory.com/7 참고
         */
        bt = new BluetoothSPP(this); //Initializing

        // 데이터 수신
        circleProgressBar = findViewById(R.id.progress);
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); // 토스트로 데이터 띄움
                try{
                    int power = (int) (Double.parseDouble(message)*100);// Double <-> String
                    circleProgressBar.setProgress(power);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                setResponse("Connected to " + name + "\n" + address, "connected...", "disconnect"); // 연결성공
            }

            public void onDeviceDisconnected() { 
                setResponse("연결이 해제되었습니다.", "disconnected...", "connect"); // 연결해제
            }

            public void onDeviceConnectionFailed() { //연결실패
                setResponse("연결에 실패하였습니다.", "disconnected...", "connect"); // 연결실패
            }
        });

        // 연결 시도: 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
        btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    private void setResponse(String message_toast, String message_textView, String message_button){
        Toast.makeText(getApplicationContext(), message_toast , Toast.LENGTH_SHORT).show();

        TextView textViewReceive = findViewById(R.id.textViewReceive);
        textViewReceive.setText(message_textView);

        btnConnect.setText(message_button);
    }

    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
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
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}