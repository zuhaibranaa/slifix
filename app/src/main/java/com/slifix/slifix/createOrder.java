package com.slifix.slifix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.app.AdapterAddCartMenu;

import static com.slifix.slifix.hotelDetails.items;

public class createOrder extends AppCompatActivity {
RecyclerView cartMenu;
CardView backbtn,showCart;
TextView restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_create_order);
        backbtn = findViewById (R.id.backBtnCreateOrder);
        showCart = findViewById (R.id.showCartCreateOrder);
        restaurantName = findViewById (R.id.restaurantName);
        restaurantName.setText (DataManager.getActiveRestaurantName ());
        backbtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        showCart.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (getApplicationContext (),ViewCart.class));
            }
        });
        try {
            cartMenu = findViewById (R.id.RecyclerViewCartMenu);
            cartMenu.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
            cartMenu.setAdapter (new AdapterAddCartMenu (items,getApplicationContext ()));
        } catch (Exception e) {
            Log.e("Error ",String.valueOf (e));
        }

    }
}