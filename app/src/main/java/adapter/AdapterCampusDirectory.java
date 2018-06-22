package adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;

import java.util.HashMap;

import databaseTable.CampusDirectoryHeadingTable;
import databaseTable.CampusDirectoryMainTable;
import method.Constant;
import method.MySharedPref;

public class AdapterCampusDirectory extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> {
    public static final int TYPE_DEPARTMENT = 0;
    public static final int TYPE_DIRECTORY  = 1;
    int viewId;
    HashMap<Integer,Integer> isPositionCovered;

    int subHeadingId;
    Context mContext;
    String colorCode,email;
    Cursor mCursor;


    private int collegeId;
    private int userLoginType;
    private int userId;

    public Cursor swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
        this.mCursor = newCursor;
        return mCursor;
    }


    public AdapterCampusDirectory(Context context, Cursor cursor, String colorCode) {
        super(context, cursor);
        this.mContext = context;
        this.colorCode = colorCode;
        this.mCursor = cursor;
        subHeadingId=-1;
        viewId=0;
        isPositionCovered= new HashMap<Integer, Integer>();
    }

    @Override
    public int getItemViewType(int position) {
        if(super.getCursor() !=null){
            super.getCursor().moveToPosition(position);
            if (super.getCursor().getInt(super.getCursor().getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id)) == 0){
                if (!isPositionCovered.containsKey(position)){
                    viewId=0;
                    isPositionCovered.put(position,TYPE_DEPARTMENT);
                }
                return TYPE_DEPARTMENT;
            }
            else{
                if (!isPositionCovered.containsKey(position)){
                    viewId++;
                    isPositionCovered.put(position,TYPE_DIRECTORY);
                    return TYPE_DIRECTORY;
                }
                else if (isPositionCovered.get(position)<=2){
                    return isPositionCovered.get(position);
                }
                else return TYPE_DIRECTORY;
            }
        }
        return TYPE_DIRECTORY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        switch (getItemViewType(cursor.getPosition())) {
            case TYPE_DEPARTMENT:
                ((ViewHolderDepartment) viewHolder).tvDirectoryHeading.setText(cursor.getString(cursor.getColumnIndex(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME)));
                break;
            case TYPE_DIRECTORY:
                ((ViewHolderDirectory) viewHolder).tvPostName.setText(cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_DESIGNATION)));
                ((ViewHolderDirectory) viewHolder).updateCursor(cursor.getPosition());

                if(cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL)).length()>0){
                    ((ViewHolderDirectory) viewHolder).tvCampusDirectoryEmail.setVisibility(View.VISIBLE);
                }else{
                    ((ViewHolderDirectory) viewHolder).tvCampusDirectoryEmail.setVisibility(View.GONE);
                }
                if(cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER)).length()>0){
                    ((ViewHolderDirectory) viewHolder).tvCampusDirectoryMobile.setVisibility(View.GONE);
                }else{
                    ((ViewHolderDirectory) viewHolder).tvCampusDirectoryMobile.setVisibility(View.GONE);
                }

                if(cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EXT)).length()>0){
                    ((ViewHolderDirectory) viewHolder).tvCampusDirectoryExt.setVisibility(View.VISIBLE);
                }else{
                    ((ViewHolderDirectory) viewHolder).tvCampusDirectoryExt.setVisibility(View.GONE);
                }

                ((ViewHolderDirectory) viewHolder).tvCampusDirectoryName.setText(cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_NAME)));
                ((ViewHolderDirectory) viewHolder).tvCampusDirectoryEmail.setText("Email: "+cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL)));
                ((ViewHolderDirectory) viewHolder).tvCampusDirectoryMobile.setText("Mob: "+cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER)));
                ((ViewHolderDirectory) viewHolder).tvCampusDirectoryExt.setText("Ext: "+cursor.getString(cursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EXT)));

                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DIRECTORY) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.campus_directory_main, parent, false);
            return new ViewHolderDirectory(itemView,colorCode);
        } else if (viewType == TYPE_DEPARTMENT) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.campus_directory_heading, parent, false);
            return new ViewHolderDepartment(itemView);
        }
        else return null;
    }


    public class ViewHolderDirectory extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvPostName,tvCampusDirectoryName,tvCampusDirectoryMobile,tvCampusDirectoryExt,tvCampusDirectoryEmail;
        public ImageView ivCampusDirectoryIcon;
        public RelativeLayout rlCampusDirectoryInfo;
        public int position;
        public void updateCursor(int position){
            this.position=position;
        }

        ViewHolderDirectory(View v, String colorCode) {
            super(v);

            collegeId       = MySharedPref.getUserCollegeId(mContext,Constant.USER_COLLAGE_ID);
            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(mContext,Constant.USER_LOGIN_TYPE));
            userId          = MySharedPref.getUserId(mContext,Constant.USER_ID);

            this.rlCampusDirectoryInfo      = (RelativeLayout) v.findViewById(R.id.rl_campus_directory_info);
            this.tvPostName                 = (TextView) v.findViewById(R.id.tv_post_name);
            this.tvCampusDirectoryName      = (TextView) v.findViewById(R.id.tv_campus_directory_name);
            this.tvCampusDirectoryMobile    = (TextView) v.findViewById(R.id.tv_campus_directory_mobile);
            this.tvCampusDirectoryExt       = (TextView) v.findViewById(R.id.tv_campus_directory_ext);
            this.tvCampusDirectoryEmail     = (TextView) v.findViewById(R.id.tv_campus_directory_email);
            this.tvCampusDirectoryEmail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if((v.getId() == tvCampusDirectoryEmail.getId()))
            {
                mCursor.moveToPosition(position);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mCursor.getString(mCursor.getColumnIndex(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL)), null));
                mContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        }
    }


    private static int getColor(String colorCode,Context context){
        int colorint = Color.parseColor(colorCode);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorint, typedValue, true);
        int color = typedValue.data;
        return color;
    }


    public static class ViewHolderDepartment extends RecyclerView.ViewHolder {
        TextView tvDirectoryHeading;
        ViewHolderDepartment(View v) {
            super(v);
            this.tvDirectoryHeading = (TextView) v.findViewById(R.id.tv_directory_heading);
        }
    }

}

