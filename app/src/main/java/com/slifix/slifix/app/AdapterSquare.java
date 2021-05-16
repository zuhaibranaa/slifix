package com.slifix.slifix.app;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;

import java.util.List;

public class AdapterSquare extends RecyclerView.Adapter<AdapterSquare.ViewHolder> {
    public List<String> data;
    public AdapterSquare(List<String> input) {
        data = input;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.squareitems,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterSquare.ViewHolder holder, int position) {

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
