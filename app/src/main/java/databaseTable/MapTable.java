package databaseTable;

import android.database.sqlite.SQLiteDatabase;

public class MapTable {
    public static final String TABLE_MAP_CATEGORY           = "map_list";
    public static final String COLUMN_ID                    = "_id";
    public static final String MAP_PLACE_ID                 = "place_id";
    public static final String MAP_PLACE_CATEGORY           = "place_category";
    public static final String MAP_PLACE_NAME               = "place_name";
    public static final String MAP_PLACE_ADD                = "place_address";
    public static final String MAP_PLACE_DETAILS            = "place_details";
    public static final String MAP_PLACE_PHONE              = "place_phone";
    public static final String MAP_PLACE_LATTITUDE          = "place_latitude";
    public static final String MAP_PLACE_LONGITUDE          = "place_longitude";
    public static final String MAP_PLACE_CATEGORY_NAME      = "place_category_name";
    public static final String MAP_PLACE_IMAGE_URL          = "place_image_url";
    public static final String MAP_PLACE_ISIMPORTANT        = "isImportant";

    public static final String[] arrayOfCols = {COLUMN_ID,MAP_PLACE_ID,
            MAP_PLACE_CATEGORY,MAP_PLACE_NAME,MAP_PLACE_ADD,MAP_PLACE_DETAILS,MAP_PLACE_PHONE,MAP_PLACE_LATTITUDE,
            MAP_PLACE_LONGITUDE,MAP_PLACE_CATEGORY_NAME,MAP_PLACE_IMAGE_URL,MAP_PLACE_ISIMPORTANT};

    public static final String[] defaultValues = {"0","0","0","","","","","","","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MAP_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + MAP_PLACE_ID + " integer, "
            + MAP_PLACE_CATEGORY + " integer, "
            + MAP_PLACE_NAME + " text, "
            + MAP_PLACE_ADD + " text, "
            + MAP_PLACE_DETAILS + " text, "
            + MAP_PLACE_PHONE + " text, "
            + MAP_PLACE_LATTITUDE + " text, "
            + MAP_PLACE_LONGITUDE + " text, "
            + MAP_PLACE_CATEGORY_NAME + " text, "
            + MAP_PLACE_IMAGE_URL + " text, "
            + MAP_PLACE_ISIMPORTANT + " integer "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MAP_CATEGORY);
        onCreate(database);
    }


}
