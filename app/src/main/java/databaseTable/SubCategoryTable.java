package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class SubCategoryTable {
    public static final String TABLE_SUB_CATEGORY                   = "sub_category_list";
    public static final String COLUMN_ID                            = "_id";
    public static final String SUB_CATEGORY_Id                      = "sub_category_id";
    public static final String SUB_CATEGORY_NAME                    = "sub_category_name";
    public static final String SUB_CATEGORY_IMAGE_URL               = "sub_category_image_url";
    public static final String SUB_CATEGORY_SUB_HEADING_ID          = "sub_category_sub_heading_id";
    public static final String SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE    = "sub_category_can_be_taken_offline";
    public static final String SUB_CATEGORY_CAT_CODE                = "sub_category_cat_code";
    public static final String SUB_CATEGORY_USER_TYPE               = "sub_category_user_type";
    public static final String SUB_CATEGORY_CONTACT_NUMBER          = "sub_category_contact_number";


    public static final String[] arrayOfCols = {COLUMN_ID,SUB_CATEGORY_Id,SUB_CATEGORY_NAME,SUB_CATEGORY_IMAGE_URL,
            SUB_CATEGORY_SUB_HEADING_ID,SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE,SUB_CATEGORY_CAT_CODE,SUB_CATEGORY_USER_TYPE,SUB_CATEGORY_CONTACT_NUMBER};

    public static final String[] defaultValues = {"0","0","0","","","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SUB_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + SUB_CATEGORY_Id + " integer UNIQUE, "
            + SUB_CATEGORY_NAME + " text, "
            + SUB_CATEGORY_SUB_HEADING_ID + " integer, "
            + SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE + " text, "
            + SUB_CATEGORY_CAT_CODE + " text, "
            + SUB_CATEGORY_USER_TYPE + " text, "
            + SUB_CATEGORY_IMAGE_URL + " text, "
            + SUB_CATEGORY_CONTACT_NUMBER + " text "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORY);
        onCreate(database);
    }


}
