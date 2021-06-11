package com.slifix.slifix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class createOrder extends AppCompatActivity {
RecyclerView cartMenu;
CardView backBtn,showCart;
TextView restaurantName,cartCount;
public RequestQueue queue;
public StringRequest req;
public JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_create_order);
        backBtn = findViewById (R.id.backBtnCreateOrder);
        showCart = findViewById (R.id.showCartCreateOrder);
        restaurantName = findViewById (R.id.restaurantName);
        cartCount = findViewById (R.id.cartCount);
        loadItems ();
        cartCount.setText (DataManager.getItemsInCart ());
        restaurantName.setText (DataManager.getActiveRestaurantName ());
        backBtn.setOnClickListener (v -> finish ());
        showCart.setOnClickListener (v -> {startActivity (new Intent (getApplicationContext (),ViewCart.class));finish ();});
        try {
            cartMenu = findViewById (R.id.RecyclerViewCartMenu);
            cartMenu.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
            cartMenu.setAdapter (new AdapterAddCartMenu ());
        } catch (Exception e) {
            Log.e("Error ",String.valueOf (e));
        }

    }
    void loadItems(){
        String url = "https://slifixfood.herokuapp.com/get-hotel/";
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        req = new StringRequest(Request.Method.POST, url, response -> {
            try {
                obj = new JSONObject (response);
            } catch (JSONException e) {
                e.printStackTrace();
            }try {
                DataManager.setItemsInCart (obj.getString ("Number of items in cart"));
                cartCount.setText (obj.getString ("Number of items in cart"));
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


    public class AdapterAddCartMenu extends RecyclerView.Adapter<AdapterAddCartMenu.ViewHolder> {

        @NonNull
        @Override
        public AdapterAddCartMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.cart_items,parent,false);
            return new ViewHolder(v);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(AdapterAddCartMenu.ViewHolder holder, int position) {
            holder.itemName.setText (hotelDetails.items.get (position).type+" "+ hotelDetails.items.get (position).name+" : "+hotelDetails.items.get (position).size);
            holder.itemPrice.setText ("Rs."+ hotelDetails.items.get (position).price);
            holder.removeItem.setOnClickListener(v -> {
                holder.q = Integer.parseInt(holder.quantity.getText().toString());
                if (holder.q >= 1){
                    holder.q--;
                    holder.quantity.setText(String.valueOf(holder.q));
                }else{
                    Toast.makeText (getApplicationContext (), "Forbidden", Toast.LENGTH_SHORT).show ();
                }
            });
            holder.addItem.setOnClickListener(v -> {
                    holder.q = Integer.parseInt(holder.quantity.getText().toString());
                    holder.q++;
                    holder.quantity.setText(String.valueOf(holder.q));
            });
            holder.saveToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.q = Integer.parseInt(holder.quantity.getText().toString());
                    holder.itemId = Integer.parseInt(hotelDetails.items.get(position).id);
                    holder.size = hotelDetails.items.get(position).size;
                    if(holder.q <= 0){
                        Toast.makeText (createOrder.this, "Item Quantity Cannot Be 0 or less", Toast.LENGTH_SHORT).show ();
                    }else{
                        saveCart();
                    }
                }

                private void saveCart() {
                    String url = "https://slifixfood.herokuapp.com/add-cart/";
                    holder.queue = VolleySingleton.getInstance(getApplicationContext ()).getRequestQueue();
                    holder.req = new StringRequest (Request.Method.POST, url, response -> {
                        holder.quantity.setText ("0");
                        loadItems ();
                        Toast.makeText (createOrder.this, "Cart Updated", Toast.LENGTH_SHORT).show ();
                    }, error -> {

                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<> ();
                            params.put("phone", DataManager.getPhoneNumber());
                            params.put("item_id",String.valueOf(holder.itemId));
                            params.put("item_size",holder.size);
                            params.put("item_quantity",String.valueOf(holder.q));
                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> params = new HashMap<> ();
                            params.put("Authorization", "Bearer "+DataManager.getAuthToken());
                            return params;
                        }
                    };
                    holder.queue.add(holder.req);
                }
            });
        }

        @Override
        public int getItemCount() {
            return hotelDetails.items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView itemName,itemPrice,addItem,removeItem,quantity,saveToCart;
            int q, itemId;
            String size;
            public RequestQueue queue;
            public StringRequest req;
            public ViewHolder(View itemView) {
                super(itemView);
                saveToCart = itemView.findViewById(R.id.updateCart);
                quantity = itemView.findViewById(R.id.cartFragmentQuantity);
                itemName = itemView.findViewById(R.id.cartFragmentTitle);
                itemPrice = itemView.findViewById(R.id.catrFragmentPrice);
                addItem = itemView.findViewById (R.id.cartFragmentAddBtn);
                removeItem = itemView.findViewById (R.id.cartFragmentRemoveBtn);
            }
        }
    }


}