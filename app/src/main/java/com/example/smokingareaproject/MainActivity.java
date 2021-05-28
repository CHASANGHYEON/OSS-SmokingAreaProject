package com.example.smokingareaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;         //지도
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync((this));
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double x = null, y = null;
        try {
            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data = new StringBuffer();
            FileInputStream fis = openFileInput("myfile.txt");//파일명
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(fis));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
            x = Double.parseDouble(str);
            str = buffer.readLine();
            y = Double.parseDouble(str);
            if (x == null || y == null) {
                LatLng startingPoint = new LatLng(36.7989522, 127.072742);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 14));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(startingPoint));
            } else {
                LatLng startingPoint = new LatLng(x, y);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 14));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(startingPoint));
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
            LatLng startingPoint = new LatLng(36.7989522, 127.072742);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 14));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(startingPoint));
        }

        //마커 클릭시 실행
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Intent info = new Intent(MainActivity.this, info.class);
                info.putExtra("marker", Integer.parseInt(marker.getTitle()));
                info.putExtra("userID", getIntent().getStringExtra("userID"));
                startActivity(info);
                return false;
            }
        });
    }
}