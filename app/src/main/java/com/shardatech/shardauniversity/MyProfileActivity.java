package com.shardatech.shardauniversity;


import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import databaseTable.HomeActivityCategoryTable;
import helper.ImageUtils;
import helper.RealPathUtil;
import method.CircleTransform;
import method.Constant;
import method.MySharedPref;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by sharda on 4/11/2018.
 */

public class MyProfileActivity extends AppCompatActivity {

    private String title;
    EditText tvUserFirstName, tvUserLastName, tvUserEmail;
    RadioGroup radioGroupMaleFemale, radioGroupStudentEmployee;
    ImageView ivUserImage;
    TextView tvUserContact, tvUserDOB, tvUpdateNumber, tvChangeImage;
    View viewDob,viewLocation;
    private RadioButton radioButtonMaleFemale, radioButtonStuEmp;
    RadioButton male, female, transgender;
    RadioButton student, employee, other;
    private static final int REQUEST_CAMERA = 1001;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_GALLERY = 1002;
    private static final int REQUEST_GALLERY_PERMISSION = 2;
    private Bitmap mBitmap;
    private Uri selectedImageUri;

    private int mYear, mMonth, mDay;
    private String edit = "EDIT";
    private String save = "SAVE";
    private boolean inEdit = true, error = false;
    String userFirstName, userLastName, userEmail, userContactNumber, userProfilePic, userGender, userAdd, userCity, userDOB, userRole;
    TextInputLayout tvInputUserFirstName;
    ProgressDialog progressDialog;
    BottomSheetDialog mBottomSheetDialog;
    LinearLayout llUserImage;
    String localAdd,serverAdd;

