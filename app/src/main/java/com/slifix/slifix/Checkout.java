package com.slifix.slifix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.app.AdapterHotelMenu;
import com.slifix.slifix.app.VolleySingleton;
import com.slifix.slifix.app.itemsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

public class Checkout extends AppCompatActivity {
ImageView btnPaymentSelector,placeOrder;
CardView backBtn;
TextView location,totalBill;
RequestQueue queue;
StringRequest req;
    List<Address> loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_checkout);
        btnPaymentSelector = findViewById (R.id.ic_edit_24px_ek1);
        backBtn = findViewById (R.id.backBtnCheckout);
        placeOrder = findViewById (R.id.PlaceOrderBtn);
        location = findViewById (R.id.shaker_shaker_no_123__sub_street___kharian__gurjat__punjab__pakistan);
        totalBill  =findViewById (R.id.totalBillCheckout);
        location.setText (DataManager.getUserLocation ());
        totalBill.setText ("Rs."+DataManager.getBill ());
        backBtn.setOnClickListener (v -> {finish ();});
        btnPaymentSelector.setOnClickListener (v -> {
            PopupMenu popupMenu = new PopupMenu (getApplicationContext (),btnPaymentSelector);
            popupMenu.getMenuInflater ().inflate (R.menu.payment_selector,popupMenu.getMenu ());
            popupMenu.setOnMenuItemClickListener (item -> {
                Toast.makeText (Checkout.this, item.getTitle (), Toast.LENGTH_SHORT).show ();
                return false;
            });
            popupMenu.show ();
        });
        placeOrder.setOnClickListener (v -> {
            sendPlaceOrderRequestToServer();
        });
    }

    private void sendPlaceOrderRequestToServer() {
            String url = "https://slifixfood.herokuapp.com/check-out/";
            queue = VolleySingleton.getInstance(this).getRequestQueue();
            req = new StringRequest (Request.Method.POST, url, response -> {
                if(response.equals ("200")){
                    startActivity (new Intent (getApplicationContext (),OrderPlacedScreen.class));
                    finish ();
                }

            }, error -> {

            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<> ();
                    params.put("latitude",DataManager.getUserLatitude ());
                    params.put("longitude",DataManager.getUserLongitude ());
                    params.put("payment_method","on_delivery");
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