package com.kengoweb.invoiceasap.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kengoweb.invoiceasap.service.GetDataIntentService;

/**
 * Created by vokrut on 11.10.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent getDataService = new Intent(context, GetDataIntentService.class);
        context.startService(getDataService);
    }
}
