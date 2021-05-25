package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderPlacedScreen extends AppCompatActivity {
    TextView myOrders;
    ImageView crossBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed_screen);
        myOrders = findViewById (R.id.myOrdersButton);
        crossBtn = findViewById (R.id.crossButton);
        crossBtn.setOnClickListener (v -> {
            finish ();
        });
        myOrders.setOnClickListener (v -> {
            startActivity (new Intent (getApplicationContext (),AllOrders.class));
            finish ();
        });
    }
}