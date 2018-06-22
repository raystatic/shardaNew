package method;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import databaseTable.SubCategoryTable;

/**
 * Created by sharda on 6/1/2017.
 */

public class Constant {

    private static Constant instance = new Constant();
    static Context context;
    public static String USER_ID            = "user_id";
    public static String USER_COLLAGE_ID    = "user_college_id";
    public static String USER_COLLAGE_NAME  = "user_college_name";
    public static String USER_LOGIN_TYPE    = "user_login_type";
    public static String USER_DEVICE_ID     = "user_device_id";
    public static String USER_FCM_TOKEN     = "user_fcm_token";

    public static String USER_FACEBOOK_ID   = "user_facebook_id";
    public static String USER_GOOGLE_ID     = "user_google_id";
    public static String USER_ERP_ID        = "user_erp_id";

    public static String USER_NAME          = "user_name";
    public static String USER_FIRST_NAME    = "user_first_name";
    public static String USER_LAST_NAME     = "user_last_name";
    public static String USER_EMAIL         = "user_email";
    public static String USER_PROFILE_PIC   = "user_profile_pic";
    public static String USER_GENDER        = "user_gender";
    public static String USER_ADD           = "user_address";
    public static String USER_CITY          = "user_city";
    public static String USER_DOB           = "user_dob";
    public static String USER_CONTACT_NUMBER= "user_contact_number";
    public static String USER_OTP           = "otp";
    public static String USER_FEEDBACK_MSG  = "user_feedback_message";
    public static String USER_CURRENT_DATE  = "user_current_date";
    public static String USER_ROLE          = "user_role";

    public static int USER_APP_VERSION                  = 2;
    public static String USER_ANDROID_APP_VERSION       = "android_version";
    public static String ANDROID_APP_VERSION            = "app_version";

    public static int USER_ID_FOR_SKIP_USER               = 0;
    //public static int USER_ID_FOR_FIRST_TIME_LOGIN      = 0;
    public static String USER_NAME_FOR_GUEST_USER         = "Welcome, Guest";

    public static String LOG_IN_CHECK                     = "login";
    //public static String SKIP_CHECK                     = "skip";
    public static String CHECK_FIRST_TIME_LOGIN_ACTIVITY  = "check_first_time_login_activity";
    public static String CHECK_FIRST_TIME_WELCOME_ACTIVITY= "check_first_time_welcome_activity";

    public static String CHECK_OTP_VERIFICATION           = "check_otp_verification";


    public static String SHARED_PREF_KEY_FOR_USER_NAME          = "shared_pref_key_user_name";
    public static String SHARED_PREF_KEY_FOR_USER_EMAIL         = "shared_pref_key_user_email";
    public static String SHARED_PREF_KEY_FOR_USER_ID            = "shared_pref_key_user_user_id";
    public static String SHARED_PREF_KEY_FOR_USER_DISPLAY_PIC   = "shared_pref_key_display_pic";
    public static String SHARED_PREF_KEY_FOR_ACTIVITY_OTP_FROM_PROFILE          = "shared_pref_key_otp_from_profile";



    /*  public static String USER_LOGIN_TYPE_FOR_SKIP        = "user_type_skip";
        public static String USER_LOGIN_TYPE_FOR_FACEBOOK    = "user_type_facebook";
        public static String USER_LOGIN_TYPE_FOR_GOOGLE      = "user_type_google";
        public static String USER_LOGIN_TYPE_FOR_ERP_STUDENT = "user_type_erp_student";
        public static String USER_LOGIN_TYPE_FOR_ERP_FACULTY = "user_type_erp_faculty";*/

    public static int LOGIN_TYPE_FOR_SKIP           = 1;
    public static int LOGIN_TYPE_FOR_FACEBOOK       = 2;
    public static int LOGIN_TYPE_FOR_GOOGLE         = 2;
    public static int LOGIN_TYPE_FOR_ERP_STUDENT    = 3;
    public static int LOGIN_TYPE_FOR_ERP_FACULTY    = 4;


    public static String contactNumber      = "tel:01204570000";
    public static String SQLITE_FOLDER_NAME = "ShardaTech";

    public static String SUB_CATEGORY_TYPE_CALL         = "call";
    public static String SUB_CATEGORY_TYPE_PDF_DOWNLOAD = "pdf";
    public static String SUB_CATEGORY_TYPE_WEB_VIEW     = "web_view";
    public static String SUB_CATEGORY_TYPE_FACILITY     = "facility";
    public static String SUB_CATEGORY_TYPE_DIRECTORY    = "directory";


    public static final String BASE_URL                 = "http://13.126.221.192/shardaapp/v1";
//    public static final String BASE_URL                 = "http://13.126.221.192/shardaapp_dev/v1";
//    public static final String BASE_URL                 = "http://117.55.241.167:8089/shardaApp/v1";
    public static final String USER_PROFILE_URL         = BASE_URL + "/user_profile";
    public static final String USER_MY_PROFILE_URL      = BASE_URL + "/show_profile";
    public static final String HOME_PAGER_URL           = BASE_URL + "/homepage_banner";
    public static final String CATEGORY_URL             = BASE_URL + "/homepage_cat";
    public static final String DRAWABLE_URL             = BASE_URL + "/drawer";
    public static final String SUB_CATEGORY_URL         = BASE_URL + "/subcat";
    public static final String SUB_SUB_CATEGORY_URL     = BASE_URL + "/sub_sub_cat";
    public static final String NOTIFICATION_URL         = BASE_URL + "/notification";
    public static final String USER_FEEDBACK_URL        = BASE_URL + "/user_feedback";
    public static final String USER_MOBILE_AUTHENTICATION    = BASE_URL + "/send_otp";
    public static final String USER_OTP_AUTHENTICATION       = BASE_URL + "/otp_verification";
    public static final String USER_EDIT_PROFILE_URL         = BASE_URL + "/edit_profile";
    public static final String USER_UPDATE_IMG_URL           = BASE_URL + "/edit_profile_picture";

    public static final String CAT_TYPE_MAP_TEMP_URL    = "https://api.myjson.com/bins/16qazn";
    public static final String FACILITY_JSON            = "https://api.myjson.com/bins/bme0j";

    //    public static final String CAT_TYPE_MAP_URL         = BASE_URL + "/coll_map";

    public static final String PREF_NAME = "EventOn";
    public static String hashMapStr = "";
    public static String hashFacilityStr = "";

    public static Constant getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

// Check Network Connectivity
    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    public static String getDate(long time) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static String getDisplayableTime(long delta)
    {
        long difference=0;
        Long mDate = java.lang.System.currentTimeMillis();

        if(mDate > delta)
        {

            difference= mDate - delta;
            final long seconds = difference/1000;
            final long minutes = seconds/60;
            final long hours = minutes/60;
            final long days = hours/24;
            final long months = days/31;
            final long years = days/365;

            if (seconds < 86400) // 24 * 60 * 60
            {
                return "Yesterday";
            }
            else if (seconds >= 86400 && seconds < 432000) // 5 * 24 * 60 * 60
            {
                return (days +1)+ " days ago";
            }
            else if (seconds >=432000 && seconds <31104000)
            {
                return getDate(delta);
            }
            else
            {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        else{
            return "Today";
        }
    }
}
