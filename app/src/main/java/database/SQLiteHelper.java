package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shardatech.shardauniversity.GenericFacilityActivity;

import java.util.Map;

import databaseTable.CampusDirectoryHeadingTable;
import databaseTable.CampusDirectoryMainTable;
import databaseTable.DrawableTable;
import databaseTable.GenericFacilityActivityTable;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.HomeActivityViewPagerImageTable;
import databaseTable.MapTable;
import databaseTable.NotificationTable;
import databaseTable.SubCategoryTable;
import databaseTable.SubHeadingTable;


public class SQLiteHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME   = "shardaunv.db";
    private static final int DATABASE_VERSION   = 2;

    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    public  void deleteDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+HomeActivityCategoryTable.TABLE_CATEGORY);
        db.execSQL("DELETE FROM "+HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE);
        db.execSQL("DELETE FROM "+SubCategoryTable.TABLE_SUB_CATEGORY);
        db.execSQL("DELETE FROM "+SubHeadingTable.TABLE_SUB_HEADING);
        db.execSQL("DELETE FROM "+ MapTable.TABLE_MAP_CATEGORY);
        db.execSQL("DELETE FROM "+ GenericFacilityActivityTable.TABLE_FACILITY);
        db.execSQL("DELETE FROM "+DrawableTable.TABLE_DRAWABLE);
        db.execSQL("DELETE FROM "+CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING);
        db.execSQL("DELETE FROM "+CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY);
        db.execSQL("DELETE FROM "+NotificationTable.TABLE_NOTIFICATION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        HomeActivityViewPagerImageTable.onCreate(db);
        HomeActivityCategoryTable.onCreate(db);
        SubCategoryTable.onCreate(db);
        SubHeadingTable.onCreate(db);
        MapTable.onCreate(db);
        GenericFacilityActivityTable.onCreate(db);
        DrawableTable.onCreate(db);
        CampusDirectoryHeadingTable.onCreate(db);
        CampusDirectoryMainTable.onCreate(db);
        NotificationTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        HomeActivityViewPagerImageTable.onUpgrade(db, oldVersion, newVersion);
        HomeActivityCategoryTable.onUpgrade(db, oldVersion, newVersion);
        SubCategoryTable.onUpgrade(db,oldVersion,newVersion);
        SubHeadingTable.onUpgrade(db,oldVersion,newVersion);
        MapTable.onUpgrade(db,oldVersion,newVersion);
        GenericFacilityActivityTable.onUpgrade(db,oldVersion,newVersion);
        DrawableTable.onUpgrade(db,oldVersion,newVersion);
        CampusDirectoryHeadingTable.onUpgrade(db,oldVersion,newVersion);
        CampusDirectoryMainTable.onUpgrade(db,oldVersion,newVersion);
        NotificationTable.onUpgrade(db,oldVersion,newVersion);
    }
}
