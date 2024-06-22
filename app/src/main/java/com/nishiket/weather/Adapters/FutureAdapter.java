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
import com.nishiket.weather.Domains.FutureDomain;
import com.nishiket.weather.Domains.Hourly;
import com.nishiket.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.viewHolder> {
    Context context;
    ArrayList<FutureDomain> items;

    public FutureAdapter(ArrayList<FutureDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FutureAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_future,parent,false);
        context= parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureAdapter.viewHolder holder, int position) {

        SimpleDateFormat impot=new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat output=new SimpleDateFormat("dd-mm");
        try{
            Date t=  impot.parse(items.get(position).getDays());
            holder.dayTxt.setText(output.format(t));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.statusTxt.setText(items.get(position).getStatud());
        holder.highTxt.setText(items.get(position).getMaxTemp()+"°");
        holder.lowTxt.setText(items.get(position).getLowTemp()+"°");

    int drwasou=holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(),"drawable",holder.itemView.getContext().getPackageName());
        Glide.with(context).load(drwasou).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView dayTxt,statusTxt,lowTxt,highTxt;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayTxt=itemView.findViewById(R.id.dayTxt);
            statusTxt=itemView.findViewById(R.id.statusTxt);
            lowTxt=itemView.findViewById(R.id.lowTxt);
            highTxt=itemView.findViewById(R.id.highTxt);
            pic=itemView.findViewById(R.id.pic);
        }
    }
}
