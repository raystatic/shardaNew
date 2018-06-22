package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shardatech.shardauniversity.FeedbackUs;
import com.shardatech.shardauniversity.LoginActivity;
import com.shardatech.shardauniversity.MyProfileActivity;
import com.shardatech.shardauniversity.R;
import com.shardatech.shardauniversity.SubCategoryActivity;
import com.shardatech.shardauniversity.WebViewDrawer;
import com.squareup.picasso.Picasso;

import database.SQLiteHelper;
import databaseTable.DrawableTable;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.SubCategoryTable;
import method.CircleTransform;
import method.Constant;
import method.HelperFunction;
import method.MySharedPref;

public class CustomDrawerAdapter extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    public Boolean isHeader = true;
    public Boolean isFooterVisible;


    private String userName;
    Context mContext;
    Cursor mCursor;
    private String userProfilePic;
    int visibility;
    String login_logout;
    DrawerLayout drawerLayout;
    private String subCatContactNumber;
    private int collegeId;
    private int userLoginType;
    private int userId;
    private int footerPos =-1;

    public CustomDrawerAdapter(Context context, Cursor cursor,DrawerLayout drawerLayout) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
        this.drawerLayout = drawerLayout;
        isFooterVisible=false;
    }

    @Override
    public void changeCursor(Cursor cursor){
        super.changeCursor(cursor);
        mCursor = super.getCursor();
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getCursor() == null || super.getCursor().getCount()==2){
            return TYPE_ITEM;
        }
        else if (position==0){
            return TYPE_HEADER;
        }
        else{
            super.getCursor().moveToPosition(position);
            if(super.getCursor().getString(super.getCursor().getColumnIndex("view_type")).equals("footer")){
                if (footerPos==-1)
                    footerPos=position;
                return TYPE_FOOTER;
            }
            else
                return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        if(!MySharedPref.getLoginCheck(mContext, Constant.LOG_IN_CHECK)){
            userName = Constant.USER_NAME_FOR_GUEST_USER;
            userProfilePic = (String.valueOf(R.mipmap.ic_home));
            visibility = View.VISIBLE;
            login_logout = "LogIn";
        }else{
            userProfilePic = MySharedPref.getUserDisplayPic(mContext,Constant.USER_PROFILE_PIC);
            userName = "Welcome, "+MySharedPref.getUserName(mContext,Constant.USER_NAME);
            visibility = View.INVISIBLE;
            login_logout = "LogOut";
        }

        if (userProfilePic.length()==0){
            userProfilePic="XX";
        }

        switch (getItemViewType(cursor.getPosition()))
        {
            case TYPE_HEADER:
                ((ViewHolderHeaderType) viewHolder).tvUserGuestName.setText(userName);
                ((ViewHolderHeaderType) viewHolder).tvSideNavLogin.setVisibility(visibility);
                ((ViewHolderHeaderType) viewHolder).tvSideNavLogin.setText(login_logout);
                ((ViewHolderHeaderType) viewHolder).tvNavAboutCollage.setText(mContext.getResources().getString(
                        R.string.about)+"  "+MySharedPref.getUserCollegeName(mContext,Constant.USER_COLLAGE_NAME));
                if(login_logout.equals("LogIn")){
                    ((ViewHolderHeaderType) viewHolder).tvSideNavLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                            ((Activity)mContext).finish();
                            ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });
                }
                else if(login_logout.equals("LogOut"))
                {
                    ((ViewHolderHeaderType) viewHolder).tvSideNavLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                            builder.setCancelable(false);
                            builder.setMessage("Are you sure you want to logout?");
                            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    mContext.deleteDatabase(SQLiteHelper.DATABASE_NAME);
                                    new SQLiteHelper(mContext).deleteDb();
                                    MySharedPref.setClearedSharedPref(mContext);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.putExtra("isLogout",true);
                                    mContext.startActivity(intent);
                                    ((Activity)mContext).finish();
                                    ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();


                        }
                    });
                }


                ((ViewHolderHeaderType) viewHolder).rlNavHeaderHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                  //      Toast.makeText(mContext,"Clicked",Toast.LENGTH_SHORT).show();
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });

                ((ViewHolderHeaderType) viewHolder).rlNavUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //      Toast.makeText(mContext,"Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MyProfileActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        /*if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }*/
                    }
                });

                Picasso.with(mContext)
                            .load(userProfilePic)
                            .placeholder(R.drawable.user)
                            .transform(new CircleTransform())
                            .skipMemoryCache()
                            .into(((ViewHolderHeaderType) viewHolder).ivUserImage);
                break;

            case TYPE_ITEM:
                ((ViewHolderMainType) viewHolder).updateCursor(cursor.getPosition());
                if(((cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("sub_cat"))
                        || ((cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("category"))
                        || ((cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("imp_links")))
                {
                    ((ViewHolderMainType) viewHolder).drawerItemName.setText(cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_NAME)));
                    String url = (cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_IMAGE_URL)));
                    if (url.length() > 0) {
                        Picasso.with(mContext)
                                .load(url)
                                .placeholder(R.drawable.default_bg_icon)
                                .into(((ViewHolderMainType) viewHolder).drawerIcon);
                    } else {
                        Picasso.with(mContext)
                                .load(R.drawable.ic_menu_gallery)
                                .placeholder(R.drawable.default_bg_icon)
                                .into(((ViewHolderMainType) viewHolder).drawerIcon);
                    }
                } else {
                    /*String cursorStr = DatabaseUtils.dumpCursorToString(cursor);
                    int i =5;*/
                }
                break;
            case TYPE_FOOTER:
                ((ViewHolderFooterType) viewHolder).updateCursor(cursor.getPosition());
                if(((cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("footer")))
                {
                    if(cursor.getPosition()==footerPos){
                        ((ViewHolderFooterType) viewHolder).tvFooterMore.setText("More");
                        ((ViewHolderFooterType) viewHolder).tvFooterMore.setVisibility(View.VISIBLE);
                    }
                    else{
                        ((ViewHolderFooterType) viewHolder).tvFooterMore.setVisibility(View.GONE);
                    }

                    ((ViewHolderFooterType) viewHolder).tvFooterContact.setText(cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_NAME)));
                    String url = (cursor.getString(cursor.getColumnIndex(DrawableTable.DRAWABLE_IMAGE_URL)));
                    if (url.length() > 0) {
                        Picasso.with(mContext)
                                .load(url)
                                .placeholder(R.drawable.default_bg_icon)
                                .into(((ViewHolderFooterType) viewHolder).ivFooterContact);
                    } else {
                        Picasso.with(mContext)
                                .load(R.drawable.ic_menu_gallery)
                                .placeholder(R.drawable.default_bg_icon)
                                .into(((ViewHolderFooterType) viewHolder).ivFooterContact);
                    }
                    if(cursor.getPosition()==footerPos+3){
                        ((ViewHolderFooterType) viewHolder).rlNavFooterLogOut.setVisibility(View.VISIBLE);
                    }
                    else{
                        ((ViewHolderFooterType) viewHolder).rlNavFooterLogOut.setVisibility(View.GONE);
                    }


                    ((ViewHolderFooterType) viewHolder).rlNavFooterLogOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                            builder.setCancelable(false);
                            builder.setMessage("Are you sure you want to logout?");
                            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    mContext.deleteDatabase(SQLiteHelper.DATABASE_NAME);
                                    new SQLiteHelper(mContext).deleteDb();
                                    MySharedPref.setClearedSharedPref(mContext);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.putExtra("isLogout",true);
                                    mContext.startActivity(intent);
                                    ((Activity)mContext).finish();
                                    ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();


                        }
                    });


                }
                else{
               //     ((ViewHolderFooterType) viewHolder).rlNavFooterHome.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_main_item, parent, false);
            return new ViewHolderMainType(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_header_item, parent, false);
            return new ViewHolderHeaderType(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_footer_item, parent, false);
            return new ViewHolderFooterType(itemView);
        }
        else return null;
    }

    public class ViewHolderMainType extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView drawerItemName;
        public ImageView drawerIcon;
        public RelativeLayout rvMainView;
        ViewHolderMainType(View v) {
            super(v);

            collegeId       = MySharedPref.getUserCollegeId(mContext,Constant.USER_COLLAGE_ID);
            userLoginType   = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK,MySharedPref.getUserLoginType(mContext,Constant.USER_LOGIN_TYPE));
            userId          = MySharedPref.getUserId(mContext,Constant.USER_ID);

            this.drawerItemName = (TextView) v.findViewById(R.id.drawer_itemName);
            this.drawerIcon     = (ImageView) v.findViewById(R.id.drawer_icon);
            this.rvMainView     = (RelativeLayout) v.findViewById(R.id.rl_nav_main_contact_us);
            rvMainView.setOnClickListener(this);
        }

        public int position;
        public void updateCursor(int position){
            this.position=position;
        }

        @Override
        public void onClick(View v) {
            if((v.getId() == rvMainView.getId())) {
                if (mCursor==null) return;
                mCursor.moveToPosition(position);
                if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("sub_cat"))){
                    int subCategoryId= mCursor.getInt(mCursor.getColumnIndex(DrawableTable.DRAWABLE_Id));
                    SQLiteDatabase sqLiteDatabase = new SQLiteHelper(mContext).getReadableDatabase();
                    Cursor dbCursor = sqLiteDatabase.rawQuery(
                            "SELECT * FROM "+
                            SubCategoryTable.TABLE_SUB_CATEGORY+
                            " where "+SubCategoryTable.SUB_CATEGORY_Id+
                            " =? ",
                            new String[]{String.valueOf(subCategoryId)});

                    if (dbCursor!=null && dbCursor.getCount()>0){
                        dbCursor.moveToFirst();
                        HelperFunction.helperFunctionOnClickSubCategory(mContext,dbCursor,subCatContactNumber,
                                subCategoryId,
                                "http://www.sharda.ac.in/assets/uploads/files/SBS%20Brochure.pdf",
                                mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_NAME)),
                                collegeId,userLoginType,userId,
                                ((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_COLOR_CODE))))
                                );
                    }
                    else{
                        Toast.makeText(mContext,"An error occured", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("category")))
                {
                    if (v.getId() == rvMainView.getId()){
                        Intent intent = new Intent(mContext, SubCategoryActivity.class);
                        intent.putExtra(HomeActivityCategoryTable.CATEGORY_Id,((mCursor.getInt(mCursor.getColumnIndex(DrawableTable.DRAWABLE_Id)))));
                        intent.putExtra(HomeActivityCategoryTable.CATEGORY_NAME,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_NAME)))));
                        intent.putExtra(HomeActivityCategoryTable.CATEGORY_COLOR,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_COLOR_CODE)))));
                        intent.putExtra(HomeActivityCategoryTable.CATEGORY_CODE,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_CODE)))));
                        mContext.startActivity(intent);
                    }
                   // Toast.makeText(mContext,"Clicked on category",Toast.LENGTH_SHORT).show();
                }
                else if (((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_VIEW_TYPE))).equals("imp_links")))
                {
                    if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_CODE))).equals("web_view"))){
                        loadWebView();
                    }
                    else if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_CODE))).equals("pdf"))){
                        new HelperFunction.DownloadFile(mContext).execute((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_PATH_URL)))
                                ,(mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_NAME))));
                    }
                }
            }
        }
    }

    public static class ViewHolderHeaderType extends RecyclerView.ViewHolder {
        TextView tvUserGuestName, tvSideNavLogin,tvNavHeaderHome,tvNavAboutCollage;
        ImageView ivUserImage;
        RelativeLayout rlNavHeaderHome,rlNavUserName;
        ViewHolderHeaderType(View v) {
            super(v);
            this.tvUserGuestName    = (TextView) v.findViewById(R.id.tv_user_guest_name);
            this.tvSideNavLogin     = (TextView) v.findViewById(R.id.tv_side_nav_login);
            this.ivUserImage        = (ImageView) v.findViewById(R.id.iv_user_image);
            this.tvNavHeaderHome    = (TextView) v.findViewById(R.id.tv_nav_header_home);
            this.tvNavAboutCollage  = (TextView) v.findViewById(R.id.tv_about_header_clg_name);
            this.rlNavHeaderHome    = (RelativeLayout) v.findViewById(R.id.rl_nav_header_home);
            this.rlNavUserName      = (RelativeLayout) v.findViewById(R.id.rl_nav_user_name);
        }
    }

    public class ViewHolderFooterType extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvFooterMore,tvFooterContact,tvFooterPrivacyPolicy,tvFooterTerms;
        ImageView ivFooterContact,ivFooterPrivacyPolicy,ivFooterTerms;
        RelativeLayout rlNavFooterHome,rlNavFooterContactUs,rlNavFooterLogOut;

        public int position;
        public void updateCursor(int position){
            this.position=position;
        }

        ViewHolderFooterType(View v) {
            super(v);
            this.tvFooterMore           = (TextView) v.findViewById(R.id.tv_footer_more);
            this.rlNavFooterLogOut        = (RelativeLayout) v.findViewById(R.id.rl_nav_footer_log_out);
            this.tvFooterContact        = (TextView) v.findViewById(R.id.tv_footer_contact);
            this.rlNavFooterHome        = (RelativeLayout) v.findViewById(R.id.rl_nav_footer_home);
            this.rlNavFooterContactUs   = (RelativeLayout) v.findViewById(R.id.rl_nav_footer_contact_us);
            this.ivFooterContact        = (ImageView) v.findViewById(R.id.iv_footer_contact);
            rlNavFooterContactUs.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if((v.getId() == rlNavFooterContactUs.getId())) {
                if (mCursor==null) return;
                mCursor.moveToPosition(position);
                if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_CODE))).equals("web_view"))){
                    loadWebView();
                }
                else if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_CODE))).equals("pdf"))){
                    new HelperFunction.DownloadFile(mContext).execute((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_PATH_URL)))
                            ,(mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_NAME))));
                }
                else if(((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_CODE))).equals("feedback"))){
                    feedbackUs();
                }
            }
        }
    }

    public void loadWebView(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, WebViewDrawer.class);
        bundle.putString(DrawableTable.DRAWABLE_NAME,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_NAME)))));
        bundle.putString(DrawableTable.DRAWABLE_COLOR_CODE,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_COLOR_CODE)))));
        bundle.putString(DrawableTable.DRAWABLE_PATH_URL,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_PATH_URL)))));
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void feedbackUs(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mContext, FeedbackUs.class);
        bundle.putString(DrawableTable.DRAWABLE_NAME,(("General")));
        bundle.putInt(SubCategoryTable.SUB_CATEGORY_Id,0);
        bundle.putString(DrawableTable.DRAWABLE_COLOR_CODE,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_COLOR_CODE)))));
        bundle.putString(DrawableTable.DRAWABLE_PATH_URL,((mCursor.getString(mCursor.getColumnIndex(DrawableTable.DRAWABLE_PATH_URL)))));
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

