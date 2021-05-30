package com.slifix.slifix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.login.LoginScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class dashboard extends AppCompatActivity {
    SwipeButton logout;
    TextView txt;
    public RequestQueue queue;
    public StringRequest req;
    public JSONObject obj;
    CardView menu,foodIco;
    int time;
    public static String gndr,categories;
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        foodIco = findViewById(R.id.FoodCard);
        logout = findViewById(R.id.logout);
        menu = findViewById(R.id.MenuButton);
        txt = findViewById(R.id.good_morning__shaker);
        getUData();
        date = new Date();
        time = parseInt(sdf1.format(date.getTime()));
        menu.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SidebarMenu.class)));
        logout.setOnActiveListener(() -> {
            DataManager.setAuthToken(null);
            Intent it = new Intent(getApplicationContext(), LoginScreen.class);
            finish();
            startActivity(it);
        });
        foodIco.setOnClickListener(v -> {
            Intent map = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(map);
        });
    }

    void getUData(){
        String url = "https://slifixfood.herokuapp.com/get-categories/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, response -> {
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }try {
                DataManager.setUserName(obj.getString("username"));
                gndr = obj.getString("gender");
                categories = obj.getString("Category");
                DataManager.setUserID (obj.getString ("Customer-Id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (DataManager.getUserName() != null){
                if (time >= 0 && time <= 11) {
                    txt.setText("Good Morning " + DataManager.getUserName());
                }else if (time >= 11 && time <= 18){
                    txt.setText("Good Afternoon " + DataManager.getUserName());
                }else if (time >= 18 && time <= 22){
                    txt.setText("Good Evening " + DataManager.getUserName());
                }else {
                    txt.setText("Good Night " + DataManager.getUserName());
                }
            }else{
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }, error -> {

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<> ();
                params.put("phone", DataManager.getPhoneNumber());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<> ();
                params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                return params;
            }
        };
        queue.add(req);
    }
    }