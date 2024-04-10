package com.example.pravaah.services.splashes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import com.example.pravaah.R;
import com.example.pravaah.services.TodoService;
import com.skyfishjy.library.RippleBackground;

public class TodoSplashScreen extends AppCompatActivity {
    private ImageView mimage;
    private static int SPLASH_TIME_OUT = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_splash_screen);

        mimage = findViewById(R.id.image);
//      mimage.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bulge));

        RippleBackground rippleBackground = findViewById(R.id.ripple_animation);
        rippleBackground.startRippleAnimation();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(TodoSplashScreen.this, TodoService.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}