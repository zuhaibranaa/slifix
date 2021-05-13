package com.slifix.slifix.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.slifix.slifix.R;
import com.slifix.slifix.DataManager;

public class OTP_Reg extends AppCompatActivity {
    EditText number;
    MaterialButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p__reg);
        number = (EditText) findViewById(R.id.otpField);
        submit = (MaterialButton) findViewById(R.id.otpSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.setPhoneNumber(number.getText().toString());
                Intent sendOTP = new Intent(getApplicationContext(),Sending_OTP.class);
                startActivity(sendOTP);
            }
        });
    }
}