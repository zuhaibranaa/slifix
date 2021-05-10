package com.slifix.slifix.app;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slifix.slifix.R;

import java.util.List;

public class AdapterSmall extends RecyclerView.Adapter<AdapterSmall.ViewHolder> {
    public List<String> data;
    public AdapterSmall(List<String> input) {
        data = input;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterSmall.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
