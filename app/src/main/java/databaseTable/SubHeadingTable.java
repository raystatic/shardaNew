package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class SubHeadingTable {
    public static final String TABLE_SUB_HEADING                    = "sub_heading_list";
    public static final String COLUMN_ID                            = "_id";
    public static final String SUB_HEADING_Id                       = "sub_heading_id";
    public static final String SUB_HEADING_NAME                     = "sub_heading_name";
    public static final String SUB_HEADING_CATEGORY_ID              = "sub_heading_category_id";
    public static final String SUB_HEADING_DISPLAY_ORDER            = "sub_heading_display_order";


    public static final String[] arrayOfCols = {COLUMN_ID,SUB_HEADING_Id,SUB_HEADING_NAME,SUB_HEADING_CATEGORY_ID,SUB_HEADING_DISPLAY_ORDER};

    public static final String[] defaultValues = {"0","0","0","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SUB_HEADING
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + SUB_HEADING_Id + " integer UNIQUE, "
            + SUB_HEADING_NAME + " text, "
            + SUB_HEADING_CATEGORY_ID + " integer, "
            + SUB_HEADING_DISPLAY_ORDER + " integer "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_HEADING);
        onCreate(database);
    }


}
