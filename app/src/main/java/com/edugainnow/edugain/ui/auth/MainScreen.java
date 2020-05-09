package com.edugainnow.edugain.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.edugainnow.edugain.R;

public class MainScreen extends AppCompatActivity {

    public CardView cardLogin;

    void initview()
    {
        findViewById(R.id.cardLogin).setOnClickListener(v -> startActivity
                (new Intent(MainScreen.this,LoginActivity.class)));
        findViewById(R.id.cardRegistration).setOnClickListener(v -> startActivity
                (new Intent(MainScreen.this,Registration.class)));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initview();
    }
}
