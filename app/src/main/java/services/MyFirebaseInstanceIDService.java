package services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shardatech.shardauniversity.BuildConfig;

import method.Constant;
import method.MySharedPref;

/**
 * Created by sharda on 12/4/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    @Override
    public void onTokenRefresh()
    {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token)
    {
        MySharedPref.setUserFCMTokenId(this, Constant.USER_FCM_TOKEN,token);
    }
}
