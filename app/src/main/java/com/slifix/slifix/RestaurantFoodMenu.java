package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RestaurantFoodMenu extends AppCompatActivity {
    Button btnSave;
    EditText name,email;
    TextView phone;
    ImageView backBtn;
    RequestQueue queue;
    StringRequest req;
    JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_food_menu);
        btnSave = findViewById(R.id._save_);
        name = findViewById(R.id.shaker_muhammed__);
        email = findViewById(R.id.slifix_gmail_com_);
        phone = findViewById(R.id.numString);
        backBtn = findViewById(R.id.icon_ionic_ios_arrow_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name.setText(DataManager.getUserName());
        phone.setText(DataManager.getPhoneNumber());
        email.setText(DataManager.getUserEmail());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.setUserName(name.getText().toString());
                DataManager.setUserEmail(email.getText().toString());
                DataManager.setPhoneNumber(phone.getText().toString());
                changeUserData();
            }
        });
    }

    private void changeUserData() {
            String url = "https://slifixfood.herokuapp.com/edit-profile/";
            queue = VolleySingleton.getInstance(this).getRequestQueue();
            req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(RestaurantFoodMenu.this, response, Toast.LENGTH_SHORT).show();
                    if (Integer.parseInt(response) == 200){
                        Toast.makeText(RestaurantFoodMenu.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",String.valueOf(error));
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("name", DataManager.getUserName());
                    params.put("phone", DataManager.getPhoneNumber());
                    params.put("email", DataManager.getUserEmail());
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                    return params;
                }
            };
            queue.add(req);
    }
}