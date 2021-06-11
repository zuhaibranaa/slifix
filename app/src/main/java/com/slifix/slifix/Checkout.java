package com.slifix.slifix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.app.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {
ImageView btnPaymentSelector,editAddress,saveAddress;
SwipeButton placeOrder;
CardView backBtn;
EditText address;
TextView location,totalBill;
RequestQueue queue;
StringRequest req;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_checkout);
        btnPaymentSelector = findViewById (R.id.ic_edit_24px_ek1);
        backBtn = findViewById (R.id.backBtnCheckout);
        saveAddress = findViewById (R.id.saveAddress);
        address = findViewById (R.id.etAddress);
        placeOrder = findViewById (R.id.PlaceOrderBtn);
        editAddress = findViewById (R.id.addressEdit);
        location = findViewById (R.id.shaker_shaker_no_123__sub_street___kharian__gurjat__punjab__pakistan);
        totalBill  =findViewById (R.id.totalBillCheckout);
        location.setText (DataManager.getUserLocation ());
        totalBill.setText ("Rs."+DataManager.getBill ());
        backBtn.setOnClickListener (v -> finish ());
        address.setText (location.getText ());

        editAddress.setOnClickListener (v-> {
            address.setVisibility (View.VISIBLE);
            location.setVisibility (View.GONE);
            editAddress.setVisibility (View.GONE);
            saveAddress.setVisibility (View.VISIBLE);
        });
        saveAddress.setOnClickListener (v -> {
            address.setVisibility (View.GONE);
            location.setVisibility (View.VISIBLE);
            editAddress.setVisibility (View.VISIBLE);
            saveAddress.setVisibility (View.GONE);
            location.setText (address.getText ());
        });



        btnPaymentSelector.setOnClickListener (v -> {
            PopupMenu popupMenu = new PopupMenu (getApplicationContext (),btnPaymentSelector);
            popupMenu.getMenuInflater ().inflate (R.menu.payment_selector,popupMenu.getMenu ());
            popupMenu.setOnMenuItemClickListener (item -> {
                Toast.makeText (Checkout.this, item.getTitle (), Toast.LENGTH_SHORT).show ();
                return false;
            });
            popupMenu.show ();
        });
        placeOrder.setOnActiveListener (Checkout.this::sendPlaceOrderRequestToServer);
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