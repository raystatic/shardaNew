package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class CampusDirectoryHeadingTable {
    public static final String TABLE_CAMPUS_DIRECTORY_HEADING                    = "campus_directory_heading_list";
    public static final String COLUMN_ID                                         = "_id";
    public static final String CAMPUS_DIRECTORY_HEADING_Id                       = "campus_directory_heading_id";
    public static final String CAMPUS_DIRECTORY_HEADING_NAME                     = "campus_directory_heading_name";
    public static final String CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID          = "campus_directory_heading_sub_category_id";

    public static final String[] arrayOfCols = {COLUMN_ID,CAMPUS_DIRECTORY_HEADING_Id,
            CAMPUS_DIRECTORY_HEADING_NAME,CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID};

    public static final String[] defaultValues = {"0","0","0",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CAMPUS_DIRECTORY_HEADING
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + CAMPUS_DIRECTORY_HEADING_Id + " integer UNIQUE, "
            + CAMPUS_DIRECTORY_HEADING_NAME + " text, "
            + CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID + " integer "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPUS_DIRECTORY_HEADING);
        onCreate(database);
    }


}
