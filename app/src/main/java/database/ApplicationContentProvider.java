package database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

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
import method.Constant;
import method.MySharedPref;


public class ApplicationContentProvider extends ContentProvider {
    private SQLiteHelper database;
    private static final int CATEGORY               = 10;
    private static final int CATEGORY_ID            = 20;

    private static final int SUB_CATEGORY           = 30;
    private static final int SUB_CATEGORY_ID        = 40;

    private static final int SUB_HEADING            = 50;
    private static final int SUB_HEADING_ID         = 60;

    private static final int SUB_HEADING_JOIN_SUB_CATEGORY  = 70;

    private static final int HOME_PAGER_IMAGE       = 11;
    private static final int HOME_PAGER_IMAGE_ID    = 21;

    private static final int MAP_CATEGORY           = 80;
    private static final int MAP_CATEGORY_ID        = 90;

    private static final int MAP_CATEGORY_UNIQUE    = 110;

    private static final int FACILITY               = 120;
    private static final int FACILITY_ID            = 130;

    private static final int DRAWABLE               = 140;
    private static final int DRAWABLE_ID            = 150;

    private static final int CAMPUS_DIRECTORY_HEADING      = 160;
    private static final int CAMPUS_DIRECTORY_HEADING_ID   = 170;

    private static final int CAMPUS_DIRECTORY_MAIN         = 180;
    private static final int CAMPUS_DIRECTORY_MAIN_ID      = 190;

    private static final int CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN  = 200;

    private static final int NOTIFICATION                  = 210;
    private static final int NOTIFICATION_ID               = 220;
    private static final int NOTIFICATION_COUNT            = 230;
    private static final int NOTIFICATION_FOR_FACILITY     = 240;
    private static final int NOTIFICATION_READ_COUNT       = 250;

    private static final String AUTHORITY = "com.shardaunv.database.ApplicationContentProvider";

    private static final String HOME_PAGER_PATH = "home_pager_path";
    private static final String CATEGORY_PATH   = "category";
    private static final String SUB_CATEGORY_PATH = "sub_category";
    private static final String SUB_HEADING_PATH = "sub_heading";
    private static final String MAP_CATEGORY_UNIQUE_PATH = "map_category_unique";
    private static final String SUB_HEADING_JOIN_SUB_CATEGORY_PATH = "sub_heading_sub_category";
    private static final String MAP_CATEGORY_PATH   = "map_category_path";
    private static final String DRAWABLE_PATH   = "drawable_path";
    private static final String FACILITY_PATH   = "facility_path";
    private static final String CAMPUS_DIRECTORY_HEADING_PATH   = "campus_directory_heading_path";
    private static final String CAMPUS_DIRECTORY_MAIN_PATH      = "campus_directory_main_path";
    private static final String NOTIFICATION_PATH               = "notification_path";
    private static final String NOTIFICATION_COUNT_PATH         = "notification_count_path";
    private static final String NOTIFICATION_FOR_FACILITY_PATH  = "notification_uri_path";
    private static final String NOTIFICATION_READ_COUNT_PATH    = "notification_read_count_path";

    private static final String CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN_PATH = "campus_directory_campus_departments";

