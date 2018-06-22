package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class CampusDirectoryMainTable {
    public static final String TABLE_CAMPUS_DIRECTORY_CATEGORY                   = "campus_directory_category_list";
    public static final String COLUMN_ID                                         = "_id";
    public static final String CAMPUS_DIRECTORY_CATEGORY_Id                      = "campus_directory_category_id";
    public static final String CAMPUS_DIRECTORY_CATEGORY_NAME                    = "campus_directory_category_name";
    public static final String CAMPUS_DIRECTORY_CATEGORY_HEADING_ID              = "campus_directory_category_heading_id";
    public static final String CAMPUS_DIRECTORY_CATEGORY_DESIGNATION             = "campus_directory_category_designation";
    public static final String CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER          = "campus_directory_category_contact_number";
    public static final String CAMPUS_DIRECTORY_CATEGORY_EXT                     = "campus_directory_category_ext";
    public static final String CAMPUS_DIRECTORY_CATEGORY_EMAIL                   = "campus_directory_category_email";

    public static final String[] arrayOfCols = {COLUMN_ID,CAMPUS_DIRECTORY_CATEGORY_Id,CAMPUS_DIRECTORY_CATEGORY_NAME,CAMPUS_DIRECTORY_CATEGORY_HEADING_ID,
            CAMPUS_DIRECTORY_CATEGORY_DESIGNATION,CAMPUS_DIRECTORY_CATEGORY_EXT,CAMPUS_DIRECTORY_CATEGORY_EMAIL};

    public static final String[] defaultValues = {"0","0","0","","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CAMPUS_DIRECTORY_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + CAMPUS_DIRECTORY_CATEGORY_Id + " integer UNIQUE, "
            + CAMPUS_DIRECTORY_CATEGORY_NAME + " text, "
            + CAMPUS_DIRECTORY_CATEGORY_HEADING_ID + " integer, "
            + CAMPUS_DIRECTORY_CATEGORY_DESIGNATION + " text, "
            + CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER + " text, "
            + CAMPUS_DIRECTORY_CATEGORY_EXT + " text, "
            + CAMPUS_DIRECTORY_CATEGORY_EMAIL + " text "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPUS_DIRECTORY_CATEGORY);
        onCreate(database);
    }


}
