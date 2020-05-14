package com.edugainnow.edugain.ui.uprofi.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;

    private EditText mobNoEt;
    private String walletBal,
            userId,
            tokenId;
    private Context context = CustomScannerActivity.this;

    private void getSharedPreferencesVal()
    {
        userId = CustomPerference.getString(context, CustomPerference.USER_ID);
        tokenId = CustomPerference.getString(context, CustomPerference.USER_TOKEN);
        walletBal = CustomPerference.getString(CustomScannerActivity.this, CustomPerference.USER_WALLET);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Pay");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSharedPreferencesVal();

        DecoratedBarcodeView barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        TextView wallet_bal = findViewById(R.id.wallet_bal);

        mobNoEt = findViewById(R.id.mobNoEt);

        String walletBalance = " \u20B9 " + walletBal;
        wallet_bal.setText(walletBalance);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        changeMaskColor(null);


        /*=========================================================*/
        mobNoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (arg0.length()==10)
                {
//                    executeVerifyCustomer(mobNoEt.getText().toString().trim());
                }
            }
        });
    }

    private void executeVerifyCustomer(String resultCode) {

        Utils.customProgress(context,"Please Wait ...");

        Map<String,String> params = new HashMap<>();
        params.put("UserId", userId);
        params.put("TokenId",tokenId);
        params.put("QRData",resultCode);

        JSONObject jsonObj = new JSONObject(params);

        System.out.println("jsonobject_profile==="+ jsonObj);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Apis.BASEURL, jsonObj,
                response -> {
                    System.out.println("result of profile===" + response);
                    Utils.customProgressStop();
                    try {
                        String status = response.getString("Status").trim();
                        String msg = response.getString("Message").trim();
                        if(status.equalsIgnoreCase("true") ){
                            JSONObject object = response.getJSONObject("Data");
                            String Mobile =  object.getString("Mobile");
                            String userName =  object.getString("Name");
                            String QRData = object.getString("QRData");

                               /* Intent intent=new Intent(CustomScannerActivity.this, PayMoney.class);
                                intent.putExtra("receiverNumber",Mobile);
                                intent.putExtra("receiverName",userName);
                                intent.putExtra("QRData",QRData);
                                startActivity(intent);*/

                        }else if(status.equalsIgnoreCase("false")){
                            Toast.makeText(CustomScannerActivity.this,msg, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(CustomScannerActivity.this,msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.customProgressStop();
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    //  mPostCommentResponse.requestCompleted();
                }, error -> Utils.customProgressStop());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }
//
    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }


    public void changeMaskColor(View view) {
//        Random rnd = new Random();
//        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        viewfinderView.setMaskColor(color);
    }

    @Override
    public void onTorchOn() {
        //switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        //switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
