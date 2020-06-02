package com.example.android.easy_book;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantsListAdapter extends BaseAdapter {
    ArrayList<RestaurantsListClass> Restaurants = new ArrayList<>();
    private Context context;
    public RestaurantsListAdapter(ArrayList<RestaurantsListClass> restaurants, Context context) {
        Restaurants = restaurants;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return Restaurants.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.restaurants, parent, false);

        }
        RestaurantsListClass currentRestaurant = (RestaurantsListClass) getItem(position);

        TextView RestaurantName =
                convertView.findViewById(R.id.restaurant_name);
        TextView RestaurantLocation =
                convertView.findViewById(R.id.primary_location);
        TextView RestaurantCategory1 =
                convertView.findViewById(R.id.category1);
        TextView RestaurantCategory2 =
                convertView.findViewById(R.id.category2);
        TextView RestaurantCategory3 =
                convertView.findViewById(R.id.category3);
        RestaurantName.setText(currentRestaurant.getName());
        RestaurantLocation.setText(currentRestaurant.getLocation());
        RestaurantCategory1.setText(currentRestaurant.getCategory1());
        RestaurantCategory2.setText(currentRestaurant.getCategory2());
        RestaurantCategory3.setText(currentRestaurant.getCategory3());
        ImageView RestaurantImage=convertView.findViewById(R.id.restaurants_btn);
        String image=currentRestaurant.getImageURL();
        Picasso.with(context).load(image).into(RestaurantImage);



        return convertView;
    }
}
