package com.slifix.slifix.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;
import com.slifix.slifix.createOrder;

import java.util.ArrayList;

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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,itemPrice,addItem,removeItem,saveToCart;
        CardView singleRestaurant;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.cartFragmentTitle);
            itemPrice = (TextView) itemView.findViewById(R.id.catrFragmentPrice);
            addItem = (TextView) itemView.findViewById (R.id.cartFragmentAddBtn);
            removeItem = (TextView) itemView.findViewById (R.id.cartFragmentRemoveBtn);

        }
    }
}
