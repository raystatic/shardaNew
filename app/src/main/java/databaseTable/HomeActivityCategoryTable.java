package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class HomeActivityCategoryTable {
    public static final String TABLE_CATEGORY               = "category_list";
    public static final String COLUMN_ID                    = "_id";
    public static final String CATEGORY_COLLEGE_EMER_NUMBER = "category_college_emer_number";
    public static final String CATEGORY_Id                  = "category_id";
    public static final String CATEGORY_NAME                = "category_name";
    public static final String CATEGORY_IMAGE_URL           = "category_image_url";
    public static final String CATEGORY_COLOR               = "category_color";
    public static final String CATEGORY_COLOR_BG_DARK       = "category_color_bg_dark";
    public static final String CATEGORY_COLOR_BG_LIGHT      = "category_color_bg_light";
    public static final String CATEGORY_CODE                = "cat_code";

    public static final String[] arrayOfCols = {COLUMN_ID,CATEGORY_COLLEGE_EMER_NUMBER,CATEGORY_Id,CATEGORY_NAME,CATEGORY_IMAGE_URL,
            CATEGORY_COLOR,CATEGORY_COLOR_BG_DARK,CATEGORY_COLOR_BG_LIGHT,CATEGORY_CODE};

    public static final String[] defaultValues = {"0","0","0","","","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + CATEGORY_COLLEGE_EMER_NUMBER + " text, "
            + CATEGORY_Id + " integer UNIQUE, "
            + CATEGORY_NAME + " text, "
            + CATEGORY_IMAGE_URL + " text, "
            + CATEGORY_COLOR + " text, "
            + CATEGORY_COLOR_BG_DARK + " text, "
            + CATEGORY_COLOR_BG_LIGHT + " text, "
            + CATEGORY_CODE + " text "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(database);
    }


}
