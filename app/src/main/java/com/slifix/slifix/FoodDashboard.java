package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.slifix.slifix.app.AdapterHotels;
import com.slifix.slifix.app.AdapterSmall;
import com.slifix.slifix.app.AdapterSquare;
import com.slifix.slifix.app.Restaurants;
import com.slifix.slifix.app.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDashboard extends AppCompatActivity {
    ImageView activityImage;
    RecyclerView recyclerViewsm,recyclerViewHotels,recyclerViewSquare;
    AdapterHotels adapterHotels;
    RequestQueue queue;
    JsonArrayRequest req;
    ArrayList<Restaurants> restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_dashboard);
        activityImage = findViewById(R.id.menuImage);
        Intent it = new Intent(this,UserProfile.class);
        activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
            }
        });
        //Food Types
//        recyclerViewsm = (RecyclerView) findViewById(R.id.smallItems);
//        layoutManagersm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewsm.setLayoutManager(layoutManagersm);
//        List<String> input = new ArrayList<>();
//        for (int i = 0; i < 4 ; i++){
//            input.add("Item Number "+i);
//        }
//        smAdapter = new AdapterSmall(input);
//        recyclerViewsm.setAdapter(smAdapter);

        //Square Items
//        recyclerViewSquare = (RecyclerView) findViewById(R.id.offers);
//        layoutManagerSquare = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewSquare.setLayoutManager(layoutManagerSquare);
//        List<String> inp = new ArrayList<>();
//        for (int i = 0; i < 4 ; i++){
//            inp.add("Item Number "+i);
//        }
//        adapterSquare = new AdapterSquare(inp);
//        recyclerViewSquare.setAdapter(adapterSquare);

        //All Restaurants
        recyclerViewHotels = (RecyclerView) findViewById(R.id.restaurants);
        restaurant = new ArrayList<>();
        getLoc();
    }
    void getLoc(){
        String url ="https://slifixfood.herokuapp.com/get-location/?longitude="+DataManager.getUserLongitude()+"&latitude="+DataManager.getUserLatitude();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new JsonArrayRequest( Request.Method.GET, url, null, response -> {
            for (int i = 0 ; i <= response.length() ; i++){
                try {
                    JSONObject restaurantObj = response.getJSONObject(i);
                    Restaurants res = new Restaurants();
                    res.setId(restaurantObj.getString("id"));
                    res.setRadius(restaurantObj.getString("Radius"));
                    res.setLongitude(restaurantObj.getString("longitude"));
                    res.setLatitude(restaurantObj.getString("latitude"));
                    res.setName(restaurantObj.getString("name"));
                    res.setPhone(restaurantObj.getString("phone"));
                    res.setAddress(restaurantObj.getString("address"));
                    res.setDelivery_rider(restaurantObj.getString("Delivery_rider"));
                    res.setStarting(restaurantObj.getString("starting"));
                    res.setClosing(restaurantObj.getString("closing"));
                    res.setActive(restaurantObj.getString("active"));
                    res.setUser_id(restaurantObj.getString("user_id"));
                    res.setStatus(restaurantObj.getString("status"));
                    res.setRoles_id(restaurantObj.getString("Roles_id"));
                    res.setMinimum_value(restaurantObj.getString("minimum_value"));
                    res.setDelivery_fee(restaurantObj.getString("delivery_fee"));
                    res.setImage(restaurantObj.getString("Image"));
                    res.setDelivery_time(restaurantObj.getString("delivery_time"));
                    res.setCompany_category(restaurantObj.getString("company_category"));
                    restaurant.add(res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerViewHotels.setLayoutManager(new LinearLayoutManager(this));
                adapterHotels = new AdapterHotels(restaurant,getApplicationContext());
                recyclerViewHotels.setAdapter(adapterHotels);
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(FoodDashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                return params;
            }
        };
        queue.add(req);
    }
}