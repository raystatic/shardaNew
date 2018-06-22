package com.shardatech.shardauniversity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import databaseTable.DrawableTable;
import databaseTable.SubCategoryTable;
import method.Constant;
import method.MySharedPref;

/**
 * Created by sharda on 9/27/2017.
 */

public class FeedbackUs extends AppCompatActivity {
    private EditText editTextYourName,editTextYourEmail,editTextYourContact,editTextYourComment;
    Button completeForm;
    private String name,email,comment,possibleEmail,contact;
    ProgressBar loading_spinner;
    private LinearLayout loading_back;
    private int collegeId;
    private int userLoginType;
    private int userId;
    private String collegeName;
    private String userDeviceId;
    private String fcmToken;
    private ImageView phone_click,email_click;
    int subCategoryId;
    String enquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(DrawableTable.DRAWABLE_NAME);
        subCategoryId = intent.getIntExtra(SubCategoryTable.SUB_CATEGORY_Id,0);

        getSupportActionBar().setTitle(message + " Enquiry");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enquiry = message+ " Enquiry";

        editTextYourName            = (EditText) findViewById(R.id.editTextName);
        editTextYourEmail           = (EditText) findViewById(R.id.editTextEmail);
        editTextYourContact         = (EditText) findViewById(R.id.editTextNumber);
        editTextYourComment         = (EditText) findViewById(R.id.editTextComment);

        phone_click                 = (ImageView) findViewById(R.id.phone_click);
        email_click                 = (ImageView) findViewById(R.id.email_click);

        completeForm                = (Button) findViewById(R.id.completeForm);
        loading_spinner             = (ProgressBar) findViewById(R.id.loading_spinner);
        loading_back                = (LinearLayout) findViewById(R.id.loading_background);

        final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts)
        {
            if (emailPattern.matcher(account.name).matches())
            {
                possibleEmail = account.name;
            }
        }

        editTextYourEmail.setText(possibleEmail);

        phone_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getResources().getString(R.string.contact_us_phone_num)));
                startActivity(intent);
            }
        });

        email_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", getResources().getString(R.string.contact_us_email_id),
                                null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getResources().getString(R.string.contact_us_email_id) });
                startActivity(Intent.createChooser(intent, ""));*/
            }
        });

        completeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContact();
            }
        });
    }


    private void sendContact(){
        if (Constant.isConnectingToInternet(FeedbackUs.this)) {
            name = editTextYourName.getText().toString();
            email = editTextYourEmail.getText().toString();
            contact = editTextYourContact.getText().toString();
            comment = editTextYourComment.getText().toString();
            if (((email.length() == 0) && (contact.length() == 0)) || (comment.length()==0)) {
                aleartDialogForFillInformation();
            } else {
                jsonContactForm(name, email, contact, comment,subCategoryId);
            }

        } else
            aleartDialogForInternetConnectivity();
    }




    public void jsonContactForm(String name,String email,String contact,String comment,int subCateId)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        loading_back.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.VISIBLE);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);

        collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
        userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
        userId          = MySharedPref.getUserId(this,Constant.USER_ID);
        userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
//        Date currentTime = Calendar.getInstance().getTime();


        sendFragmentAnalytics("Enquiry Name = " + enquiry + " , User Id = " + userId);


        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        RequestBody formBody =  new FormEncodingBuilder()
                .add(Constant.USER_NAME, name)
                .add(Constant.USER_EMAIL, email)
                .add(SubCategoryTable.SUB_CATEGORY_Id, String.valueOf(subCateId))
                .add(Constant.USER_CONTACT_NUMBER,contact)
                .add(Constant.USER_FEEDBACK_MSG,comment)
                .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))
                .add(Constant.USER_ID, String.valueOf(userId))
                .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
                .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
                .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
                .build();
        Request request = new Request.Builder()
                .url(Constant.USER_FEEDBACK_URL)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                FeedbackUs.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        loading_spinner.setVisibility(View.GONE);
                        loading_back.setVisibility(View.GONE);
                        aleartDialogForResponceFailed();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String responseJsonData = response.body().string();

                try {
                    final JSONObject jsonObject = new JSONObject(responseJsonData);
                    jsonObject.optString("message");


                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            loading_spinner.setVisibility(View.GONE);
                            loading_back.setVisibility(View.GONE);
                            aleartDialogSubmitForm(jsonObject.optString("message"));
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading_spinner.setVisibility(View.GONE);
                            loading_back.setVisibility(View.GONE);
                            aleartDialogForResponceFailed();
                        }
                    });
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // Aleart Dialog for Net Connection...
    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_internet_title));
        builder.setMessage(getResources().getString(R.string.aleart_dialog_for_internet_message));
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendContact();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void aleartDialogForResponceFailed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_response_failed_title));
        builder.setMessage(getResources().getString(R.string.aleart_dialog_for_response_failed_message));
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendContact();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void aleartDialogForFillInformation()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_fill_info_title));
        builder.setMessage(getResources().getString(R.string.aleart_dialog_for_fill_info_message));
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void aleartDialogSubmitForm(String feedbackMessage)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_submit_contact_form_title));
        builder.setMessage(feedbackMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("Close form", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendFragmentAnalytics(String TAG) {
        ((AppApplication) this.getApplication()).trackScreenView(TAG);
    }


}
