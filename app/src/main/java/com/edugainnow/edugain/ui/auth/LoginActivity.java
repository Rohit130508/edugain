package com.edugainnow.edugain.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.MainActivity;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

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
                userMobile = null,
                userPassword = null;


        public void initView()
        {

            edtUserMobile = findViewById(R.id.edtUserMobile);
            edtUserPassword = findViewById(R.id.edtUserPassword);



            findViewById(R.id.btnSignUp).setOnClickListener(v -> getSignIn());


        }
        boolean validation()
        {
            return !userMobile.isEmpty() &&
                    !userPassword.isEmpty();

        }
        void getSignIn()
        {

            userMobile = edtUserMobile.getText().toString().trim();
            userPassword = edtUserPassword.getText().toString();

            if(validation())
                if(Utils.isNetworkAvailable(LoginActivity.this))
                    executeSignIn();
                else Utils.CustomAlert(LoginActivity.this,R.string.nointernet_title,
                        R.string.nointernet_message);
            else
                Utils.CustomAlert(LoginActivity.this,R.string.logintitle,
                        R.string.loginmess);

        }

        public void executeSignIn()
        {
            Utils.customProgress(LoginActivity.this,"Loging you in ...");
            JSONObject object = new JSONObject();
            try {
                object.put("Password",userPassword);
                object.put("RregID",userMobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("request"+object);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.Login, object,
                    response -> {

                        try {
                            String result = response.getString("result");

                            Utils.customProgressStop();

                            if(result.equalsIgnoreCase("Login Successfully"))
                            {
                                String regId = response.getString("Reg");
                                String userName = response.getString("name");

                                CustomPerference.putString(LoginActivity.this,CustomPerference.USER_NAME,userName);
                                CustomPerference.putString(LoginActivity.this,CustomPerference.USER_MOBILE,userMobile);
                                CustomPerference.putString(LoginActivity.this,CustomPerference.USER_PASSWORD,userPassword);
                                CustomPerference.putString(LoginActivity.this,CustomPerference.USER_ID,regId);
                                CustomPerference.putBoolean(LoginActivity.this,CustomPerference.ISLOGIN,true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                            else
                            {
                                Utils.CustomAlert(LoginActivity.this,R.string.logintitle,
                                        R.string.loginmess);
                            }
                        } catch (JSONException e) {
                            Utils.customProgressStop();
                            Utils.CustomAlert(LoginActivity.this,R.string.servertit,
                                    R.string.servermess);
                            e.printStackTrace();
                        }



                    }, error -> {

                if(error.networkResponse.statusCode==400)
                {
                    Utils.customProgressStop();
                    Utils.CustomAlert(LoginActivity.this,R.string.logintitle,
                            R.string.loginmess);

                }

            });

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);

        }
    }
