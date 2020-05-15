package com.edugainnow.edugain.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.edugainnow.edugain.MainActivity;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.CustomPerference;

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
        setTheme(R.style.AppTheme);
        if(CustomPerference.getBoolean(this,CustomPerference.ISLOGIN))
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        setContentView(R.layout.activity_main_screen);

        initview();
    }
}
