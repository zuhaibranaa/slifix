package com.slifix.slifix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.app.AdapterHotelMenu;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.app.itemsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class hotelDetails extends AppCompatActivity {
CardView bckbtn;
public RequestQueue queue;
public StringRequest req;
public JSONObject obj,obj2,obj3;
public JSONObject[] obj1;
RecyclerView hotelMenu;
List<String> menuItems = new ArrayList<String>();
ArrayList<itemsMenu> items;
RecyclerView restaurantMenuItems;
TextView restaurantName,status,statusTime,deliveryFee,deliveryTime;
ImageView homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);
        if (DataManager.getActiveRestaurantId() == null){
            Toast.makeText(this, "No Restaurant Clicked", Toast.LENGTH_SHORT).show();
            finish();
        }
        bckbtn = findViewById(R.id.hoteldetailsbckbtn);
        restaurantName = findViewById(R.id.restaurantName);
        homeBtn = findViewById(R.id.homeBtn);
        status = findViewById(R.id.restaurantActiveStatus);
        statusTime = findViewById(R.id.statusValue);
        deliveryTime = findViewById(R.id.deliveryTime);
        deliveryFee = findViewById(R.id.deliveryFee);
        restaurantMenuItems = findViewById(R.id.restaurantMenuItems);
        loadItems();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    void loadItems(){
        String url = "https://slifixfood.herokuapp.com/get-hotel/";
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
                    restaurantName.setText(obj.getString("name"));
                    statusTime.setText(obj.getString("open Time"));
                    deliveryTime.setText(obj.getString("Delivery Time")+" min");
                    deliveryFee.setText("Delivery Fee Rs."+obj.getString("Delivery Fee"));
                    JSONArray arr= new JSONArray(obj.getString("Menu"));

                    obj1 = new JSONObject[arr.length ()];

                    //Extract Keys From Menu

                    for (int i = 0 ; i < arr.length(); i++){
                        obj1[i] = new  JSONObject(String.valueOf(arr.get(i)));
                        Iterator<String> keyList = obj1[i].keys();
                        do {
                            menuItems.add(String.valueOf(keyList.next()));
                        }while (keyList.hasNext());
                    }
                    items = new ArrayList<> ();
                    hotelMenu = (RecyclerView) findViewById (R.id.restaurantMenuItems);
                    for (int j = 0; j < menuItems.size() ; j++){
                        try {
                            String itemName = String.valueOf (menuItems.get (j));
                            Log.e ("Object",obj1[j].getString (itemName));
                            JSONArray arr1 = new JSONArray (obj1[j].getString (itemName));
                            for (int k = 0 ; k < arr1.length () ; k++){
                                Toast.makeText (hotelDetails.this, arr1.get (k).toString (), Toast.LENGTH_SHORT).show ();
                                obj3 = new JSONObject (arr1.get (k).toString ());
                                itemsMenu item = new itemsMenu ();
                                item.setName (String.valueOf (menuItems.get(j)));
                                item.setId (obj3.getString ("ID"));
                                item.setType (obj3.getString ("Type"));
                                item.setSize (obj3.getString ("size"));
                                item.setPrice (obj3.getString ("Price"));
                                items.add (item);
                            }
                        } catch (JSONException e) {
                            Log.e("Error ",String.valueOf (e)+"s in University");
                        }
                    }
                    hotelMenu.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
                    restaurantMenuItems.setAdapter (new AdapterHotelMenu (items,getApplicationContext ()));

                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("id",DataManager.getActiveRestaurantId());
                params.put("phone", DataManager.getPhoneNumber());
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