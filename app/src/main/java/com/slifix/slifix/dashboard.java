package com.slifix.slifix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.OnActiveListener;
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
    int time;
    public static String uName,gndr,categories;
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (DataManager.getAuth() == null){
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            finish();
        }
        CardView foodIco = (CardView) findViewById(R.id.FoodCard);
        logout = (SwipeButton) findViewById(R.id.logout);
        txt = (TextView) findViewById(R.id.good_morning__shaker);
        getUData();
        date = new Date();
        time = parseInt(sdf1.format(date.getTime()));
        logout.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                DataManager.setAuthToken(null);
                Intent it = new Intent(getApplicationContext(), LoginScreen.class);
                finish();
                startActivity(it);
            }
        });
        foodIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(it);
            }
        });
    }

    void getUData(){
        String url = "https://slifixfood.herokuapp.com/get-categories/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString =response ;
                try {
                    obj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }try {
                    uName = obj.getString("username");
                    gndr = obj.getString("gender");
                    categories = obj.getString("Category");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (uName != null){
                    if (time >= 0 && time <= 11) {
                        txt.setText("Good Morning " + uName);
                    }else if (time >= 11 && time <= 18){
                        txt.setText("Good Afternoon " + uName);
                    }else if (time >= 18 && time <= 22){
                        txt.setText("Good Evening " + uName);
                    }else {
                        txt.setText("Good Night " + uName);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
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
                params.put("phone", DataManager.getPhoneNumber());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+DataManager.getAuth());
                return params;
            }
        };
        queue.add(req);
    }
    }