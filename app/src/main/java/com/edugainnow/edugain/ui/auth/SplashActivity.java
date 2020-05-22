package com.edugainnow.edugain.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.edugainnow.edugain.MainActivity;
import com.edugainnow.edugain.R;

public class SplashActivity extends AppCompatActivity {

    Animation downtoup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash);

        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        ImageView imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setAnimation(downtoup);

        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            Intent i = new Intent(SplashActivity.this, MainScreen.class);
            startActivity(i);
            finish();
        }, 5000);
    }
}
