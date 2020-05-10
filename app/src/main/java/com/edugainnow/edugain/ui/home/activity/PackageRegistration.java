package com.edugainnow.edugain.ui.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageRegistration extends AppCompatActivity {

    private String userName,userMobile,
            packageName,amount,packageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        setContentView(R.layout.activity_package_registration);

        preferencesValue();
        initView();
        getExecuteMethods();


    }

    private EditText edtPackageName,
            edtAmount,
            edtRegNum,
            edtUserName,
            edtMobileNum;
    private Button btnPackageReg;

    void initView()
    {
        edtPackageName = findViewById(R.id.edtPackageName);
        edtAmount = findViewById(R.id.edtAmount);
        edtRegNum = findViewById(R.id.edtRegNum);
        edtUserName = findViewById(R.id.edtUserName);
        edtMobileNum = findViewById(R.id.edtMobileNum);

        btnPackageReg = findViewById(R.id.btnPackageReg);


        edtUserName.setText(userName);
        edtMobileNum.setText(userMobile);
        edtPackageName.setText(packageName);
        edtAmount.setText(amount);
        edtRegNum.setText(userMobile);



        btnPackageReg.setOnClickListener(v -> getExecuteMethods());
    }

    void preferencesValue()
    {
        userName = CustomPerference.getString(PackageRegistration.this,CustomPerference.USER_NAME);
        userMobile = CustomPerference.getString(PackageRegistration.this,CustomPerference.USER_MOBILE);

        //INTENT VALUES

        packageName = getIntent().getStringExtra("packName");
        amount = getIntent().getStringExtra("amount");
        packageId = getIntent().getStringExtra("packageId");
    }

    void getExecuteMethods()
    {
        if(Utils.isNetworkAvailable(this))
            executeSavePackage();

        else
            Utils.CustomAlert(this,R.string.nointernet_title,R.string.nointernet_message);
    }

    void executeSavePackage()
    {

        Utils.customProgress(this,"Please wait...");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RegID",edtRegNum.getText().toString().trim());
            jsonObject.put("UserName",edtUserName.getText().toString().trim());
            jsonObject.put("Mobile",edtMobileNum.getText().toString().trim());
            jsonObject.put("packageID",packageId);
            jsonObject.put("packageName",edtPackageName.getText().toString().trim());
            jsonObject.put("Amount",edtAmount.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.PackageRegistration, jsonObject,
                response -> {

            Utils.customProgressStop();
                    System.out.println("Responce"+response);



                }, error -> {
                        Utils.customProgressStop();

                });

        RequestQueue queue = Volley.newRequestQueue(PackageRegistration.this);
        request.setRetryPolicy( new DefaultRetryPolicy(20*1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
