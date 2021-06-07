package com.slifix.slifix;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.slifix.slifix.login.LoginScreen;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (DataManager.getAuthToken () == null){
            startService(new Intent(this, DataManager.class));
        }
        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext ());

        if (status == "Internet Access"){
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
//                Intent intent = new Intent(MainActivity.this, Checkout.class);
                startActivity(intent);
                finish();
            },2000);

        }else{
            startActivity(new Intent (Settings.ACTION_WIFI_SETTINGS));
            Toast.makeText(getApplicationContext (), status, Toast.LENGTH_LONG).show();
        }

//        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE );
//        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//            buildAlertMessageNoGps();
//        }else{
//        MyReceiver = new MyReceiver();
//        registerReceiver(MyReceiver, new IntentFilter (ConnectivityManager.CONNECTIVITY_ACTION));
//        }
    }
}