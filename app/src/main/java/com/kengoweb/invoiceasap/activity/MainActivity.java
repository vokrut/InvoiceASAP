package com.kengoweb.invoiceasap.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.kengoweb.invoiceasap.R;
import com.kengoweb.invoiceasap.application.Constants;
import com.kengoweb.invoiceasap.fragment.BeersFragment;
import com.kengoweb.invoiceasap.fragment.LocationsFragment;
import com.kengoweb.invoiceasap.receiver.AlarmReceiver;
import com.kengoweb.invoiceasap.service.GetDataIntentService;
import com.kengoweb.invoiceasap.utils.VolleyUtils;

/**
 * Created by vokrut on 11.10.2016.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private ProgressDialog progressDialog;
    private BroadcastReceiver updateDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        firstDataRetrieving();
    }

    /**
     * Get data after activity creating
     */
    private void firstDataRetrieving() {
        progressDialog = ProgressDialog.show(this, "", "Loading data from server");
        Intent getDataService = new Intent(this, GetDataIntentService.class);
        startService(getDataService);
    }

    /**
     * Start every 3 minutes getting data from server
     */
    private void startScheduledReceiver() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        if (manager != null) {
            manager.cancel(pendingIntent);
            Log.d(TAG, "Test Task: cancelled");
        }

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 3 * 60 * 1000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }

    /**
     * register reciever about finish first getting data from server
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
                            if (updateDataReceiver != null)
                                LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(updateDataReceiver);
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            startScheduledReceiver();
                            break;

                        default:
                            Log.d(TAG, "onReceive: Received unknown intent action");
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateDataReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestQueue requestQueue = VolleyUtils.getInstance(this).getRequestQueue();
        if (requestQueue != null)
            requestQueue.cancelAll(GetDataIntentService.class.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new LocationsFragment();
            } else {
                return new BeersFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "List of Locations";
                case 1:
                    return "List of Beers";
            }
            return null;
        }
    }
}
