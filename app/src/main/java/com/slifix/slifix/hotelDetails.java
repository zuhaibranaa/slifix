package com.slifix.slifix;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
public JSONObject obj,obj3;
public JSONObject[] obj1;
RecyclerView hotelMenu;
List<String> menuItems = new ArrayList<>();
public static ArrayList<itemsMenu> items;
TextView restaurantName,status,statusTime,deliveryFee,deliveryTime;
ImageView homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);
        if (DataManager.getActiveRestaurantId() == null){
            Toast.makeText(this, "No Restaurant Selected", Toast.LENGTH_SHORT).show();
            finish();
        }
        bckbtn = findViewById(R.id.hoteldetailsbckbtn);
        restaurantName = findViewById(R.id.restaurantName);
        homeBtn = findViewById(R.id.homeBtn);
        status = findViewById(R.id.restaurantActiveStatus);
        statusTime = findViewById(R.id.statusValue);
        deliveryTime = findViewById(R.id.deliveryTime);
        deliveryFee = findViewById(R.id.deliveryFee);
        loadItems();

        homeBtn.setOnClickListener(v -> finish());
        bckbtn.setOnClickListener(v -> finish());


    }
    void loadItems(){
        String url = "https://slifixfood.herokuapp.com/get-hotel/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, response -> {
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }try {
                restaurantName.setText(obj.getString("name"));
                DataManager.setActiveRestaurantName (obj.getString ("name"));
                statusTime.setText(obj.getString("open Time"));
                deliveryTime.setText(obj.getString("Delivery Time")+" min");
                deliveryFee.setText("Delivery Fee Rs."+obj.getString("Delivery Fee"));
                DataManager.setItemsInCart (obj.getString ("Number of items in cart"));
                Toast.makeText (this, DataManager.getItemsInCart (), Toast.LENGTH_SHORT).show ();
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
                hotelMenu = findViewById (R.id.restaurantMenuItems);
                for (int j = 0; j < menuItems.size() ; j++){
                    try {
                        String itemName = String.valueOf (menuItems.get (j));
                        JSONArray arr1 = new JSONArray (obj1[j].getString (itemName));
                        for (int k = 0 ; k < arr1.length () ; k++){
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
                        Log.e("Error ", String.valueOf(e));
                    }
                }
                try {
                    hotelMenu.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
                    hotelMenu.setAdapter (new AdapterHotelMenu (items,getApplicationContext ()));
                } catch (Exception e) {
                    e.printStackTrace ();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",DataManager.getActiveRestaurantId());
                params.put("phone", DataManager.getPhoneNumber ());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                return params;
            }
        };
        queue.add(req);
    }

}