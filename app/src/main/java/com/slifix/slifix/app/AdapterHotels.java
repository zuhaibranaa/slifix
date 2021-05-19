package com.slifix.slifix.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.DataManager;
import com.slifix.slifix.R;
import com.slifix.slifix.hotelDetails;

import java.util.ArrayList;
import java.util.List;

public class AdapterHotels extends RecyclerView.Adapter<AdapterHotels.ViewHolder> {
    public ArrayList<Restaurants> data;
    Context ctx;
    public AdapterHotels(ArrayList<Restaurants> input , Context context) {
        this.data = input;
        this.ctx = context;
    }

    @Override
    public AdapterHotels.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.restaurants,parent,false);
        AdapterHotels.ViewHolder viewHolder = new AdapterHotels.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterHotels.ViewHolder holder, int position) {
        holder.restaurantName.setText(data.get(position).getName());
        holder.minOrder.setText("Minimum Rs."+data.get(position).getMinimum_value());
        holder.deliveryFee.setText("Delivery Fee Rs."+data.get(position).getDelivery_fee());
        holder.singleRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.setActiveRestaurantId(data.get(position).getId());
                ctx.startActivity(new Intent(ctx,hotelDetails.class));
            }
        });
    }

    @Override
    public int getItemCount() {
//        return 1;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView restaurantName,minOrder,deliveryFee,distance;
        CardView singleRestaurant;
//        public ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurantTitle);
            minOrder = (TextView) itemView.findViewById(R.id.restaurnatMinOrder);
            deliveryFee = (TextView) itemView.findViewById(R.id.restaurantDeliveryFee);
            distance = (TextView) itemView.findViewById(R.id.restaurantDistance);
            singleRestaurant = (CardView) itemView.findViewById(R.id.singleRestaurant);
//            img = (ImageView) itemView.findViewById(R.id.smimg);
        }
    }
}
