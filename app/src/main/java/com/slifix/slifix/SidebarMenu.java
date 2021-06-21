package com.slifix.slifix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class SidebarMenu extends AppCompatActivity {
TextView userName,userId,editProfile;
ImageView userProfilePic;
RelativeLayout dashboard,myOrders,promo,customerSupport,rateOurApp,settings;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_sidebar_menu);
        // Initializations
        userId = findViewById (R.id.SettingsUId);
        userName = findViewById (R.id.SettingsUName);
        editProfile = findViewById (R.id.EditProfile);
        userProfilePic = findViewById (R.id.sidebarUserImage);
        dashboard = findViewById (R.id.SettingsDashboard);
        myOrders = findViewById (R.id.SettingsMyOrders);
        promo = findViewById (R.id.SettingsPromo);
        customerSupport = findViewById (R.id.SettingsCustomerSupport);
        rateOurApp = findViewById (R.id.SettingsRateOurApp);
        settings = findViewById (R.id.Settings);

        Picasso.get()
                .load(DataManager.getUserImage ())
                .into(userProfilePic);

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
        customerSupport.setOnClickListener (v -> Toast.makeText (this, "Customer Support", Toast.LENGTH_SHORT).show ());
        rateOurApp.setOnClickListener (v -> Toast.makeText (this, "Rate Our App", Toast.LENGTH_SHORT).show ());

    }


}