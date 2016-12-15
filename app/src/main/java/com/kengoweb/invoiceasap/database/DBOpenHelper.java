package com.kengoweb.invoiceasap.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vokrut on 11.10.2016.
 */

class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "invoiceasap";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + DatabaseContract.LocationsTable.TABLE_NAME + " (" +
                    DatabaseContract.LocationsTable.COLUMN_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.LocationsTable.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.LocationsTable.COLUMN_GEO_LAT + REAL_TYPE + COMMA_SEP +
                    DatabaseContract.LocationsTable.COLUMN_GEO_LNG + REAL_TYPE +
                    ")";
    private static final String SQL_CREATE_BEERS =
            "CREATE TABLE " + DatabaseContract.BeersTable.TABLE_NAME + " (" +
                    DatabaseContract.BeersTable.COLUMN_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.BeersTable.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.BeersTable.COLUMN_TYPE + TEXT_TYPE +
                    ")";

    private static final String SQL_DELETE_LOCATIONS =
            "DROP TABLE IF EXISTS " + DatabaseContract.LocationsTable.TABLE_NAME;
    private static final String SQL_DELETE_BEERS =
            "DROP TABLE IF EXISTS " + DatabaseContract.BeersTable.TABLE_NAME;

    DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATIONS);
        db.execSQL(SQL_CREATE_BEERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_LOCATIONS);
        db.execSQL(SQL_DELETE_BEERS);
        onCreate(db);
    }
}
