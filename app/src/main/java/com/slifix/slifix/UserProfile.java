package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
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
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(UserProfile.this, "Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeUsernameEmail.class));
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChangePass.class));
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "Privacy", Toast.LENGTH_SHORT).show();
            }
        });
        feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "Request Feature", Toast.LENGTH_SHORT).show();
            }
        });

    }
}