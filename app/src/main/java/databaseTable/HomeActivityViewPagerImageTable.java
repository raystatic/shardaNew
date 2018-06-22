package databaseTable;

import android.database.sqlite.SQLiteDatabase;


public class HomeActivityViewPagerImageTable
{
    // Database table
    public static final String TABLE_HOME_PAGER_IMAGE       = "home_pager_image_list";
    public static final String COLUMN_ID                    = "_id";
    public static final String HOME_PAGER_IMAGE_ID          = "home_pager_image_id";
    public static final String HOME_PAGER_IMAGE_TITLE       = "home_pager_image_title";
    public static final String HOME_PAGER_IMAGE_TIME        = "home_pager_image_time";
    public static final String HOME_PAGER_IMAGE_ADDRESS     = "home_pager_image_address";
    public static final String HOME_PAGER_IMAGE_URL         = "home_pager_image_url";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_HOME_PAGER_IMAGE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + HOME_PAGER_IMAGE_ID + " integer UNIQUE, "
            + HOME_PAGER_IMAGE_TITLE + " text, "
            + HOME_PAGER_IMAGE_TIME + " text, "

            + HOME_PAGER_IMAGE_ADDRESS + " text, "
            + HOME_PAGER_IMAGE_URL + " text "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HOME_PAGER_IMAGE);
        onCreate(database);
    }
}