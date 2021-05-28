package com.example.smokingareaproject;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class  info extends AppCompatActivity {

    private final String host = "192.168.0.37";
    private final String username = "smokingarea";
    private final String password = "smoking1!";
    private final String TAG = "Connect FTP";
    private ConnectFTP ConnectFTP;
    private final int port = 21;

    private ImageView iv_1;

    private Toast toast;

    phpDown task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        iv_1 = findViewById(R.id.iv_1);
        task = new phpDown();

        int no = getIntent().getIntExtra("marker", 0);

        try {
            task.execute("http://smokingarea.dothome.co.kr/info_load.php?no=" + no);
        } catch (Exception e) {
            e.printStackTrace();
            task.cancel(true);
            task = new phpDown();
            task.execute("http://smokingarea.dothome.co.kr/info_load.php?no=" + no);
        }


        ConnectFTP = new ConnectFTP();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean status = false;
                try {
                    status = ConnectFTP.ftpConnect(host, username, password, port);
                    if (status == true) {
                        Log.d(TAG, "Connection Success");
                    } else {
                        Log.d(TAG, "Connection failed");
                    }

                    InputStream image = ConnectFTP.retrieveFileStream(no + ".jpg");
                    bmImg = BitmapFactory.decodeStream(image);
                    iv_1.setImageBitmap(bmImg);

                    boolean result = ConnectFTP.ftpDisconnect();
                    if (result == true)
                        Log.d(TAG, "DisConnection Success");
                    else
                        Log.d(TAG, "DisConnection Success");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
}
