package services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import method.Constant;
import method.MySharedPref;

/**
 * Created by sharda on 12/6/2017.
 */

public class FCMIntentService extends IntentService {

    private static final int NOTIFICATION_ID_SIMPLE     = 1;
    private static final int NOTIFICATION_ID_NEWS       = 2;
    private static final int NOTIFICATION_ID_EVENT      = 3;
    private static final int NOTIFICATION_EVENING       = 4;
    private static final int NOTIFICATION_MORNING       = 5;
    private static final int NOTIFICATION_UPDATE        = 6;
    private static final int NOTIFICATION_ACTIVITY      = 7;
    private static final int NOTIFICATION_SINGLE_BUTTON = 8;
    private static final int NOTIFICATION_OPEN_WEBPAGE  = 9;

   int collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
   int userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_SKIP,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
   int userId          = MySharedPref.getUserId(this,Constant.USER_ID);

    public FCMIntentService() {
        super(FCMIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        if (!extras.isEmpty() && extras.getString("type")!= null) {

            int type = Integer.parseInt(extras.getString("type"));
            if (type == 1) {
            } else if (type == 2) {
            }
        }
        }

}
