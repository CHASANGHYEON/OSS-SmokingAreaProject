package com.example.smokingareaproject;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    // 서버 URL 설정 (PHP연동)
    final static private String URL ="http://smokingarea.dothome.co.kr/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPassword, String userName, String userRRN, String phoneNumber, String userEmail, int emailConfirm, Response.Listener<String> listener){
        super(Method.POST,URL,listener, null);

        map= new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        map.put("userName",userName);
        map.put("userRRN",userRRN);
        map.put("phoneNumber",phoneNumber);
        map.put("userEmail",userEmail);
        map.put("emailConfirm",emailConfirm + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}