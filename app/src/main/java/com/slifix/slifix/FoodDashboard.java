package com.slifix.slifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.slifix.slifix.app.AdapterSmall;

import java.util.ArrayList;
import java.util.List;

public class FoodDashboard extends AppCompatActivity {
    RecyclerView recyclerViewsm;
    RecyclerView.LayoutManager layoutManagersm;
    RecyclerView.Adapter smAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_dashboard);
        recyclerViewsm = (RecyclerView) findViewById(R.id.smallItems);
        layoutManagersm = new LinearLayoutManager(this);
        recyclerViewsm.setLayoutManager(layoutManagersm);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 10 ; i++){
           input.add("Item Number "+i);
        }
        smAdapter = new AdapterSmall(input);
        recyclerViewsm.setAdapter(smAdapter);
    }
}