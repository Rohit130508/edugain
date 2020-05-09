package com.edugainnow.edugain.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.apis;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {

    private EditText edtUserName;
    private EditText edtUserEmail;
    private EditText edtUserMobile;
    private EditText edtUserPassword;


    private String userName,
            userEmail,
            userMobile,
            userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();
    }
    public void initView()
    {

        edtUserName = findViewById(R.id.edtUserName);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserMobile = findViewById(R.id.edtUserMobile);
        edtUserPassword = findViewById(R.id.edtUserPassword);

        findViewById(R.id.btnSignUp).setOnClickListener(v -> getSignUp());


        userName = edtUserName.getText().toString();
        userEmail = edtUserEmail.getText().toString();
        userMobile = edtUserMobile.getText().toString();
        userPassword = edtUserPassword.getText().toString();


    }
    boolean validation()
    {
        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userMobile) &&
                !TextUtils.isEmpty(userPassword) )
            Toast.makeText(Registration.this, "All Feilds are mandatory", Toast.LENGTH_LONG).show();

        else
            return true;

            return false;



    }
    void getSignUp()
    {
        if(validation())
            executeSignUp();
        else
            Toast.makeText(Registration.this,"InValidate",Toast.LENGTH_LONG).show();

    }

    public void executeSignUp()
    {
        JSONObject object = new JSONObject();
        try {
            object.put("Password",userPassword);
            object.put("FullName",userName);
            object.put("DOB","");
            object.put("Gender","");
            object.put("Mobile",userMobile);
            object.put("Email",userEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apis.REGISTERATION, object,
                response -> {

                            startActivity(new Intent(Registration.this,LoginActivity.class).
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                }, error -> {

                });

        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }
}
