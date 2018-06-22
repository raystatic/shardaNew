package fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shardatech.shardauniversity.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import adapter.AdapterCommonAcadCampHealth;
import database.ApplicationContentProvider;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.SubCategoryTable;
import databaseTable.SubHeadingTable;
import method.Constant;
import method.MySharedPref;


/**
 * Created by sharda on 9/10/2017.
 */

public class FragCommonAcadCampHealth extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private AdapterCommonAcadCampHealth academicAdapter;
    private int categoryId,collegeId;
    private String categoryCode;
    private String categoryColor;
    private ContentLoadingProgressBar contentLoadingProgressBarSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_academics, null);
        recyclerView    = (RecyclerView) v.findViewById(R.id.rl_sub_category);

        categoryId      = getArguments().getInt(HomeActivityCategoryTable.CATEGORY_Id);
        categoryColor   = getArguments().getString(HomeActivityCategoryTable.CATEGORY_COLOR);
        categoryCode    = getArguments().getString(HomeActivityCategoryTable.CATEGORY_CODE);
        collegeId       = MySharedPref.getUserCollegeId(getActivity(),Constant.USER_COLLAGE_ID);



        contentLoadingProgressBarSlider = (ContentLoadingProgressBar) v.findViewById(R.id.contentLoadingPBViewPager);
        contentLoadingProgressBarSlider.setVisibility(View.VISIBLE);
        subCategoryJsonLoad(getActivity().getApplicationContext());

        showSubCategoryData();
        refreshData();
        return v;
    }



    private void refreshData(){
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }


    public void subCategoryJsonLoad(final Context mContext)
    {
        int userId          = MySharedPref.getUserId(getActivity(),Constant.USER_ID);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.SUB_CATEGORY_URL+"?"+HomeActivityCategoryTable.CATEGORY_Id+"="+categoryId
                +"&"/*+HomeActivityCategoryTable.CATEGORY_CODE+"="+categoryCode
                +"&"*/+Constant.USER_COLLAGE_ID+"="+collegeId
                        +"&"+Constant.USER_ID+"="+userId)
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
                    JSONArray jsonArray = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("sub_cat");
                    JSONArray jsonArray2 = jsonArray.getJSONObject(0).getJSONArray("sub_heading");

                    String selection            = SubHeadingTable.SUB_HEADING_CATEGORY_ID+ "=?";
                    String selection_sub_cat    = SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID+
                                                    " in (select "+SubHeadingTable.SUB_HEADING_Id+
                                                    " from "+SubHeadingTable.TABLE_SUB_HEADING+
                                                    " WHERE "+SubHeadingTable.SUB_HEADING_CATEGORY_ID+"=?)";
                    String[] selectionArgs = { String.valueOf(categoryId) };

                    mContext.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_SUB_CATEGORY,selection_sub_cat,selectionArgs);
                    mContext.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_SUB_HEADING,selection,selectionArgs);

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put(SubCategoryTable.SUB_CATEGORY_Id, jsonArray1.getJSONObject(i).optString("sub_cat_id"));
                        values.put(SubCategoryTable.SUB_CATEGORY_NAME, jsonArray1.getJSONObject(i).optString("sub_cat_name"));
                        values.put(SubCategoryTable.SUB_CATEGORY_IMAGE_URL, jsonArray1.getJSONObject(i).optString("sub_cat_image_url"));
                        values.put(SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID, jsonArray1.getJSONObject(i).optString("sub_cat_heading_id"));
                        values.put(SubCategoryTable.SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE, jsonArray1.getJSONObject(i).optString("sub_cat_taken_offline"));
                        values.put(SubCategoryTable.SUB_CATEGORY_CAT_CODE, jsonArray1.getJSONObject(i).optString("sub_cat_cat_code"));
                        values.put(SubCategoryTable.SUB_CATEGORY_USER_TYPE, jsonArray1.getJSONObject(i).optString("sub_cat_user_type"));
                        values.put(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER, jsonArray1.getJSONObject(i).optString("sub_cat_contact_number"));
                        mContext.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_SUB_CATEGORY, values);
                    }

                    values = new ContentValues();
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        values.put(SubHeadingTable.SUB_HEADING_Id, jsonArray2.getJSONObject(i).optString("sub_heading_id"));
                        values.put(SubHeadingTable.SUB_HEADING_NAME, jsonArray2.getJSONObject(i).optString("sub_heading_name"));
                        values.put(SubHeadingTable.SUB_HEADING_CATEGORY_ID, jsonArray2.getJSONObject(i).optString("sub_heading_cat_id"));
                        values.put(SubHeadingTable.SUB_HEADING_DISPLAY_ORDER, jsonArray2.getJSONObject(i).optString("sub_heading_display_order"));
                        mContext.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_SUB_HEADING, values);
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


    public void showSubCategoryData() {
        academicAdapter = new AdapterCommonAcadCampHealth(getContext(),null,categoryColor);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(academicAdapter.getItemViewType(position)){
                    case AdapterCommonAcadCampHealth.TYPE_SUB_CATEGORY:
                        return 1;
                    case AdapterCommonAcadCampHealth.TYPE_SUB_CATEGORY_FIRST:
                        return 1;
                    case AdapterCommonAcadCampHealth.TYPE_SUBHEADING:
                        return 3;
                    default:
                        return 1;
                }
            }
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(academicAdapter);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = SubHeadingTable.SUB_HEADING_CATEGORY_ID+ "=?";
        String[] selectionArgs = { String.valueOf(categoryId) };
        return new CursorLoader(getActivity(), ApplicationContentProvider.CONTENT_URI_SUB_HEADING_JOIN_SUB_CATEGORY.buildUpon().build(), null,selection, selectionArgs,null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0 && !Constant.isConnectingToInternet(getActivity()) )
        {
            aleartDialogForInternetConnectivity();
        }
        if(data.getCount()>0)
        {
            contentLoadingProgressBarSlider.setVisibility(View.INVISIBLE);
        }
        academicAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

}
