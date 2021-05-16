package com.slifix.slifix.login;

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
import com.slifix.slifix.R;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.DataManager;
import com.slifix.slifix.dashboard;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static com.slifix.slifix.login.LoginScreen.isForgot;

public class createFirstTimePassword extends AppCompatActivity {
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
        setContentView(R.layout.activity_create_first_time_password);
        btn =(SwipeButton) findViewById(R.id.createftp);
        pass = (EditText) findViewById(R.id.newpassword);
        confirmpass = (EditText) findViewById(R.id.confirmPass);

        if (DataManager.getAuthToken() == null){
            Toast.makeText(this, "User Not Authenticated", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            btn.setOnActiveListener(new OnActiveListener() {
                @Override
                public void onActive() {
                    v1=pass.getText().toString();
                    v2= confirmpass.getText().toString();
                    if(v1.equals(v2)){
                        createPass(v1);
                    }else{
                        Toast.makeText(createFirstTimePassword.this, "Passwords Mismatch", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private void createPass(String pwd) {
        String url = "https://slifixfood.herokuapp.com/set_password/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString =response ;
                try {
                    res = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }try {
                     status = res.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status == 200){
                    Toast.makeText(createFirstTimePassword.this, "Password Saved", Toast.LENGTH_SHORT).show();
                    Intent intent;
                    if (!isForgot) {
                        intent = new Intent(getApplicationContext(), SetName.class);
                    }else{
                        intent =  new Intent(getApplicationContext(), dashboard.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(createFirstTimePassword.this, "Error Occured", Toast.LENGTH_SHORT).show();
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
                params.put("phone","+"+ DataManager.getPhoneNumber());
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