    String username, userdob, usergender, errorMessage, user_dob;
    Uri uri;
    public static final int RequestPermissionCode = 7;
    PlaceAutocompleteFragment places;
    TextView originEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        tvUserFirstName = (EditText) findViewById(R.id.tv_user_first_name);
        tvUpdateNumber = (TextView) findViewById(R.id.tv_update_number);
        /*tvUserLastName    = (EditText) findViewById(R.id.tv_user_last_name);*/
        tvUserEmail = (EditText) findViewById(R.id.tv_user_email);
        tvUserContact = (TextView) findViewById(R.id.tv_user_contact);
        radioGroupMaleFemale = (RadioGroup) findViewById(R.id.radioGroupMaleFemale);
        male = (RadioButton) findViewById(R.id.radioMale);
        female = (RadioButton) findViewById(R.id.radioFeMale);
        transgender = (RadioButton) findViewById(R.id.radioTransGender);
        tvUserDOB = (TextView) findViewById(R.id.tv_user_dob);
        tvChangeImage = (TextView) findViewById(R.id.tv_change_image);
        ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
        viewDob = (View) findViewById(R.id.view_dob);
        viewLocation = (View) findViewById(R.id.view_location);
        radioGroupStudentEmployee = (RadioGroup) findViewById(R.id.radioGroupStudentEmployee);
        student = (RadioButton) findViewById(R.id.radioStudent);
        employee = (RadioButton) findViewById(R.id.radioEmp);
        other = (RadioButton) findViewById(R.id.radioOthers);
        tvInputUserFirstName = (TextInputLayout) findViewById(R.id.tv_input_user_first_name);
        llUserImage = (LinearLayout) findViewById(R.id.ll_user_image);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        title = getString(R.string.my_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorNavSideBarTopView)));
        getSupportActionBar().setTitle(title);

        userFirstName = MySharedPref.getUserName(MyProfileActivity.this, Constant.USER_NAME);
        userLastName = MySharedPref.getUserLastName(MyProfileActivity.this, Constant.USER_LAST_NAME);
        userEmail = MySharedPref.getUserEmail(MyProfileActivity.this, Constant.USER_EMAIL);
        userContactNumber = MySharedPref.getUserContactNumber(MyProfileActivity.this, Constant.USER_CONTACT_NUMBER);
        userProfilePic = MySharedPref.getUserDisplayPic(MyProfileActivity.this, Constant.USER_PROFILE_PIC);
        userGender = MySharedPref.getUserGender(MyProfileActivity.this, Constant.USER_GENDER);
        userAdd = MySharedPref.getUserAdd(MyProfileActivity.this, Constant.USER_ADD);
        userCity = MySharedPref.getUserCity(MyProfileActivity.this, Constant.USER_CITY);
        userDOB = MySharedPref.getUserDOB(MyProfileActivity.this, Constant.USER_DOB);
        userRole = MySharedPref.getUserIam(MyProfileActivity.this, Constant.USER_ROLE);

        places = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        originEditText = (TextView) places.getView().findViewById(R.id.editWorkLocation);

        serverAdd = userAdd;
                places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        localAdd = place.getName().toString();
                        serverAdd = place.getAddress().toString();
                        Log.i("TAG", "Place: " + place.getLocale());
                        Log.i("TAG", "Place Selected: " + place.getName() + "  " + place.getLatLng());
                    }
                    @Override
                    public void onError(Status status) {
                        Log.i("TAG", "An error occurred: " + status.getStatusMessage());
                        Log.i("TAG", "An error occurred: " + status);
                    }
                });

        userProfilePic = MySharedPref.getUserDisplayPic(MyProfileActivity.this, Constant.USER_PROFILE_PIC);
        if (userProfilePic.length() > 0 && userProfilePic != null) {
            Picasso.with(MyProfileActivity.this)
                    .load(userProfilePic)
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(ivUserImage);
        } else {
            Picasso.with(MyProfileActivity.this)
                    .load(R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .transform(new CircleTransform())
                    .into(ivUserImage);
        }

        if (userCity.length() > 0 || userFirstName != null) {
            originEditText.setText(userCity);
        } else {
            originEditText.setText("Enter Your Location");
        }

        if (userFirstName.length() > 0 || userFirstName != null) {
            tvUserFirstName.setText(userFirstName);
        } else {
            tvUserFirstName.setText("Enter Name");
        }
        if (userEmail.length() > 0 || userEmail != null) {
            tvUserEmail.setText(userEmail);
        } else {
            tvUserEmail.setText("Enter Email-id");
        }
        if (userContactNumber.length() > 0) {
            tvUserContact.setText("+" + userContactNumber);
        } else {
            tvUserContact.setText("Please Verify your Number");
        }
        if (userDOB.length() > 0 && userDOB != null) {
            tvUserDOB.setText(userDOB.toString());
        }
        if (userGender.length() > 0 && userGender != null) {
            if (userGender.equals("M")) {
                male.setChecked(true);
                female.setChecked(false);
                transgender.setChecked(false);
            } else if (userGender.equals("F")) {
                male.setChecked(false);
                female.setChecked(true);
                transgender.setChecked(false);
            } else if (userGender.equals("T")) {
                male.setChecked(false);
                female.setChecked(false);
                transgender.setChecked(true);
            }
        }

        if (userGender.length() > 0 && userGender != null) {
            if (userGender.equals("M")) {
                male.setChecked(true);
                female.setChecked(false);
                transgender.setChecked(false);
            } else if (userGender.equals("F")) {
                male.setChecked(false);
                female.setChecked(true);
                transgender.setChecked(false);
            } else if (userGender.equals("O")) {
                male.setChecked(false);
                female.setChecked(false);
                transgender.setChecked(true);
            }
        } else {
            male.setChecked(false);
            female.setChecked(false);
            transgender.setChecked(false);
        }

        if (userRole.length() > 0 || userRole != null) {
            if (userRole.equals("S")) {
                student.setChecked(true);
                employee.setChecked(false);
                other.setChecked(false);
            } else if (userRole.equals("P")) {
                student.setChecked(false);
                employee.setChecked(true);
                other.setChecked(false);
            } else if (userRole.equals("O")) {
                student.setChecked(false);
                employee.setChecked(false);
                other.setChecked(true);
            }
        } else {
            student.setChecked(false);
            employee.setChecked(false);
            other.setChecked(false);
        }

        tvUserDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MyProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tvUserDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        tvUpdateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, SubCategoryActivity.class);
                intent.putExtra("checkActivity", true);
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_COLOR, "#e65553");
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_NAME, "University Login");
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_CODE, "facility");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        tvChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

        llUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });

    }


    public void toggleBottomSheet() {

        if (CheckingPermissionIsEnabledOrNot()) {
            mBottomSheetDialog = new BottomSheetDialog(this);
            View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
            mBottomSheetDialog.setContentView(sheetView);
            mBottomSheetDialog.show();

            RelativeLayout bottomSheetGallery = (RelativeLayout) sheetView.findViewById(R.id.rl_gallery);
            RelativeLayout bottomSheetCamera = (RelativeLayout) sheetView.findViewById(R.id.rl_choose_cam);

            bottomSheetCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initCameraIntent();
                }
            });

            bottomSheetGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initGalleryIntent();
                }
            });

        }

        // If, If permission is not enabled then else condition will execute.
        else {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }
    }


    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean GalleryPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && CameraPermission) {
//                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                        toggleBottomSheet();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void initGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void initCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getOutputMediafile(1);
        selectedImageUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private File getOutputMediafile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

    String realPath = "";

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                realPath = Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name) + "/" + selectedImageUri.getLastPathSegment();

            } else if (requestCode == REQUEST_GALLERY) {
                selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 11) {
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                } else if (Build.VERSION.SDK_INT < 19) {
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                } else {
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                }
            }
            mBitmap = ImageUtils.getScaledImage(selectedImageUri, this);
            imageOreintationValidator(mBitmap, realPath);
            setImageBitmap(mBitmap);
        }
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }


    private void setImageBitmap(Bitmap bm) {
//        ivUserImage.setImageBitmap(bm);
        Picasso.with(MyProfileActivity.this)
                .load(selectedImageUri)
                .placeholder(R.drawable.user)
                .transform(new CircleTransform())
                .into(ivUserImage);

        try {
            execMultipartPost(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void execMultipartPost(Bitmap bm) throws Exception {

        progressDialog.show();
        mBottomSheetDialog.dismiss();

        File file = new File(realPath);
        String contentType = file.toURL().openConnection().getContentType();
        RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);
        final String filename = "profile_pic_" + /*System.currentTimeMillis() / 1000L*/ String.valueOf(MySharedPref.getUserId(this, Constant.USER_ID));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", String.valueOf(MySharedPref.getUserId(this, Constant.USER_ID)))
                .addFormDataPart("group_id", "1")
                .addFormDataPart("token", "NVbk9J_eE@ux2v?3")
                .addFormDataPart("temp_image", filename + ".jpg", fileBody)
                .addFormDataPart("img_stream", imgString)
                .build();

        Request request = new Request.Builder()
                .url(Constant.USER_UPDATE_IMG_URL)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(200, TimeUnit.SECONDS).build();
        okHttpClient.newBuilder().readTimeout(200, TimeUnit.SECONDS).build();
        okHttpClient.newBuilder().writeTimeout(200, TimeUnit.SECONDS).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                String responseJsonData = response.body().string();
                if (!response.isSuccessful()) {
                    MyProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    MySharedPref.setUserDisplayPic(MyProfileActivity.this, Constant.USER_PROFILE_PIC, jsonObject.optString("user_profile_pic"));

                    MyProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException paramResponse) {
                    MyProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit_done:
                errorMessage = "Please fill ";
                error = false;
                if (inEdit) {
                    item.setTitle(save);
                    ediTable();
                    inEdit = false;
                    viewDob.setBackgroundColor(getResources().getColor(R.color.colorNavSideBarTopView));
                    viewLocation.setBackgroundColor(getResources().getColor(R.color.colorNavSideBarTopView));

                } else {

                    localAdd = originEditText.getText().toString();


                    if (Constant.isConnectingToInternet(MyProfileActivity.this.getApplicationContext())) {
                        int selectedId = radioGroupMaleFemale.getCheckedRadioButtonId();
                        radioButtonMaleFemale = (RadioButton) findViewById(selectedId);
                        int selectedIdStuEmp = radioGroupStudentEmployee.getCheckedRadioButtonId();
                        radioButtonStuEmp = (RadioButton) findViewById(selectedIdStuEmp);

                        if (tvUserFirstName.getText().toString() == null || tvUserFirstName.getText().toString().length() == 0) {
                            error = true;
                            errorMessage = errorMessage + "Name,";
                        }
                        if (radioButtonMaleFemale == null) {
                            error = true;
                            errorMessage = errorMessage + "Gender,";
                        }
                        if (originEditText.getText().toString() == null || originEditText.getText().toString().length() == 0) {
                            error = true;
                            errorMessage = errorMessage + "Your Location,";
                        }
                        if (radioButtonStuEmp == null) {
                            error = true;
                            errorMessage = errorMessage + "Role.,";
                        }

                        if (error) {
                            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
                            Toast.makeText(MyProfileActivity.this,
                                    errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            item.setTitle(edit);
                            unEdiTable();
                            inEdit = true;
                            viewDob.setBackgroundColor(getResources().getColor(R.color.colorGrayLight));
                            viewLocation.setBackgroundColor(getResources().getColor(R.color.colorGrayLight));
                            updateProfile(tvUserFirstName.getText().toString(),
                                    radioButtonMaleFemale.getText().toString(),
                                    tvUserDOB.getText().toString(),
                                    radioButtonStuEmp.getText().toString(),localAdd,serverAdd);
                        }
                    } else {
                        aleartDialogForInternetConnectivity();
                    }
                }
        }
        return false;
    }

    private void updateProfile(String name, String gender, String dateOfBirth, String iAm, String city, String address) {

        int userId = MySharedPref.getUserId(this, Constant.USER_ID);

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(Constant.USER_ID, String.valueOf(userId))
                .add(Constant.USER_FIRST_NAME, String.valueOf(name))
                .add(Constant.USER_GENDER, String.valueOf(gender))
                .add(Constant.USER_DOB, dateOfBirth)
                .add(Constant.USER_ROLE, String.valueOf(iAm))
                .add(Constant.USER_CITY, city)
                .add(Constant.USER_ADD, address)
                .build();

        Request request = new Request.Builder()
                .url(Constant.USER_EDIT_PROFILE_URL)
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                MyProfileActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MyProfileActivity.this, "An error occurred, Please try again" + e, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()) {
                    MyProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    MySharedPref.setUserName(MyProfileActivity.this, Constant.USER_NAME, jsonObject.optString("user_first_name"));
//                    MySharedPref.setUserName(MyProfileActivity.this,Constant.USER_FIRST_NAME, jsonObject.optString("user_first_name"));
                    MySharedPref.setUserGender(MyProfileActivity.this, Constant.USER_GENDER, jsonObject.optString("user_gender"));
                    MySharedPref.setUserDOB(MyProfileActivity.this, Constant.USER_DOB, jsonObject.optString("user_dob"));
                    MySharedPref.setUserIam(MyProfileActivity.this, Constant.USER_ROLE, jsonObject.optString("user_role"));
                    MySharedPref.setUserCity(MyProfileActivity.this, Constant.USER_CITY, jsonObject.optString("user_city"));
                    MySharedPref.setUserAdd(MyProfileActivity.this, Constant.USER_ADD, jsonObject.optString("user_address"));

                    MyProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //finish();
                } catch (JSONException paramResponse) {
                    MyProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        userContactNumber = MySharedPref.getUserContactNumber(MyProfileActivity.this, Constant.USER_CONTACT_NUMBER);
        if (userContactNumber.length() > 0) {
            tvUserContact.setText("+" + userContactNumber);
        } else {
            tvUserContact.setText("Please Verify your Number");
        }

        userFirstName = MySharedPref.getUserName(MyProfileActivity.this, Constant.USER_NAME);
        if (userFirstName.length() > 0 || userFirstName != null) {
            tvUserFirstName.setText(userFirstName);
        } else {
            tvUserFirstName.setText("Enter Name");
        }
    }

    @SuppressLint("ResourceType")
    public void ediTable() {
        tvUserFirstName.setEnabled(true);
        tvUserDOB.setEnabled(true);
        tvUserDOB.setTextColor(getResources().getColor(R.color.colorBlack));
        tvUserEmail.setTextColor(getResources().getColor(R.color.colorBlack));
        originEditText.setTextColor(getResources().getColor(R.color.colorBlack));
        originEditText.setEnabled(true);
        for (int i = 0; i < radioGroupMaleFemale.getChildCount(); i++) {
            radioGroupMaleFemale.getChildAt(i).setEnabled(true);
        }

        for (int i = 0; i < radioGroupStudentEmployee.getChildCount(); i++) {
            radioGroupStudentEmployee.getChildAt(i).setEnabled(true);
        }
    }

    @SuppressLint("ResourceType")
    public void unEdiTable() {
        tvUserFirstName.setEnabled(false);
        tvUserDOB.setEnabled(false);
        originEditText.setEnabled(false);
        originEditText.setTextColor(getResources().getColor(R.color.colorGrayDark));
        tvUserEmail.setTextColor(getResources().getColor(R.color.colorGrayDark));
        tvUserDOB.setTextColor(getResources().getColor(R.color.colorGrayDark));
        for (int i = 0; i < radioGroupMaleFemale.getChildCount(); i++) {
            radioGroupMaleFemale.getChildAt(i).setEnabled(false);
        }

        for (int i = 0; i < radioGroupStudentEmployee.getChildCount(); i++) {
            radioGroupStudentEmployee.getChildAt(i).setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_edit_save, menu);
        return true;
    }

    public void aleartDialogForInternetConnectivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_internet_title));
        builder.setMessage(getResources().getString(R.string.aleart_dialog_for_internet_message));
        builder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
}
