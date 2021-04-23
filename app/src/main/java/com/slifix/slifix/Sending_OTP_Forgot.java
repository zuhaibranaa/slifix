package com.slifix.slifix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import static com.slifix.slifix.ForgotPassword.number;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sending_OTP_Forgot extends AppCompatActivity {
    TextView tv, tv_9_Cancel,tv_10_SendOTP;
    String Message,txt;
    RequestQueue queue;
    StringRequest req;
    int status;
    JSONObject res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending__o_t_p);
        tv = (TextView) findViewById(R.id.textView7);
        tv_9_Cancel = (TextView) findViewById(R.id.textView9);
        tv_10_SendOTP = (TextView) findViewById(R.id.textView10);
        Message= tv.getText().toString();
        txt = Message+number;
        number = "+"+number;
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
                Toast.makeText(Sending_OTP_Forgot.this, number, Toast.LENGTH_SHORT).show();
                userSignUp();
            }
        });
    }
    public void userSignUp() {
        String url = "https://slifixfood.herokuapp.com/change_password/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString =response ;
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
                    Toast.makeText(getApplicationContext(), "Enter OTP",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),enterOTPF.class);
                    startActivity(intent);
                    finish();
                }else if(status == 500){
                    Toast.makeText(Sending_OTP_Forgot.this, "No Record Found Against This Number", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Sending_OTP_Forgot.this, "Something Gone Wrong", Toast.LENGTH_SHORT).show();
                }
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