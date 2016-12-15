package com.kengoweb.invoiceasap.managers;

import android.content.Context;

import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.database.AppDAO;
import com.kengoweb.invoiceasap.model.Beer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */

public class BeersManager {

    public static final String TAG = BeersManager.class.getSimpleName();

    public Context context;

    private static volatile BeersManager instance;

    public static synchronized BeersManager getInstance(Context context) {
        if (instance == null) {
            synchronized (BeersManager.class) {
                if (instance == null) {
                    instance = new BeersManager(context.getApplicationContext());
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
    private BeersManager(Context context) {
        this.context = context;
    }

    /**
     * Get list of beers from database
     * @return list of beers
     */
    public List<Beer> getBeers() {
        return AppDAO.getInstance(context).getBeers();
    }

    /**
     * save beers to database
     * @param listBeer
     */
    public void storeBeers(List<Beer> listBeer) {
        AppDAO.getInstance(context).storeBeers(listBeer);
    }

    /**
     * Parsing response from server and generate list of beers
     * @param jsonObject
     * @return list of beers
     */
    public List<Beer> parseBeers(JSONObject jsonObject) {
        List<Beer> listBeers = new ArrayList<>();
        try {
            if (!jsonObject.isNull(Constants.BEERS)) {
                JSONArray jsonArrayBeers = jsonObject.optJSONArray(Constants.BEERS);
                for (int i = 0; i < jsonArrayBeers.length(); i++) {
                    Beer beer = new Beer(jsonArrayBeers.getJSONObject(i));
                    listBeers.add(beer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listBeers;
    }
}
