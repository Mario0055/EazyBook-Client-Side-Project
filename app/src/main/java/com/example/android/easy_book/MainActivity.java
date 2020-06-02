package com.example.android.easy_book;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton myImageButton;
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    //Show Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ProfileItem:
                Intent myIntent = new Intent(this, ProfileActivity.class);
                startActivityForResult(myIntent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EASYBOOK");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        //click on first image an go to list
        imageView = (ImageView) findViewById(R.id.restaurants_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent book = new  Intent(MainActivity.this, RestaurantsListActivity.class);
                startActivity(book);
            }
        });


    }


}
