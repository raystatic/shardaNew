//package com.shardatech.shardauniversity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.HttpMethod;
//import com.facebook.Profile;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.OptionalPendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.io.IOException;
//
//import method.Constant;
//import method.MySharedPref;
//
///**
// * Created by sharda on 5/31/2017.
// */
//
//public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
//    ProgressDialog progressDialog;
//    private static final String TAG = LoginActivity.class.getSimpleName();
//    ImageView imgLogo;
//    RelativeLayout rlSignInWithGoogle,rlSignInWithFacebook;
//    LoginButton fbLoginButton;
//    TextView tvSkip,tvAlreadyHaveAccount,tvStartWithShardaAcc;
//
//    private GoogleApiClient mGoogleApiClient;
//    private static final int RC_SIGN_IN = 007;
//
//    CallbackManager callbackManager;
//    AccessTokenTracker accessTokenTracker;
//
//    private String facebookId;
//    private String googleId;
//    private String erpId;
//
//    private String userProfileImage="";
//    private String userConatctNumber;
//    private String userDOB;
//    private String userName="";
//    private String userEmailId="";
//    private String userLastName="";
//    private String userGender="";
//
//    private int collegeId;
//    private int userLoginType;
//    private int userId;
//    private String collegeName;
//    private String userDeviceId;
//    private String fcmToken;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        setContentView(R.layout.activity_login);
//        collegeName             = MySharedPref.getUserCollegeName(this,Constant.USER_COLLAGE_NAME);
//        progressDialog          = new ProgressDialog(LoginActivity.this);
//        imgLogo                 = (ImageView) findViewById(R.id.imgLogo);
//        rlSignInWithGoogle      = (RelativeLayout) findViewById(R.id.rl_sign_in_with_google);
//        rlSignInWithFacebook    = (RelativeLayout) findViewById(R.id.rl_sign_in_with_facebook);
//        tvSkip                  = (TextView) findViewById(R.id.tv_skip);
//        tvStartWithShardaAcc    = (TextView) findViewById(R.id.tv_start_with_sharda_acc);
//        fbLoginButton           = (LoginButton) findViewById(R.id.fb_login_button);
//
//        rlSignInWithGoogle.setOnClickListener(this);
//
//        fbLoginButton.setReadPermissions(new String[] {"email","user_friends","public_profile","user_photos",
//                "user_birthday","read_custom_friendlists"});
//
//        tvStartWithShardaAcc.setText(getString(R.string.start_with)+" "+collegeName+" Account");
//
//        userDeviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        MySharedPref.setUserDeviceID(LoginActivity.this, Constant.USER_DEVICE_ID, userDeviceId);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//
//        tvSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
//                //MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
//                MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE,Constant.LOGIN_TYPE_FOR_SKIP);
//                MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID,Constant.USER_ID_FOR_SKIP_USER);
//                skipDataSendToServer(userEmailId,userName,userLastName,userGender,facebookId,userProfileImage);
//                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
//        callbackManager = CallbackManager.Factory.create();
//        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                if (progressDialog != null && !progressDialog.isShowing()){
//                    progressDialog.show();
//                    progressDialog.setMessage("fetching profile data...");
//                }
//                setFacebookData(loginResult);
//                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
//                getFriendListFromFacebook();
//            }
//            @Override
//            public void onCancel() {
//                Toast.makeText(getBaseContext(),"Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onError(FacebookException exception) {
//                Toast.makeText(getBaseContext(),"Facebook Login Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        //          updateUI(false);
//                    }
//                });
//    }
//
//    private void revokeAccess() {
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        //          updateUI(false);
//                    }
//                });
//    }
//
//    public void onClick(View v) {
//        if (v == rlSignInWithFacebook) {
//            fbLoginButton.performClick();
//        }
//        if (v== rlSignInWithGoogle){
//            signIn();
//        }
//    }
//    private void nextActivity(Profile profile){
//        if(profile != null){
//            Toast.makeText(getBaseContext(),"Login Success"+profile.getFirstName()+
//                    profile.getLastName(), Toast.LENGTH_SHORT).show();
//            Intent main = new Intent(LoginActivity.this, HomePageActivity.class);
//            main.putExtra("name", profile.getFirstName());
//            main.putExtra("surname", profile.getLastName());
//            main.putExtra("imageUrl", profile.getProfilePictureUri(200,200).toString());
//            startActivity(main);
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//
//    }
//
//    private void setFacebookData(final LoginResult loginResult)
//    {
//        GraphRequest request = GraphRequest.newMeRequest(
//                loginResult.getAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        userEmailId     = response.getJSONObject().optString("email");
//                        userName        = response.getJSONObject().optString("first_name");
//                        userLastName    = response.getJSONObject().optString("last_name");
//                        userGender      = response.getJSONObject().optString("gender");
//                        facebookId      = response.getJSONObject().optString("id");
////                        String id2= loginResult.getAccessToken().getUserId();
////                        facebookId      = Profile.getCurrentProfile().getId();
//                        userProfileImage= "http://graph.facebook.com/"+facebookId+"/picture?type=large";
//                        sendToServer(userEmailId,userName,userLastName,userGender,facebookId,userProfileImage);
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,email,first_name,last_name,gender");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
//
//    private void sendToServer(String email,String firstName,String lastName,String gender,String facebookId,String userProfileImage){
//
//        collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
//        userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
//        userId          = MySharedPref.getUserId(this,Constant.USER_ID);
//        userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
//        fcmToken        = MySharedPref.getUserFCMTokenId(this,Constant.USER_FCM_TOKEN);
//        // Date currentTime = Calendar.getInstance().getTime();
//
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody formBody = new FormEncodingBuilder()
//                .add(Constant.USER_FACEBOOK_ID, facebookId)
//                .add(Constant.USER_GOOGLE_ID,MySharedPref.getUserGoogleId(LoginActivity.this,Constant.USER_GOOGLE_ID))
//                .add(Constant.USER_ERP_ID,MySharedPref.getUserERPId(LoginActivity.this,Constant.USER_ERP_ID))
//
//                .add(Constant.USER_ID, String.valueOf(userId))
//                .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
//                .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
//                .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
//                .add(Constant.USER_FCM_TOKEN, String.valueOf(fcmToken))
//
//                .add(Constant.USER_NAME, String.valueOf(firstName))
//                .add(Constant.USER_EMAIL, String.valueOf(email))
//                .add(Constant.USER_PROFILE_PIC,userProfileImage)
//                .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))
//
//                .build();
//
//        Request request = new Request.Builder()
//                .url(Constant.USER_PROFILE_URL)
//                .post(formBody)
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            public void onFailure(Request paramRequest, final IOException paramIOException) {
//                paramIOException.printStackTrace();
//                LoginActivity.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, "An error occurred, Please try again"+paramIOException, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            public void onResponse(Response response)
//                    throws IOException {
//                String responseJsonData = response.body().string();
//                if (!response.isSuccessful()){
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(LoginActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    return;
//                }
//                try {
//                    MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
//                    //MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
//                    MySharedPref.setSkipCheck(LoginActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY, true);
//                    MySharedPref.setLoginCheck(LoginActivity.this, Constant.LOG_IN_CHECK, true);
//                    JSONObject jsonObject = new JSONObject(responseJsonData);
//                    MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID, jsonObject.getInt("user_id"));
//                    MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID, jsonObject.getString("user_fb_id"));
//                    MySharedPref.setUserName(LoginActivity.this,Constant.USER_NAME, jsonObject.getString("user_name"));
//                    MySharedPref.setUserEmail(LoginActivity.this,Constant.USER_EMAIL, jsonObject.getString("user_email"));
//                    MySharedPref.setUserDisplayPic(LoginActivity.this,Constant.USER_PROFILE_PIC, jsonObject.getString("user_profile_picture"));
//                    MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE, Math.max(jsonObject.getInt("user_login_type"),MySharedPref.getUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE)));
//                    if (progressDialog != null && progressDialog.isShowing())
//                        progressDialog.cancel();
//
//                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                    startActivity(intent);
//                    finish();
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                } catch (JSONException paramResponse) {
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(LoginActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    paramResponse.printStackTrace();
//                }
//            }
//        });
//
//
//
//    }
//
//    private void getFriendListFromFacebook(){
//        GraphRequest request = new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "me/friends",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse graphResponse) {
//                        JSONObject object = graphResponse.getJSONObject();
//                        try {
//                            JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
//                            JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
//                            String usersName = user.getString("id");
//                            String name = user.getString("name");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        );
//
////        Bundle param = new Bundle();
////        param.putString("fields", "friends");
////        request.setParameters(param);
//        request.executeAsync();
//
//    }
//
//    private void myNewGraphReq(String friendlistId) {
//        final String graphPath = ""+friendlistId+"/members/";
//        AccessToken token = AccessToken.getCurrentAccessToken();
//        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//                JSONObject object = graphResponse.getJSONObject();
//                try {
//                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
//                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
//                    String usersName = user.getString("name");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle param = new Bundle();
//        param.putString("fields", "name");
//        request.setParameters(param);
//        request.executeAsync();
//    }
//
//    private void skipDataSendToServer(String email,String firstName,String lastName,String gender,String facebookId,String userProfileImage){
//
//        collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
//        userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_SKIP,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
//        userId          = MySharedPref.getUserId(this,Constant.USER_ID);
//        userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
//        fcmToken        = MySharedPref.getUserFCMTokenId(this,Constant.USER_FCM_TOKEN);
//        //     Date currentTime = Calendar.getInstance().getTime();
//
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody formBody = new FormEncodingBuilder()
//                .add(Constant.USER_FACEBOOK_ID, MySharedPref.getUserGoogleId(LoginActivity.this,Constant.USER_GOOGLE_ID))
//                .add(Constant.USER_GOOGLE_ID,MySharedPref.getUserGoogleId(LoginActivity.this,Constant.USER_GOOGLE_ID))
//                .add(Constant.USER_ERP_ID,MySharedPref.getUserERPId(LoginActivity.this,Constant.USER_ERP_ID))
//
//                .add(Constant.USER_ID, String.valueOf(userId))
//                .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
//                .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
//                .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
//                .add(Constant.USER_FCM_TOKEN, String.valueOf(fcmToken))
//                .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))
//
//                .add(Constant.USER_NAME, String.valueOf(firstName))
//                .add(Constant.USER_EMAIL, String.valueOf(email))
//                .add(Constant.USER_PROFILE_PIC,userProfileImage)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(Constant.USER_PROFILE_URL)
//                .post(formBody)
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            public void onFailure(Request paramRequest, final IOException paramIOException) {
//                paramIOException.printStackTrace();
//                LoginActivity.this.runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, "An error occurred, Please try again"+paramIOException, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            public void onResponse(Response response)
//                    throws IOException {
//                String responseJsonData = response.body().string();
//                if (!response.isSuccessful()){
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(LoginActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    return;
//                }
//                try {
//                    MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
//                    //MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
//                    MySharedPref.setSkipCheck(LoginActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY, true);
//                    MySharedPref.setLoginCheck(LoginActivity.this, Constant.LOG_IN_CHECK, false);
//                    JSONObject jsonObject = new JSONObject(responseJsonData);
//                    MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID, jsonObject.getInt("user_id"));
//                    MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID, jsonObject.getString("user_fb_id"));
//                    MySharedPref.setUserName(LoginActivity.this,Constant.USER_NAME, jsonObject.getString("user_name"));
//                    MySharedPref.setUserEmail(LoginActivity.this,Constant.USER_EMAIL, jsonObject.getString("user_email"));
//                    MySharedPref.setUserDisplayPic(LoginActivity.this,Constant.USER_PROFILE_PIC, jsonObject.getString("user_profile_picture"));
//                    MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE, Math.max(jsonObject.getInt("user_login_type"),MySharedPref.getUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE)));
//                    if (progressDialog != null && progressDialog.isShowing())
//                        progressDialog.cancel();
//
//                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                    startActivity(intent);
//                    finish();
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                } catch (JSONException paramResponse) {
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(LoginActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    paramResponse.printStackTrace();
//                }
//            }
//        });
//
//
//
//    }
//
//
//
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        if ( progressDialog!=null && progressDialog.isShowing() ){
//            progressDialog.cancel();
//        }
//    }
//
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//
//            String googleId = acct.getId();
//            String personName = acct.getDisplayName();
//            String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
//            String email = acct.getEmail();
//
////            Date currentTime = Calendar.getInstance().getTime();
//
//            Long tsLong = System.currentTimeMillis()/1000;
//            String ts = tsLong.toString();
//
//            collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
//            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_GOOGLE,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
//            userId          = MySharedPref.getUserId(this,Constant.USER_ID);
//            userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
//            fcmToken        = MySharedPref.getUserFCMTokenId(this,Constant.USER_FCM_TOKEN);
//
//            OkHttpClient okHttpClient = new OkHttpClient();
//            RequestBody formBody = new FormEncodingBuilder()
//                    .add(Constant.USER_FACEBOOK_ID, MySharedPref.getUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID))
//                    .add(Constant.USER_GOOGLE_ID,googleId)
//                    .add(Constant.USER_ERP_ID,MySharedPref.getUserERPId(LoginActivity.this,Constant.USER_ERP_ID))
//
//                    .add(Constant.USER_ID, String.valueOf(userId))
//                    .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
//                    .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
//                    .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
//                    .add(Constant.USER_FCM_TOKEN, String.valueOf(fcmToken))
//                    .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))
//
//                    .add(Constant.USER_NAME, String.valueOf(personName))
//                    .add(Constant.USER_EMAIL, String.valueOf(email))
//                    .add(Constant.USER_PROFILE_PIC,personPhotoUrl)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(Constant.USER_PROFILE_URL)
//                    .post(formBody)
//                    .build();
//
//            Call call = okHttpClient.newCall(request);
//            call.enqueue(new Callback() {
//                public void onFailure(Request paramRequest, final IOException paramIOException) {
//                    paramIOException.printStackTrace();
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(LoginActivity.this, "An error occurred, Please try again"+paramIOException, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                public void onResponse(Response response)
//                        throws IOException {
//                    String responseJsonData = response.body().string();
//                    if (!response.isSuccessful()){
//                        LoginActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText(LoginActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return;
//                    }
//                    try {
//                        MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
//                        //  MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
//                        MySharedPref.setSkipCheck(LoginActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY, true);
//                        MySharedPref.setLoginCheck(LoginActivity.this, Constant.LOG_IN_CHECK, true);
//                        JSONObject jsonObject = new JSONObject(responseJsonData);
//                        MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID, jsonObject.getInt("user_id"));
//                        MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID, jsonObject.getString("user_fb_id"));
//                        MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_GOOGLE_ID, jsonObject.getString("user_google_id"));
//                        MySharedPref.setUserName(LoginActivity.this,Constant.USER_NAME, jsonObject.getString("user_name"));
//                        MySharedPref.setUserEmail(LoginActivity.this,Constant.USER_EMAIL, jsonObject.getString("user_email"));
//                        MySharedPref.setUserDisplayPic(LoginActivity.this,Constant.USER_PROFILE_PIC, jsonObject.getString("user_profile_picture"));
//                        MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE, Math.max(jsonObject.getInt("user_login_type"),MySharedPref.getUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE)));
//                        if (progressDialog != null && progressDialog.isShowing())
//                            progressDialog.cancel();
//
//                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    } catch (JSONException paramResponse) {
//                        LoginActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText(LoginActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        paramResponse.printStackTrace();
//                    }
//                }
//            });
//
//
//
//
//        } else {
//            // Signed out, show unauthenticated UI.
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//    }
//
//    private void showProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setIndeterminate(true);
//        }
//
//        progressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.hide();
//        }
//    }
//}




package com.shardatech.shardauniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FacebookAuthProvider;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import method.Constant;
import method.MySharedPref;

import static com.shardatech.shardauniversity.SplashActivity.COLLAGE_ID;
import static com.shardatech.shardauniversity.SplashActivity.COLLAGE_NAME;

/**
 * Created by sharda on 5/31/2017.
 */


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    ProgressDialog progressDialog;
    private static final String TAG = LoginActivity.class.getSimpleName();
    ImageView imgLogo;
    RelativeLayout rlSignInWithGoogle,rlSignInWithFacebook;
    LoginButton fbLoginButton;
    TextView tvSkip,tvAlreadyHaveAccount,tvStartWithShardaAcc;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;

    private String facebookId;
    private String googleId;
    private String erpId;

    private String userProfileImage="";
    private String userConatctNumber;
    private String userDOB;
    private String userName="";
    private String userEmailId="";
    private String userLastName="";
    private String userGender="";

    private int collegeId;
    private int userLoginType;
    private int userId;
    private String collegeName;
    private String userDeviceId;
    private String fcmToken;

    private boolean isLogout=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


      if(getIntent().hasExtra("isLogout")){
          isLogout = getIntent().getBooleanExtra("isLogout",false);
          if (isLogout) {
              if (MySharedPref.getUserFacebookId(this,Constant.USER_FACEBOOK_ID)!= null){
                  logoutFromFacebook();
              }
              if (MySharedPref.getUserGoogleId(this,Constant.USER_GOOGLE_ID)!= null){
                  signOut();
              }
          }
      }

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        collegeName             = MySharedPref.getUserCollegeName(this,Constant.USER_COLLAGE_NAME);
        progressDialog          = new ProgressDialog(LoginActivity.this);
        imgLogo                 = (ImageView) findViewById(R.id.imgLogo);
        rlSignInWithGoogle      = (RelativeLayout) findViewById(R.id.rl_sign_in_with_google);
        rlSignInWithFacebook    = (RelativeLayout) findViewById(R.id.rl_sign_in_with_facebook);
        tvSkip                  = (TextView) findViewById(R.id.tv_skip);
        tvStartWithShardaAcc    = (TextView) findViewById(R.id.tv_start_with_sharda_acc);
        fbLoginButton           = (LoginButton) findViewById(R.id.fb_login_button);

        rlSignInWithGoogle.setOnClickListener(this);

        fbLoginButton.setReadPermissions(new String[] {"email","user_friends","public_profile","user_photos",
                                                        "user_birthday","read_custom_friendlists"});

        tvStartWithShardaAcc.setText(getString(R.string.start_with)+" "+collegeName+" Account");

        userDeviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        MySharedPref.setUserDeviceID(LoginActivity.this, Constant.USER_DEVICE_ID, userDeviceId);





        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
                //MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
                MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE,Constant.LOGIN_TYPE_FOR_SKIP);
                MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID,Constant.USER_ID_FOR_SKIP_USER);
                skipDataSendToServer(userEmailId,userName,userLastName,userGender,facebookId,userProfileImage);
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (progressDialog != null && !progressDialog.isShowing()){
                    progressDialog.show();
                    progressDialog.setMessage("fetching profile data...");
                }
                setFacebookData(loginResult);
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                getFriendListFromFacebook();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(),"Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getBaseContext(),"Facebook Login Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut() {
        final ProgressDialog progressDialog2 = ProgressDialog.show(LoginActivity.this, "", "Logging out...",true,false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient.isConnected()){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Log out successful!", Toast.LENGTH_SHORT).show();
                                    if (progressDialog2!=null && progressDialog2.isShowing())
                                        progressDialog2.cancel();
                                }
                            });
                        }
                    });
                }
                else{
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (progressDialog2!=null && progressDialog2.isShowing())
                                progressDialog2.cancel();
                            Toast.makeText(LoginActivity.this, "Log out unsuccessful!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, 2000);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onClick(View v) {
        if (v == rlSignInWithFacebook) {
            fbLoginButton.performClick();
        }
        if (v== rlSignInWithGoogle){
                signIn();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        userEmailId     = response.getJSONObject().optString("email");
                        userName        = response.getJSONObject().optString("first_name");
                        userLastName    = response.getJSONObject().optString("last_name");
                        userGender      = response.getJSONObject().optString("gender");
                        facebookId      = response.getJSONObject().optString("id");
                        userProfileImage= "http://graph.facebook.com/"+facebookId+"/picture?type=large";
                        sendToServer(userEmailId,userName,userLastName,userGender,facebookId,userProfileImage);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void sendToServer(String email,String firstName,String lastName,String gender,String facebookId,String userProfileImage){

        MySharedPref.setUserCollegeId(this,Constant.USER_COLLAGE_ID,COLLAGE_ID);
        MySharedPref.setUserCollegeName(this,Constant.USER_COLLAGE_NAME,COLLAGE_NAME);

        collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
        userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
        userId          = MySharedPref.getUserId(this,Constant.USER_ID);
        userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
        fcmToken        = MySharedPref.getUserFCMTokenId(this,Constant.USER_FCM_TOKEN);
       // Date currentTime = Calendar.getInstance().getTime();

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add(Constant.USER_FACEBOOK_ID, facebookId)
                .add(Constant.USER_GOOGLE_ID,MySharedPref.getUserGoogleId(LoginActivity.this,Constant.USER_GOOGLE_ID))
                .add(Constant.USER_ERP_ID,MySharedPref.getUserERPId(LoginActivity.this,Constant.USER_ERP_ID))

                .add(Constant.USER_ID, String.valueOf(userId))
                .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
                .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
                .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
                .add(Constant.USER_FCM_TOKEN, String.valueOf(fcmToken))

                .add(Constant.USER_NAME, String.valueOf(firstName))
                .add(Constant.USER_EMAIL, String.valueOf(email))
                .add(Constant.USER_PROFILE_PIC,userProfileImage)
                .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))
                .add(Constant.USER_ANDROID_APP_VERSION, String.valueOf(MySharedPref.getUserAppVersion(LoginActivity.this, Constant.USER_ANDROID_APP_VERSION)))
                .build();

        Request request = new Request.Builder()
                .url(Constant.USER_PROFILE_URL)
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, final IOException paramIOException) {
                paramIOException.printStackTrace();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, "An error occurred, Please try again"+paramIOException, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
                    //MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
                    MySharedPref.setSkipCheck(LoginActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY, true);
                    MySharedPref.setLoginCheck(LoginActivity.this, Constant.LOG_IN_CHECK, true);
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID, jsonObject.getInt("user_id"));
                    MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID, jsonObject.getString("user_fb_id"));
                    MySharedPref.setUserName(LoginActivity.this,Constant.USER_NAME, jsonObject.getString("user_name"));
                    MySharedPref.setUserEmail(LoginActivity.this,Constant.USER_EMAIL, jsonObject.getString("user_email"));
                    MySharedPref.setUserDisplayPic(LoginActivity.this,Constant.USER_PROFILE_PIC, jsonObject.getString("user_profile_picture"));
                    MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE, Math.max(jsonObject.getInt("user_login_type"),MySharedPref.getUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE)));
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.cancel();

                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (JSONException paramResponse) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });



    }

    private void getFriendListFromFacebook(){
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse graphResponse) {
                        JSONObject object = graphResponse.getJSONObject();
                        try {
                            JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                            JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
                            String usersName = user.getString("id");
                            String name = user.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

//        Bundle param = new Bundle();
//        param.putString("fields", "friends");
//        request.setParameters(param);
        request.executeAsync();

    }

    private void myNewGraphReq(String friendlistId) {
        final String graphPath = ""+friendlistId+"/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
                    String usersName = user.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }

    private void skipDataSendToServer(String email,String firstName,String lastName,String gender,String facebookId,String userProfileImage){

        collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
        userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_SKIP,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
        userId          = MySharedPref.getUserId(this,Constant.USER_ID);
        userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
        fcmToken        = MySharedPref.getUserFCMTokenId(this,Constant.USER_FCM_TOKEN);
   //     Date currentTime = Calendar.getInstance().getTime();

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add(Constant.USER_FACEBOOK_ID, MySharedPref.getUserGoogleId(LoginActivity.this,Constant.USER_GOOGLE_ID))
                .add(Constant.USER_GOOGLE_ID,MySharedPref.getUserGoogleId(LoginActivity.this,Constant.USER_GOOGLE_ID))
                .add(Constant.USER_ERP_ID,MySharedPref.getUserERPId(LoginActivity.this,Constant.USER_ERP_ID))

                .add(Constant.USER_ID, String.valueOf(userId))
                .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
                .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
                .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
                .add(Constant.USER_FCM_TOKEN, String.valueOf(fcmToken))
                .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))

                .add(Constant.USER_NAME, String.valueOf(firstName))
                .add(Constant.USER_EMAIL, String.valueOf(email))
                .add(Constant.USER_PROFILE_PIC,userProfileImage)
                .add(Constant.USER_ANDROID_APP_VERSION, String.valueOf(MySharedPref.getUserAppVersion(LoginActivity.this, Constant.USER_ANDROID_APP_VERSION)))
                .build();

        Request request = new Request.Builder()
                .url(Constant.USER_PROFILE_URL)
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, final IOException paramIOException) {
                paramIOException.printStackTrace();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                Toast.makeText(LoginActivity.this, "An error occurred, Please try again"+paramIOException, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
                    //MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
                    MySharedPref.setSkipCheck(LoginActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY, true);
                    MySharedPref.setLoginCheck(LoginActivity.this, Constant.LOG_IN_CHECK, false);
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID, jsonObject.getInt("user_id"));
                    MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID, jsonObject.getString("user_fb_id"));
                    MySharedPref.setUserName(LoginActivity.this,Constant.USER_NAME, jsonObject.getString("user_name"));
                    MySharedPref.setUserEmail(LoginActivity.this,Constant.USER_EMAIL, jsonObject.getString("user_email"));
                    MySharedPref.setUserDisplayPic(LoginActivity.this,Constant.USER_PROFILE_PIC, jsonObject.getString("user_profile_picture"));
                    MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE, Math.max(jsonObject.getInt("user_login_type"),MySharedPref.getUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE)));
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.cancel();

                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } catch (JSONException paramResponse) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });



    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            String googleId = acct.getId();
            String personName = acct.getDisplayName();
            String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
            String email = acct.getEmail();

