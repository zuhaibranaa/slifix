package com.slifix.slifix;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.Pair;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.slifix.slifix.login.LoginScreen;

public class MainActivity extends AppCompatActivity {
    Animation animation;
    ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        animation = AnimationUtils.loadAnimation (this,R.anim.splash);
        splash = findViewById (R.id.splashImage);
        splash.setAnimation (animation);
        if (DataManager.getAuthToken () == null){
            startService(new Intent(this, DataManager.class));
        }
        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext ());

        if (status == "Internet Access"){
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                Pair pair = new Pair (splash,"logoImage");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation (this,pair);
                    startActivity(intent,options.toBundle ());
                }
                finish ();
            },2000);

        }else{
            startActivity(new Intent (Settings.ACTION_WIFI_SETTINGS));
            Toast.makeText(getApplicationContext (), status, Toast.LENGTH_LONG).show();
        }
    }
}