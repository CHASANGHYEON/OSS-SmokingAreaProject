package com.example.smokingareaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class find_id extends AppCompatActivity {

    private ImageButton back;
    private EditText id_name;
    private EditText id_RRN1;
    private EditText id_RRN2;
    private Button btn_fid;
    private TextView find_id;

    private long backKeyPressedTime = 0;
    private Toast toast;

    phpDown task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);


        back = findViewById(R.id.back);
        id_name = findViewById(R.id.id_name);
        id_RRN1 = findViewById(R.id.id_RRN1);
        id_RRN2 = findViewById(R.id.id_RRN2);
        btn_fid = findViewById(R.id.btn_fid);
        find_id = findViewById(R.id.find_id);

        task = new phpDown();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.smokingareaproject.find_id.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        btn_fid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = id_name.getText().toString();
                String userRRN = id_RRN1.getText().toString() + id_RRN2.getText().toString();
                if (userName.equals("")||userRRN.equals("")){
                    Toast.makeText(com.example.smokingareaproject.find_id.this, "빈칸을 다 채워 주세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        task.execute("http://smokingarea.dothome.co.kr/find_id.php?userName=\"" + userName + "\"&userRRN=" + userRRN);
                    } catch (Exception e) {
                        e.printStackTrace();
                        task.cancel(true);
                        task = new phpDown();
                    }
                }
            }
        });


    }

    private class phpDown extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            infoItem item = new infoItem();
            try {
                JSONObject jsonObject = new JSONObject(str);

                JSONArray infoArray = jsonObject.getJSONArray("USER");

                for (int i = 0; i < infoArray.length(); i++) {
                    JSONObject infoObject = infoArray.getJSONObject(i);
                    item.setuserID(infoObject.getString("userID"));
                    item.setuserPassword(infoObject.getString("userPassword"));
                }

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            find_id.setText(item.getuserID());
        }
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
