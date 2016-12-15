package com.kengoweb.invoiceasap.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.managers.BeersManager;
import com.kengoweb.invoiceasap.managers.LocationsManager;
import com.kengoweb.invoiceasap.model.Beer;
import com.kengoweb.invoiceasap.model.Location;
import com.kengoweb.invoiceasap.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */

public class GetDataIntentService extends IntentService {

    private static final String TAG = GetDataIntentService.class.getSimpleName();

    public GetDataIntentService() {
        super(GetDataIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.SERVER_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        List<Location> listLocations = LocationsManager.getInstance(GetDataIntentService.this).parseLocations(response);
                        LocationsManager.getInstance(GetDataIntentService.this).storeLocations(listLocations);
                        Log.d(TAG, listLocations.toString());

                        List<Beer> listBeers = BeersManager.getInstance(GetDataIntentService.this).parseBeers(response);
                        BeersManager.getInstance(GetDataIntentService.this).storeBeers(listBeers);
                        Log.d(TAG, listBeers.toString());

                        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        jsonObjectRequest.setTag(TAG);
        VolleyUtils.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
