package com.slifix.slifix;

import android.content.Intent;
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
import com.slifix.slifix.app.AdapterCartItems;
import com.slifix.slifix.app.CartItems;
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

public class ViewCart extends AppCompatActivity {
CardView backBtn;
ImageView checkoutBtn;
TextView totalBillTop,totalBillBottom,billWithoutDeliveryFee,deliveryFee;
RequestQueue queue;
StringRequest req;
JSONObject obj,obj3;
JSONObject obj1;
ArrayList<CartItems> cartItemsArrayList;
RecyclerView ViewCart;
List<String> cartItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_view_cart);
        backBtn = findViewById (R.id.backBtnViewCart);
        totalBillTop = findViewById (R.id.totalBillTopSide);
        totalBillBottom = findViewById (R.id.totalBillBottomSide);
        checkoutBtn = findViewById (R.id.checkOutBtn);
        billWithoutDeliveryFee = findViewById (R.id.billWithoutDeliveryFee);
        ViewCart = findViewById (R.id.viewCartRV);
        deliveryFee = findViewById (R.id.deliveryFee);
        getData();

        backBtn.setOnClickListener (v -> finish ());
        checkoutBtn.setOnClickListener (v -> {
            startActivity (new Intent (this,Checkout.class));
            finish ();
        });
    }

    void getData(){
        String url = "https://slifixfood.herokuapp.com/cart/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest (Request.Method.POST, url, response -> {
            try {
                obj = new JSONObject (response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                DataManager.setBill(obj.getString ("Bill"));
                totalBillBottom.setText ("Rs."+DataManager.getBill ());
                totalBillTop.setText ("Rs."+obj.getString ("Bill"));
                deliveryFee.setText ("Rs."+obj.getString ("Delivery Fee"));
                int noDeliveryBill = Integer.parseInt (obj.getString ("Bill")) - Integer.parseInt (obj.getString ("Delivery Fee"));
                billWithoutDeliveryFee.setText ("Rs."+ noDeliveryBill);

                JSONArray arr= new JSONArray(obj.getString("Items"));

                obj1 = new JSONObject ();

                //Extract Keys From Menu

                for (int i = 0 ; i < arr.length(); i++){
                    obj1 = new  JSONObject(String.valueOf(arr.get(i)));
                    Iterator<String> keyList = obj1.keys();
                    while (keyList.hasNext()) {
                        cartItems.add(String.valueOf(keyList.next()));
                    }
                }
                cartItemsArrayList = new ArrayList<> ();
                for (int j = 0; j < cartItems.size() ; j++){
                    try {
                        String itemName = String.valueOf (cartItems.get (j));
                            obj3 = new JSONObject (obj1.getString (itemName));
                            CartItems item = new CartItems ();
                            item.setId (obj3.getString ("ID"));
                            item.setType (obj3.getString ("type"));
                            item.setPrice (obj3.getString ("Price"));
                            item.setSize (obj3.getString ("Size"));
                            item.setQuantity (obj3.getString ("Quantity"));
                            cartItemsArrayList.add (item);
                    } catch (JSONException e) {
                        Log.e("Error ", String.valueOf(e));
                    }
                }
                ViewCart.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
                ViewCart.setAdapter (new AdapterCartItems (cartItemsArrayList,getApplicationContext ()));

            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }, error -> {

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("phone", DataManager.getPhoneNumber());
                params.put("hotel_id",DataManager.getActiveRestaurantId());
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