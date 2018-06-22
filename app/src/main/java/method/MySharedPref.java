package method;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.File;
import java.net.URI;

/**
 * Created by sharda on 10/13/2017.
 */

public class MySharedPref {

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static MySharedPref instance = new MySharedPref();
    static Context context;
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;


    public static MySharedPref getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public static void setClearedSharedPref(Context ctx) {
        PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext()).edit().clear().commit();
    }
    // SharedPref EventOn
    public static void setLoginCheck(Context ctx, String sharedPrefLoginKey, boolean sharedPrefLoginValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefLoginKey, sharedPrefLoginValue);
        editor.commit();
    }

    public static boolean getLoginCheck(Context ctx, String sharedPrefLoginKey) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean loginCheck = pref.getBoolean(sharedPrefLoginKey, false);
        return loginCheck;
    }


    public static void setSkipCheck(Context ctx, String sharedPrefSkipKey, boolean sharedPrefSkipValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefSkipKey, sharedPrefSkipValue);
        editor.commit();
    }

    public static boolean getSkipCheck(Context ctx, String sharedPrefSkipKey) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean skipCheck = pref.getBoolean(sharedPrefSkipKey, false);
        return  skipCheck;
    }

    public static void setFirstTimeWelcomeScreenLaunch(Context ctx, String sharedPrefWelcomeKey, boolean isFirstTime) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefWelcomeKey, isFirstTime);
        editor.commit();
    }

    public static boolean getFirstTimeWelcomeScreenLaunch(Context ctx, String sharedPrefWelcomeKey) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean welcomeLaunchScreen = pref.getBoolean(sharedPrefWelcomeKey, true);
        return  welcomeLaunchScreen;
    }

    public static void setShowFirstTimeLoginActivity(Context ctx, String sharedPrefShowFirstTimeLoginActivityKey,
                                                     boolean sharedPrefShowFirstTimeLoginActivityValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefShowFirstTimeLoginActivityKey, sharedPrefShowFirstTimeLoginActivityValue);
        editor.commit();
    }

    public static boolean getShowFirstTimeLoginActivity(Context ctx, String sharedPrefShowFirstTimeLoginActivityKey) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean showFirstTimeLoginActivity = pref.getBoolean(sharedPrefShowFirstTimeLoginActivityKey, false);
        return  showFirstTimeLoginActivity;
    }


    public static void setUserCollegeId(Context ctx, String sharedPrefUserCollegeId, int sharedPrefCollegeId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(sharedPrefUserCollegeId, sharedPrefCollegeId);
        editor.commit();
    }

    public static int getUserCollegeId(Context ctx, String sharedPrefUserCollegeId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        int userCollegeId = pref.getInt(sharedPrefUserCollegeId, 0);
        return userCollegeId;
    }

    public static void setUserCollegeName(Context ctx, String sharedPrefUserCollegeName, String sharedPrefCollegeName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserCollegeName, sharedPrefCollegeName);
        editor.commit();
    }

    public static String getUserCollegeName(Context ctx, String sharedPrefUserCollegeName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String userCollegeName = pref.getString(sharedPrefUserCollegeName,"");
        return userCollegeName;
    }


    public static void setUserLoginType(Context ctx, String sharedPrefUserLoginType, int sharedPrefLoginType) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(sharedPrefUserLoginType, sharedPrefLoginType);
        editor.commit();
    }

    public static int getUserLoginType(Context ctx, String sharedPrefUserLoginType) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        int userLoginType = pref.getInt(sharedPrefUserLoginType, 0);
        return userLoginType;
    }

    public static void setUserId(Context ctx, String sharedPrefUserId, int sharedPrefId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(sharedPrefUserId, sharedPrefId);
        editor.commit();
    }

    public static int getUserId(Context ctx, String sharedPrefUserId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        int userId = pref.getInt(sharedPrefUserId, 0);
        return userId;
    }

    public static void setUserDeviceID(Context ctx, String sharedPrefUserDeviceIdKey, String sharedPrefUserDeviceIdValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserDeviceIdKey, sharedPrefUserDeviceIdValue);
        editor.apply();
    }

    public static String getUserDeviceID(Context ctx, String sharedPrefUserDeviceId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserDeviceID = pref.getString(sharedPrefUserDeviceId, "");
        return myPrefUserDeviceID;
    }

    public static void setUserFCMTokenId(Context ctx, String sharedPreUserFCMToken, String sharedPreUserFCMTokenId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPreUserFCMToken, sharedPreUserFCMTokenId);
        editor.commit();
    }

    public static String getUserFCMTokenId(Context ctx, String sharedPreUserFCMToken) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String userFCMToken = pref.getString(sharedPreUserFCMToken,"");
        return userFCMToken;
    }


    public static void setUserName(Context ctx, String sharedPrefUserProfileName, String sharedPrefUserName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserProfileName, sharedPrefUserName);
        editor.apply();
    }

    public static String getUserName(Context ctx, String sharedPrefuserName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserName = pref.getString(sharedPrefuserName, "");
        return myPrefUserName;
    }

    public static void setUserFacebookId(Context ctx, String sharedPrefUserFacebookId, String sharedPrefFacebookID) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserFacebookId, sharedPrefFacebookID);
        editor.apply();
    }

    public static String getUserFacebookId(Context ctx, String sharedPrefFacebookID) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefFacebookId = pref.getString(sharedPrefFacebookID, "");
        return myPrefFacebookId;
    }

    public static void setUserGoogleId(Context ctx, String sharedPrefUserGoogleId, String sharedPrefGoogleId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserGoogleId, sharedPrefGoogleId);
        editor.apply();
    }

    public static String getUserGoogleId(Context ctx, String sharedPrefGoogleId) {
        SharedPreferences pref   = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefGoogleId    = pref.getString(sharedPrefGoogleId, "");
        return myPrefGoogleId;
    }

    public static void setUserERPId(Context ctx, String sharedPrefUserERPId, String sharedPrefERPId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserERPId, sharedPrefERPId);
        editor.apply();
    }

    public static String getUserERPId(Context ctx, String sharedPrefERPId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefERPId = pref.getString(sharedPrefERPId, "");
        return myPrefERPId;
    }

    public static void setUserEmail(Context ctx, String sharedPrefUserProfileEmail, String sharedPrefUserEmail) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserProfileEmail, sharedPrefUserEmail);
        editor.apply();
    }

    public static String getUserEmail(Context ctx, String sharedPrefuserEmail) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserEmail = pref.getString(sharedPrefuserEmail, "");
        return myPrefUserEmail;
    }

    public static void setUserDisplayPic(Context ctx, String sharedPrefUserProfileDisplayPic, String sharedPrefUserProfilePic) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserProfileDisplayPic, sharedPrefUserProfilePic);
        editor.apply();
    }

    public static String getUserDisplayPic(Context ctx, String sharedPrefuserProfilePic) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserProfilePic = pref.getString(sharedPrefuserProfilePic, "");
        return myPrefUserProfilePic;
    }

    public static void setHomePageEmergNumber(Context ctx, String sharedPrefHomePageEmerNum, String sharedPrefHomeEmerNum) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefHomePageEmerNum, sharedPrefHomeEmerNum);
        editor.apply();
    }

    public static String getHomePageEmergNumber(Context ctx, String sharedPrefHomePageEmerNum) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String homePageEmerNum = pref.getString(sharedPrefHomePageEmerNum, "");
        return homePageEmerNum;
    }


    public static void setUserOtpVerify(Context ctx, String sharedPrefUserOtpKey,
                                                     boolean sharedPrefUserOtpValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefUserOtpKey, sharedPrefUserOtpValue);
        editor.commit();
    }

    public static boolean getUserOtpVerify(Context ctx, String sharedPrefOtpVerify) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean otpverify = pref.getBoolean(sharedPrefOtpVerify, false);
        return  otpverify;
    }



    public static void setUserFirstName(Context ctx, String sharedPrefUserProfileFirstName, String sharedPrefUserFirstName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserProfileFirstName, sharedPrefUserFirstName);
        editor.apply();
    }

    public static String getUserFirstName(Context ctx, String sharedPrefuserFirstName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserFirstName = pref.getString(sharedPrefuserFirstName, "");
        return myPrefUserFirstName;
    }

    public static void setUserLastName(Context ctx, String sharedPrefUserProfileLastName, String sharedPrefUserLastName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserProfileLastName, sharedPrefUserLastName);
        editor.apply();
    }

    public static String getUserLastName(Context ctx, String sharedPrefuserLastName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserLastName = pref.getString(sharedPrefuserLastName, "");
        return myPrefUserLastName;
    }

    public static void setUserContactNumber(Context ctx, String sharedPrefUserKeyContactNumber, String sharedPrefUserContactNumber) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserKeyContactNumber, sharedPrefUserContactNumber);
        editor.apply();
    }

    public static String getUserContactNumber(Context ctx, String sharedPrefuserContactNumber) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserContactNumber = pref.getString(sharedPrefuserContactNumber, "");
        return myPrefUserContactNumber;
    }


    public static void setUserGender(Context ctx, String sharedPrefUserKeyGender, String sharedPrefUserGender) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserKeyGender, sharedPrefUserGender);
        editor.apply();
    }

    public static String getUserGender(Context ctx, String sharedPrefuserGender) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserGender = pref.getString(sharedPrefuserGender, "");
        return myPrefUserGender;
    }



    public static void setUserDOB(Context ctx, String sharedPrefUserKeyDOB, String sharedPrefUserDOB) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserKeyDOB, sharedPrefUserDOB);
        editor.apply();
    }

    public static String getUserDOB(Context ctx, String sharedPrefuserDOB) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserDOB = pref.getString(sharedPrefuserDOB, "");
        return myPrefUserDOB;
    }

    public static void setUserIam(Context ctx, String sharedPrefUserKeyIam, String sharedPrefUserIam) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserKeyIam, sharedPrefUserIam);
        editor.apply();
    }

    public static String getUserIam(Context ctx, String sharedPrefuserIam) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserIam = pref.getString(sharedPrefuserIam, "");
        return myPrefUserIam;
    }

    public static void setOtpLoginFromProfile(Context ctx, String sharedPrefKeyWhichActiviy, boolean otpVerificationActivity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefKeyWhichActiviy, otpVerificationActivity);
        editor.apply();
    }

    public static boolean getOtpLoginFromProfile(Context ctx, String sharedPrefKeyActivity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.getBoolean(sharedPrefKeyActivity, false);
    }

    public static void setUserAppVersion(Context ctx, String sharedPrefUserAndroidAppVersion, int sharedPrefUserAppVersion) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(sharedPrefUserAndroidAppVersion, sharedPrefUserAppVersion);
        editor.commit();
    }

    public static int getUserAppVersion(Context ctx, String sharedPrefUserAppVersion) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        int userAppVersion= pref.getInt(sharedPrefUserAppVersion, 2);
        return userAppVersion;
    }

    public static void setAppVersion(Context ctx, String sharedPrefAppVersion, boolean sharedPrefApplicationVersion) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(sharedPrefAppVersion, sharedPrefApplicationVersion);
        editor.commit();
    }

    public static boolean getAppVersion(Context ctx, String sharedPrefAppVersion) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean appVersion = pref.getBoolean(sharedPrefAppVersion, false);
        return appVersion;
    }


    public static void setUserCity(Context ctx, String sharedPrefKeyUserCity, String sharedPrefUserCity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefKeyUserCity, sharedPrefUserCity);
        editor.apply();
    }

    public static String getUserCity(Context ctx, String sharedPrefUserCity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserCity = pref.getString(sharedPrefUserCity, "");
        return myPrefUserCity;
    }

    public static void setUserAdd(Context ctx, String sharedPrefUserAddress, String sharedPrefUserAdd) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(sharedPrefUserAddress, sharedPrefUserAdd);
        editor.apply();
    }

    public static String getUserAdd(Context ctx, String sharedPrefuserAddress) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String myPrefUserAddress = pref.getString(sharedPrefuserAddress, "");
        return myPrefUserAddress;
    }


}
