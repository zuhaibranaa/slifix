package com.slifix.slifix;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.slifix.slifix.ForgotPassword.number;

public class enterOTPF extends AppCompatActivity {
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
        txt = otpText.getText().toString();
        txt = txt + number;
        number = "+"+number;
        otpText.setText(txt);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = otpField.getText().toString();
                OTPChange();
            }
        });
    }

    public void OTPChange() {
        String url = "https://slifixfood.herokuapp.com/confirm/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(enterOTPF.this, response, Toast.LENGTH_LONG).show();
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
                if (strJson != null){
                    Intent enterPass = new Intent(getApplicationContext(),createFirstTimePassword.class);
                    startActivity(enterPass);
                }else{
                    Toast.makeText(enterOTPF.this, "Try Again", Toast.LENGTH_SHORT).show();
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
                params.put("phone",number);
                params.put("change","true");
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