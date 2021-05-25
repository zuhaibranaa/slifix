package com.slifix.slifix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;
class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.isEmpty()) {
            status="No Internet Connection";
        }
        if (status == "Internet Access"){

        }else{
            context.startActivity(new Intent (Settings.ACTION_WIFI_SETTINGS));
            Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        }
    }
}