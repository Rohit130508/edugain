package com.edugainnow.edugain.profile;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.edugainnow.edugain.BuildConfig;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Utils;
import com.edugainnow.edugain.util.WebService;
import com.edugainnow.edugain.util.Apis;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class MainProfile extends AppCompatActivity
{

    AppCompatTextView
            txt_emailId,
            txt_mobileNumber,
            version;

    RelativeLayout
            rl14;

    CoordinatorLayout coordinatorLayout;

    String userId,
            tokenId,
            userName,
            userMail;


    final Context context = MainProfile.this;

    private String mobileNumber,
            userProfileImage;
    private String appPakageName;

//    public static void  openDialogNoInternet(Context context)
//    {
//        Dialog dialog1 = new Dialog(context);
//        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog1.setContentView(R.layout.no_internet);
//        dialog1.show();
//        Window window = dialog1.getWindow();
//        assert window != null;
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        AppCompatButton restart = dialog1.findViewById(R.id.retry);
//
//        restart.setOnClickListener(v -> {
//
//            dialog1.dismiss();
//            context.startActivity(new Intent(context, FirstScreen.class)
//            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//        });
//    }

//    void getPreferencesValue()
//    {
//        userId = CustomPerference.getString(context, CustomPerference.USER_ID);
//        tokenId = CustomPerference.getString(context, CustomPerference.USER_TOKEN);
//        userName = CustomPerference.getString(context, CustomPerference.USER_NAME);
//        userMail = CustomPerference.getString(context, CustomPerference.USER_EMAIL);
//        appPakageName = getApplicationContext().getPackageName();
//        mobileNumber = CustomPerference.getString(context, CustomPerference.USER_MOBILE);
//        userProfileImage = CustomPerference.getString(context, CustomPerference.USER_PROFILE_IMAGE);
//    }

    void initView() {

        coordinatorLayout = findViewById(R.id.layout);

        txt_mobileNumber = findViewById(R.id.txt_mobileNumber);
        txt_mobileNumber.setText(mobileNumber);

        txt_emailId = findViewById(R.id.txt_emailId);
        txt_emailId.setText(userMail);

        findViewById(R.id.about).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
        .putExtra("url", Apis.BASEURL+"aboutus")));
        findViewById(R.id.terms).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
        .putExtra("url", Apis.BASEURL+"term_condition")));
        findViewById(R.id.support).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
        .putExtra("url", Apis.BASEURL+"contact")));

        findViewById(R.id.review).setOnClickListener(view -> openAppRating(context));
//        findViewById(R.id.share).setOnClickListener(view -> shareApp());
        findViewById(R.id.txt_updateProfile).setOnClickListener(view -> startActivity(new Intent(context, UpdateProfile.class)));
        findViewById(R.id.change_password).setOnClickListener(view -> startActivity(new Intent(context, ChangePassword.class)));
        findViewById(R.id.view_profile).setOnClickListener(view -> startActivity(new Intent(context, ViewProfile.class)));


        rl14 = findViewById(R.id.my_orders);

        ImageView imgProfilePicture =  findViewById(R.id.imgProfilePicture);
        Utils.Picasso(userProfileImage,imgProfilePicture);
        imgProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(context,FullScreenPopupWindow.class);
            intent.putExtra("image",userProfileImage);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(MainProfile.this).toBundle());
            else
                startActivity(new Intent(context,FullScreenPopupWindow.class));

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        setContentView(R.layout.activity_mainprofile);

//        getPreferencesValue();

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(userName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        version = findViewById(R.id.version);

        String verName =  "version : " + BuildConfig.VERSION_NAME;
        version.setText(verName);

        initView();

        AppBarLayout mAppBarLayout =  findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        getPreferencesValue();
        initView();
    }

    public static void openAppRating(Context context) {
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.vexilinfotech.etaka_wallet")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }

//    void shareApp()
//    {
////            String RefLink = sharedpreferences.getString("ReferralCode",null);
//            String RefLink = CustomPerference.getString(context,CustomPerference.USER_REFERALCODE);
//
//            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            String shareBody = "https://play.google.com/store/apps/details?id="+appPakageName +"&referrer=" +RefLink;
//            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "FreedomStar");
//            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));
//    }


}
