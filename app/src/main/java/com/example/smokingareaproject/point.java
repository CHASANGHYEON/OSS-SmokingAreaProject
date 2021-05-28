package com.example.smokingareaproject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public class point extends AppCompatActivity {

    private ImageButton back;
    private EditText st_point;
    private Button next;

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point);

        back = findViewById(R.id.back);
        st_point = findViewById(R.id.st_point);
        next = findViewById(R.id.next);
        String userID = getIntent().getStringExtra("userID");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.smokingareaproject.point.this, mypage.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = st_point.getText().toString();
                if (address.equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 채워주세요!", Toast.LENGTH_LONG).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Geocoder geocoder = new Geocoder(com.example.smokingareaproject.point.this, Locale.KOREA);
                                List<Address> addresses = geocoder.getFromLocationName(address, 3);

                                StringBuffer buffer = new StringBuffer();
                                for (Address t : addresses) {
                                    buffer.append(t.getLatitude() + ", " + t.getLongitude() + "\n");
                                }
                                //대표 좌표값(첫번쨰 결과)의 경도 , 위도
                                String latitude = String.format("%.9f", addresses.get(0).getLatitude());
                                String longitude = String.format("%.9f", addresses.get(0).getLongitude());

                                try {
                                    FileOutputStream fos = openFileOutput
                                            ("myfile.txt", // 파일명 지정
                                                    Context.MODE_PRIVATE);// 저장모드
                                    PrintWriter out = new PrintWriter(fos);
                                    out.println(latitude + "\n" + longitude);
                                    out.close();

                                    Intent intent = new Intent(com.example.smokingareaproject.point.this, mypage.class);
                                    intent.putExtra("userID", userID);
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
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
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}