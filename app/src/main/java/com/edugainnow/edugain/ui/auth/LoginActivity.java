package com.edugainnow.edugain.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.MainActivity;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.apis;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

        private EditText edtUserMobile;
        private EditText edtUserPassword;


        private String
                userMobile,
                userPassword;


        public void initView()
        {


            edtUserMobile = findViewById(R.id.edtUserMobile);
            edtUserPassword = findViewById(R.id.edtUserPassword);

            findViewById(R.id.btnSignUp).setOnClickListener(v -> getSignIn());


            userMobile = edtUserMobile.getText().toString();
            userPassword = edtUserPassword.getText().toString();


        }
        boolean validation()
        {
            if(!TextUtils.isEmpty(userMobile) &&
                    !TextUtils.isEmpty(userPassword) )
                Toast.makeText(LoginActivity.this, "All Feilds are mandatory", Toast.LENGTH_LONG).show();

            else
                return true;

            return false;



        }
        void getSignIn()
        {
            if(validation())
                executeSignUp();
            else
                Toast.makeText(LoginActivity.this,"InValidate",Toast.LENGTH_LONG).show();

        }

        public void executeSignUp()
        {
            JSONObject object = new JSONObject();
            try {
                object.put("Password",userPassword);
                object.put("RregID",userMobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apis.REGISTERATION, object,
                    response -> {

                        startActivity(new Intent(LoginActivity.this, MainActivity.class).
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    }, error -> {

            });

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);

        }
    }