    public static final Uri CONTENT_URI_PAGER = Uri.parse("content://" + AUTHORITY + "/" + HOME_PAGER_PATH);
    public static final Uri CONTENT_URI_CATEGORY = Uri.parse("content://" + AUTHORITY + "/" + CATEGORY_PATH);
    public static final Uri CONTENT_URI_SUB_CATEGORY = Uri.parse("content://" + AUTHORITY + "/" + SUB_CATEGORY_PATH);
    public static final Uri CONTENT_URI_SUB_HEADING = Uri.parse("content://" + AUTHORITY + "/" + SUB_HEADING_PATH);
    public static final Uri CONTENT_URI_MAP_CATEGORY_UNIQUE = Uri.parse("content://" + AUTHORITY + "/" + MAP_CATEGORY_UNIQUE_PATH);
    public static final Uri CONTENT_URI_SUB_HEADING_JOIN_SUB_CATEGORY = Uri.parse("content://" + AUTHORITY + "/" + SUB_HEADING_JOIN_SUB_CATEGORY_PATH);
    public static final Uri CONTENT_URI_MAP_CATEGORY = Uri.parse("content://" + AUTHORITY + "/" + MAP_CATEGORY_PATH);
    public static final Uri CONTENT_URI_FACILITY = Uri.parse("content://" + AUTHORITY + "/" + FACILITY_PATH);
    public static final Uri CONTENT_URI_DRAWABLE = Uri.parse("content://" + AUTHORITY + "/" + DRAWABLE_PATH);
    public static final Uri CONTENT_URI_CAMPUS_DIRECTORY_HEADING = Uri.parse("content://" + AUTHORITY + "/" + CAMPUS_DIRECTORY_HEADING_PATH);
    public static final Uri CONTENT_URI_CAMPUS_DIRECTORY_MAIN    = Uri.parse("content://" + AUTHORITY + "/" + CAMPUS_DIRECTORY_MAIN_PATH);
    public static final Uri CONTENT_URI_NOTIFICATION    = Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_PATH);
    public static final Uri CONTENT_URI_NOTIFICATION_COUNT  = Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_COUNT_PATH);
    public static final Uri CONTENT_URI_NOTIFICATION_FOR_FACILITY  = Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_FOR_FACILITY_PATH);

    public static final Uri CONTENT_URI_NOTIFICATION_READ_COUNT  = Uri.parse("content://" + AUTHORITY + "/" + NOTIFICATION_READ_COUNT_PATH);

    public static final Uri CONTENT_URI_CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN =
            Uri.parse("content://" + AUTHORITY + "/" + CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN_PATH);


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, HOME_PAGER_PATH, HOME_PAGER_IMAGE);
        sURIMatcher.addURI(AUTHORITY, HOME_PAGER_PATH + "/#", HOME_PAGER_IMAGE_ID);

        sURIMatcher.addURI(AUTHORITY, CATEGORY_PATH, CATEGORY);
        sURIMatcher.addURI(AUTHORITY, CATEGORY_PATH + "/#", CATEGORY_ID);

        sURIMatcher.addURI(AUTHORITY, SUB_CATEGORY_PATH, SUB_CATEGORY);
        sURIMatcher.addURI(AUTHORITY, SUB_CATEGORY_PATH + "/#", SUB_CATEGORY_ID);

        sURIMatcher.addURI(AUTHORITY, SUB_HEADING_PATH, SUB_HEADING);
        sURIMatcher.addURI(AUTHORITY, SUB_HEADING_PATH + "/#", SUB_HEADING_ID);

        sURIMatcher.addURI(AUTHORITY, MAP_CATEGORY_PATH, MAP_CATEGORY);
        sURIMatcher.addURI(AUTHORITY, MAP_CATEGORY_PATH + "/#", MAP_CATEGORY_ID);

        sURIMatcher.addURI(AUTHORITY, FACILITY_PATH, FACILITY);
        sURIMatcher.addURI(AUTHORITY, FACILITY_PATH + "/#", FACILITY_ID);

        sURIMatcher.addURI(AUTHORITY, SUB_HEADING_JOIN_SUB_CATEGORY_PATH, SUB_HEADING_JOIN_SUB_CATEGORY);
        sURIMatcher.addURI(AUTHORITY, MAP_CATEGORY_UNIQUE_PATH, MAP_CATEGORY_UNIQUE);

        sURIMatcher.addURI(AUTHORITY, DRAWABLE_PATH, DRAWABLE);
        sURIMatcher.addURI(AUTHORITY, DRAWABLE_PATH + "/#", DRAWABLE_ID);

        sURIMatcher.addURI(AUTHORITY, CAMPUS_DIRECTORY_HEADING_PATH, CAMPUS_DIRECTORY_HEADING);
        sURIMatcher.addURI(AUTHORITY, CAMPUS_DIRECTORY_HEADING_PATH + "/#", CAMPUS_DIRECTORY_HEADING_ID);

        sURIMatcher.addURI(AUTHORITY, CAMPUS_DIRECTORY_MAIN_PATH, CAMPUS_DIRECTORY_MAIN);
        sURIMatcher.addURI(AUTHORITY, CAMPUS_DIRECTORY_MAIN_PATH + "/#", CAMPUS_DIRECTORY_MAIN_ID);

        sURIMatcher.addURI(AUTHORITY, CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN_PATH,
                CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN);

        sURIMatcher.addURI(AUTHORITY, NOTIFICATION_PATH, NOTIFICATION);
        sURIMatcher.addURI(AUTHORITY, NOTIFICATION_PATH + "/#", NOTIFICATION_ID);

        sURIMatcher.addURI(AUTHORITY, NOTIFICATION_COUNT_PATH, NOTIFICATION_COUNT);
        sURIMatcher.addURI(AUTHORITY, NOTIFICATION_FOR_FACILITY_PATH, NOTIFICATION_FOR_FACILITY);

        sURIMatcher.addURI(AUTHORITY, NOTIFICATION_READ_COUNT_PATH, NOTIFICATION_READ_COUNT);

    }


    @Override
    public boolean onCreate() {
        database = new SQLiteHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = database.getWritableDatabase();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case HOME_PAGER_IMAGE:
                queryBuilder.setTables(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE);
                break;
            case HOME_PAGER_IMAGE_ID:
                queryBuilder.setTables(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE);
                queryBuilder.appendWhere(HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_ID + "=" + uri.getLastPathSegment());
                break;

            case CATEGORY:
                queryBuilder.setTables(HomeActivityCategoryTable.TABLE_CATEGORY);
                break;
            case CATEGORY_ID:
                queryBuilder.setTables(HomeActivityCategoryTable.TABLE_CATEGORY);
                queryBuilder.appendWhere(HomeActivityCategoryTable.TABLE_CATEGORY + "=" + uri.getLastPathSegment());
                break;

            case SUB_CATEGORY:
                queryBuilder.setTables(SubCategoryTable.TABLE_SUB_CATEGORY);
                break;
            case SUB_CATEGORY_ID:
                queryBuilder.setTables(SubCategoryTable.TABLE_SUB_CATEGORY);
                queryBuilder.appendWhere(SubCategoryTable.TABLE_SUB_CATEGORY + "=" + uri.getLastPathSegment());
                break;

            case SUB_HEADING:
                queryBuilder.setTables(SubHeadingTable.TABLE_SUB_HEADING);
                break;
            case SUB_HEADING_ID:
                queryBuilder.setTables(SubHeadingTable.TABLE_SUB_HEADING);
                queryBuilder.appendWhere(SubHeadingTable.TABLE_SUB_HEADING + "=" + uri.getLastPathSegment());
                break;

            case FACILITY:
                queryBuilder.setTables(GenericFacilityActivityTable.TABLE_FACILITY);
                break;
            case FACILITY_ID:
                queryBuilder.setTables(GenericFacilityActivityTable.TABLE_FACILITY);
                queryBuilder.appendWhere(GenericFacilityActivityTable.TABLE_FACILITY + "=" + uri.getLastPathSegment());
                break;

            case DRAWABLE:
                queryBuilder.setTables(DrawableTable.TABLE_DRAWABLE);
                break;
            case DRAWABLE_ID:
                queryBuilder.setTables(DrawableTable.TABLE_DRAWABLE);
                queryBuilder.appendWhere(DrawableTable.TABLE_DRAWABLE + "=" + uri.getLastPathSegment());
                break;

            case CAMPUS_DIRECTORY_MAIN:
                queryBuilder.setTables(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY);
                break;
            case CAMPUS_DIRECTORY_MAIN_ID:
                queryBuilder.setTables(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY);
                queryBuilder.appendWhere(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY
                        + "=" + uri.getLastPathSegment());
                break;
            case NOTIFICATION_FOR_FACILITY:
                queryBuilder.setTables(NotificationTable.TABLE_NOTIFICATION);
                break;
            case NOTIFICATION_COUNT:
                Cursor cursor = db.rawQuery(
                        "SELECT COUNT(*) FROM " +NotificationTable.TABLE_NOTIFICATION,null);
                cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI_NOTIFICATION_COUNT);
                return cursor;

            case NOTIFICATION_READ_COUNT:
                cursor = db.rawQuery(
                        "SELECT COUNT(*) FROM " +NotificationTable.TABLE_NOTIFICATION
                                +" WHERE "+NotificationTable.NOTIFICATION_READ+"=0"
                        ,null);
                cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI_NOTIFICATION_READ_COUNT);
                return cursor;

            case NOTIFICATION:
                cursor = db.rawQuery(
                        "SELECT * FROM (SELECT " +
                                NotificationTable.COLUMN_ID+","+
                                NotificationTable.NOTIFICATION_ID+","+
                                " 0 as isHeader,"+
                                NotificationTable.NOTIFICATION_ADD_DATE+","+
                                NotificationTable.NOTIFICATION_DATE+","+
                                NotificationTable.NOTIFICATION_DETAILS+","+
                                NotificationTable.NOTIFICATION_IMAGE_URL+","+
                                NotificationTable.NOTIFICATION_URL+","+
                                NotificationTable.NOTIFICATION_SUB_SUB_CAT_NAME+","+
                                NotificationTable.NOTIFICATION_TITLE+","+
                                NotificationTable.NOTIFICATION_SUB_SUB_CAT_ID+" FROM "+NotificationTable.TABLE_NOTIFICATION+
                                " UNION ALL " +
                                "SELECT " +
                                "distinct  " +
                                "0 as "+NotificationTable.COLUMN_ID+","+
                                "0 as "+NotificationTable.NOTIFICATION_ID+","+
                                "1 as isHeader,"+
                                "ROUND("+NotificationTable.NOTIFICATION_ADD_DATE+"/86400-0.5,0)*86400+86399 as "+NotificationTable.NOTIFICATION_ADD_DATE+","+
                                "'' as "+NotificationTable.NOTIFICATION_DETAILS+","+
                                "'' as "+NotificationTable.NOTIFICATION_DATE+","+
                                "'' as "+NotificationTable.NOTIFICATION_IMAGE_URL+","+
                                "'' as "+NotificationTable.NOTIFICATION_URL+","+
                                "'' as "+NotificationTable.NOTIFICATION_SUB_SUB_CAT_NAME+","+
                                "'' as "+NotificationTable.NOTIFICATION_TITLE+","+
                                "0 as "+NotificationTable.NOTIFICATION_SUB_SUB_CAT_ID+
                                " FROM "+NotificationTable.TABLE_NOTIFICATION+
                                ") ORDER BY "+NotificationTable.NOTIFICATION_ADD_DATE+" DESC"
                        ,null);
                cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI_SUB_HEADING);
                return cursor;
            case NOTIFICATION_ID:
                queryBuilder.setTables(NotificationTable.TABLE_NOTIFICATION);
                queryBuilder.appendWhere(NotificationTable.TABLE_NOTIFICATION
                        + "=" + uri.getLastPathSegment());
                break;

            case CAMPUS_DIRECTORY_HEADING:
                queryBuilder.setTables(CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING);
                break;
            case CAMPUS_DIRECTORY_HEADING_ID:
                queryBuilder.setTables(CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING);
                queryBuilder.appendWhere(CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING +
                        "=" + uri.getLastPathSegment());
                break;

            case MAP_CATEGORY:
                queryBuilder.setTables(MapTable.TABLE_MAP_CATEGORY);
                break;
            case MAP_CATEGORY_ID:
                queryBuilder.setTables(MapTable.TABLE_MAP_CATEGORY);
                queryBuilder.appendWhere(MapTable.TABLE_MAP_CATEGORY + "=" + uri.getLastPathSegment());
                break;

            case MAP_CATEGORY_UNIQUE:
                cursor = db.rawQuery(
                        "SELECT distinct "+MapTable.MAP_PLACE_CATEGORY+","+MapTable.MAP_PLACE_CATEGORY+" as _id,"
                                +MapTable.MAP_PLACE_CATEGORY_NAME+" FROM "
                                +MapTable.TABLE_MAP_CATEGORY+" WHERE "+MapTable.MAP_PLACE_CATEGORY+">1"
                                +" ORDER BY "+MapTable.MAP_PLACE_CATEGORY_NAME+" ASC"
                        ,null);
                cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI_SUB_HEADING);
                return cursor;

            case SUB_HEADING_JOIN_SUB_CATEGORY:
                cursor = db.rawQuery(
                        "SELECT * FROM (SELECT " +
                                SubHeadingTable.TABLE_SUB_HEADING+"."+SubHeadingTable.COLUMN_ID+","+
                                "0 as "+SubCategoryTable.SUB_CATEGORY_Id+","+
                                "'' as "+SubCategoryTable.SUB_CATEGORY_IMAGE_URL+","+
                                "'' as "+SubCategoryTable.SUB_CATEGORY_NAME+","+
                                "3 as "+SubCategoryTable.SUB_CATEGORY_USER_TYPE+","+
                                SubHeadingTable.TABLE_SUB_HEADING+"."+SubHeadingTable.SUB_HEADING_Id+","+
                                SubHeadingTable.TABLE_SUB_HEADING+"."+SubHeadingTable.SUB_HEADING_NAME+","+
                                SubHeadingTable.TABLE_SUB_HEADING+"."+SubHeadingTable.SUB_HEADING_DISPLAY_ORDER+","+
                                "0"+" as "+SubCategoryTable.SUB_CATEGORY_CAT_CODE+","+
                                "0"+" as "+SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER+","+
                                "0 as "+SubCategoryTable.SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE+" FROM "+
                                SubHeadingTable.TABLE_SUB_HEADING+
                                " WHERE "+SubHeadingTable.SUB_HEADING_CATEGORY_ID+"="+selectionArgs[0]+
                                " UNION ALL " +
                                "SELECT "+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.COLUMN_ID+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_Id+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_IMAGE_URL+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_NAME+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_USER_TYPE+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID+" as "+
                                SubHeadingTable.SUB_HEADING_Id+","+
                                "'' as "+SubHeadingTable.SUB_HEADING_NAME+","+
                                SubHeadingTable.SUB_HEADING_DISPLAY_ORDER+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_CAT_CODE+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+""+SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER+","+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE+" FROM "+
                                SubCategoryTable.TABLE_SUB_CATEGORY +" LEFT JOIN "+
                                SubHeadingTable.TABLE_SUB_HEADING+" ON "+
                                SubCategoryTable.TABLE_SUB_CATEGORY+"."+SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID+"="+
                                SubHeadingTable.TABLE_SUB_HEADING+"."+SubHeadingTable.SUB_HEADING_Id
                                +" WHERE "+SubCategoryTable.SUB_CATEGORY_USER_TYPE+"<="+ MySharedPref.getUserLoginType(getContext(),
                                Constant.USER_LOGIN_TYPE)+" AND "+SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID+" in " +
                                "( select "+SubHeadingTable.SUB_HEADING_Id+" FROM "+SubHeadingTable.TABLE_SUB_HEADING+
                                " WHERE "+SubHeadingTable.SUB_HEADING_CATEGORY_ID+"="+selectionArgs[0]+")"
                        +") ORDER BY "+SubHeadingTable.SUB_HEADING_DISPLAY_ORDER+" ASC"
                                ,null);
            cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI_SUB_HEADING);
            return cursor;

            case CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN:
                String whereCond = "";
                if (selectionArgs.length>1){
                    whereCond = " WHERE "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+" in ("+selectionArgs[1]+") ";
                }
                cursor = db.rawQuery(
                        "SELECT * FROM (SELECT " +
                                CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING+"."+
                                CampusDirectoryHeadingTable.COLUMN_ID+","+
                                "0 as "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id+","+
                                "'' as "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_DESIGNATION+","+
                                "'' as "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_NAME+","+
                                CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING+"."+
                                CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+","+
                                CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING+"."+
                                CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME+","+
                                "0"+" as "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL+","+
                                "0"+" as "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER+","+
                                "0 as "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EXT+" FROM "+
                                CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING+
                                " WHERE "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID+"="+
                                selectionArgs[0]+
                                " UNION ALL " +
                                "SELECT "+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.COLUMN_ID+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_DESIGNATION+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_NAME+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_HEADING_ID+" as "+
                                CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+","+
                                "'' as "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+""+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER+","+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY+"."+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EXT+" FROM "+
                                CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY
                                +" WHERE "+CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_HEADING_ID+" in ( select "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+" FROM "+
                                CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING+" WHERE "+
                                CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID+"="+selectionArgs[0]+")"
                                +") "+whereCond+ " ORDER BY "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+" ASC"
                        ,null);
                cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI_CAMPUS_DIRECTORY_HEADING);
                return cursor;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        Uri _uri = null;

        switch (uriType) {
            case HOME_PAGER_IMAGE:
                id = insertOrUpdateById(sqlDB, uri, HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE,values,
                        HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_ID);
                _uri = ContentUris.withAppendedId(CONTENT_URI_PAGER, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;
            case CATEGORY:
                id = insertOrUpdateById(sqlDB, uri, HomeActivityCategoryTable.TABLE_CATEGORY,values, HomeActivityCategoryTable.CATEGORY_Id);
                _uri = ContentUris.withAppendedId(CONTENT_URI_CATEGORY, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;
            case SUB_CATEGORY:
                id = insertOrUpdateById(sqlDB, uri, SubCategoryTable.TABLE_SUB_CATEGORY,values, SubCategoryTable.SUB_CATEGORY_Id);
                _uri = ContentUris.withAppendedId(CONTENT_URI_SUB_CATEGORY, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            case SUB_HEADING:
                id = insertOrUpdateById(sqlDB, uri, SubHeadingTable.TABLE_SUB_HEADING,values, SubHeadingTable.SUB_HEADING_Id);
                _uri = ContentUris.withAppendedId(CONTENT_URI_SUB_HEADING, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            case MAP_CATEGORY:
                id = insertOrUpdateById(sqlDB, uri, MapTable.TABLE_MAP_CATEGORY,values, MapTable.MAP_PLACE_ID);
                _uri = ContentUris.withAppendedId(CONTENT_URI_MAP_CATEGORY, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            case FACILITY:
                id = insertOrUpdateById(sqlDB, uri, GenericFacilityActivityTable.TABLE_FACILITY,values,
                        GenericFacilityActivityTable.FACILITY_SUB_CATEGORY_ID);
                _uri = ContentUris.withAppendedId(CONTENT_URI_FACILITY, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            case DRAWABLE:
                id = insertOrUpdateById(sqlDB, uri, DrawableTable.TABLE_DRAWABLE,values, DrawableTable.DRAWABLE_Id);
                _uri = ContentUris.withAppendedId(CONTENT_URI_DRAWABLE, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;


            case CAMPUS_DIRECTORY_HEADING:
                id = insertOrUpdateById(sqlDB, uri, CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING,
                        values, CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id);
                _uri = ContentUris.withAppendedId(CONTENT_URI_CAMPUS_DIRECTORY_HEADING, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            case CAMPUS_DIRECTORY_MAIN:
                id = insertOrUpdateById(sqlDB, uri, CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY,
                        values, CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id);
                _uri = ContentUris.withAppendedId(CONTENT_URI_CAMPUS_DIRECTORY_MAIN, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            case NOTIFICATION:
                id = insertOrUpdateById(sqlDB, uri, NotificationTable.TABLE_NOTIFICATION,
                        values, NotificationTable.NOTIFICATION_ID);
                _uri = ContentUris.withAppendedId(CONTENT_URI_NOTIFICATION, id);
                getContext().getContentResolver().notifyChange(uri, null, false);
                break;

            default:

                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;


    }

    private long insertOrUpdateById(SQLiteDatabase db, Uri uri, String table,
                                    ContentValues values, String column) throws SQLException {
        try {
            return db.insertOrThrow(table, null, values);
        } catch (SQLiteConstraintException e) {
            int nrRows = update(uri, values, column + "=?",
                    new String[]{values.getAsString(column)});
            if (nrRows == 0)
                throw e;
            return 0;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case HOME_PAGER_IMAGE:
                rowsDeleted = sqlDB.delete(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE, selection,
                        selectionArgs);
                break;
            case HOME_PAGER_IMAGE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE,
                            HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE,
                            HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case CATEGORY:
                rowsDeleted = sqlDB.delete(HomeActivityCategoryTable.TABLE_CATEGORY, selection,
                        selectionArgs);
                break;
            case CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(HomeActivityCategoryTable.TABLE_CATEGORY,
                            HomeActivityCategoryTable.CATEGORY_Id + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(HomeActivityCategoryTable.TABLE_CATEGORY,
                            HomeActivityCategoryTable.CATEGORY_Id + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case SUB_CATEGORY:
                rowsDeleted = sqlDB.delete(SubCategoryTable.TABLE_SUB_CATEGORY, selection,
                        selectionArgs);
                break;
            case SUB_CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(SubCategoryTable.TABLE_SUB_CATEGORY,
                            SubCategoryTable.SUB_CATEGORY_Id+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(SubCategoryTable.TABLE_SUB_CATEGORY,
                            SubCategoryTable.SUB_CATEGORY_Id+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case MAP_CATEGORY:
                rowsDeleted = sqlDB.delete(MapTable.TABLE_MAP_CATEGORY, selection,
                        selectionArgs);
                break;
            case MAP_CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MapTable.TABLE_MAP_CATEGORY,
                            MapTable.MAP_PLACE_ID+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(MapTable.TABLE_MAP_CATEGORY,
                            MapTable.MAP_PLACE_ID+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case SUB_HEADING:
                rowsDeleted = sqlDB.delete(SubHeadingTable.TABLE_SUB_HEADING, selection,
                        selectionArgs);
                break;
            case SUB_HEADING_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(SubHeadingTable.TABLE_SUB_HEADING,
                            SubHeadingTable.SUB_HEADING_Id+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(SubHeadingTable.TABLE_SUB_HEADING,
                            SubHeadingTable.SUB_HEADING_Id+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case FACILITY:
                rowsDeleted = sqlDB.delete(GenericFacilityActivityTable.TABLE_FACILITY, selection,
                        selectionArgs);
                break;
            case FACILITY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(GenericFacilityActivityTable.TABLE_FACILITY,
                            GenericFacilityActivityTable.FACILITY_SUB_CATEGORY_ID+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(GenericFacilityActivityTable.TABLE_FACILITY,
                            GenericFacilityActivityTable.FACILITY_SUB_CATEGORY_ID+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case DRAWABLE:
                rowsDeleted = sqlDB.delete(DrawableTable.TABLE_DRAWABLE, selection,
                        selectionArgs);
                break;
            case DRAWABLE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DrawableTable.TABLE_DRAWABLE,
                            DrawableTable.DRAWABLE_Id+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DrawableTable.TABLE_DRAWABLE,
                            DrawableTable.DRAWABLE_Id+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case CAMPUS_DIRECTORY_HEADING:
                rowsDeleted = sqlDB.delete(CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING, selection,
                        selectionArgs);
                break;
            case CAMPUS_DIRECTORY_HEADING_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING,
                            CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING,
                            CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case CAMPUS_DIRECTORY_MAIN:
                rowsDeleted = sqlDB.delete(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY, selection,
                        selectionArgs);
                break;
            case CAMPUS_DIRECTORY_MAIN_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY,
                            CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY,
                            CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            case NOTIFICATION:
                rowsDeleted = sqlDB.delete(NotificationTable.TABLE_NOTIFICATION, selection,
                        selectionArgs);
                break;
            case NOTIFICATION_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(NotificationTable.TABLE_NOTIFICATION,
                            NotificationTable.NOTIFICATION_ID+ "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(NotificationTable.TABLE_NOTIFICATION,
                            NotificationTable.NOTIFICATION_ID+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case HOME_PAGER_IMAGE:
                rowsUpdated = sqlDB.update(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case HOME_PAGER_IMAGE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE,
                            values,
                            HomeActivityViewPagerImageTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(HomeActivityViewPagerImageTable.TABLE_HOME_PAGER_IMAGE,
                            values,
                            HomeActivityViewPagerImageTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case CATEGORY:
                rowsUpdated = sqlDB.update(HomeActivityCategoryTable.TABLE_CATEGORY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(HomeActivityCategoryTable.TABLE_CATEGORY,
                            values,
                            HomeActivityCategoryTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(HomeActivityCategoryTable.TABLE_CATEGORY,
                            values,
                            HomeActivityCategoryTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case SUB_CATEGORY:
                rowsUpdated = sqlDB.update(SubCategoryTable.TABLE_SUB_CATEGORY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SUB_CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(SubCategoryTable.TABLE_SUB_CATEGORY,
                            values,
                            SubCategoryTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(SubCategoryTable.TABLE_SUB_CATEGORY,
                            values,
                            SubCategoryTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case FACILITY:
                rowsUpdated = sqlDB.update(GenericFacilityActivityTable.TABLE_FACILITY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case FACILITY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(GenericFacilityActivityTable.TABLE_FACILITY,
                            values,
                            GenericFacilityActivityTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(GenericFacilityActivityTable.TABLE_FACILITY,
                            values,
                            GenericFacilityActivityTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case SUB_HEADING:
                rowsUpdated = sqlDB.update(SubHeadingTable.TABLE_SUB_HEADING,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SUB_HEADING_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(SubHeadingTable.TABLE_SUB_HEADING,
                            values,
                            SubHeadingTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(SubHeadingTable.TABLE_SUB_HEADING,
                            values,
                            SubHeadingTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case MAP_CATEGORY:
                rowsUpdated = sqlDB.update(MapTable.TABLE_MAP_CATEGORY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MAP_CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MapTable.TABLE_MAP_CATEGORY,
                            values,
                            MapTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MapTable.TABLE_MAP_CATEGORY,
                            values,
                            MapTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case DRAWABLE:
                rowsUpdated = sqlDB.update(DrawableTable.TABLE_DRAWABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case DRAWABLE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DrawableTable.TABLE_DRAWABLE,
                            values,
                            DrawableTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DrawableTable.TABLE_DRAWABLE,
                            values,
                            DrawableTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case CAMPUS_DIRECTORY_HEADING:
                rowsUpdated = sqlDB.update(DrawableTable.TABLE_DRAWABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CAMPUS_DIRECTORY_HEADING_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DrawableTable.TABLE_DRAWABLE,
                            values,
                            DrawableTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DrawableTable.TABLE_DRAWABLE,
                            values,
                            DrawableTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case CAMPUS_DIRECTORY_MAIN:
                rowsUpdated = sqlDB.update(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CAMPUS_DIRECTORY_MAIN_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY,
                            values,
                            CampusDirectoryMainTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(CampusDirectoryMainTable.TABLE_CAMPUS_DIRECTORY_CATEGORY,
                            values,
                            CampusDirectoryMainTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case NOTIFICATION:
                rowsUpdated = sqlDB.update(NotificationTable.TABLE_NOTIFICATION,
                        values,
                        selection,
                        selectionArgs);
                break;
            case NOTIFICATION_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(NotificationTable.TABLE_NOTIFICATION,
                            values,
                            NotificationTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(NotificationTable.TABLE_NOTIFICATION,
                            values,
                            NotificationTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

/*    private void checkColumns(String[] projection) {
        String[] available =
                {

                        HomeActivityCategoryTable.COLUMN_ID,
                        HomeActivityCategoryTable.CATEGORY_Id,
                        HomeActivityCategoryTable.CATEGORY_NAME,
                        HomeActivityCategoryTable.CATEGORY_IMAGE_URL
                };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }*/

}