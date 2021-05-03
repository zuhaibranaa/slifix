package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.app.dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.slifix.slifix.ForgotPassword.number;
import static com.slifix.slifix.LoginScreen.isForgot;
import static com.slifix.slifix.OTP_Reg.mobile;

public class Sending_OTP extends AppCompatActivity {
    TextView tv,tv6, tv_9_Cancel,tv_10_SendOTP;
    String Message,txt,url;
    RequestQueue queue;
    StringRequest req;
    int status;
    JSONObject res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending__o_t_p);
        tv = (TextView) findViewById(R.id.textView7);
        tv6 = (TextView) findViewById(R.id.textView6);
        tv_9_Cancel = (TextView) findViewById(R.id.textView9);
        tv_10_SendOTP = (TextView) findViewById(R.id.textView10);
        Message= tv.getText().toString();
        if (isForgot == true){
            tv6.setText("Forgot");
            txt = Message+number;
            number = "+"+number;
            url = "https://slifixfood.herokuapp.com/change_password/";
        }else{
            txt = Message+mobile;
            mobile = "+"+mobile;
            url = "https://slifixfood.herokuapp.com/reg/";
        }
        tv.setText(txt);
        tv_9_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_10_SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });
    }
    public void userSignUp() {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, response -> {
            String jsonString =response ; //assign your JSON String here
            try {
                res = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                status = res.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status == 200){
                Intent it = new Intent(this,enterOTP.class);
                startActivity(it);
            }else if (status == 400){
                Toast.makeText(this, "Phone Already Registered Try Login", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(this,LoginScreen.class);
                startActivity(it);
                finish();
            }else{
                Toast.makeText(this, "Internal Server Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                if (isForgot == true){
                    params.put("phone",number);
                }else{
                    params.put("phone",mobile);
                }
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