package com.slifix.slifix.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;
import com.slifix.slifix.createOrder;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterHotelMenu extends RecyclerView.Adapter<AdapterHotelMenu.ViewHolder> {
    public ArrayList<itemsMenu> data;
    Context ctx;
    public AdapterHotelMenu(ArrayList<itemsMenu> input , Context context) {
        this.data = input;
        this.ctx = context;
    }

    @NonNull
    @Override
    public AdapterHotelMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.itemselector,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterHotelMenu.ViewHolder holder, int position) {
        holder.itemName.setText (data.get (position).name);
        holder.itemCategory.setText (data.get (position).type+" : "+data.get (position).size);
        holder.itemPrice.setText ("Rs."+data.get (position).price);
        holder.itemLayout.setOnClickListener (v -> {
            Intent intent = new Intent (ctx,createOrder.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,itemPrice,itemCategory;
        RelativeLayout itemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.menuItemSelectorName);
            itemPrice = itemView.findViewById(R.id.menuItemSelectorValue);
            itemCategory = itemView.findViewById (R.id.menuItemSelectorCategory);
            itemLayout = itemView.findViewById (R.id.itemLayout);
        }
    }
}
