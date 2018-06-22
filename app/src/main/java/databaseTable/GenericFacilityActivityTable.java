package databaseTable;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

public class GenericFacilityActivityTable {
    public static final String TABLE_FACILITY               = "facility_list";
    public static final String COLUMN_ID                    = "_id";
    public static final String FACILITY_SUB_CATEGORY_ID     = "facility_sub_category_id";
    public static final String FACILITY_JSON_ARRAY          = "facility_json_array";

    public static final String[] arrayOfCols = {COLUMN_ID,FACILITY_SUB_CATEGORY_ID,FACILITY_JSON_ARRAY};

    public static final String[] defaultValues = {"0","0","0","","","",""};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FACILITY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + FACILITY_SUB_CATEGORY_ID + " integer UNIQUE, "
            + FACILITY_JSON_ARRAY + " text "
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FACILITY);
        onCreate(database);
    }


}
