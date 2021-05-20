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

public class AdapterHotelMenu extends RecyclerView.Adapter<AdapterHotelMenu.ViewHolder> {
    public ArrayList<itemsMenu> data;
    Context ctx;
    public AdapterHotelMenu(ArrayList<itemsMenu> input , Context context) {
        this.data = input;
        this.ctx = context;
    }

    @Override
    public AdapterHotelMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.itemselector,parent,false);
        AdapterHotelMenu.ViewHolder viewHolder = new AdapterHotelMenu.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterHotelMenu.ViewHolder holder, int position) {
        holder.itemName.setText (data.get (position).name);
        holder.itemCategory.setText (data.get (position).type+" : "+data.get (position).size);
        holder.itemPrice.setText ("Rs."+data.get (position).price);
        holder.itemLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent (ctx, createOrder.class));
            }
        });

    }

    @Override
    public int getItemCount() {
//        return 1;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,itemPrice,itemCategory;
        CardView singleRestaurant;
        RelativeLayout itemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.menuItemSelectorName);
            itemPrice = (TextView) itemView.findViewById(R.id.menuItemSelectorValue);
            itemCategory = (TextView) itemView.findViewById (R.id.menuItemSelectorCategory);
            itemLayout = (RelativeLayout) itemView.findViewById (R.id.itemLayout);
        }
    }
}
