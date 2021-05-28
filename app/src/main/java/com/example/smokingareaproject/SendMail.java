package com.example.smokingareaproject;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "ghdi1434@gmail.com"; // 보내는 계정의 id
    String password = "ghTJS159514!"; // 보내는 계정의 pw


    public void sendSecurityCode(Context context, String sendTo, String rand) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("인증메일입니다.", rand, sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

