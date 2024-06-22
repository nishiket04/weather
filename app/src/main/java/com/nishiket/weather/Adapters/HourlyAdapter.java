package com.nishiket.weather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nishiket.weather.Domains.Hourly;
import com.nishiket.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.viewHolder> {
    Context context;
    ArrayList<Hourly> items;

    public HourlyAdapter(ArrayList<Hourly> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HourlyAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_viewholder,parent,false);
        context= parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.viewHolder holder, int position) {
//        SimpleDateFormat input=new SimpleDateFormat("yyy-mm-dd hh:mm");
//        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        holder.hourText.setText(items.get(position).getHour());
        holder.tempTxt.setText(items.get(position).getTemp()+" °C");
    int drwasou=holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(),"drawable",holder.itemView.getContext().getPackageName());
        Glide.with(context).load(drwasou).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView hourText,tempTxt;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            hourText=itemView.findViewById(R.id.cardTxt);
            tempTxt=itemView.findViewById(R.id.cardTxt2);
            pic=itemView.findViewById(R.id.cardImg);
        }
    }
}
