//package services;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.provider.Settings;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v7.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.shardatech.shardauniversity.NotificationActivity;
//import com.shardatech.shardauniversity.R;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Map;
//import java.util.Random;
//
//import database.ApplicationContentProvider;
//import databaseTable.NotificationTable;
//import method.Constant;
//import method.MySharedPref;
//
///**
// * Created by sharda on 12/4/2017.
// */
//
//public class MyFirebaseMessagingServiceOld extends FirebaseMessagingService {
//    private static final String TAG = "MyFirebaseMsgService";
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "From: " + remoteMessage.getData());
//        Log.d(TAG, "From: " + remoteMessage.getNotification());
//        Log.d(TAG, "From: " + remoteMessage.getTtl());
//
//        notificationJsonLoad(remoteMessage);
//        // sendNotification(remoteMessage.getData());
//    }
//
//    private void sendNotification(Map<String,String> messageBody)
//    {
//        if(messageBody.get("type").equals("0")){
//
//            String imageUri = messageBody.get("image");
//            Bitmap bitmap = getBitmapfromUrl(imageUri);
//
//
///*            Bundle bundle = new Bundle();
//
//            bundle.putString("type",messageBody.get("type"));
//            bundle.putString("title",messageBody.get("title"));
//            bundle.putString("content",messageBody.get("content"));*/
//            Intent resultIntent = new Intent(this, NotificationActivity.class);
//            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent =
//                    PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_ONE_SHOT);
////
////            Intent intent = new Intent(this, NotificationActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
//
//            android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.logo_main)
//                    .setContentTitle(messageBody.get("title"))
//                    .setContentText(messageBody.get("content"))
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap))
//                    .setAutoCancel(true)
//                    .setVibrate(new long[] { 1000, 1000, 1000 })
//                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                    .setContentIntent(pendingIntent);
//
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify((new Random()).nextInt(), notificationBuilder.build());
//        }
//        else if (messageBody.get("type").equals("1"))
//        {
////            Intent intent = new Intent(this, NotificationActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
//            Intent resultIntent = new Intent(this, NotificationActivity.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            stackBuilder.addNextIntentWithParentStack(resultIntent);
//            PendingIntent pendingIntent =
//                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            String imageUri = messageBody.get("image");
//            if(imageUri.length()>0){
//                Bitmap bitmap = getBitmapfromUrl(imageUri);
//                android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_main)
//                        .setContentTitle(messageBody.get("title"))
//                        .setContentText(messageBody.get("content"))
//                        .setStyle(new NotificationCompat.BigPictureStyle()
//                                .bigPicture(bitmap))
//                        .setAutoCancel(true)
//                        .setVibrate(new long[] { 1000, 1000, 1000 })
//                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setContentIntent(pendingIntent);
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify((new Random()).nextInt(), notificationBuilder.build());
//            }
//            else{
//
//                android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_main)
//                        .setContentTitle(messageBody.get("title"))
//                        .setContentText(messageBody.get("content"))
//                        .setAutoCancel(true)
//                        .setVibrate(new long[] { 1000, 1000, 1000 })
//                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setContentIntent(pendingIntent);
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(0, notificationBuilder.build());
//            }
//
//        }
//        else if (messageBody.get("type").equals("2"))
//        {
//            Intent resultIntent = new Intent(this, NotificationActivity.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            stackBuilder.addNextIntentWithParentStack(resultIntent);
//            PendingIntent pendingIntent =
//                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
////            Intent intent = new Intent(this, NotificationActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
//
//            String imageUri = messageBody.get("image");
//            if(imageUri.length()>0){
//                Bitmap bitmap = getBitmapfromUrl(imageUri);
//                android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_main)
//                        .setContentTitle(messageBody.get("title"))
//                        .setContentText(messageBody.get("content"))
//                        .setStyle(new NotificationCompat.BigPictureStyle()
//                                .bigPicture(bitmap))
//                        .setAutoCancel(true)
//                        .setVibrate(new long[] { 1000, 1000, 1000 })
//                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setContentIntent(pendingIntent);
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify((new Random()).nextInt(), notificationBuilder.build());
//            }
//            else{
//                android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_main)
//                        .setContentTitle(messageBody.get("title"))
//                        .setContentText(messageBody.get("content"))
//                        .setAutoCancel(true)
//                        .setVibrate(new long[] { 1000, 1000, 1000 })
//                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                        .setContentIntent(pendingIntent);
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(0, notificationBuilder.build());
//            }
//        }
//    }
//
//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//
//        }
//    }
//
//    public void notificationJsonLoad(final RemoteMessage remoteMessage) {
//        int collegeId       = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
//        int userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_SKIP,MySharedPref.getUserLoginType(this,Constant.USER_LOGIN_TYPE));
//        int userId          = MySharedPref.getUserId(this,Constant.USER_ID);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(Constant.NOTIFICATION_URL
//                        + "?" + Constant.USER_ID + "=" + userId
//                        + "&" + Constant.USER_COLLAGE_ID + "=" + collegeId
//                        + "&" + Constant.USER_LOGIN_TYPE + "=" + userLoginType)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            public void onFailure(Request paramRequest, IOException paramIOException) {
//
//            }
//
//            public void onResponse(Response response)
//                    throws IOException {
//                String responseJsonData = response.body().string();
//                if (!response.isSuccessful()) {
//                  }
//
//                try {
//                    JSONArray jsonArray = new JSONArray(responseJsonData);
//                    ContentValues values = new ContentValues();
//                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("notification");
////                    getContentResolver().
////                            delete(ApplicationContentProvider.CONTENT_URI_NOTIFICATION, null, null);
//                    for (int i = 0; i < jsonArray1.length(); i++) {
//                        values.put(NotificationTable.NOTIFICATION_ID, jsonArray1.getJSONObject(i).optInt("notify_id"));
//                        values.put(NotificationTable.NOTIFICATION_SUB_SUB_CAT_ID, jsonArray1.getJSONObject(i).optInt("notify_sub_sub_cat_id"));
//                        values.put(NotificationTable.NOTIFICATION_SUB_SUB_CAT_NAME, jsonArray1.getJSONObject(i).optString("notify_sub_sub_cat_name"));
//                        values.put(NotificationTable.NOTIFICATION_TITLE, jsonArray1.getJSONObject(i).optString("notify_title"));
//                        values.put(NotificationTable.NOTIFICATION_DETAILS, jsonArray1.getJSONObject(i).optString("notify_details"));
//                        values.put(NotificationTable.NOTIFICATION_ADD_DATE, jsonArray1.getJSONObject(i).optInt("notify_add_date"));
//                        values.put(NotificationTable.NOTIFICATION_DATE, jsonArray1.getJSONObject(i).optInt("notify_date"));
//                        values.put(NotificationTable.NOTIFICATION_IMAGE_URL, jsonArray1.getJSONObject(i).optString("notify_image_url"));
//                        values.put(NotificationTable.NOTIFICATION_IS_IMP, jsonArray1.getJSONObject(i).optInt("is_important"));
//                        getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_NOTIFICATION, values);
//                    }
//                    Log.d(TAG, "From: " + remoteMessage.getData().toString());
//
//                    sendNotification(remoteMessage.getData());
//                } catch (JSONException paramResponse) {
//                    paramResponse.printStackTrace();
//                }
//            }
//        });
//    }
//}
