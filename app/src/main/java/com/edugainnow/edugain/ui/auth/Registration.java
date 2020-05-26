package com.edugainnow.edugain.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.Utils;

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
    private RadioGroup rgroup;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_registration);

        initView();
    }
    public void initView()
    {

        edtUserName = findViewById(R.id.edtUserName);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserMobile = findViewById(R.id.edtUserMobile);
        edtUserPassword = findViewById(R.id.edtUserPassword);

        ImageView imgMale = findViewById(R.id.imgMale);
        ImageView imgMaleClick = findViewById(R.id.imgMaleClick);
        ImageView imgFeMale = findViewById(R.id.imgFeMale);
        ImageView imgFeMaleClick = findViewById(R.id.imgFeMaleClick);

        imgMaleClick.setOnClickListener(view -> {
            imgMale.setVisibility(View.VISIBLE);
            imgFeMale.setVisibility(View.GONE);
            gender = "Male";
        });

        imgFeMaleClick.setOnClickListener(view -> {
            imgMale.setVisibility(View.GONE);
            imgFeMale.setVisibility(View.VISIBLE);
            gender = "Female";
        });


      /*  rgroup = findViewById(R.id.rgroup);

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(R.id.rbFemale==checkedId)
                {
                    gender = "Female";
                }
                else {
                    gender = "Male";
                }
            }
        });*/
        findViewById(R.id.btnSignUp).setOnClickListener(v -> getSignUp());





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
        Utils.customProgress(Registration.this,"Checking Details ...");
        userName = edtUserName.getText().toString();
        userEmail = edtUserEmail.getText().toString();
        userMobile = edtUserMobile.getText().toString();
        userPassword = edtUserPassword.getText().toString();

        JSONObject object = new JSONObject();
        try {
            object.put("Password",userPassword);
            object.put("FullName",userName);
            object.put("DOB","");
            object.put("Gender",gender);
            object.put("Mobile",userMobile);
            object.put("Email",userEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("responce"+object);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.REGISTERATION, object,
                response -> {

            Utils.customProgressStop();
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
