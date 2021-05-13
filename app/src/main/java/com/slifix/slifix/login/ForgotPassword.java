package com.slifix.slifix.login;

import android.content.Intent;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.R;
import com.slifix.slifix.DataManager;

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
                DataManager.setPhoneNumber(num.getText().toString());
                Intent intent = new Intent(ForgotPassword.this, Sending_OTP.class);
                startActivity(intent);
            }
        });
    }
}