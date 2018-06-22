package com.shardatech.shardauniversity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import method.Constant;
import method.MySharedPref;

/**
 * Created by sharda on 5/31/2017.
 */

public class SplashActivity extends AppCompatActivity{

    private static int      SPLASH_TIME_OUT = 3000;
    private static final    String TAG      = HomePageActivity.class.getSimpleName();
    public static int      COLLAGE_ID      = 1;
    public static String   COLLAGE_NAME    = "Sharda";
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        MySharedPref.setUserCollegeId(this,Constant.USER_COLLAGE_ID,COLLAGE_ID);
        MySharedPref.setUserCollegeName(this,Constant.USER_COLLAGE_NAME,COLLAGE_NAME);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.shardatech.shardauniversity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String s = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP);

                Log.e("HASH KEY ", s);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((MySharedPref.getShowFirstTimeLoginActivity(SplashActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY)))
                {
                    Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
