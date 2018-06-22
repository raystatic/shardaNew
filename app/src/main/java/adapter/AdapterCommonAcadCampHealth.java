package adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import databaseTable.SubCategoryTable;
import databaseTable.SubHeadingTable;
import method.Constant;
import method.HelperFunction;
import method.MySharedPref;

public class AdapterCommonAcadCampHealth extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> {
    public static final int TYPE_SUBHEADING = 0;
    public static final int TYPE_SUB_CATEGORY = 1;
    public static final int TYPE_SUB_CATEGORY_FIRST = 2;
    int viewId;
    HashMap<Integer,Integer> isPositionCovered;

    int subHeadingId;
    int subCategoryId;
    Context mContext;
    String colorCode;
    Cursor mCursor;
    private String subCatContactNumber;

    private int collegeId;
    private int userLoginType;
    private int userId;

    public Cursor swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
        this.mCursor = newCursor;
        return mCursor;
    }


    public AdapterCommonAcadCampHealth(Context context, Cursor cursor,String colorCode) {
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
            if (super.getCursor().getInt(super.getCursor().getColumnIndex(SubCategoryTable.SUB_CATEGORY_Id)) == 0){
                if (!isPositionCovered.containsKey(position)){
                    viewId=0;
                    isPositionCovered.put(position,TYPE_SUBHEADING);
                }
                return TYPE_SUBHEADING;
            }
            else if(super.getCursor().getInt(super.getCursor().getColumnIndex(SubHeadingTable.SUB_HEADING_Id) )!=subHeadingId || viewId <=2){
                subHeadingId= super.getCursor().getInt(super.getCursor().getColumnIndex(SubHeadingTable.SUB_HEADING_Id));
                if (!isPositionCovered.containsKey(position)){
                    viewId++;
                    isPositionCovered.put(position,TYPE_SUB_CATEGORY_FIRST);
                    return TYPE_SUB_CATEGORY_FIRST;
                }
                else if (isPositionCovered.get(position)<=2){
                    return isPositionCovered.get(position);
                }
                else return TYPE_SUB_CATEGORY_FIRST;
            }
            else{
                if (!isPositionCovered.containsKey(position)){
                    viewId++;
                    isPositionCovered.put(position,TYPE_SUB_CATEGORY);
                    return TYPE_SUB_CATEGORY;
                }
                else if (isPositionCovered.get(position)<=2){
                    return isPositionCovered.get(position);
                }
                else return TYPE_SUB_CATEGORY;
            }
        }
        return TYPE_SUB_CATEGORY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        switch (getItemViewType(cursor.getPosition())) {
            case TYPE_SUBHEADING:
                ((ViewHolderHeaderType) viewHolder).tv_sub_heading.setText(cursor.getString(cursor.getColumnIndex(SubHeadingTable.SUB_HEADING_NAME)));
                break;
            case TYPE_SUB_CATEGORY_FIRST:
                ((ViewHolderMainType2) viewHolder).tv_sub_category_item.setText(cursor.getString(cursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_NAME)));
                ((ViewHolderMainType2) viewHolder).updateCursor(cursor.getPosition());
                String url = (cursor.getString(cursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_IMAGE_URL)));
                if (url.length() > 0) {
                    Picasso.with(mContext)
                            .load(cursor.getString(cursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_IMAGE_URL)))
                            .placeholder(R.drawable.default_bg_icon)
                            .into(((ViewHolderMainType2) viewHolder).iv_sub_category_image);
                } else {
                    Picasso.with(mContext)
                            .load(R.drawable.ic_menu_gallery)
                            .placeholder(R.drawable.default_bg_icon)
                            .into(((ViewHolderMainType2) viewHolder).iv_sub_category_image);
                }
                break;
            case TYPE_SUB_CATEGORY:
                ((ViewHolderMainType) viewHolder).tv_sub_category_item.setText(cursor.getString(cursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_NAME)));
                ((ViewHolderMainType) viewHolder).updateCursor(cursor.getPosition());
                url = (cursor.getString(cursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_IMAGE_URL)));
                if (url.length() > 0) {
                    Picasso.with(mContext)
                            .load(cursor.getString(cursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_IMAGE_URL)))
                            .placeholder(R.drawable.default_bg_icon)
                            .into(((ViewHolderMainType) viewHolder).iv_sub_category_image);
                } else {
                    Picasso.with(mContext)
                            .load(R.drawable.ic_menu_gallery)
                            .placeholder(R.drawable.default_bg_icon)
                            .into(((ViewHolderMainType) viewHolder).iv_sub_category_image);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUB_CATEGORY) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_academics_sub_category, parent, false);
            return new ViewHolderMainType(itemView,colorCode);
        } else if (viewType == TYPE_SUBHEADING) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_academics_heading, parent, false);
            return new ViewHolderHeaderType(itemView);
        } else if (viewType == TYPE_SUB_CATEGORY_FIRST) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_academics_sub_category, parent, false);
            return new ViewHolderMainType2(itemView,colorCode);
        }
        else return null;
    }


    public class ViewHolderMainType extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_sub_category_item;
        public ImageView iv_sub_category_image;
        public RelativeLayout relative_layout;
        public int position;
        public void updateCursor(int position){
            this.position=position;
        }


        ViewHolderMainType(View v, String colorCode) {
            super(v);

            collegeId       = MySharedPref.getUserCollegeId(mContext,Constant.USER_COLLAGE_ID);
            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(mContext,Constant.USER_LOGIN_TYPE));
            userId          = MySharedPref.getUserId(mContext,Constant.USER_ID);

            this.relative_layout            = (RelativeLayout) v.findViewById(R.id.rl_sub_category);
            this.tv_sub_category_item       = (TextView) v.findViewById(R.id.tv_sub_category_item);
            this.iv_sub_category_image      = (ImageView) v.findViewById(R.id.iv_sub_category_image);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)relative_layout.getLayoutParams();
            this.iv_sub_category_image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_shape));
            GradientDrawable bgShape = (GradientDrawable)this.iv_sub_category_image.getBackground();
            bgShape.setStroke((int) v.getContext().getResources().getDimension(R.dimen.dimen_1dp),Color.parseColor(colorCode));
            params.setMargins(0, 0, 0, 120);
            relative_layout.setLayoutParams(params);
            this.relative_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if((v.getId() == relative_layout.getId()))
            {
                mCursor.moveToPosition(position);
                HelperFunction.helperFunctionOnClickSubCategory(mContext,mCursor,subCatContactNumber,
                        mCursor.getInt(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_Id)),
                        "http://www.sharda.ac.in/assets/uploads/files/SBS%20Brochure.pdf",
                        mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_NAME)),
                        collegeId,userLoginType,userId,colorCode);
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

    public class ViewHolderMainType2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_sub_category_item;
        public ImageView iv_sub_category_image;
        public RelativeLayout relative_layout;

        Drawable mDrawable;

        public int position;
        public void updateCursor(int position){
            this.position=position;
        }

        ViewHolderMainType2(View v, String colorCode) {
            super(v);
            this.relative_layout = (RelativeLayout) v.findViewById(R.id.rl_sub_category);
            this.tv_sub_category_item       = (TextView) v.findViewById(R.id.tv_sub_category_item);
            this.iv_sub_category_image      = (ImageView) v.findViewById(R.id.iv_sub_category_image);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)relative_layout.getLayoutParams();
            this.iv_sub_category_image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_shape));
            GradientDrawable bgShape = (GradientDrawable)this.iv_sub_category_image.getBackground();
            bgShape.setStroke((int) v.getContext().getResources().getDimension(R.dimen.dimen_1dp),Color.parseColor(colorCode));
            this.relative_layout.setOnClickListener(this);
            params.setMargins(0, 55, 0, 70);
            relative_layout.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            if((v.getId() == relative_layout.getId()))
            {
                mCursor.moveToPosition(position);
                HelperFunction.helperFunctionOnClickSubCategory(mContext,mCursor,subCatContactNumber,
                        mCursor.getInt(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_Id)),
                        "http://www.sharda.ac.in/assets/uploads/files/SBS%20Brochure.pdf",
                        mCursor.getString(mCursor.getColumnIndex(SubCategoryTable.SUB_CATEGORY_NAME)),
                        collegeId,userLoginType,userId,colorCode);
            }
        }
    }

    public static class ViewHolderHeaderType extends RecyclerView.ViewHolder {
        TextView tv_sub_heading;
        ViewHolderHeaderType(View v) {
            super(v);
            this.tv_sub_heading = (TextView) v.findViewById(R.id.tv_sub_heading);
        }
    }

    public void downloadData(String url, String title, int subCategoryId){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("downloading data...");
        request.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subCategoryId+".pdf");
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

}

