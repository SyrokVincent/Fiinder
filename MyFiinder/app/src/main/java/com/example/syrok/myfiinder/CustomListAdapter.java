package com.example.syrok.myfiinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<Lieu> list_lieux;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<Lieu> l) {
        this.list_lieux = l;
        layoutInflater = LayoutInflater.from(aContext);

    }

    @Override
    public int getCount() {
        return list_lieux.size();
    }

    @Override
    public Object getItem(int position) {
        return list_lieux.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.ligne_lieu, null);
            holder = new ViewHolder();
            holder.nomView = (TextView) convertView.findViewById(R.id.nomLieu);
            holder.imageview = (ImageView) convertView.findViewById(R.id.imageView);
            holder.distanceView = (TextView) convertView.findViewById(R.id.distance);
            holder.barView = (RatingBar) convertView.findViewById(R.id.note);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String type = list_lieux.get(position).getType();
        if (type.contains("library") || type.contains("book_store") || type.contains("school") || type.contains("university"))
            holder.imageview.setImageResource(R.drawable.culture);
        else if (type.contains("museum") || type.contains("art_gallery"))
            holder.imageview.setImageResource(R.drawable.arts);
        else if (type.contains("bank") || type.contains("atm"))
            holder.imageview.setImageResource(R.drawable.money);
        else if (type.contains("restaurant") || type.contains("bakery") || type.contains("bar") || type.contains("cafe") || type.contains("food"))
            holder.imageview.setImageResource(R.drawable.food);
        else if (type.contains("amusement_park") || type.contains("casino") || type.contains("aquarium") || type.contains("movie_theater") || type.contains("zoo") || type.contains("bowling_alley") || type.contains("night_club"))
            holder.imageview.setImageResource(R.drawable.divertissement);
        else if (type.contains("store") || type.contains("shoe_store") || type.contains("electronics_store") || type.contains("convenience_store") || type.contains("grocery_or_supermarket") || type.contains("home_goods_store") || type.contains("clothing_store"))
            holder.imageview.setImageResource(R.drawable.shopping);
        else if (type.contains("gym") || type.contains("stadium"))
            holder.imageview.setImageResource(R.drawable.sport);
        else if (type.contains("health") || type.contains("dentist") || type.contains("doctor") || type.contains("hospital") || type.contains("pharmacy") || type.contains("veterinary_care"))
            holder.imageview.setImageResource(R.drawable.health);
        else if (type.contains("spa") || type.contains("hair_care") || type.contains("beauty_salon"))
            holder.imageview.setImageResource(R.drawable.detente);
        else if (type.contains("cemetery") || type.contains("church") || type.contains("funeral_home") || type.contains("hindu_temple") || type.contains("synagogue"))
            holder.imageview.setImageResource(R.drawable.religion);
        else holder.imageview.setImageResource(R.drawable.unknown);


        holder.nomView.setText(list_lieux.get(position).getNom());
        holder.distanceView.setText("à " + list_lieux.get(position).getDistance() + "m");
        //holder.typeView.setText(list_lieux.get(position).getType());
        holder.barView.setRating(list_lieux.get(position).getNote());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageview;
        TextView nomView;
        TextView distanceView;
        TextView typeView;
        RatingBar barView;
    }

}