package com.shardatech.shardauniversity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import databaseTable.HomeActivityCategoryTable;
import fragments.FragCommonAcadCampHealth;
import fragments.FragOTPAuthentication;
import fragments.FragOTPVerification;
import fragments.FragTypeMap2;
import fragments.FragmentApplyNowForAdmission;
import fragments.FragmentGroupsAndClubs;
import method.Constant;
import method.HelperFunction;
import method.MySharedPref;

import static method.HelperFunction.STORAGE_PERMISSION_CODE;

/**
 * Created by sharda on 9/10/2017.
 */

public class SubCategoryActivity extends AppCompatActivity {
    int getCategoryId;
    String getCategoryName;
    String getCategoryColor;
    String getCategoryCode;
    private FirebaseAnalytics mFirebaseAnalytics;

    boolean checkActivity = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        getCategoryId = getIntent().getIntExtra(HomeActivityCategoryTable.CATEGORY_Id, 0);
        getCategoryCode = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_CODE);
        getCategoryName = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_NAME);
        getCategoryColor = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);
        checkActivity = getIntent().getBooleanExtra("checkActivity", false);

        if (!Constant.isConnectingToInternet(SubCategoryActivity.this)) {
        }

        if (getCategoryColor == null)
            finish();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getCategoryName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getCategoryColor)));

        FragCommonAcadCampHealth fragCommonAcadCampHealth = new FragCommonAcadCampHealth();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        int userId = MySharedPref.getUserId(this, Constant.USER_ID);
        Bundle params = new Bundle();


        switch (getCategoryCode) {
            case "facility":
                params.putInt(FirebaseAnalytics.Param.ITEM_ID, userId);
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, getCategoryName);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                mFirebaseAnalytics.setMinimumSessionDuration(100);
                mFirebaseAnalytics.setSessionTimeoutDuration(500);
                mFirebaseAnalytics.setUserId(String.valueOf(userId));
                mFirebaseAnalytics.setUserProperty("Page", getCategoryName);

                if (getCategoryName.equals("University Login")) {
                    if (!(MySharedPref.getUserOtpVerify(SubCategoryActivity.this, Constant.CHECK_OTP_VERIFICATION)) || checkActivity) {
                        if (checkActivity)
                            MySharedPref.setOtpLoginFromProfile(SubCategoryActivity.this,Constant.SHARED_PREF_KEY_FOR_ACTIVITY_OTP_FROM_PROFILE,true);
                        else
                            MySharedPref.setOtpLoginFromProfile(SubCategoryActivity.this,Constant.SHARED_PREF_KEY_FOR_ACTIVITY_OTP_FROM_PROFILE,false);

                        sendFragmentAnalytics("Category Name = " + getCategoryName + " , User Id = " + userId);

                        FragOTPAuthentication otpFrag = new FragOTPAuthentication();
                        bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, getCategoryId);
                        bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getCategoryColor);
                        otpFrag.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, otpFrag, getCategoryName);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else {
                        bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, getCategoryId);
                        bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getCategoryColor);
                        fragCommonAcadCampHealth.setArguments(bundle);
                        fragmentTransaction.add(R.id.fragment_container, fragCommonAcadCampHealth, getCategoryName);
                        fragmentTransaction.commit();
                    }
                } else {

                    sendFragmentAnalytics("Category Name = " + getCategoryName + " , User Id = " + userId);

                    bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, getCategoryId);
                    bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getCategoryColor);
                    fragCommonAcadCampHealth.setArguments(bundle);
                    fragmentTransaction.add(R.id.fragment_container, fragCommonAcadCampHealth, getCategoryName);
                    fragmentTransaction.commit();
                    //  break;
                }
                break;
            case "maps":
                params.putInt(FirebaseAnalytics.Param.ITEM_ID, userId);
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, getCategoryName);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                mFirebaseAnalytics.setMinimumSessionDuration(100);
                mFirebaseAnalytics.setSessionTimeoutDuration(500);

                mFirebaseAnalytics.setUserId(String.valueOf(userId));
                mFirebaseAnalytics.setUserProperty("Page", getCategoryName);

                sendFragmentAnalytics("Category Name = " + getCategoryName + " , User Id = " + userId);

                FragTypeMap2 mapsFrag = new FragTypeMap2();
                bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, getCategoryId);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getCategoryColor);
                mapsFrag.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, mapsFrag, getCategoryName);
                fragmentTransaction.commit();
                break;

            case "web_view":
                params.putInt(FirebaseAnalytics.Param.ITEM_ID, userId);
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, getCategoryName);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                mFirebaseAnalytics.setMinimumSessionDuration(100);
                mFirebaseAnalytics.setSessionTimeoutDuration(500);

                mFirebaseAnalytics.setUserId(String.valueOf(userId));
                mFirebaseAnalytics.setUserProperty("Page", getCategoryName);

                sendFragmentAnalytics("Category Name = " + getCategoryName + " , User Id = " + userId);

                FragmentApplyNowForAdmission placementCell = new FragmentApplyNowForAdmission();
                bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, getCategoryId);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getCategoryColor);
                placementCell.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, placementCell, "web_view");
                fragmentTransaction.commit();
                break;

            case "notification":
                params.putInt(FirebaseAnalytics.Param.ITEM_ID, userId);
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, getCategoryName);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                mFirebaseAnalytics.setMinimumSessionDuration(100);
                mFirebaseAnalytics.setSessionTimeoutDuration(500);

                mFirebaseAnalytics.setUserId(String.valueOf(userId));
                mFirebaseAnalytics.setUserProperty("Page", getCategoryName);

                sendFragmentAnalytics("Category Name = " + getCategoryName + " , User Id = " + userId);

                Intent intent = new Intent(SubCategoryActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            default:

        }

    }

    private void sendFragmentAnalytics(String TAG) {
        ((AppApplication) this.getApplication()).trackScreenView(TAG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                HelperFunction.helperFunctionOnClickSubCategory(this);
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (MySharedPref.getOtpLoginFromProfile(SubCategoryActivity.this,Constant.SHARED_PREF_KEY_FOR_ACTIVITY_OTP_FROM_PROFILE)){
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            finish();
        }
        else{
            if (getSupportFragmentManager().findFragmentByTag(FragOTPVerification.class.getName()) != null) {
                getSupportFragmentManager().popBackStackImmediate();
            } else {
                finish();
            }
        }
    }

}
