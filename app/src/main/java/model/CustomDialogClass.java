package model;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;

import org.json.JSONObject;

/**
 * Created by sharda on 9/24/2017.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button close;
    public JSONObject jsonObject;

    public TextView heading,text;
    public CustomDialogClass(Activity a, JSONObject jsonObject) {
        super(a);
        // TODO Auto-generated constructor stub
        this.jsonObject = jsonObject;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_map_alert);
        heading = (TextView) findViewById(R.id.tv_heading);
        text = (TextView) findViewById(R.id.tv_sub_heading);
        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(this);

        String name = jsonObject.optString("name");
        String add = jsonObject.optString("address");
        final String phone = jsonObject.optString("phone");
        String timings = jsonObject.optString("timings");
        String otherDetails = jsonObject.optString("other_details");

        StringBuilder subTextBuilder = new StringBuilder();
        if (add.length()>0){
            subTextBuilder.append("Address: ").append(add).append("\n");
        }
        if (timings.length()>0){
            subTextBuilder.append("Timings: ").append(timings).append("\n");
        }
        if (phone.length()>0){
            subTextBuilder.append("Contact: ").append(phone);
        }
        if (otherDetails.length()>0){
            subTextBuilder.append("Details: ").append(otherDetails);
        }

        text.setText(subTextBuilder.toString());
        heading.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}