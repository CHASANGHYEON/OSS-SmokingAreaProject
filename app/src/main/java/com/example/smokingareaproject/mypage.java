package com.example.smokingareaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class mypage extends AppCompatActivity {
    private ImageButton back;
    private Button logout;
    private Button btn_start;
    private TextView id_copy;

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        id_copy = findViewById(R.id.id_copy);
        String userID = getIntent().getStringExtra("userID");
        id_copy.setText(userID+"님 반갑습니다!");

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {    //초기위치 설정
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.smokingareaproject.mypage.this, point.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.smokingareaproject.mypage.this, MainActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

       logout = findViewById(R.id.logout);
       logout.setOnClickListener(new View.OnClickListener() {    //로그아웃
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.smokingareaproject.mypage.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}