package com.slifix.slifix;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {
CardView account,changePassword,privacy,feature;
ImageView backbtn;
Switch notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (DataManager.getAuthToken() == null){
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Initialization
        account = findViewById(R.id.account);
        changePassword = findViewById(R.id.change_password);
        privacy = findViewById(R.id.Privacy);
        feature = findViewById(R.id.requestFeature);
        notifications = findViewById(R.id.notificationSwitch);
        backbtn = findViewById(R.id.upbckbtn);

        // Listeners
        backbtn.setOnClickListener(v -> {
            startActivity (new Intent (getApplicationContext (),dashboard.class));
            finish();
        });
        notifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(UserProfile.this, "Checked", Toast.LENGTH_SHORT).show();
            }
        });
        account.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ChangeUsernameEmail.class)));
        changePassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),ChangePass.class)));
        privacy.setOnClickListener(v -> Toast.makeText(UserProfile.this, "Privacy", Toast.LENGTH_SHORT).show());
        feature.setOnClickListener(v -> Toast.makeText(UserProfile.this, "Request Feature", Toast.LENGTH_SHORT).show());

    }
    @Override
    public void onBackPressed() {
        startActivity (new Intent (getApplicationContext (),dashboard.class));
        finish();
    }
}