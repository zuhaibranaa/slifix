package com.slifix.slifix.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.getIntent;

public class AdapterCartItems extends RecyclerView.Adapter<AdapterCartItems.ViewHolder> {
    public ArrayList<CartItems> data;
    Context ctx;
    public AdapterCartItems(ArrayList<CartItems> input , Context context) {
        this.data = input;
        this.ctx = context;
    }

    @Override
    public AdapterCartItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_cart_items,parent,false);
        AdapterCartItems.ViewHolder viewHolder = new AdapterCartItems.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterCartItems.ViewHolder holder, int position) {
        holder.itemName.setText (data.get (position).type+" "+data.get (position).size);
        holder.quantity.setText (data.get (position).quantity);
        holder.itemPrice.setText ("Rs."+data.get (position).price);
        holder.deleteItem.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//                Toast.makeText (ctx, data.get (position).id, Toast.LENGTH_SHORT).show ();
                deleteItem();
            }

            private void deleteItem() {
                String url = "https://slifixfood.herokuapp.com/remove-cart/";
                holder.queue = VolleySingleton.getInstance(ctx).getRequestQueue();
                holder.req = new StringRequest (Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = new JSONObject ();
                        try {
                            object = new JSONObject (response);
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                        try {
                            if ((object.getString ("status").equals ("200")))
                            Toast.makeText(ctx, "Items Deleted", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                        remove(position);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String,String> ();
                        params.put("phone", DataManager.getPhoneNumber());
                        params.put("item_id",String.valueOf(data.get (position).id));
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
//        return 2;
        return data.size();
    }
    public void remove(int position){
        data.remove (position);
        notifyItemRemoved (position);
        notifyDataSetChanged();
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
