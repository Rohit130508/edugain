package com.edugainnow.edugain.ui.uprofi.qrscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.CustomPerference;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class GenerateQRCode extends AppCompatActivity {
    Context context = GenerateQRCode.this;

    TextView txt_name;
    ImageView qrImage;

    private String qrCode,
            userName;

    void getPreferencesValue()
    {
       userName = CustomPerference.getString(context,CustomPerference.USER_NAME);
       qrCode = CustomPerference.getString(context,CustomPerference.QRCODE);

        System.out.println("qr===>"+qrCode);

    }
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate_qrcode);
	    qrImage = findViewById(R.id.QR_Image);
        txt_name = findViewById(R.id.txt_name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        getSupportActionBar().setTitle("QR Code");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getPreferencesValue();

        txt_name.setText(userName);

        Glide.with(context)
                .asBitmap()
                .load(qrCode)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        qrImage.setImageBitmap(resource);
                    }
                });

     /*   Picasso.get().load(qrCode)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        qrImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        qrImage.setImageDrawable(errorDrawable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        qrImage.setImageDrawable(placeHolderDrawable);
                    }
                });*/
//        Utils.Picasso("data:image/png;base64,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",qrImage);
//        Utils.Picasso(qrCode,qrImage);

    }

}