//            Date currentTime = Calendar.getInstance().getTime();

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            MySharedPref.setUserCollegeId(this,Constant.USER_COLLAGE_ID,COLLAGE_ID);
            MySharedPref.setUserCollegeName(this,Constant.USER_COLLAGE_NAME,COLLAGE_NAME);

            collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_GOOGLE,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
            userId          = MySharedPref.getUserId(this,Constant.USER_ID);
            userDeviceId    = MySharedPref.getUserDeviceID(this,Constant.USER_DEVICE_ID);
            fcmToken        = MySharedPref.getUserFCMTokenId(this,Constant.USER_FCM_TOKEN);

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormEncodingBuilder()
                    .add(Constant.USER_FACEBOOK_ID, MySharedPref.getUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID))
                    .add(Constant.USER_GOOGLE_ID,googleId)
                    .add(Constant.USER_ERP_ID,MySharedPref.getUserERPId(LoginActivity.this,Constant.USER_ERP_ID))

                    .add(Constant.USER_ID, String.valueOf(userId))
                    .add(Constant.USER_COLLAGE_ID, String.valueOf(collegeId))
                    .add(Constant.USER_LOGIN_TYPE, String.valueOf(userLoginType))
                    .add(Constant.USER_DEVICE_ID, String.valueOf(userDeviceId))
                    .add(Constant.USER_FCM_TOKEN, String.valueOf(fcmToken))
                    .add(Constant.USER_CURRENT_DATE, String.valueOf(ts))

                    .add(Constant.USER_NAME, String.valueOf(personName))
                    .add(Constant.USER_EMAIL, String.valueOf(email))
                    .add(Constant.USER_PROFILE_PIC,personPhotoUrl)
                    .add(Constant.USER_ANDROID_APP_VERSION, String.valueOf(MySharedPref.getUserAppVersion(LoginActivity.this, Constant.USER_ANDROID_APP_VERSION)))
                    .build();

            Request request = new Request.Builder()
                    .url(Constant.USER_PROFILE_URL)
                    .post(formBody)
                    .build();

            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                public void onFailure(Request paramRequest, final IOException paramIOException) {
                    paramIOException.printStackTrace();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "An error occurred, Please try again"+paramIOException, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                public void onResponse(Response response)
                        throws IOException {
                    String responseJsonData = response.body().string();
                    if (!response.isSuccessful()){
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    try {
                        MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,false);
                      //  MySharedPref.setFirstTimeWelcomeScreenLaunch(LoginActivity.this, Constant.CHECK_FIRST_TIME_WELCOME_ACTIVITY,true);
                        MySharedPref.setSkipCheck(LoginActivity.this, Constant.CHECK_FIRST_TIME_LOGIN_ACTIVITY, true);
                        MySharedPref.setLoginCheck(LoginActivity.this, Constant.LOG_IN_CHECK, true);
                        JSONObject jsonObject = new JSONObject(responseJsonData);
                        MySharedPref.setUserId(LoginActivity.this,Constant.USER_ID, jsonObject.getInt("user_id"));
                        MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_FACEBOOK_ID, jsonObject.getString("user_fb_id"));
                        MySharedPref.setUserFacebookId(LoginActivity.this,Constant.USER_GOOGLE_ID, jsonObject.getString("user_google_id"));
                        MySharedPref.setUserName(LoginActivity.this,Constant.USER_NAME, jsonObject.optString("user_first_name"));
                        MySharedPref.setUserEmail(LoginActivity.this,Constant.USER_EMAIL, jsonObject.optString("user_email"));
                        MySharedPref.setUserDisplayPic(LoginActivity.this,Constant.USER_PROFILE_PIC, jsonObject.getString("user_profile_picture"));
                        MySharedPref.setUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE, Math.max(jsonObject.getInt("user_login_type"),MySharedPref.getUserLoginType(LoginActivity.this,Constant.USER_LOGIN_TYPE)));
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.cancel();

                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } catch (JSONException paramResponse) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                        paramResponse.printStackTrace();
                    }
                }
            });




        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onStart() {
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
        super.onStart();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void showProgressDialog(String message) {
        progressDialog = ProgressDialog.show(LoginActivity.this, message, message,true,false);
    }

//    private void showProgressDialog() {
//        showProgressDialog("Loading...");
//    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog=null;
        }
    }


//    private void logoutFromGoogle() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        Toast.makeText(getApplicationContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void logoutFromFacebook(){
        LoginManager.getInstance().logOut();
    }

}

