package com.slifix.slifix;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.app.AdapterAllOrders;
import com.slifix.slifix.app.Orders;
import com.slifix.slifix.app.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllOrders extends AppCompatActivity {
CardView backBtn;
RequestQueue queue;
StringRequest req;
JSONObject obj,obj1;
RecyclerView allOrders;
ArrayList<Orders> allOrdersArrayList = new ArrayList<> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_all_orders);
        backBtn = findViewById (R.id.backBtnMyOrders);
        allOrders = findViewById (R.id.AllOrders);
        getData ();
        backBtn.setOnClickListener (v -> finish ());

    }
    void getData(){
        String url = "https://slifixfood.herokuapp.com/all-orders/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest (Request.Method.POST, url, response -> {
            try {
                obj = new JSONObject (response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray arr= new JSONArray(obj.getString("All Orders"));

                obj1 = new JSONObject ();
                for (int i = 0 ; i < arr.length(); i++){
                    obj1 = new  JSONObject(String.valueOf(arr.get(i)));
                    Orders order = new Orders ();
                    order.setBill(obj1.getString ("Bill"));
                    order.setTime(obj1.getString ("Time"));
                    order.setDate(obj1.getString ("Date"));
                    order.setOrder(obj1.getString ("Order No: "));
                    allOrdersArrayList.add (order);
                }

                allOrders.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
                allOrders.setAdapter (new AdapterAllOrders (allOrdersArrayList,getApplicationContext ()));

            } catch (JSONException e) {
                e.printStackTrace ();
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
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                return params;
            }
        };
        queue.add(req);
    }

}