package com.slifix.slifix;

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

public class createOrder extends AppCompatActivity {
RecyclerView cartMenu;
CardView backbtn,showCart;
TextView restaurantName,cartCount;
public RequestQueue queue;
public StringRequest req;
public JSONObject obj,obj3;
public JSONObject[] obj1;
RecyclerView hotelMenu;
List<String> menuItems = new ArrayList<>();
public static ArrayList<itemsMenu> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_create_order);
        backbtn = findViewById (R.id.backBtnCreateOrder);
        showCart = findViewById (R.id.showCartCreateOrder);
        restaurantName = findViewById (R.id.restaurantName);
        cartCount = findViewById (R.id.cartCount);
        loadItems ();
        cartCount.setText (DataManager.getItemsInCart ());
        restaurantName.setText (DataManager.getActiveRestaurantName ());
        backbtn.setOnClickListener (v -> finish ());
        showCart.setOnClickListener (v -> startActivity (new Intent (getApplicationContext (),ViewCart.class)));
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
                Toast.makeText (this, DataManager.getItemsInCart (), Toast.LENGTH_SHORT).show ();
                JSONArray arr= new JSONArray(obj.getString("CART-Items"));

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


    public class AdapterAddCartMenu extends RecyclerView.Adapter<AdapterAddCartMenu.ViewHolder> {

        @NonNull
        @Override
        public AdapterAddCartMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.cart_items,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(AdapterAddCartMenu.ViewHolder holder, int position) {
            holder.itemName.setText (hotelDetails.items.get (position).type+" "+ hotelDetails.items.get (position).name+" : "+hotelDetails.items.get (position).size);
            holder.itemPrice.setText ("Rs."+ hotelDetails.items.get (position).price);
            holder.removeItem.setOnClickListener(v -> {
                holder.q = Integer.parseInt(holder.quantity.getText().toString());
                if (holder.q > 1){
                    holder.q--;
                    holder.quantity.setText(String.valueOf(holder.q));
                }else{
                    Toast.makeText (getApplicationContext (), "Forbidden", Toast.LENGTH_SHORT).show ();
                }
            });
            holder.addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.q = Integer.parseInt(holder.quantity.getText().toString());
                    holder.q++;
                    holder.quantity.setText(String.valueOf(holder.q));
                }
            });
            holder.saveToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.q = Integer.parseInt(holder.quantity.getText().toString());
                    holder.itemId = Integer.parseInt(hotelDetails.items.get(position).id);
                    holder.size = hotelDetails.items.get(position).size;
                    saveCart();
                }

                private void saveCart() {
                    String url = "https://slifixfood.herokuapp.com/add-cart/";
                    holder.queue = VolleySingleton.getInstance(getApplicationContext ()).getRequestQueue();
                    holder.req = new StringRequest (Request.Method.POST, url, response -> {
                        String jsonString =response ;
                        Toast.makeText(getApplicationContext (), "Cart Updated", Toast.LENGTH_SHORT).show();
                        holder.quantity.setText ("1");
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