package method;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.shardatech.shardauniversity.AppApplication;
import com.shardatech.shardauniversity.BuildConfig;
import com.shardatech.shardauniversity.CampusDirectoryActivity;
import com.shardatech.shardauniversity.GenericFacilityActivity;
import com.shardatech.shardauniversity.R;
import com.shardatech.shardauniversity.WebViewActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import databaseTable.HomeActivityCategoryTable;
import databaseTable.SubCategoryTable;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by sharda on 10/3/2017.
 */

public class HelperFunction {
    public static int STORAGE_PERMISSION_CODE = 23;

    public static Cursor cursor;
    public static int mSubCategoryId = -1;
    public static String mDownnloadPdfUrl;
    public static String mSubCategoryName, mSubCatColor, mSubContactNumber;
    public static int mCollegeId, mUserLoginType, mUserId;

    public static void helperFunctionOnClickSubCategory(Context mContext) {
        if (mSubCategoryId != -1) {
            helperFunctionOnClickSubCategory(mContext, cursor, mSubContactNumber, mSubCategoryId, mDownnloadPdfUrl, mSubCategoryName, mCollegeId, mUserLoginType, mUserId, mSubCatColor);
        }
    }

    public static void helperFunctionOnClickSubCategory(Context mContext, Cursor mCursor, String subCatContactNumber,
                                                        int subCategoryID, String downloadPdfUrl, String subCategoryName,
                                                        int collegeId, int userLoginType, int userId, String subCatColor) {
        cursor = mCursor;
        mSubCategoryId = subCategoryID;
        mSubCatColor = subCatColor;
        mSubCategoryName = subCategoryName;
        mUserId = userId;
        mCollegeId = collegeId;
        mUserLoginType = userLoginType;
        mDownnloadPdfUrl = downloadPdfUrl;
        mSubContactNumber = subCatContactNumber;

        if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE)).equals("yes")) {
            if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CAT_CODE)).equals(Constant.SUB_CATEGORY_TYPE_CALL)) {
                HelperFunction.callSafty(mContext, mCursor, subCatContactNumber);
            } else if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CAT_CODE)).equals(Constant.SUB_CATEGORY_TYPE_PDF_DOWNLOAD)) {
                if (isStoragePermissionGranted((AppCompatActivity) mContext)) {
                    pdfViewJsonLoad(mContext, subCategoryID, subCategoryName, subCatColor, Constant.SUB_CATEGORY_TYPE_PDF_DOWNLOAD, userId, userLoginType, collegeId);
                    return;
                }
//                requestStoragePermission((AppCompatActivity) mContext);
            } else if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CAT_CODE)).equals(Constant.SUB_CATEGORY_TYPE_WEB_VIEW)) {
                loadWebView(mContext, subCategoryID, subCategoryName, subCatColor, Constant.SUB_CATEGORY_TYPE_WEB_VIEW, userId, userLoginType, collegeId);
            } else if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CAT_CODE)).equals(Constant.SUB_CATEGORY_TYPE_FACILITY)) {
                loadFacilityData(mContext, subCategoryID, subCategoryName, subCatColor, Constant.SUB_CATEGORY_TYPE_FACILITY, userId, userLoginType, collegeId);
            } else if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CAT_CODE)).equals(Constant.SUB_CATEGORY_TYPE_DIRECTORY)) {
                loadDirectoryData(mContext, subCategoryID, subCategoryName, subCatColor, Constant.SUB_CATEGORY_TYPE_DIRECTORY, userId, userLoginType, collegeId);
            }
        }
    }
//
//    private static boolean isReadStorageAllowed(Activity context) {
//        //Getting the permission status
//        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTER);
//        if (result == PackageManager.PERMISSION_GRANTED)
//            return true;
//        //If permission is not granted returning false
//        return false;
//    }
    //Requesting permission
