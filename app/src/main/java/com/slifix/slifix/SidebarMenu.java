package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SidebarMenu extends AppCompatActivity {
TextView userName,userId,editProfile;
RelativeLayout dashboard,myOrders,promo,customerSupport,rateOurApp,settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_sidebar_menu);
        // Initializations
        userId = findViewById (R.id.SettingsUId);
        userName = findViewById (R.id.SettingsUName);
        editProfile = findViewById (R.id.EditProfile);
        dashboard = findViewById (R.id.SettingsDashboard);
        myOrders = findViewById (R.id.SettingsMyOrders);
        promo = findViewById (R.id.SettingsPromo);
        customerSupport = findViewById (R.id.SettingsCustomerSupport);
        rateOurApp = findViewById (R.id.SettingsRateOurApp);
        settings = findViewById (R.id.Settings);
        // Text Setters
        userName.setText (DataManager.getUserName ());
        userId.setText ("ID "+DataManager.getUserID ());
        // Listeners
        editProfile.setOnClickListener (v -> {
            startActivity (new Intent (getApplicationContext (),ChangeUsernameEmail.class));
            finish ();
        });
        dashboard.setOnClickListener (v -> {
            startActivity (new Intent (getApplicationContext (),dashboard.class));
            finish ();
        });
        settings.setOnClickListener (v -> {
            startActivity (new Intent (getApplicationContext (),UserProfile.class));
            finish ();
        });
        myOrders.setOnClickListener (v -> {
            startActivity (new Intent (getApplicationContext (),AllOrders.class));
            finish ();
        });
        promo.setOnClickListener (v -> {
            Toast.makeText (this, "Promo", Toast.LENGTH_SHORT).show ();
//            startActivity (new Intent (getApplicationContext (),));
//            finish ();
        });
        customerSupport.setOnClickListener (v -> {
            Toast.makeText (this, "Customer Support", Toast.LENGTH_SHORT).show ();
        });
        rateOurApp.setOnClickListener (v -> {
            Toast.makeText (this, "Rate Our App", Toast.LENGTH_SHORT).show ();
        });

    }
}