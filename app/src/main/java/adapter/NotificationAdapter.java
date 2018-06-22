package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shardatech.shardauniversity.NotificationDetailsActivity;
import com.shardatech.shardauniversity.R;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

import databaseTable.NotificationTable;
import method.Constant;
import method.MySharedPref;

public class NotificationAdapter extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder>{
    public static final int TYPE_HEADING = 0;
    public static final int TYPE_NOTIFICATION= 1;
    int viewId;
    HashMap<Integer,Integer> isPositionCovered;

    int subHeadingId;
    Context mContext;
    String colorCode,email;
    Cursor mCursor;


    private int collegeId;
    private int userLoginType;
    private int userId;
    String url,webViewUrl;
    String title,addDate,details;
    public Cursor getmCursor(){
        return this.mCursor;
    }

    public Cursor swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
        this.mCursor = newCursor;
        return mCursor;
    }


    public NotificationAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
        isPositionCovered= new HashMap<Integer, Integer>();
    }

    @Override
    public int getItemViewType(int position) {
        if(super.getCursor() !=null){
            super.getCursor().moveToPosition(position);
            if (super.getCursor().getInt(super.getCursor().getColumnIndex("isHeader")) == 0){
                return TYPE_NOTIFICATION;
            }
            else{
                return TYPE_HEADING;
            }
        }
        return TYPE_NOTIFICATION;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        long notiDate = cursor.getInt(cursor.getColumnIndex(NotificationTable.NOTIFICATION_DATE)) * 1000L;
        long timeStampAddDate = cursor.getInt(cursor.getColumnIndex(NotificationTable.NOTIFICATION_ADD_DATE)) * 1000L;
        String notificationAddDate = Constant.getDate(timeStampAddDate);
        String notificationDaysAgo = Constant.getDisplayableTime(timeStampAddDate);
      //  String notifiAddDate = Constant.getDate(notiDate);



        switch (getItemViewType(cursor.getPosition())) {
            case TYPE_HEADING:
                ((ViewHolderNotificationSection) viewHolder).sectionText.setText(notificationDaysAgo);
                break;
            case TYPE_NOTIFICATION:
                ((ViewHolderNotification) viewHolder).tvNotificationTitle.setText(cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_TITLE)));
                //((ViewHolderNotification) viewHolder).tvNotificationDetails.setText("Category - "+cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_SUB_SUB_CAT_NAME)));
                ((ViewHolderNotification) viewHolder).tvNotificationDetails.setText(""+cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_SUB_SUB_CAT_NAME)));
                //((ViewHolderNotification) viewHolder).tvNotificationAddDate.setText("Posted on - "+notificationAddDate);
                ((ViewHolderNotification) viewHolder).tvNotificationAddDate.setText(""+notificationAddDate);

                url = (cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_IMAGE_URL)));
                title = cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_TITLE));
                details = cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_DETAILS));

                ((ViewHolderNotification) viewHolder).updateCursor(cursor);
                if (url.length() > 0) {
                    Picasso.with(mContext)
                            .load(url)
                            .placeholder(R.drawable.default_bg_icon)
                            .into(((ViewHolderNotification) viewHolder).ivNotification);
                } else {
                    Picasso.with(mContext)
                            .load(R.drawable.ic_menu_gallery)
                            .placeholder(R.drawable.default_bg_icon)
                            .into(((ViewHolderNotification) viewHolder).ivNotification);
                }
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_NOTIFICATION) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
                return new ViewHolderNotification(itemView);
            }
            else if(viewType == TYPE_HEADING){
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_notification, parent, false);
                return new ViewHolderNotificationSection(itemView);
            }
            else return null;
    }


    public class ViewHolderNotification extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNotificationTitle,tvNotificationDetails,tvNotificationAddDate,tvNotificationDate;
        public ImageView ivNotification;
        public int position;
        private Cursor cursor;
        RelativeLayout rlNotificationItem;
        public void updateCursor(Cursor cursor){
            this.cursor= cursor;
            this.position = cursor.getPosition();
        }

        ViewHolderNotification(View v) {
            super(v);

            collegeId       = MySharedPref.getUserCollegeId(mContext,Constant.USER_COLLAGE_ID);
            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(mContext,Constant.USER_LOGIN_TYPE));
            userId          = MySharedPref.getUserId(mContext,Constant.USER_ID);

            this.rlNotificationItem         = (RelativeLayout) v.findViewById(R.id.rl_notification_item);
            this.ivNotification             = (ImageView) v.findViewById(R.id.iv_notification_title);
            this.tvNotificationTitle        = (TextView) v.findViewById(R.id.tv_notification_title);
            this.tvNotificationDetails      = (TextView) v.findViewById(R.id.tv_notification_details);
            this.tvNotificationAddDate      = (TextView) v.findViewById(R.id.tv_notification_add_date);
            this.rlNotificationItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if((v.getId() == rlNotificationItem.getId()))
            {
                cursor.moveToPosition(position);
                String url = (cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_IMAGE_URL)));
                String webViewUrl = (cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_URL)));

                String title = cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_TITLE));
                String notiDate = cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_DATE));
                String details = cursor.getString(cursor.getColumnIndex(NotificationTable.NOTIFICATION_DETAILS));



                Intent intent = new Intent(mContext, NotificationDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NotificationTable.NOTIFICATION_TITLE,title);
                bundle.putString(NotificationTable.NOTIFICATION_DETAILS,details);
                bundle.putString(NotificationTable.NOTIFICATION_DATE,notiDate);
                bundle.putString(NotificationTable.NOTIFICATION_IMAGE_URL,url);
                bundle.putString(NotificationTable.NOTIFICATION_URL,webViewUrl);
 //               bundle.putString(NotificationTable.NOTIFICATION_DATE,mCursor.getString(mCursor.getColumnIndex(NotificationTable.NOTIFICATION_DATE)));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

               // Toast.makeText(mContext, "Notification clicked...", Toast.LENGTH_SHORT).show();
            /*  mCursor.moveToPosition(position);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mCursor.getString(mCursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL)), null));
                mContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));*/
            }
        }
    }

    public class ViewHolderNotificationSection extends RecyclerView.ViewHolder {
        public TextView sectionText;
        public int position;
        public void updateCursor(int position){
            this.position=position;
        }

        ViewHolderNotificationSection(View v) {
            super(v);

            collegeId       = MySharedPref.getUserCollegeId(mContext,Constant.USER_COLLAGE_ID);
            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(mContext,Constant.USER_LOGIN_TYPE));
            userId          = MySharedPref.getUserId(mContext,Constant.USER_ID);
            this.sectionText                = (TextView) v.findViewById(R.id.section_text);
        }


    }
}