//    private static void requestStoragePermission(AppCompatActivity context){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(context,Manifest.permission.READ_EXTERNAL_STORAGE)){
//        }
//        ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//    }

    public static boolean isStoragePermissionGranted(Activity mContext) {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            return false;
        }
    }

    private static void callSafty(Context mContext, Cursor mCursor, String subCatContactNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER)).isEmpty() ||
                mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER)).equals(null) ||
                mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER)).equals("null") ||
                mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER)).equals("")) {
            subCatContactNumber = Constant.contactNumber;
        } else {
            subCatContactNumber = "tel:" + (mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER)));
        }
        intent.setData(Uri.parse(subCatContactNumber));
        mContext.startActivity(intent);
    }

    public static void downloadPdf(Context mContext, Cursor mCursor, String url, String title, int subCategoryId) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("downloading data...");
        request.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subCategoryId + ".pdf");
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private static void loadWebView(Context mContext, int subCategoryID, String subCatName, String subCatColor, String subCatCode, int userId,
                                    int collegeId, int userLoginType) {


        ((AppApplication) mContext.getApplicationContext()).trackScreenView("Sub Category= " + subCatName + " , User Id = " + userId);

        if (subCatName.equals("Webmail")) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            ((AppApplication) mContext.getApplicationContext()).startActivity(intent);
        } else {

            Bundle bundle = new Bundle();
            Intent intent = new Intent(mContext, WebViewActivity.class);

            bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, subCatColor);
            bundle.putInt(SubCategoryTable.SUB_CATEGORY_Id, subCategoryID);
            bundle.putString(SubCategoryTable.SUB_CATEGORY_NAME, subCatName);
            bundle.putString(SubCategoryTable.SUB_CATEGORY_CAT_CODE, subCatCode);
            bundle.putInt(Constant.USER_ID, userId);
            bundle.putInt(Constant.USER_COLLAGE_ID, collegeId);
            bundle.putInt(Constant.USER_LOGIN_TYPE, userLoginType);

            intent.putExtras(bundle);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private static void loadFacilityData(Context mContext, int subCategoryID, String subCatName, String subCatColor, String subCatCode, int userId,
                                         int collegeId, int userLoginType) {

        ((AppApplication) mContext.getApplicationContext()).trackScreenView("Sub Category= " + subCatName + " , User Id = " + userId);

        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, GenericFacilityActivity.class);

        bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, subCatColor);
        bundle.putInt(SubCategoryTable.SUB_CATEGORY_Id, subCategoryID);
        bundle.putString(SubCategoryTable.SUB_CATEGORY_NAME, subCatName);
        bundle.putString(SubCategoryTable.SUB_CATEGORY_CAT_CODE, subCatCode);
        bundle.putInt(Constant.USER_ID, userId);
        bundle.putInt(Constant.USER_COLLAGE_ID, collegeId);
        bundle.putInt(Constant.USER_LOGIN_TYPE, userLoginType);

        intent.putExtras(bundle);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    private static void loadDirectoryData(Context mContext, int subCategoryID, String subCatName, String subCatColor, String subCatCode, int userId,
                                          int collegeId, int userLoginType) {

        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, CampusDirectoryActivity.class);

        bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, subCatColor);
        bundle.putInt(SubCategoryTable.SUB_CATEGORY_Id, subCategoryID);
        bundle.putString(SubCategoryTable.SUB_CATEGORY_NAME, subCatName);
        bundle.putString(SubCategoryTable.SUB_CATEGORY_CAT_CODE, subCatCode);
        bundle.putInt(Constant.USER_ID, userId);
        bundle.putInt(Constant.USER_COLLAGE_ID, collegeId);
        bundle.putInt(Constant.USER_LOGIN_TYPE, userLoginType);

        intent.putExtras(bundle);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }


    private static void pdfViewJsonLoad(final Context mContext, int subCategoryID, final String subCatName, String subCatColor, String subCatCode, int userId,
                                        int collegeId, int userLoginType) {
        if (openFile(subCatName, mContext)) {

            return;
        }

        ((AppApplication) mContext.getApplicationContext()).trackScreenView("Sub Category= " + subCatName + " , User Id = " + userId);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.SUB_SUB_CATEGORY_URL
                        + "?" + SubCategoryTable.SUB_CATEGORY_Id + "=" + subCategoryID
                        + "&" + SubCategoryTable.SUB_CATEGORY_CAT_CODE + "=" + subCatCode)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    final String url = jsonObject.optString("url");
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                     /*       contentLoadingWebView.setVisibility(View.GONE);
                            webView.loadUrl(String.valueOf(url));*/
                            new DownloadFile(mContext).execute(url, subCatName);
                        }
                    });
                } catch (JSONException paramResponse) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }


    public static class DownloadFile extends AsyncTask<String, String, Void> {
        Context context;
        ProgressDialog dialog;
        String fileUrl, fileName;

        public DownloadFile(Context mContext) {
            context = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            fileUrl = strings[0];
            fileName = strings[1];
            File folder = new File(Environment.getExternalStorageDirectory(), Constant.SQLITE_FOLDER_NAME);
            folder.mkdir();
            File pdfFile = new File(folder + "/" + fileName);
            if (pdfFile.exists()) {
                return null;
            }

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile, this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            boolean isSuccess = openFile(fileName, context);
            if (!isSuccess) {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            }
            dismissDialog();
        }

        @Override
        protected void onProgressUpdate(String... progress) {

            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        private void dismissDialog() {
            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        }

        protected Dialog onCreateDialog() {
            ProgressDialog pDialog = new ProgressDialog(context);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            this.dialog = pDialog;
            return pDialog;
        }

        public void publishProgressFromDownloader(float progress) {
            publishProgress("" + (int) (100 * progress));
        }
    }


    private static boolean openFile(String fileName, Context context) {
        File folder = new File(Environment.getExternalStorageDirectory(), Constant.SQLITE_FOLDER_NAME);
        File pdfFile = new File(folder, fileName);
        if (!pdfFile.exists()) {
            return false;
        }
        Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", pdfFile);

        context.grantUriPermission(context.getPackageName(),
                path, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
        return true;
    }



    public static class FileDownloader {
        private static final int MEGABYTE = 1024 * 5;

        public static void downloadFile(String fileUrl, File directory, DownloadFile asyncTask) {
            int length = 0;
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();
                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    length = length + bufferLength;
                    asyncTask.publishProgressFromDownloader(1.0f * length / (1.0f * totalSize));
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
