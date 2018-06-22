package fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.hbb20.CountryCodePicker;
import com.shardatech.shardauniversity.AppApplication;
import com.shardatech.shardauniversity.HomePageActivity;
import com.shardatech.shardauniversity.LoginActivity;
import com.shardatech.shardauniversity.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import databaseTable.HomeActivityCategoryTable;
import method.Constant;
import method.MySharedPref;


/**
 * Created by sharda on 9/10/2017.
 */

public class FragOTPVerification extends Fragment {

    private int categoryId,collegeId,userId;
    private String categoryCode;
    private String categoryColor;
    private ContentLoadingProgressBar contentLoadingProgressBarSlider;
    String mobileNumberForOTPVerification;
    EditText mobileNumber;
    Button btnVerify;
    String categoryName;
    PinEntryEditText pinEntryEditText;
    String otp;
    TextView textViewResendOtp,textViewOtpTimer,tvOtpVerify;
    RelativeLayout rlResendOtp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_otp_verification, null);

        pinEntryEditText = (PinEntryEditText) v.findViewById(R.id.txt_pin_entry);
        btnVerify        = (Button) v.findViewById(R.id.btn_verify);
        textViewOtpTimer = (TextView) v.findViewById(R.id.tv_otp_timer);
        rlResendOtp      = (RelativeLayout) v.findViewById(R.id.rl_resend_otp);
        tvOtpVerify      = (TextView) v.findViewById(R.id.tv_otp_verify);

        categoryId      = getArguments().getInt(HomeActivityCategoryTable.CATEGORY_Id);
        categoryColor   = getArguments().getString(HomeActivityCategoryTable.CATEGORY_COLOR);
        categoryName    = getArguments().getString(HomeActivityCategoryTable.CATEGORY_NAME);
        categoryCode    = getArguments().getString(HomeActivityCategoryTable.CATEGORY_CODE);
        collegeId       = MySharedPref.getUserCollegeId(getActivity(),Constant.USER_COLLAGE_ID);

        userId          = getArguments().getInt(Constant.USER_ID);
        mobileNumberForOTPVerification = getArguments().getString(Constant.USER_CONTACT_NUMBER);

        tvOtpVerify.setText("Please enter the OTP sent to your entered mobile number.");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("OTP Verification");

        sendFragmentAnalytics("Category Name = " + "OTP Verification" + " , User Id = " + userId);

        new CountDownTimer(51000, 1000) {
            public void onTick(long millisUntilFinished) {
                textViewOtpTimer.setText("(Enter the OTP below.) " + millisUntilFinished / 1000+" second left");
                rlResendOtp.setVisibility(View.GONE);
            }
            public void onFinish() {
                textViewOtpTimer.setText("(Enter the OTP below.)");
                rlResendOtp.setVisibility(View.VISIBLE);
            }
        }.start();

        rlResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtpJsonLoad(getActivity().getApplicationContext(),userId,mobileNumberForOTPVerification);
            }
        });

        pinEntryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pinEntryEditText.getEditableText().toString().isEmpty() || pinEntryEditText.getEditableText().length() <= 3 ) {
                    btnVerify.setBackgroundResource(R.drawable.ripple_effect_submit_norma_backl);
                }else if (pinEntryEditText.getEditableText().length() > 3) {
                    btnVerify.setBackgroundResource(R.drawable.ripple_effect_submit_normal);
                }
            }
            public void afterTextChanged(Editable s) {
            }

        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isConnectingToInternet(getActivity().getApplicationContext())) {
                    otp = pinEntryEditText.getEditableText().toString();
                    numberVerifyJsonLoad(getActivity().getApplicationContext(),userId,mobileNumberForOTPVerification,otp);
                }else{
                    aleartDialogForInternetConnectivity();
                }
            }
        });
        return v;
    }



    public void numberVerifyJsonLoad(final Context mContext, final int userid, final String mobileNumberForOTPVerify, String otp)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.USER_OTP_AUTHENTICATION+"?"+Constant.USER_ID+"="+userid
                        +"&"+Constant.USER_CONTACT_NUMBER+"="+mobileNumberForOTPVerify
                        +"&"+Constant.USER_OTP+"="+otp)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {

            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    if (!isAdded()) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }


                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    boolean success = jsonObject.optBoolean("success");
                    final String message  = jsonObject.optString("error_msg");

                    if (success){
                        Bundle bundle=new Bundle();
                        if(bundle!=null) {
                            MySharedPref.setUserOtpVerify(getActivity(), Constant.CHECK_OTP_VERIFICATION,true);
                            MySharedPref.setUserContactNumber(getActivity(),Constant.USER_CONTACT_NUMBER, mobileNumberForOTPVerify);
                            if (MySharedPref.getOtpLoginFromProfile(getActivity(),Constant.SHARED_PREF_KEY_FOR_ACTIVITY_OTP_FROM_PROFILE)){
                                getActivity().onBackPressed();
                            }
                            else{
                                FragCommonAcadCampHealth fragCommonAcadCampHealth = new FragCommonAcadCampHealth();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                if (fragmentManager.findFragmentByTag(FragOTPVerification.class.getName()) != null) {
                                    for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                        fragmentManager.popBackStack();
                                    }
                                }

                                bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, categoryId);
                                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, categoryColor);
                                fragCommonAcadCampHealth.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container, fragCommonAcadCampHealth, categoryName);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                    }
                    else{
                        MySharedPref.setUserOtpVerify(getActivity(), Constant.CHECK_OTP_VERIFICATION,false);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException paramResponse) {
                    if (!isAdded()) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void resendOtpJsonLoad(final Context mContext, final int userid, final String mobileNumberForOTPVerify)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.USER_MOBILE_AUTHENTICATION+"?"+Constant.USER_ID+"="+userid
                        +"&"+Constant.USER_CONTACT_NUMBER+"="+mobileNumberForOTPVerify)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {

            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    if (!isAdded()) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    boolean success = jsonObject.optBoolean("success");

                    if (success){
                        Bundle bundle=new Bundle();
                        if(bundle!=null) {
                            FragOTPVerification fragOTPVerification = new FragOTPVerification();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            bundle.putInt(HomeActivityCategoryTable.CATEGORY_Id, categoryId);
                            bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, categoryColor);
                            bundle.putInt(Constant.USER_ID, userid);
                            bundle.putString(Constant.USER_CONTACT_NUMBER, mobileNumberForOTPVerify);
                            fragOTPVerification.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_container, fragOTPVerification, fragOTPVerification.getClass().getName());
                            fragmentTransaction.addToBackStack(fragOTPVerification.getClass().getName());
                            fragmentTransaction.commit();
                        }
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), "Please enter a Valid Number", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException paramResponse) {
                    if (!isAdded()) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_internet_title));
        builder.setMessage(getResources().getString(R.string.aleart_dialog_for_internet_message));
        builder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                dialog.dismiss();
                getActivity().finish();
            }
        });
        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
        builder.show();
    }

    private void sendFragmentAnalytics(String TAG) {
        ((AppApplication) getActivity().getApplicationContext()).trackScreenView(TAG);
    }
}
