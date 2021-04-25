package com.slifix.slifix;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;

import java.time.Instant;


public class ForgotPassword extends AppCompatActivity {
    EditText num;
    public static String number;
    SwipeButton forget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        num = (EditText) findViewById(R.id.forgotEtNo);
        forget = (SwipeButton) findViewById(R.id.resetSwipe);
        forget.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                number = num.getText().toString();
                Intent intent = new Intent(ForgotPassword.this,Sending_OTP.class);
                startActivity(intent);
            }
        });
    }
}