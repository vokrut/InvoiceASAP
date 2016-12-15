package com.kengoweb.invoiceasap.managers;

import android.content.Context;

import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.database.AppDAO;
import com.kengoweb.invoiceasap.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */

public class LocationsManager {

    public static final String TAG = LocationsManager.class.getSimpleName();

    public Context context;

    private static volatile LocationsManager instance;

    public static synchronized LocationsManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationsManager.class) {
                if (instance == null) {
                    instance = new LocationsManager(context.getApplicationContext());
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
    private LocationsManager(Context context) {
        this.context = context;
    }

    /**
     * Get list of beers from database
     * @return list of beers
     */
    public List<Location> getLocations() {
        return AppDAO.getInstance(context).getLocations();
    }

    /**
     * save locations to database
     * @param listLocation
     */
    public void storeLocations(List<Location> listLocation) {
        AppDAO.getInstance(context).storeLocations(listLocation);
    }

    /**
     * Parsing response from server and generate list of locations
     * @param jsonObject
     * @return list of locations
     */
    public List<Location> parseLocations(JSONObject jsonObject) {
        List<Location> listLocations = new ArrayList<>();
        try {
            if (!jsonObject.isNull(Constants.LOCATIONS)) {
                JSONArray jsonArrayLocations = jsonObject.optJSONArray(Constants.LOCATIONS);
                for (int i = 0; i < jsonArrayLocations.length(); i++) {
                    Location location = new Location(jsonArrayLocations.getJSONObject(i));
                    listLocations.add(location);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listLocations;
    }
}
