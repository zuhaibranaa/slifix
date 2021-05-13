package com.slifix.slifix.login;


import android.content.Intent;
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
import com.slifix.slifix.R;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.DataManager;
import com.slifix.slifix.dashboard;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    TextView forgot;
    EditText number,pass;
    public static String p,t;
    SwipeButton login,signup;
    public RequestQueue queue;
    public StringRequest req;
    public JSONObject obj;
    public static boolean isForgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        number = (EditText) findViewById(R.id.number);
        pass = (EditText) findViewById(R.id.input_pass);
        signup = (SwipeButton) findViewById(R.id.signup);
        login = (SwipeButton) findViewById(R.id.createPass);
        forgot = (TextView) findViewById(R.id.forgot);
        if (!(DataManager.getAuth() == null)){
            Intent it = new Intent(getApplicationContext(),dashboard.class);
            startActivity(it);
        }
        login.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                DataManager.setPhoneNumber("+"+number.getText().toString());
                t = pass.getText().toString();
                login();
            }
        });
        forgot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LoginScreen.this, ForgotPassword.class);
                isForgot = true;
                startActivity(intent);
                return false;
            }
        });
        signup.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                Intent intent = new Intent(LoginScreen.this, OTP_Reg.class);
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
                DataManager.setAuthToken(obj.getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (DataManager.getAuth() != null){
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                finish();
                startActivity(intent);
            }else {
                Toast.makeText(LoginScreen.this, "Ooooops! There is an Error ", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("phone",DataManager.getPhoneNumber());
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