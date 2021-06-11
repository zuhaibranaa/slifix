package com.slifix.slifix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.slifix.slifix.app.CartItems;
import com.slifix.slifix.app.VolleySingleton;

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
SwipeButton checkoutBtn;
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

        backBtn.setOnClickListener (v -> {
            finish ();
            startActivity (new Intent (getApplicationContext (),createOrder.class));
        });
        checkoutBtn.setOnActiveListener (() -> {
            com.slifix.slifix.ViewCart.this.startActivity (new Intent (com.slifix.slifix.ViewCart.this, Checkout.class));
            com.slifix.slifix.ViewCart.this.finish ();
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
                ViewCart.setAdapter (new AdapterCartItems ());

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


    public class AdapterCartItems extends RecyclerView.Adapter<AdapterCartItems.ViewHolder> {

        @NonNull
        @Override
        public AdapterCartItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.view_cart_items,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(AdapterCartItems.ViewHolder holder, int position) {
            holder.itemName.setText (cartItemsArrayList.get (position).type+" "+cartItemsArrayList.get (position).size);
            holder.quantity.setText (cartItemsArrayList.get (position).quantity);
            holder.itemPrice.setText ("Rs."+cartItemsArrayList.get (position).price);
            holder.deleteItem.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    deleteItem();
                }

                private void deleteItem() {
                    String url = "https://slifixfood.herokuapp.com/remove-cart/";
                    holder.queue = VolleySingleton.getInstance(getApplicationContext ()).getRequestQueue();
                    holder.req = new StringRequest (Request.Method.POST, url, response -> {
                        JSONObject object = new JSONObject ();
                        try {
                            object = new JSONObject (response);
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                        try {
                            if ((object.getString ("status").equals ("200")))
                                Toast.makeText(getApplicationContext (), "Items Deleted", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                        remove(position);
                    }, error -> {

                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String,String> ();
                            params.put("phone", DataManager.getPhoneNumber());
                            params.put("item_id",String.valueOf(cartItemsArrayList.get (position).id));
                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();
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
            if (cartItemsArrayList.size () == 0){
                Toast.makeText (ViewCart.this, "No Item In Cart", Toast.LENGTH_SHORT).show ();
                finish ();
            }
            return cartItemsArrayList.size();
        }
        public void remove(int position){
            cartItemsArrayList.remove (position);
            notifyItemRemoved (position);
            notifyDataSetChanged();
            recreate ();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView itemName,itemPrice,quantity;
            ImageView deleteItem;
            RequestQueue queue;
            StringRequest req;
            public ViewHolder(View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.cartViewItemFragmentTitle);
                itemPrice = itemView.findViewById(R.id.cartViewFragmentPrice);
                quantity = itemView.findViewById(R.id.cartViewFragmentQuantity);
                deleteItem = itemView.findViewById (R.id.deleteButton);

            }
        }
    }

}