package com.slifix.slifix.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;

import java.util.ArrayList;

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
        holder.itemName.setText (data.get (position).type+" "+data.get (position).name+" "+data.get (position).size);
        holder.quantity.setText (data.get (position).quantity);
        holder.itemPrice.setText ("Rs."+data.get (position).price);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView itemName,itemPrice,quantity;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.cartViewItemFragmentTitle);
            itemPrice = (TextView) itemView.findViewById(R.id.cartViewFragmentPrice);
            quantity = (TextView) itemView.findViewById(R.id.cartViewFragmentQuantity);

        }
    }
}
