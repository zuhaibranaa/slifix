package com.slifix.slifix.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.slifix.slifix.DataManager;
import com.slifix.slifix.R;
import com.slifix.slifix.createOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterAddCartMenu extends RecyclerView.Adapter<AdapterAddCartMenu.ViewHolder> {
    public ArrayList<itemsMenu> data;
    Context ctx;
    public AdapterAddCartMenu(ArrayList<itemsMenu> input , Context context) {
        this.data = input;
        this.ctx = context;
    }

    @Override
    public AdapterAddCartMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.cart_items,parent,false);
        AdapterAddCartMenu.ViewHolder viewHolder = new AdapterAddCartMenu.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterAddCartMenu.ViewHolder holder, int position) {
        holder.itemName.setText (data.get (position).type+" "+data.get (position).name+" : "+data.get (position).size);
        holder.itemPrice.setText ("Rs."+data.get (position).price);
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.q = Integer.parseInt(holder.quantity.getText().toString());
                holder.q--;
                holder.quantity.setText(String.valueOf(holder.q));
                Toast.makeText(ctx, String.valueOf(holder.q), Toast.LENGTH_SHORT).show();
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
                holder.itemid = Integer.parseInt(data.get(position).id);
                holder.size = data.get(position).size;
                saveCart();
            }

            private void saveCart() {
                    String url = "https://slifixfood.herokuapp.com/add-cart/";
                    holder.queue = VolleySingleton.getInstance(ctx).getRequestQueue();
                    holder.req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String jsonString =response ;
                            Toast.makeText(ctx, String.valueOf(response), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String,String>();
                            params.put("phone", DataManager.getPhoneNumber());
                            params.put("item_id",String.valueOf(holder.itemid));
                            params.put("item_size",holder.size);
                            params.put("item_quantity",String.valueOf(holder.q));
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
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,itemPrice,addItem,removeItem,quantity,saveToCart;
        int q,itemid;
        String size;
        public RequestQueue queue;
        public StringRequest req;
        public ViewHolder(View itemView) {
            super(itemView);
            saveToCart = (TextView) itemView.findViewById(R.id.updateCart);
            quantity = (TextView) itemView.findViewById(R.id.cartFragmentQuantity);
            itemName = (TextView) itemView.findViewById(R.id.cartFragmentTitle);
            itemPrice = (TextView) itemView.findViewById(R.id.catrFragmentPrice);
            addItem = (TextView) itemView.findViewById (R.id.cartFragmentAddBtn);
            removeItem = (TextView) itemView.findViewById (R.id.cartFragmentRemoveBtn);

        }
    }
}
