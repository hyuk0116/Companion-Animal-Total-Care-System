package org.techtown.hello;

import android.annotation.SuppressLint;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;


public class Blutooth extends AppCompatActivity {
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    TextView mTvDate;
    TextView mTvBluetoothStatus;
    TextView mTvReceiveData;
    TextView mTvFinalData;
    Button mBtnBlueToothOn;
    Button mBtnBlueToothOff;
    Button mBtnConnect;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnetedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    int i=0;
    float s_SUM=0;
    float f_SUM=0;
    String email;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueweight);

        mTvDate = (TextView)findViewById(R.id.tvDate);
        mTvBluetoothStatus = (TextView) findViewById(R.id.tvBluetoothStatus);
        mTvReceiveData = (TextView) findViewById(R.id.tvReceiveData);
        mTvFinalData = (TextView)findViewById(R.id.tvFinaldata);
        mBtnBlueToothOn = (Button) findViewById(R.id.btnBluetoothOn);
        mBtnBlueToothOff = (Button) findViewById(R.id.btnBluetoothOff);
        mBtnConnect = (Button) findViewById(R.id.btnConnect);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        //아이디값 받아오는 부분
        Intent intent = getIntent();
        email = intent.getExtras().getString("id");


        //뒤로가기 버튼
        final ImageButton backbtn = (ImageButton)findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final Button btn_info=(Button)findViewById(R.id.btnToMyInfo);
        //그래프보기로 가는 버튼동작
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_info=new Intent(Blutooth.this, Chart.class);
                intent_info.putExtra("id", email);
                startActivity(intent_info);
            }
        });


        mBtnBlueToothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOn();
            }
        });
        mBtnBlueToothOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOff();
            }
        });
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        mBluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String[] change_m = readMessage.split(" ");

                    if(change_m[0].equals("s")){
                        s_SUM = Float.valueOf(change_m[1]);
                        mTvReceiveData.setText(" " + s_SUM + " Kg ");
                    }
                    else if(change_m[0].equals("f")&&i==0){
                        f_SUM = Float.valueOf(change_m[1]);
                        mTvReceiveData.setText(" " + f_SUM + " Kg ");
                        mTvFinalData.setText(" " + f_SUM + " Kg ");
                        Date time = new Date();

                        String time_text = format.format(time);

                        mTvDate.setText(time_text);
                        i = 1;

                        insertWeight();
                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();

                    }
                    else if(i==1){
                        mTvFinalData.setText(" " + f_SUM + " Kg ");

                    }
                    else mTvReceiveData.setText("오류");

                }
            }
        };

    }


    void bluetoothOn() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                mTvBluetoothStatus.setText("활성화");
            } else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);

            }
        }
    }

    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_LONG).show();
            mTvBluetoothStatus.setText("비활성화");
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case BT_REQUEST_ENABLE:
                if(resultCode == RESULT_OK){ // 블루투스 활성화 클릭
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("활성화");
                } else if(resultCode == RESULT_CANCELED){ // 블루투스 활성화 취소
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("비활성화");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void listPairedDevices(){
        if(mBluetoothAdapter.isEnabled()){
            mPairedDevices=mBluetoothAdapter.getBondedDevices();
            i = 0;

            if(mPairedDevices.size()>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for(BluetoothDevice device:mPairedDevices){
                    mListPairedDevices.add(device.getName());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        connectSelectionDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else{
                Toast.makeText(getApplicationContext(), "페어링 된 장치가 없습니다.", Toast.LENGTH_LONG).show();

            }
        }
        else{
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_LONG).show();

        }
    }

    void connectSelectionDevice(String selectionDeviceName){
        for(BluetoothDevice tempDevice:mPairedDevices){
            if (selectionDeviceName.equals(tempDevice.getName())){
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket=mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnetedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();

        } catch (IOException e){
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private class ConnetedBluetoothThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnetedBluetoothThread(BluetoothSocket socket){
            mmSocket=socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = socket.getInputStream();
                tmpOut=socket.getOutputStream();
            }catch (IOException e){
                Toast.makeText(getApplicationContext(),"소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream=tmpIn;
            mmOutStream=tmpOut;

        }

        public void run(){
            byte[] buffer = new byte[2048];
            int bytes;

            while (true){
                try{
                    bytes = mmInStream.available();
                    if(bytes > 0){
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e){
                    break;
                }
            }
        }
        public void  write(String str){
            byte[] bytes = str.getBytes();
            try{
                mmOutStream.write(bytes);
            } catch (IOException e){
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            } catch (IOException e){
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void insertWeight(){
        class weightDB extends AsyncTask<Void, Integer, Void > {

            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    String link = "http://106.10.53.84/sendWeight.php";
                    String data = URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode(email,"UTF-8") + "&" + URLEncoder.encode("weight", "UTF-8") + "=" + f_SUM + "";

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }
        }
        weightDB wd = new weightDB();
        wd.execute();
    }

}
