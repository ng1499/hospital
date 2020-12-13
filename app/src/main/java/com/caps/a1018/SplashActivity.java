package com.caps.a1018;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(3000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,HospitalListActivity.class));
        finish();
        }
}
