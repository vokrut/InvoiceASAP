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
import android.widget.ListView;

import com.kengoweb.invoiceasap.R;
import com.kengoweb.invoiceasap.activity.MainActivity;
import com.kengoweb.invoiceasap.adapter.BeerListAdapter;
import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.managers.BeersManager;
import com.kengoweb.invoiceasap.model.Beer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vokrut on 11.10.2016.
 */
public class BeersFragment extends Fragment {

    private static final String TAG = BeersFragment.class.getSimpleName();
    private BroadcastReceiver updateDataReceiver;
    private List<Beer> listBeer = new ArrayList<>();
    private BeerListAdapter adapter;

    protected MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beers, container, false);
        ListView listViewBeers = (ListView) view.findViewById(R.id.listViewBeers);
        adapter = new BeerListAdapter(getContext(), android.R.layout.simple_list_item_1, listBeer);
        listViewBeers.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataForList();
        registerReceiver();
    }

    /**
     * Get beers list from database
     */
    private void getDataForList() {
        listBeer.clear();
        listBeer.addAll(BeersManager.getInstance(activity).getBeers());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    /**
     * Registere recievers about needed update data from database
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
                            getDataForList();
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
     * Unregister recievers about needed update data from database
     */
    private void unregisterReceiver() {
        if (updateDataReceiver != null)
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(updateDataReceiver);
    }
}
