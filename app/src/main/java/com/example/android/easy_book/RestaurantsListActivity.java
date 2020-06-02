package com.example.android.easy_book;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class RestaurantsListActivity extends AppCompatActivity {
    public static ArrayList<RestaurantsListClass> restaurants = new ArrayList<>();
    ListView list;
     FirebaseFirestore db;
     String RestaurantID;
    public static ArrayList<String> RestaurantIDs = new ArrayList<>();

     int RestaurantListPosition;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);
        list = findViewById(R.id.list);
        db=FirebaseFirestore.getInstance();

        db.collection("Restaurants").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                restaurants.clear();
                for(final DocumentSnapshot snapshot : queryDocumentSnapshots){
                    RestaurantsListClass restaurantsListClass=new RestaurantsListClass(snapshot.getString("RestaurantName") ,
                            snapshot.getString("Location")
                            ,snapshot.getString("Category1"),snapshot.getString("Category2"),snapshot.getString("Category3")
                            ,snapshot.getString("RestaurantCoverPage"));

                    RestaurantID=snapshot.getId();
                    RestaurantIDs.add(RestaurantID);

                    restaurants.add(restaurantsListClass);
                    final com.example.android.easy_book.RestaurantsListAdapter Adapter =
                            new com.example.android.easy_book.RestaurantsListAdapter(restaurants, RestaurantsListActivity.this);
                    Adapter.notifyDataSetChanged();
                    list.setAdapter(Adapter);
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long l) {

                Intent intent = new Intent(RestaurantsListActivity.this, BookingActivity.class);
                intent.putExtra("RestaurantID" ,RestaurantIDs.get(position));


                startActivity(intent);


            }
        });





    }

    
}