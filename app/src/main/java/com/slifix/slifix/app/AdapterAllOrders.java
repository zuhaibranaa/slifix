package com.slifix.slifix.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;

import java.util.ArrayList;

public class AdapterAllOrders extends RecyclerView.Adapter<AdapterAllOrders.ViewHolder> {
    public ArrayList<Orders> data;
    Context ctx;

    public AdapterAllOrders(ArrayList<Orders> input , Context context) {
        this.data = input;
        this.ctx = context;
    }

    @NonNull
    @Override
    public AdapterAllOrders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.all_orders,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllOrders.ViewHolder holder, int position) {
        holder.orderNumber.setText ("Order : # "+data.get (position).order);
        holder.orderDetails.setText ("Total Bill : "+data.get (position).bill);
        holder.orderDate.setText ("Order Date : "+data.get (position).date);

    }

    @Override
    public int getItemCount() {
//        return 1;
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView orderDate,orderDetails,orderNumber;
        ImageView orderImage;
        public ViewHolder(View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            orderNumber = itemView.findViewById (R.id.orderNumber);
            orderImage = itemView.findViewById (R.id.orderImage);
        }
    }
}
