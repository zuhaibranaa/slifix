package com.slifix.slifix.login;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.material.textfield.TextInputEditText;
import com.slifix.slifix.DataManager;
import com.slifix.slifix.R;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    TextView forgot;
    TextInputEditText pass,number;
    public static String t;
    SwipeButton login,signup;
    JSONObject obj;
    StringRequest req;
    RequestQueue queue;
    public static boolean isForgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        number = findViewById(R.id.number);
        pass = findViewById(R.id.input_pass);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.createPass);
        forgot = findViewById(R.id.forgot);
        if (DataManager.getAuthToken() != null){
            finish ();
            Intent it = new Intent(getApplicationContext(),dashboard.class);
            startActivity(it);
        }
        login.setOnActiveListener(() -> {
            DataManager.setPhoneNumber("+"+number.getText().toString());
            t = pass.getText().toString();
            login();
        });
        forgot.setOnClickListener (v -> {
            Intent intent = new Intent (LoginScreen.this, ForgotPassword.class);
            isForgot = true;
            LoginScreen.this.startActivity (intent);
        });
        signup.setOnActiveListener(() -> {
            Intent intent = new Intent(LoginScreen.this, OTP_Reg.class);
            isForgot = false;
            startActivity(intent);
        });

    }
    void login(){
        String url = "https://slifixfood.herokuapp.com/login/";
        queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, response -> {
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                DataManager.setAuthToken(obj.getString("token"));
                DataManager.setUserName(obj.getString("user_nam"));
                DataManager.setUserEmail (obj.getString ("user_email"));
                DataManager.setUserImage ("https://slifixfood.herokuapp.com"+obj.getString ("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (DataManager.getAuthToken() != null){
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                finish();
                startActivity(intent);
            }else {
                Toast.makeText(LoginScreen.this, "Check You Phone and Password", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<> ();
                params.put("phone",DataManager.getPhoneNumber());
                params.put("password",t);
                return params;
            }
            @Override
            public Map<String,String> getHeaders() {
                Map<String,String> params = new HashMap<> ();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(req);
    }

}