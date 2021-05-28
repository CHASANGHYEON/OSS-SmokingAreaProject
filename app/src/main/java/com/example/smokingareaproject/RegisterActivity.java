package com.example.smokingareaproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id;
    private EditText et_pass;
    private EditText et_pass1;
    private EditText et_name;
    private EditText et_RRN1;
    private EditText et_RRN2;
    private EditText et_email;
    private EditText et_pn1;
    private EditText et_pn2;
    private EditText et_pn3;
    private Button btn_reg;
    private Button btn_confirm;
    private ImageButton back;

    private long backKeyPressedTime = 0;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        class Reference {
            int emailConfirm = 0;

            private void convert(int value) {
                this.emailConfirm = value;
            }
        }

        Reference reference = new Reference();

        // 아이디 값 찾아주기
        back = findViewById(R.id.back);
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_pass1 = findViewById(R.id.et_pass1);
        et_name = findViewById(R.id.et_name);
        et_RRN1 = findViewById(R.id.et_RRN1);
        et_RRN2 = findViewById(R.id.et_RRN2);
        btn_reg = findViewById(R.id.btn_reg);
        btn_confirm = findViewById(R.id.btn_confirm);
        et_email = findViewById(R.id.et_email);
        et_pn1 = findViewById(R.id.et_pn1);
        et_pn2 = findViewById(R.id.et_pn2);
        et_pn3 = findViewById(R.id.et_pn3);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.smokingareaproject.RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random = (int) (Math.random() * (999999 - 100000 + 1)) + 1;
                String rand = Integer.toString(random);

                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), et_email.getText().toString(), rand);

                AlertDialog.Builder ad = new AlertDialog.Builder(com.example.smokingareaproject.RegisterActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("이메일 인증");
                ad.setMessage("인증번호 를 입력해주십시오.");

                final EditText email_confirm = new EditText(com.example.smokingareaproject.RegisterActivity.this);
                ad.setView(email_confirm);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //이메일인증 성공
                        if (rand.equals(email_confirm.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "인증되었습니다!", Toast.LENGTH_SHORT).show();
                            reference.convert(1);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "인증에 실패하셨습니다!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        // 회원가입 버튼 클릭시 수행
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();
                String userPassword1 = et_pass1.getText().toString();
                String userName = et_name.getText().toString();
                String userRRN = et_RRN1.getText().toString() + et_RRN2.getText().toString();
                String phoneNumber = et_pn1.getText().toString() + et_pn2.getText().toString() + et_pn3.getText().toString();
                String userEmail = et_email.getText().toString();

                if (userID.equals("") || userPassword.equals("") || userPassword1.equals("") || userName.equals("") || userEmail.equals("") || userRRN.equals("")|| userPassword.equals("")) {
                    Toast.makeText(com.example.smokingareaproject.RegisterActivity.this, "빈칸을 다 채워 주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if (userPassword == userPassword1) {
                        if (reference.emailConfirm == 1) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);  //예외처리 서버통신이 잘 되었냐 회원등록이 잘되엇냐
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) { // 회원등록에 성공한 경우
                                            Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(com.example.smokingareaproject.RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else { //회원등록에 실패한 경우
                                            Toast.makeText(getApplicationContext(), "회원등록에 실패했습니다!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            // 서버로 Volley를 이용해서 요청을 함
                            RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userRRN, phoneNumber, userEmail, reference.emailConfirm, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(registerRequest);
                        } else {
                            Toast.makeText(getApplicationContext(), "이메일 인증을 해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 서로 다릅니다!", Toast.LENGTH_SHORT).show();
                    }
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
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}