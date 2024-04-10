package com.example.pravaah;
// This is the main splash screen from where the application starts its execution.
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashSupplierScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;   // denotes how much time the splash time must be staying on screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_supplier_screen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // to remove the upper toolbar and convert screen to fullscreen

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(SplashSupplierScreen.this, FingerPrintAuth.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);   // a handler delays the execution of a activity by time provided to it(in our case SPLASH_TIME_OUT)
    }
}