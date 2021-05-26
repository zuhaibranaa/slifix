package com.slifix.slifix.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    }

    @Override
    public int getItemCount() {
//        return 1;
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
