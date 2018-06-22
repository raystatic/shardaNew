package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class DrawableTable {
    public static final String TABLE_DRAWABLE                       = "drawable_category";
    public static final String COLUMN_ID                            = "_id";
    public static final String DRAWABLE_Id                          = "id_value";
    public static final String DRAWABLE_NAME                        = "drawer_name";
    public static final String DRAWABLE_CODE                        = "drawer_code";
    public static final String DRAWABLE_COLOR_CODE                  = "drawer_color_code";
    public static final String DRAWABLE_PATH_URL                    = "drawer_path_url";
    public static final String DRAWABLE_VIEW_TYPE                   = "view_type";
    public static final String DRAWABLE_IMAGE_URL                   = "drawer_image_url";

    public static final String[] arrayOfCols = {COLUMN_ID,DRAWABLE_Id,DRAWABLE_NAME, DRAWABLE_CODE,DRAWABLE_VIEW_TYPE,DRAWABLE_IMAGE_URL,DRAWABLE_COLOR_CODE,DRAWABLE_PATH_URL};
    public static final String[] defaultValues = {"0","0","","","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_DRAWABLE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + DRAWABLE_Id + " integer, "
            + DRAWABLE_NAME + " text, "
            + DRAWABLE_CODE + " text, "
            + DRAWABLE_COLOR_CODE + " text, "
            + DRAWABLE_VIEW_TYPE + " text, "
            + DRAWABLE_IMAGE_URL + " text, "
            + DRAWABLE_PATH_URL + " text "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAWABLE);
        onCreate(database);
    }


}
