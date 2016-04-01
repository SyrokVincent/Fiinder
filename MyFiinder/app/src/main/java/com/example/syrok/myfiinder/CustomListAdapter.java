package com.example.syrok.myfiinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<Lieu> list_lieux;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<Lieu> l){
        this.list_lieux = l;
        layoutInflater = LayoutInflater.from(aContext);

    }

    @Override
    public int getCount(){
        return list_lieux.size();
    }

    @Override
    public Object getItem(int position){
        return list_lieux.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.ligne_lieu, null);
            holder = new ViewHolder();
            holder.nomView = (TextView) convertView.findViewById(R.id.nomLieu);
            holder.distanceView = (TextView) convertView.findViewById(R.id.distance);
            // holder.typeView = (TextView) convertView.findViewById(R.id.type);
            holder.barView = (RatingBar) convertView.findViewById(R.id.note);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nomView.setText(list_lieux.get(position).getNom());
        holder.distanceView.setText("à " + list_lieux.get(position).getDistance() + "m");
        //holder.typeView.setText(list_lieux.get(position).getType());
        holder.barView.setRating(list_lieux.get(position).getNote());
        return convertView;
    }

    static class ViewHolder {
        TextView nomView;
        TextView distanceView;
        TextView typeView;
        RatingBar barView;
    }



}
