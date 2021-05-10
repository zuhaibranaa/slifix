package com.slifix.slifix.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.LoginScreen;
import com.slifix.slifix.MapsActivity;
import com.slifix.slifix.R;
import com.slifix.slifix.VolleySingleton;
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
    String uName;
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        CardView foodIco = (CardView) findViewById(R.id.FoodCard);
        logout = (SwipeButton) findViewById(R.id.logout);
        txt = (TextView) findViewById(R.id.good_morning__shaker);
        getUData();
        date = new Date();
        int time = parseInt(sdf1.format(date.getTime()));
        if (time >= 0 && time <= 11) {
            txt.setText("Good Morning ");
        }else if (time >= 11 && time <= 18){
            txt.setText("Good Afternoon ");
        }else if (time >= 18 && time <= 22){
            txt.setText("Good Evening ");
        }else {
            txt.setText("Good Night ");
        }
        logout.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
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
        req = new StringRequest(Request.Method.POST, url, response -> {
            String jsonString =response ; //assign your JSON String here
            try {
                obj = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                uName = obj.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (uName != null){
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("phone",LoginScreen.n);
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