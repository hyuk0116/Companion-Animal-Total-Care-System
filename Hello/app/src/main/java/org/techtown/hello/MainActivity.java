package org.techtown.hello;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private String TAG = "[MYLOG]";
    private TextView useremail;
    private String emailobj;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //아이디 값 받아옴
        Intent intent = getIntent();
        LoginObject loginObject = (LoginObject) intent.getSerializableExtra("LoginObject");
        useremail = (TextView)findViewById(R.id.useremail);
        useremail.setText("[ " + loginObject.getEmail() + " ] 으로 로그인 중입니다.");

        emailobj = loginObject.getEmail(); //아이디값 저장

        //팝업 알림
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toastR.string.msg_token_fmttoken
                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        //이미지 버튼 정의
        ImageButton diseasebtn = (ImageButton)findViewById(R.id.btn_disease);
        ImageButton feedbtn = (ImageButton)findViewById(R.id.btn_feed);
        ImageButton weightbtn = (ImageButton)findViewById(R.id.btn_fat);
        ImageButton tipbtn = (ImageButton)findViewById(R.id.btn_tipboard);
        ImageButton infobtn = (ImageButton)findViewById(R.id.btn_info);


        //내정보로 가는 버튼
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_info=new Intent(MainActivity.this,MyInfo.class);
                intent_info.putExtra("id", emailobj);
                startActivity(intent_info);
            }
        });

        //사료추천으로 가는 버튼
        feedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_feed=new Intent(MainActivity.this,Recommend.class);
                intent_feed.putExtra("id", emailobj);
                startActivity(intent_feed);
            }
        });

        //질병조회로 가는 버튼
        diseasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_disease=new Intent(MainActivity.this,Disease.class);
                intent_disease.putExtra("id", emailobj);
                startActivity(intent_disease);
            }
        });

        //체중관리로 가는 버튼
        weightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_weight=new Intent(MainActivity.this,Blutooth.class);
                intent_weight.putExtra("id", emailobj);
                startActivity(intent_weight);
            }
        });

        //게시판으로 가는 버튼
        tipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_tip=new Intent(MainActivity.this,Board.class);
                startActivity(intent_tip);
            }
        });
    }

    //뒤로가기 종료 버튼
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
