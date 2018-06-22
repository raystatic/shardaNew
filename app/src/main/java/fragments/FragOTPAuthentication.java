package fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.shardatech.shardauniversity.AppApplication;
import com.shardatech.shardauniversity.FeedbackUs;
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

public class FragOTPAuthentication extends Fragment {

    private int categoryId,collegeId,userId;
    private String categoryCode;
    private String categoryColor;
    String mobileNumberForOTPVerification;
    CountryCodePicker ccp;
    EditText mobileNumber;
    Button btnSendOtp;
    String categoryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_otp_authentication, null);

        ccp             = (CountryCodePicker) v.findViewById(R.id.ccp);
        mobileNumber    = (EditText)v.findViewById(R.id.editTextNumber);
        btnSendOtp      = (Button) v.findViewById(R.id.btn_send_otp);

        categoryId      = getArguments().getInt(HomeActivityCategoryTable.CATEGORY_Id);
        categoryColor   = getArguments().getString(HomeActivityCategoryTable.CATEGORY_COLOR);
        categoryCode    = getArguments().getString(HomeActivityCategoryTable.CATEGORY_CODE);
        categoryName    = getArguments().getString(HomeActivityCategoryTable.CATEGORY_NAME);

        collegeId       = MySharedPref.getUserCollegeId(getActivity(),Constant.USER_COLLAGE_ID);
        userId          = MySharedPref.getUserId(getActivity(),Constant.USER_ID);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("OTP Authentication");

        sendFragmentAnalytics("Category Name = " + "OTP Authentication" + " , User Id = " + userId);

        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mobileNumber.getText().toString().isEmpty() || mobileNumber.length() <= 9 ) {
                    btnSendOtp.setBackgroundResource(R.drawable.ripple_effect_submit_norma_backl);
                }else if (mobileNumber.length() >= 9) {
                    btnSendOtp.setBackgroundResource(R.drawable.ripple_effect_submit_normal);
                }
            }
            public void afterTextChanged(Editable s) {
            }
        });

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isConnectingToInternet(getActivity().getApplicationContext())) {
                    mobileNumberForOTPVerification = ccp.getSelectedCountryCode().toString() + mobileNumber.getText().toString();
                    numberVerifyJsonLoad(getActivity().getApplicationContext(), userId, mobileNumberForOTPVerification);
                }else{
                    aleartDialogForInternetConnectivity();
                }
            }
        });
        return v;
    }

    public void numberVerifyJsonLoad(final Context mContext, final int userid, final String mobileNumberForOTPVerify)
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
