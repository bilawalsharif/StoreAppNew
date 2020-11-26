package com.example.chofem.store.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chofem.store.R;
import com.example.chofem.store.utils.SharedPref;


public class SplashActivity extends AppCompatActivity {
    private Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Context c = getApplicationContext();
        Handler handler = new Handler();
//        HttpAsyncTask httpAsyncTask = new HttpAsyncTask();
//        httpAsyncTask.doInBackground("");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPref.getInstance(SplashActivity.this).getData().getSTORE_ID().equalsIgnoreCase("")) {
                    Intent intent = new Intent(SplashActivity.this, Login_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, DefaultDrawerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                }
            }
        }, 3000);
    }


}
