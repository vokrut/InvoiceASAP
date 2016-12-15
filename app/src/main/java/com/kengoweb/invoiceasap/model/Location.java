package com.kengoweb.invoiceasap.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.database.DatabaseContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vokrut on 11.10.2016.
 */
public class Location {

    public static final String TAG = Location.class.getSimpleName();

    private Integer id;
    private String name;
    private Float latitude;
    private Float longitude;

    public Location() {}

    /**
     * Constructor for cursor from database
     *
     * @param cursor - Cursor with data
     */
    public Location(Cursor cursor) {
        setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.LocationsTable.COLUMN_ID)));
        setName(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsTable.COLUMN_NAME)));
        setLatitude(cursor.getFloat(cursor.getColumnIndex(DatabaseContract.LocationsTable.COLUMN_GEO_LAT)));
        setLongitude(cursor.getFloat(cursor.getColumnIndex(DatabaseContract.LocationsTable.COLUMN_GEO_LNG)));
    }

    /**
     * Constructor for json result presented as JSONObject
     *
     * @param jsonObject - JSONObject with data
     */
    public Location(JSONObject jsonObject) {
        try {
            setId(jsonObject.getInt(Constants.ID));
            setName(jsonObject.getString(Constants.NAME));
            setLatitude((float) jsonObject.getDouble(Constants.LATITUDE));
            setLongitude((float) jsonObject.getDouble(Constants.LONGITUDE));
        } catch (JSONException e) {
            Log.d(TAG, "Location: " + e);
            e.printStackTrace();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public ContentValues prepareContentValuesForDatabase(ContentValues cv) {
        cv.put(DatabaseContract.LocationsTable.COLUMN_ID, id);
        cv.put(DatabaseContract.LocationsTable.COLUMN_NAME, name);
        cv.put(DatabaseContract.LocationsTable.COLUMN_GEO_LAT, latitude);
        cv.put(DatabaseContract.LocationsTable.COLUMN_GEO_LNG, longitude);
        return cv;
    }

    @Override
    public String toString() {
        return name + " (" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ')';
    }
}
