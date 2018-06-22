package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class NotificationTable {
    public static final String TABLE_NOTIFICATION           = "notification_list";
    public static final String COLUMN_ID                    = "_id";
    public static final String NOTIFICATION_ID              = "notification_id";
    public static final String NOTIFICATION_SUB_SUB_CAT_ID  = "notification_sub_sub_cat_id";
    public static final String NOTIFICATION_SUB_SUB_CAT_NAME= "notification_sub_sub_cat_name";
    public static final String NOTIFICATION_TITLE           = "notification_title";
    public static final String NOTIFICATION_DETAILS         = "notification_details";
    public static final String NOTIFICATION_ADD_DATE        = "notification_add_date";
    public static final String NOTIFICATION_DATE            = "notification_date";
    public static final String NOTIFICATION_IS_IMP          = "notification_is_imp";
    public static final String NOTIFICATION_IMAGE_URL       = "notification_image_url";
    public static final String NOTIFICATION_READ            = "notification_read";
    public static final String NOTIFICATION_URL             = "notification_url";


    public static final String[] arrayOfCols = {COLUMN_ID,NOTIFICATION_ID,NOTIFICATION_SUB_SUB_CAT_ID,
                                                NOTIFICATION_TITLE,NOTIFICATION_DETAILS,NOTIFICATION_ADD_DATE,
                                                NOTIFICATION_DATE,NOTIFICATION_IS_IMP,
                                                NOTIFICATION_IMAGE_URL,NOTIFICATION_SUB_SUB_CAT_NAME,NOTIFICATION_READ,NOTIFICATION_URL};

    public static final String[] defaultValues = {"0","0","0","","","","","","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NOTIFICATION
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + NOTIFICATION_ID + " integer not null unique, "
            + NOTIFICATION_SUB_SUB_CAT_ID + " integer, "
            + NOTIFICATION_SUB_SUB_CAT_NAME + " text, "
            + NOTIFICATION_TITLE + " text, "
            + NOTIFICATION_DETAILS + " text, "
            + NOTIFICATION_ADD_DATE + " integer, "
            + NOTIFICATION_DATE + " integer, "
            + NOTIFICATION_IS_IMP + " integer, "
            + NOTIFICATION_IMAGE_URL + " text, "
            + NOTIFICATION_URL + " text, "
            + NOTIFICATION_READ + " integer default 0 "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(database);
    }


}
