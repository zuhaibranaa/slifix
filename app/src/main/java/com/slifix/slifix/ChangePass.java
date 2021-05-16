package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.login.SetName;
import com.slifix.slifix.login.createFirstTimePassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.slifix.slifix.login.LoginScreen.isForgot;

public class ChangePass extends AppCompatActivity {
    SwipeButton btn;
    EditText pass,confirmpass;
    JSONObject res;
    RequestQueue queue;
    StringRequest req;
    String v1;
    String v2;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        btn = findViewById(R.id.changeNewPassword);
        pass = findViewById(R.id.changePass);
        confirmpass = findViewById(R.id.confirmChangePass);

            btn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    v1=pass.getText().toString();
                    v2= confirmpass.getText().toString();
                    if(v1.equals(v2)){
                        changePass(v1);
                    }else{
                        Toast.makeText(getApplicationContext(), "Passwords Mismatch", Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }

    private void changePass(String pwd) {
        String url = "https://slifixfood.herokuapp.com/edit-profile/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (Integer.parseInt(response) == 200){
                    Toast.makeText(ChangePass.this, "Password Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                    finish();
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
                params.put("phone",DataManager.getPhoneNumber());
                params.put("password",pwd);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " +DataManager.getAuthToken());
                return params;
            }
        };
        queue.add(req);
    }

}