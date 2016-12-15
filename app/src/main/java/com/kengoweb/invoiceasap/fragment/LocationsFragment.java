package com.kengoweb.invoiceasap.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kengoweb.invoiceasap.R;
import com.kengoweb.invoiceasap.activity.MainActivity;
import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.managers.LocationsManager;
import com.kengoweb.invoiceasap.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */
public class LocationsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = LocationsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private MapView mapView;
    private List<Marker> listMarker = new ArrayList<>();
    private BroadcastReceiver updateDataReceiver;

    protected MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView.onResume();

        mMap = googleMap;

        showMarkers();
    }

    /**
     * Show markers of locations on map
     */
    private void showMarkers() {

        if (listMarker.size() > 0) {
            for (Marker marker :
                    listMarker) {
                marker.remove();
            }
            listMarker.clear();
        }

        List<Location> listLocation = LocationsManager.getInstance(activity).getLocations();
        Log.d(TAG, "listLocation: " + listLocation);
        if (listLocation != null) {
            LatLng position = null;
            for (Location location :
                    listLocation) {
                position = new LatLng(location.getLatitude(), location.getLongitude());
                Marker newMarker = mMap.addMarker(new MarkerOptions().position(position).title(location.getName()));
                listMarker.add(newMarker);
            }
            if (position != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    /**
     * register recievers about database changes
     */
    private void registerReceiver() {
        updateDataReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    Log.d(TAG, "onReceive: action=" + action + " intent=" + intent);
                    switch (action) {
                        case Constants.BROADCAST_ACTION:
                            showMarkers();
                            break;

                        default:
                            Log.d(TAG, "onReceive: Received unknown intent action");
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateDataReceiver, intentFilter);
    }

    /**
     * unregister recievers about database changes
     */
    private void unregisterReceiver() {
        if (updateDataReceiver != null)
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(updateDataReceiver);
    }

}
