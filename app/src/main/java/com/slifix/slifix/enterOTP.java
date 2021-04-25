package com.slifix.slifix;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.slifix.slifix.OTP_Reg.mobile;

public class enterOTP extends AppCompatActivity {
    Button submit;
    RequestQueue queue;
    StringRequest req;
    String otp,txt;
    TextView otpText;
    EditText otpField;
    public static String strJson;
    JSONObject res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_p);
        submit = (Button) findViewById(R.id.otpSubmit);
        otpField = (EditText) findViewById(R.id.otpField);
        otpText = (TextView) findViewById(R.id.otpText);
//        mobile = "+"+mobile;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = otpField.getText().toString();
                OTPValidator();
            }
        });
    }

    public void OTPValidator() {
        String url = "https://slifixfood.herokuapp.com/confirm/";
        queue = Volley.newRequestQueue(this);
        req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(enterOTP.this, mobile, Toast.LENGTH_SHORT).show();
                String jsonString =response ;
                try {
                    res = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    strJson = res.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(enterOTP.this, strJson, Toast.LENGTH_SHORT).show();
                if (strJson != null){
                    Intent enterPass = new Intent(getApplicationContext(),createFirstTimePassword.class);
                    startActivity(enterPass);
                }else{
                    Toast.makeText(enterOTP.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("phone",mobile);
                params.put("otp",otp);
                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(req);
    }
}