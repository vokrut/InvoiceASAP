package com.kengoweb.invoiceasap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kengoweb.invoiceasap.model.Beer;
import com.kengoweb.invoiceasap.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */

public class AppDAO {

    public static final String TAG = AppDAO.class.getSimpleName();

    public Context context;

    private static volatile AppDAO instance;

    public static synchronized AppDAO getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDAO.class) {
                if (instance == null) {
                    instance = new AppDAO(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor because of singleton
     *
     * @param context - application context
     */
    private AppDAO(Context context) {
        this.context = context;
    }

    /**
     * Save locations to database
     * @param locationList
     * @return boolean
     */
    public boolean storeLocations(List<Location> locationList) {

        try {

            SQLiteDatabase db = new DBOpenHelper(context).getWritableDatabase();

            db.beginTransaction();

            db.delete(DatabaseContract.LocationsTable.TABLE_NAME, null, null);

            for (Location location : locationList) {

                ContentValues cv = new ContentValues();
                cv = location.prepareContentValuesForDatabase(cv);
                db.insert(DatabaseContract.LocationsTable.TABLE_NAME, null, cv);

            }

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();

        } catch ( Exception e ) {

            return false;

        }

        return true;

    }

    /**
     * Get list of locations
     * @return list of locations
     */
    public List<Location> getLocations() {

        SQLiteDatabase db = new DBOpenHelper(context).getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseContract.LocationsTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Location> locationList = new ArrayList<>();

        while (cursor.moveToNext()) {

            Location location = new Location(cursor);

            locationList.add(location);

        }

        cursor.close();
        db.close();

        return locationList;

    }

    /**
     * Save beers to database
     * @param beerList
     * @return boolean
     */
    public boolean storeBeers(List<Beer> beerList) {

        try {

            SQLiteDatabase db = new DBOpenHelper(context).getWritableDatabase();

            db.beginTransaction();

            db.delete(DatabaseContract.BeersTable.TABLE_NAME, null, null);

            for (Beer beer : beerList) {

                ContentValues cv = new ContentValues();
                cv = beer.prepareContentValuesForDatabase(cv);
                db.insert(DatabaseContract.BeersTable.TABLE_NAME, null, cv);

            }

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();

        } catch ( Exception e ) {

            return false;

        }

        return true;

    }

    /**
     * Get list of beers from database
     * @return List of beers
     */
    public List<Beer> getBeers() {

        SQLiteDatabase db = new DBOpenHelper(context).getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseContract.BeersTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Beer> beerList = new ArrayList<>();

        while (cursor.moveToNext()) {

            Beer beer = new Beer(cursor);

            beerList.add(beer);

        }

        cursor.close();
        db.close();

        return beerList;

    }
}
