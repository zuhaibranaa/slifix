package com.slifix.slifix;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.app.dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginScreen extends AppCompatActivity {
    TextView forgot;
    EditText number,pass;
    public static String n,p,t;
    SwipeButton login,signup;
    public String authToken;
    public RequestQueue queue;
    public StringRequest req;
    public JSONObject obj;
    public static boolean isForgot;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        number = (EditText) findViewById(R.id.number);
        pass = (EditText) findViewById(R.id.input_pass);
        signup = (SwipeButton) findViewById(R.id.signup);
        login = (SwipeButton) findViewById(R.id.createPass);
        forgot = (TextView) findViewById(R.id.forgot);
        login.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                n = "+" + number.getText().toString();
                t = pass.getText().toString();
                login();
            }
        });
        forgot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LoginScreen.this,ForgotPassword.class);
                isForgot = true;
                startActivity(intent);
                return false;
            }
        });
        signup.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                Intent intent = new Intent(LoginScreen.this,OTP_Reg.class);
                isForgot = false;
                startActivity(intent);
            }
        });

    }
    void login(){
        String url = "https://slifixfood.herokuapp.com/login/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, response -> {
            String jsonString =response ; //assign your JSON String here
            try {
                obj = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                authToken = obj.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (authToken != null){
                prefs=this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                edit=prefs.edit();
                edit.putString("token",authToken);
                edit.commit();
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginScreen.this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("phone",n);
                params.put("password",t);
                return params;
            }
            @Override
            public Map<String,String> getHeaders() {
                Map<String,String> params = new HashMap<String,String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(req);
    }
}