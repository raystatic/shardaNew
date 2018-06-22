package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.shardatech.shardauniversity.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import databaseTable.HomeActivityCategoryTable;
import method.Constant;
import method.MySharedPref;

/**
 * Created by sharda on 9/10/2017.
 */

public class FragmentApplyNowForAdmission extends Fragment {
    private View view;
    private WebView webView;
    private int categoryId,collegeId;
    private String categoryCode;
    private String categoryColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.webview_for_category, null);

        categoryId      = getArguments().getInt(HomeActivityCategoryTable.CATEGORY_Id);
        categoryColor   = getArguments().getString(HomeActivityCategoryTable.CATEGORY_COLOR);
        categoryCode    = getArguments().getString(HomeActivityCategoryTable.CATEGORY_CODE);
        collegeId       = MySharedPref.getUserCollegeId(getActivity(),Constant.USER_COLLAGE_ID);

        webViewJsonLoad();

        webView = (WebView) view.findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        return view;
    }

    public void webViewJsonLoad()
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
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    final String url = jsonObject.optString("url");
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                       /*     contentLoadingWebView.setVisibility(View.GONE);*/
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(String.valueOf(url));
                        }
                    });
                } catch (JSONException paramResponse) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }
}
