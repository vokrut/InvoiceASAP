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
public class Beer {

    public static final String TAG = Beer.class.getSimpleName();

    private Integer id;
    private String name;
    private String type;

    public Beer() {}

    /**
     * Constructor for cursor from database
     *
     * @param cursor - Cursor with data
     */
    public Beer(Cursor cursor) {
        setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.BeersTable.COLUMN_ID)));
        setName(cursor.getString(cursor.getColumnIndex(DatabaseContract.BeersTable.COLUMN_NAME)));
        setType(cursor.getString(cursor.getColumnIndex(DatabaseContract.BeersTable.COLUMN_TYPE)));
    }

    /**
     * Constructor for json result presented as JSONObject
     *
     * @param jsonObject - JSONObject with data
     */
    public Beer(JSONObject jsonObject) {
        try {
            setId(jsonObject.getInt(Constants.ID));
            setName(jsonObject.getString(Constants.NAME));
            setType(jsonObject.getString(Constants.TYPE));
        } catch (JSONException e) {
            Log.d(TAG, "Location: " + e);
            e.printStackTrace();
        }
    }

    /**
     * prepare data for storing to database
     * @param cv empty ContentValues
     * @return prepared ContentValues
     */
    public ContentValues prepareContentValuesForDatabase(ContentValues cv) {
        cv.put(DatabaseContract.BeersTable.COLUMN_ID, id);
        cv.put(DatabaseContract.BeersTable.COLUMN_NAME, name);
        cv.put(DatabaseContract.BeersTable.COLUMN_TYPE, type);
        return cv;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name + " (" +
                type +
                ')';
    }
}
