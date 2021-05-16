package com.slifix.slifix.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterHotels extends RecyclerView.Adapter<AdapterHotels.ViewHolder> {
    public List<Restaurants> data;
    Context ctx;
    public AdapterHotels(List<Restaurants> input) {
        this.data = input;
        Toast.makeText(ctx, this.data.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public AdapterHotels.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.restaurants,parent,false);
        AdapterHotels.ViewHolder viewHolder = new AdapterHotels.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterHotels.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txt;
        public ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.smtxt);
            img = (ImageView) itemView.findViewById(R.id.smimg);
        }
    }
}
